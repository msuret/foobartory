package com.example.foobartory.action

import com.example.foobartory.runner.RunContext
import com.example.foobartory.workstation.Workstation
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
internal class MoveRobotActionTest {

    @RelaxedMockK
    lateinit var fromWorkstation: Workstation<Unit>

    @RelaxedMockK
    lateinit var toWorkstation: Workstation<Unit>

    @Test
    fun onStart() {
        val logs = emptyList<String>().toMutableList()
        val runContext = RunContext(logs::add)
        val startDate = 0.0

        MoveRobotAction(0, fromWorkstation, toWorkstation, startDate, runContext).onStart()
        assertEquals(1, logs.size)
        assertArrayEquals(doubleArrayOf(startDate), runContext.metrics.movingRobotCount.asTrace().x.doubles)
        assertArrayEquals(doubleArrayOf(1.0), runContext.metrics.movingRobotCount.asTrace().y.doubles)
    }


    @Test
    fun onCompletion() {
        val logs = emptyList<String>().toMutableList()
        val runContext = RunContext { logs.add(it) }
        val startDate = 0.0
        runContext.metrics.movingRobotCount.add(startDate, 1.0)

        val action = MoveRobotAction(0, fromWorkstation, toWorkstation, startDate, runContext)
        action.onCompletion()

        assertEquals(1, logs.size)
        assertArrayEquals(
            doubleArrayOf(startDate, action.endDate),
            runContext.metrics.movingRobotCount.asTrace().x.doubles
        )
        assertArrayEquals(doubleArrayOf(1.0, 0.0), runContext.metrics.movingRobotCount.asTrace().y.doubles)
    }
}