package com.cdsap.talaiot.publisher

import com.cdsap.talaiot.entities.TaskLength
import com.cdsap.talaiot.entities.TaskMeasurementAggregated
import com.cdsap.talaiot.logger.LogTracker
import java.util.concurrent.TimeUnit


class OutputPublisher(private val logTracker: LogTracker) : Publisher {
    override fun publish(measurementAggregated: TaskMeasurementAggregated) {

        measurementAggregated.apply {
            logTracker.log("================")
            logTracker.log("OutputReporting")
            logTracker.log("================")
            val orderedTiming = sort(taskMeasurement)
            if (!orderedTiming.isEmpty()) {
                val max = orderedTiming.last().ms
                MAX_UNIT.length
                sort(taskMeasurement).forEach {
                    val x = if (max == 0L) 0 else (it.ms * MAX_UNIT.length) / max
                    val s = MAX_UNIT.substring(0, x.toInt())
                    val maskMs = maskMs(it.ms)
                    logTracker.log("$s ${it.path} : $maskMs")
                }
            }
        }
    }

    private fun sort(items: List<TaskLength>): List<TaskLength> {
        if (items.count() < 2) {
            return items
        }
        val pivot = items[items.count() / 2].ms
        val equal = items.filter { it.ms == pivot }
        val less = items.filter { it.ms < pivot }
        val greater = items.filter { it.ms > pivot }
        return sort(less) + equal + sort(greater)
    }

    private fun maskMs(ms: Long): String =
        when {
            ms < 1000 -> ms.toString() + " ms"
            ms < 60000 -> TimeUnit.MILLISECONDS.toSeconds(ms).toString() + " sec"
            else -> TimeUnit.MILLISECONDS.toMinutes(ms).toString() + " min"
        }


    companion object {
        const val MAX_UNIT = "¯\\_(ツ)_/¯ ¯\\_(ツ)_/¯ ¯\\_(ツ)_/¯ ¯\\_(ツ)_/¯ ¯\\_(ツ)_/¯"
    }
}
