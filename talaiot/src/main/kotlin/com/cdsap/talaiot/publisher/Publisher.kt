package com.cdsap.talaiot.publisher

import com.cdsap.talaiot.entities.TaskMeasurementAggregated
import com.cdsap.talaiot.logger.LogTracking
import com.cdsap.talaiot.metrics.MetricsProvider

interface Publisher {
    var logTracker: LogTracking
    fun send(measurementAggregated: TaskMeasurementAggregated)
}
