package com.example.foobartory.action

import com.example.foobartory.runner.RunContext
import com.example.foobartory.workstation.Workstation

abstract class RobotAction<ProcessingState>(
    val robotId: Int,
    val workstation: Workstation<ProcessingState>,
    val startDate: Double,
    val runContext: RunContext
) {
    abstract val completionTime: Double
    open val endDate: Double
        get() = startDate + completionTime

    abstract fun onStart()
    abstract fun onCompletion()
}