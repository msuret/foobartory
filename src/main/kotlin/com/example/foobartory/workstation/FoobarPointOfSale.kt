package com.example.foobartory.workstation

import com.example.foobartory.Product
import com.example.foobartory.Stocks
import com.example.foobartory.runner.RunContext

object FoobarPointOfSale : Workstation<Double> {
    private const val maxSaleQuantity = 5.0

    override val name = "Foobar point of sale"
    override val finishedProduct = Product.EURO
    override val averageProcessingTime = 10.0
    override val averageIngredientAmount = mapOf(Product.FOOBAR to 5.0)
    override val averageOutputAmount: Double = 5.0 // to maximize efficiency we aim at selling 5 foobars at a time

    override fun canStartProcessing(stocks: Stocks): Boolean = stocks.get(Product.FOOBAR) > 0

    override fun onStartProcessing(date: Double, robotId: Int, context: RunContext): Double {
        val amountSold = context.stocks.get(Product.FOOBAR).coerceAtMost(maxSaleQuantity)
        context.logRobotAction(date, robotId, "started selling $amountSold foobars")
        context.stocks.add(Product.FOOBAR, -amountSold, date)
        return amountSold
    }

    override fun onEndProcessing(date: Double, robotId: Int, context: RunContext, state: Double) {
        context.logRobotAction(date, robotId, "sold $state foobars")
        context.stocks.add(finishedProduct, state, date)
    }

}