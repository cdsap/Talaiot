package com.cdsap.talaiot.reporter

import com.agoda.gradle.tracking.entities.TaskMeasurementAggregated

interface Reporter {
    fun send(measurementAggregated: TaskMeasurementAggregated)
}
