package com.example.foobartory.workstation

import com.example.foobartory.Product
import com.example.foobartory.Stocks
import com.example.foobartory.runner.RunContext

interface Workstation<ProcessingState> {

    val name: String
    val finishedProduct: Product

    val averageProcessingTime: Double

    /**
     * average amount of ingredients required to start processing
     */
    val averageIngredientAmount: Map<Product, Double>

    /**
     * average amount of finished product produced after processing
     */
    val averageOutputAmount: Double

    /**
     * Compute processing time for next processing
     */
    fun getProcessingTime(): Double = averageProcessingTime

    fun canStartProcessing(stocks: Stocks): Boolean =
        averageIngredientAmount.all { (ingredient, amount) -> stocks.get(ingredient) >= amount }

    fun onStartProcessing(date: Double, robotId: Int, context: RunContext): ProcessingState

    fun onEndProcessing(date: Double, robotId: Int, context: RunContext, state: ProcessingState)
}