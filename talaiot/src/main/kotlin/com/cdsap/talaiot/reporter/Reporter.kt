package com.cdsap.talaiot.reporter

import com.cdsap.talaiot.entities.TaskMeasurementAggregated
import com.cdsap.talaiot.logger.LogTracking

interface Reporter {
    var logTracker: LogTracking
    fun send(measurementAggregated: TaskMeasurementAggregated)
}
