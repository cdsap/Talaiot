package com.cdsap.talaiot

import com.cdsap.talaiot.entities.TaskLength
import com.cdsap.talaiot.entities.TaskMeasurementAggregated
import com.cdsap.talaiot.metrics.MetricsParser

class AggregateData(
    private val timing: MutableList<TaskLength>,
    private val metricsProvider: MetricsParser
) {

    fun build(): TaskMeasurementAggregated {
        return TaskMeasurementAggregated(
            metricsProvider.get(),
            taskMeasurement = timing
        )
    }
}

fun String.trimSpaces() = this.replace("\\s".toRegex(), "")