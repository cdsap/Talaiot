package com.cdsap.talaiot.publisher

import com.cdsap.talaiot.configuration.IgnoreWhenConfiguration
import com.cdsap.talaiot.entities.TaskMeasurementAggregated

interface Publisher {
   // var ignoreWhen: IgnoreWhenConfiguration
    fun publish(measurementAggregated: TaskMeasurementAggregated)
}
