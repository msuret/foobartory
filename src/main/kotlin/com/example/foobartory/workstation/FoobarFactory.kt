package com.example.foobartory.workstation

import com.example.foobartory.Product
import com.example.foobartory.Stocks
import com.example.foobartory.runner.RunContext
import kotlin.random.Random

object FoobarFactory : SimpleWorkstation {
    private const val successRate = 0.6

    override val name = "Foobar Factory"
    override val finishedProduct = Product.FOOBAR
    override val averageProcessingTime = 2.0
    override val averageIngredientAmount = mapOf(
        Product.FOO to 1.0,
        Product.BAR to successRate
    )
    override val averageOutputAmount: Double = successRate

    override fun canStartProcessing(stocks: Stocks): Boolean =
        stocks.get(Product.FOO) >= 1.0 && stocks.get(Product.BAR) >= 1.0

    override fun onStartProcessing(date: Double, robotId: Int, context: RunContext) {
        context.logRobotAction(date, robotId, "started working in $name")
        context.stocks.add(Product.FOO, -1.0, date)
        context.stocks.add(Product.BAR, -1.0, date)
    }

    override fun onEndProcessing(date: Double, robotId: Int, context: RunContext) {
        val success = Random.nextDouble() < successRate
        context.logRobotAction(
            date,
            robotId,
            "${if (success) "successfully produced 1" else "failed to produce"} $finishedProduct"
        )
        // increase foobar stock in case of success, else increase bar stock
        context.stocks.add(if (success) Product.FOOBAR else Product.BAR, 1.0, date)
        context.metrics.foobarProductionOutcome.add(if (success) "success" else "failure", 1.0)
    }
}