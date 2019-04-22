package com.cdsap.talaiot.composer

import com.cdsap.talaiot.entities.TaskMeasurementAggregated
import com.cdsap.talaiot.logger.LogTracker
import com.cdsap.talaiot.writer.FileWriter

interface Composer<T> {
    var logTracker: LogTracker
    var fileWriter: FileWriter<T>

    fun compose(taskMeasurementAggregated: TaskMeasurementAggregated)

}
