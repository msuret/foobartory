package com.example.foobartory

import com.example.foobartory.metrics.Metrics

class Stocks(private val metrics: Metrics) {
    private val values: MutableMap<Product, Double> = Product.values().associateWith { 0.0 }.toMutableMap()

    fun get(product: Product) = values[product] ?: 0.0

    /**
     * Increase/Decrease the stock of the given amount
     */
    fun add(product: Product, amount: Double, date: Double) {
        values.compute(product) { _, oldValue ->
            oldValue?.plus(amount)?.takeIf { it >= 0.0 } ?: throw IllegalArgumentException("Not enough $product stock")
        }
        metrics.stocks[product]?.add(date, amount)
    }

}