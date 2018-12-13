package com.cdsap.talaiot

import com.cdsap.talaiot.entities.TaskLength
import com.cdsap.talaiot.entities.TaskMeasurementAggregated
import com.cdsap.talaiot.metrics.MetricsProvider
import org.gradle.BuildResult
import org.gradle.internal.os.OperatingSystem
import java.io.BufferedReader
import java.io.InputStreamReader

class AggregateData(
    private val timing: MutableList<TaskLength>,
    private val metricsProvider: MetricsProvider
) {

    fun build(): TaskMeasurementAggregated {
        return TaskMeasurementAggregated(
            metricsProvider.get(),
            taskMeasurement = timing
        )
    }
}

fun String.trimSpaces() = this.replace("\\s".toRegex(), "")