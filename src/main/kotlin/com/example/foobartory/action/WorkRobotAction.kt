package com.example.foobartory.action

import com.example.foobartory.runner.RunContext
import com.example.foobartory.workstation.Workstation

class WorkRobotAction<ProcessingState : Any>(
    robotId: Int,
    workstation: Workstation<ProcessingState>,
    startDate: Double,
    runContext: RunContext
) : RobotAction<ProcessingState>(robotId, workstation, startDate, runContext) {

    override val completionTime = workstation.getProcessingTime()
    private lateinit var state: ProcessingState

    override fun onStart() {
        state = workstation.onStartProcessing(startDate, robotId, runContext)
        runContext.metrics.workingRobotCount.add(startDate, 1.0)
    }

    override fun onCompletion() {
        runContext.metrics.workingRobotCount.add(endDate, -1.0)
        workstation.onEndProcessing(endDate, robotId, runContext, state)
    }
}