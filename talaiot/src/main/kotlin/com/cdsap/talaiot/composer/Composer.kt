package com.cdsap.talaiot.composer

import com.cdsap.talaiot.entities.TaskMeasurementAggregated
import com.cdsap.talaiot.logger.LogTracker
import com.cdsap.talaiot.writer.DefaultWriter
import com.cdsap.talaiot.writer.FileWriter

interface Composer {
    var logTracker: LogTracker
    var fileWriter: FileWriter

    fun compose(taskMeasurementAggregated: TaskMeasurementAggregated)

}
