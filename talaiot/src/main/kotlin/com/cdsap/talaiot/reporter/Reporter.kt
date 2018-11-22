package com.cdsap.talaiot.reporter

import com.cdsap.talaiot.entities.TaskMeasurementAggregated

interface Reporter {
    fun send(measurementAggregated: TaskMeasurementAggregated)
}
