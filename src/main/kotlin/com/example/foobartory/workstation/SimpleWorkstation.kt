package com.example.foobartory.workstation

import com.example.foobartory.runner.RunContext

/**
 * Workstation that does not need to pass context between start and end processing callbacks
 */
interface SimpleWorkstation : Workstation<Unit> {

    override fun onStartProcessing(date: Double, robotId: Int, context: RunContext) {
        context.logRobotAction(date, robotId, "started working in $name")
        averageIngredientAmount.forEach { (product, amount) -> context.stocks.add(product, -amount, date) }
    }

    override fun onEndProcessing(date: Double, robotId: Int, context: RunContext, state: Unit) {
        onEndProcessing(date, robotId, context)
    }

    fun onEndProcessing(date: Double, robotId: Int, context: RunContext) {
        context.logRobotAction(date, robotId, "produced $averageOutputAmount $finishedProduct(s)")
        context.stocks.add(finishedProduct, averageOutputAmount, date)
    }
}