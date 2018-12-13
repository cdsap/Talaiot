package com.cdsap.talaiot.publisher

import com.cdsap.talaiot.entities.TaskMeasurementAggregated
import com.cdsap.talaiot.logger.LogTracking

interface Publisher {
    var logTracker: LogTracking
    fun publish(measurementAggregated: TaskMeasurementAggregated)
}
