package com.example.foobartory.metrics

import space.kscience.plotly.models.Trace
import space.kscience.plotly.models.invoke
import java.util.*
import kotlin.math.sign

class TimeMetric(val name: String) {
    private val times = LinkedList<Double>().apply { add(0.0) }
    private val values = LinkedList<Double>().apply { add(0.0) }

    fun add(time: Double, amount: Double) {
        val newValue = values.last + amount
        when (sign(time - times.last)) {
            1.0 -> {
                times.add(time)
                values.add(newValue)
            }
            0.0 -> {
                values.removeLast()
                values.add(newValue)
            }
            -1.0 -> throw IllegalArgumentException("past values cannot be changed: $time < ${times.last}")
        }
    }

    fun asTrace(): Trace = Trace(times.toDoubleArray(), values.toDoubleArray()) { name = this@TimeMetric.name }

}