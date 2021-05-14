package com.example.foobartory.action

import com.example.foobartory.runner.RunContext
import com.example.foobartory.workstation.Workstation

class WaitRobotAction<ProcessingState>(
    robotId: Int,
    workstation: Workstation<ProcessingState>,
    startDate: Double,
    runContext: RunContext
) : RobotAction<ProcessingState>(robotId, workstation, startDate, runContext) {

    override val completionTime: Double = Double.MAX_VALUE //no fixed completion time
    override var endDate: Double = Double.MAX_VALUE

    override fun onStart() {
        runContext.metrics.waitingRobotCount.add(startDate, 1.0)
    }

    override fun onCompletion() {
        runContext.metrics.waitingRobotCount.add(endDate, -1.0)
    }
}