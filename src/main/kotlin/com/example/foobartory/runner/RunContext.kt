package com.example.foobartory.runner

import com.example.foobartory.Stocks
import com.example.foobartory.metrics.Metrics
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class RunContext(val logWriter: (log: String) -> Unit = defaultLogger::info) {
    companion object {
        val defaultLogger: Logger = LoggerFactory.getLogger(RunContext::class.java)
    }

    val metrics: Metrics = Metrics()
    val stocks: Stocks = Stocks(metrics)

    fun logEvent(date: Double, event: String) = logWriter("%.2fs\t$event".format(date))
    fun logRobotAction(date: Double, robotId: Int, event: String) = logEvent(date, "Robot #$robotId $event")
}