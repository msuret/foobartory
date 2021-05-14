package com.example.foobartory.workstation

import com.example.foobartory.Product
import com.example.foobartory.Stocks
import com.example.foobartory.metrics.Metrics
import com.example.foobartory.runner.RunContext
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class FoobarPointOfSaleTest {

    @Test
    fun canStartProcessing() {
        val stocks = Stocks(Metrics())
        assertFalse(FoobarPointOfSale.canStartProcessing(stocks))
        stocks.add(Product.FOOBAR, 1.0, 0.0)
        assertTrue(FoobarPointOfSale.canStartProcessing(stocks))
    }

    @Test
    fun onStartProcessing() {
        val logs = emptyList<String>().toMutableList()
        val context = RunContext(logs::add)
        val startDate = 0.0
        val robotId = 0
        context.stocks.add(Product.FOOBAR, 6.0, startDate)

        FoobarPointOfSale.onStartProcessing(startDate, robotId, context)
        assertEquals(1, logs.size)
        assertEquals(1.0, context.stocks.get(Product.FOOBAR))
    }

    @Test
    fun onEndProcessing() {
        val logs = emptyList<String>().toMutableList()
        val context = RunContext(logs::add)
        val startDate = 0.0
        val robotId = 0
        val soldFoobars = 5.0
        context.stocks.add(Product.FOOBAR, 6.0, startDate)

        FoobarPointOfSale.onEndProcessing(startDate, robotId, context, soldFoobars)
        assertEquals(1, logs.size)
        assertEquals(soldFoobars, context.stocks.get(Product.EURO))
    }
}