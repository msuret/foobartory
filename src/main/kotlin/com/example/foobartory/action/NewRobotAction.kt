package com.example.foobartory.action

import com.example.foobartory.runner.RunContext
import com.example.foobartory.workstation.RobotShop

class NewRobotAction(robotId: Int, startDate: Double, runContext: RunContext) :
    RobotAction<Unit>(robotId, RobotShop, startDate, runContext) {

    override val completionTime: Double = 0.0 // new robots are immediately available
    override fun onStart() {
        runContext.logRobotAction(startDate, robotId, "joined")
        runContext.metrics.totalRobotCount.add(startDate, 1.0)
        runContext.metrics.robotsPerWorkstation[RobotShop]?.add(startDate, 1.0)
    }

    override fun onCompletion() {}

}