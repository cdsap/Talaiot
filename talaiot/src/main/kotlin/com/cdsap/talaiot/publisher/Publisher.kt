package com.cdsap.talaiot.publisher

import com.cdsap.talaiot.entities.TaskMeasurementAggregated

interface Publisher {
    fun publish(measurementAggregated: TaskMeasurementAggregated)
}
