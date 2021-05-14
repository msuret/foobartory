package com.example.foobartory.runner

import com.example.foobartory.Product
import com.example.foobartory.action.*
import com.example.foobartory.workstation.Workstation
import com.example.foobartory.workstation.workstationWeights
import com.example.foobartory.workstation.workstations
import java.util.*

class Runner(private val maxRobots: Int = 30) {

    val context = RunContext()
    private val actionQueue = PriorityQueue(Comparator.comparingDouble(RobotAction<out Any>::endDate)).apply {
        // there are initially two robots
        repeat(2) { add(NewRobotAction(it + 1, 0.0, context).apply { onStart() }) }
    }
    private val waitingRobots = LinkedList<WaitRobotAction<out Any>>()

    private fun totalRobotNb(): Int = actionQueue.size + waitingRobots.size

    private fun Workstation<*>.targetRobotNb(): Double =
        workstationWeights.getOrDefault(this, 0.0) * totalRobotNb()

    private fun Workstation<out Any>.robotNb(): Int =
        actionQueue.count { it.workstation == this } + waitingRobots.count { it.workstation == this }

    // approximate amount of finished product being produced now
    private fun Workstation<*>.pendingStock(): Double =
        actionQueue.mapNotNull { it as? WorkRobotAction<*> }
            .filter { it.workstation == this }
            .count() * averageOutputAmount

    // amount of finished product still missing to produce the next robot
    private fun Workstation<out Any>.missingStock(): Double =
        if (this.finishedProduct === Product.ROBOT) {
            1.0
        } else {
            // amount of final product the upstream workstations need
            val upstreamNeeds = workstations
                .filter { it.averageIngredientAmount.keys.contains(this.finishedProduct) }
                .sumOf { upstreamWorkstation ->
                    upstreamWorkstation.missingStock() * upstreamWorkstation.averageIngredientAmount[this.finishedProduct]!! / upstreamWorkstation.averageOutputAmount
                }
            (upstreamNeeds - context.stocks.get(this.finishedProduct) - pendingStock()).coerceAtLeast(0.0)
        }

    private fun nextAction(completedAction: RobotAction<out Any>, currentDate: Double): RobotAction<out Any> {
        val currentWorkstation = completedAction.workstation

        // if there are more robots in the workstation than the target number, and there already is enough stock or there are not enough ingredients to produce more,
        // reallocate the robots to a workshop that has enough stock and have ingredients to produce more (if any)
        return currentWorkstation.takeIf {
            it.robotNb() > it.targetRobotNb() && (it.missingStock() <= 0 || !it.canStartProcessing(context.stocks))
        }
            ?.let { _ ->
                workstations.find {
                    it != currentWorkstation
                            && it.robotNb() <= it.targetRobotNb()
                            && it.missingStock() > 0
                            && it.canStartProcessing(context.stocks)
                }
            }
            ?.let { MoveRobotAction(completedAction.robotId, currentWorkstation, it, currentDate, context) }
            //else if there is more than one additional robot in the workstation compared to the target number
            //relocate the robot to the workshop that is the furthest from its target number
            .let { action ->
                action ?: currentWorkstation.takeIf { it.robotNb() > 1 + it.targetRobotNb() }
                    ?.let { _ -> workstations.minByOrNull { it.robotNb() - it.targetRobotNb() } }
                    ?.let { MoveRobotAction(completedAction.robotId, currentWorkstation, it, currentDate, context) }
            }
            // else start working in the current workstation if possible
            .let { action ->
                action ?: currentWorkstation.takeIf { it.canStartProcessing(context.stocks) }
                    ?.let {
                        WorkRobotAction(
                            completedAction.robotId,
                            currentWorkstation,
                            currentDate,
                            context
                        )
                    }
            }
        // else wait
            ?: WaitRobotAction(completedAction.robotId, currentWorkstation, currentDate, context)
    }


    fun run() {
        while (totalRobotNb() < maxRobots) {
            val completedAction = actionQueue.remove()
            val currentDate = completedAction.endDate
            completedAction.onCompletion()
            // pass the robot in waiting state
            waitingRobots.add(
                WaitRobotAction(
                    completedAction.robotId,
                    completedAction.workstation,
                    currentDate,
                    context
                ).apply { onStart() })

            // try to find something to do for waiting robots
            val waitingRobotsIterator = waitingRobots.iterator()
            while (waitingRobotsIterator.hasNext()) {
                val waitRobotAction = waitingRobotsIterator.next()
                val nextAction = nextAction(waitRobotAction, currentDate)
                if (nextAction !is WaitRobotAction) {
                    waitingRobotsIterator.remove()
                    waitRobotAction.endDate = currentDate
                    waitRobotAction.onCompletion()
                    nextAction.onStart()
                    actionQueue.add(nextAction)
                } else if (nextAction.robotId == completedAction.robotId) {
                    context.logRobotAction(currentDate, nextAction.robotId, "is waiting for a better time to act")
                }
            }
            // add the potential newly bought robot to the loop
            if (completedAction is WorkRobotAction && completedAction.workstation.finishedProduct == Product.ROBOT) {
                actionQueue.add(NewRobotAction(totalRobotNb() + 1, currentDate, context).apply { onStart() })
            }
        }
        val endDate = actionQueue.peek().endDate
        context.logEvent(endDate, "$maxRobots robots goal was achieved in %.2fs".format(endDate))
        context.metrics.close(endDate)
    }

}