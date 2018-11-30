package com.cdsap.talaiot.reporter

import com.cdsap.talaiot.entities.TaskLenght
import com.cdsap.talaiot.entities.TaskMeasurementAggregated
import com.cdsap.talaiot.logger.LogTracking


class OutputReporter : Reporter {
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
            val orderedTiming = sort(taskMeasurment)
            if(!orderedTiming.isEmpty()) {
                val max = orderedTiming.last().ms
                MAX_UNIT.length
                sort(taskMeasurment).forEach {
                    val x = (it.ms * MAX_UNIT.length) / max
                    val s = MAX_UNIT.substring(0, x.toInt())
                    logTracker.log("$s ${it.path} ---- ${it.ms}")
                }
            }
        }
    }

    fun sort(items: List<TaskLenght>): List<TaskLenght> {
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
