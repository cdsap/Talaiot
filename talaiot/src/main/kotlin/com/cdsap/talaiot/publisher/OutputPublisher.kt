package com.cdsap.talaiot.publisher

import com.cdsap.talaiot.configuration.Order
import com.cdsap.talaiot.configuration.OutputPublisherConfiguration
import com.cdsap.talaiot.entities.TaskLength
import com.cdsap.talaiot.entities.TaskMeasurementAggregated
import com.cdsap.talaiot.logger.LogTracker
import java.util.concurrent.TimeUnit


class OutputPublisher(
    private val outputPublisherConfiguration: OutputPublisherConfiguration,
    private val logTracker: LogTracker
) : Publisher {
    override fun publish(taskMeasurementAggregated: TaskMeasurementAggregated) {
        logTracker.log("================")
        logTracker.log("OutputPublisher")
        logTracker.log("================")

        taskMeasurementAggregated.apply {
            val orderedTiming = sort(taskMeasurement, outputPublisherConfiguration.order)
            if (!orderedTiming.isEmpty()) {
                val max = when (outputPublisherConfiguration.order) {
                    Order.ASC -> orderedTiming.last().ms
                    Order.DESC -> orderedTiming.first().ms
                }
                val limit = when {
                    outputPublisherConfiguration.numberOfTasks < 0 -> orderedTiming.size
                    outputPublisherConfiguration.numberOfTasks <= orderedTiming.size -> outputPublisherConfiguration.numberOfTasks
                    else -> orderedTiming.size
                }

                for (i in 0 until limit) {
                    val x = if (max == 0L) 0 else (orderedTiming[i].ms * MAX_UNIT.length) / max
                    val shrug = MAX_UNIT.substring(0, x.toInt())
                    val maskMs = maskMs(orderedTiming[i].ms)
                    logTracker.log("$shrug ${orderedTiming[i].taskName}: $maskMs : ${orderedTiming[i].state} ")
                }
            }
        }
    }

    private fun sort(items: List<TaskLength>, order: Order): List<TaskLength> {
        if (items.count() < 2) {
            return items
        }
        return when (order) {
            Order.ASC -> {
                val pivot = items[items.count() / 2].ms
                val equal = items.filter { it.ms == pivot }
                val less = items.filter { it.ms < pivot }
                val greater = items.filter { it.ms > pivot }
                sort(less, order) + equal + sort(greater, order)
            }
            Order.DESC -> {
                val pivot = items[items.count() / 2].ms
                val equal = items.filter { it.ms == pivot }
                val less = items.filter { it.ms > pivot }
                val greater = items.filter { it.ms < pivot }
                sort(less, order) + equal + sort(greater, order)
            }
        }
    }

    private fun maskMs(ms: Long): String =
        when {
            ms < 1000 -> ms.toString() + "ms"
            ms < 60000 -> TimeUnit.MILLISECONDS.toSeconds(ms).toString() + "sec"
            else -> TimeUnit.MILLISECONDS.toMinutes(ms).toString() + "min"
        }


    companion object {
        const val MAX_UNIT = "¯\\_(ツ)_/¯ ¯\\_(ツ)_/¯ ¯\\_(ツ)_/¯ ¯\\_(ツ)_/¯ ¯\\_(ツ)_/¯"
    }
}