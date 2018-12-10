package com.cdsap.talaiot.reporter

import com.cdsap.talaiot.entities.TaskLength
import com.cdsap.talaiot.entities.TaskMeasurementAggregated
import com.cdsap.talaiot.logger.LogTracking


class OutputPublisher : Reporter {
    override var logTracker = LogTracking(LogTracking.Mode.INFO)


    override fun send(measurementAggregated: TaskMeasurementAggregated) {
        measurementAggregated.apply {
            logTracker.log("================")
            logTracker.log("OutputReporting")
            logTracker.log("================")
            logTracker.log("User ${this.user}")
            logTracker.log("Branch ${this.branch}")
            logTracker.log("Os ${this.os}")
            logTracker.log("Project ${this.project}")
            logTracker.log("Gradle Version ${this.gradleVersion}")
            val orderedTiming = sort(taskMeasurement)
            if (!orderedTiming.isEmpty()) {
                val max = orderedTiming.last().ms
                MAX_UNIT.length
                sort(taskMeasurement).forEach {
                    val x = (it.ms * MAX_UNIT.length) / max
                    val s = MAX_UNIT.substring(0, x.toInt())
                    logTracker.log("$s ${it.path} ---- ${it.ms}")
                }
            }
        }
    }

    fun sort(items: List<TaskLength>): List<TaskLength> {
        if (items.count() < 2) {
            return items
        }
        val pivot = items[items.count() / 2].ms
        val equal = items.filter { it.ms == pivot }
        val less = items.filter { it.ms < pivot }
        val greater = items.filter { it.ms > pivot }
        return sort(less) + equal + sort(greater)
    }

    companion object {
        const val MAX_UNIT = "¯\\_(ツ)_/¯ ¯\\_(ツ)_/¯ ¯\\_(ツ)_/¯ ¯\\_(ツ)_/¯ ¯\\_(ツ)_/¯"
    }
}
