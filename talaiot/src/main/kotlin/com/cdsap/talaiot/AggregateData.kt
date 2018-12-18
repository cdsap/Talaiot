package com.cdsap.talaiot

import com.cdsap.talaiot.entities.TaskLength
import com.cdsap.talaiot.entities.TaskMeasurementAggregated
import com.cdsap.talaiot.metrics.MetricsParser

class AggregateData(
    private val listTaskLength: MutableList<TaskLength>,
    private val metricsProvider: MetricsParser
) {

    fun build(): TaskMeasurementAggregated {
        return TaskMeasurementAggregated(
            metricsProvider.get(),
            taskMeasurement = listTaskLength
        )
    }
}
