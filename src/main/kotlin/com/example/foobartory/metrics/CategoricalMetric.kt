package com.example.foobartory.metrics

import space.kscience.plotly.models.Pie
import space.kscience.plotly.models.Trace

class CategoricalMetric(vararg categories: String) {

    private val values = categories.associateWith { 0.0 }.toMutableMap()

    fun add(category: String, amount: Double) {
        values.computeIfPresent(category) { _, oldValue -> oldValue + amount }
    }

    fun asTrace(): Trace =
        Pie {
            val entries = this@CategoricalMetric.values.entries
            labels(entries.map { it.key })
            values(entries.map { it.value })
        }
}