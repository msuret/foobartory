package com.example.foobartory.action

import com.example.foobartory.runner.RunContext
import com.example.foobartory.workstation.Workstation

class MoveRobotAction<ProcessingState : Any>(
    robotId: Int,
    private val fromWorkstation: Workstation<*>,
    toWorkstation: Workstation<ProcessingState>,
    startDate: Double,
    runContext: RunContext
) : RobotAction<ProcessingState>(robotId, toWorkstation, startDate, runContext) {

    override val completionTime: Double = 10.0
    override fun onStart() {
        runContext.logRobotAction(startDate, robotId, "is moving from ${fromWorkstation.name} to ${workstation.name}")
        runContext.metrics.movingRobotCount.add(startDate, 1.0)
        runContext.metrics.robotsPerWorkstation[fromWorkstation]?.add(startDate, -1.0)
        runContext.metrics.robotsPerWorkstation[workstation]?.add(startDate, 1.0)
    }

    override fun onCompletion() {
        runContext.logRobotAction(endDate, robotId, "arrived to ${workstation.name}")
        runContext.metrics.movingRobotCount.add(endDate, -1.0)
    }

}