package com.example.foobartory.workstation

import com.example.foobartory.Product
import kotlin.random.Random

object BarMine : SimpleWorkstation {
    private const val minProcessingTime = 0.5
    private const val maxProcessingTime = 2.0

    override val name = "Bar Mine"
    override val finishedProduct = Product.BAR
    override val averageProcessingTime = (maxProcessingTime + minProcessingTime) / 2.0
    override val averageIngredientAmount = emptyMap<Product, Double>()
    override val averageOutputAmount: Double = 1.0

    override fun getProcessingTime(): Double =
        Random.nextDouble(minProcessingTime, maxProcessingTime)

}