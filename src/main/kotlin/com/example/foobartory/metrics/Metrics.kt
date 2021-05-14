package com.example.foobartory.metrics

import com.example.foobartory.Product
import com.example.foobartory.workstation.workstations
import java.util.*

class Metrics {
    val totalRobotCount = TimeMetric("total")
    val workingRobotCount = TimeMetric("working")
    val movingRobotCount = TimeMetric("moving")
    val waitingRobotCount = TimeMetric("waiting")
    val robotsPerWorkstation = workstations.associateWith { TimeMetric(it.name) }
    val stocks = Product.values().associateWith { TimeMetric(it.name.lowercase(Locale.getDefault())) }
    val foobarProductionOutcome = CategoricalMetric("success", "failure")

    fun close(endDate: Double) {
        //add a point at the end date so the curves reaches the end of the graph
        (listOf(
            totalRobotCount,
            workingRobotCount,
            movingRobotCount,
            waitingRobotCount
        ) + robotsPerWorkstation.values + stocks.values)
            .forEach { it.add(endDate, 0.0) }
    }
}