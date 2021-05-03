package io.github.cdsap.talaiot.publisher.graph

import io.github.cdsap.talaiot.entities.CustomProperties
import io.github.cdsap.talaiot.entities.ExecutionReport
import io.github.cdsap.talaiot.entities.TaskLength
import io.github.cdsap.talaiot.entities.TaskMessageState

object TaskMeasurementAggregatedMock {
    fun taskMeasurementAggregated() = ExecutionReport(
        tasks = listOf(
            TaskLength(
                1,
                "assemble",
                "assemble",
                TaskMessageState.EXECUTED,
                true,
                "app",
                emptyList()
            ),
            TaskLength(
                2,
                "compileKotlin",
                "compileKotlin",
                TaskMessageState.EXECUTED,
                false,
                "app",
                listOf("assemble")

            )
        )
    )

    fun taskMeasurementAggregatedWrongMetricsFormat() = ExecutionReport(
        customProperties = CustomProperties(
            mutableMapOf(
                "me=tric1" to "va====lue1",
                "metric2" to "val,,   , ue2"
            )
        ),
        tasks = listOf(
            TaskLength(
                1, "clean", ":clean", TaskMessageState.EXECUTED,
                true, "app", emptyList()
            )
        )
    )
}
