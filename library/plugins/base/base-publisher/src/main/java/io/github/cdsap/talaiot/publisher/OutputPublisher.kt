package io.github.cdsap.talaiot.publisher

import io.github.cdsap.talaiot.entities.ExecutionReport
import io.github.cdsap.talaiot.entities.TaskLength
import io.github.cdsap.talaiot.logger.LogTracker
import java.util.concurrent.TimeUnit

/**
 * Publisher using the default output to show the tasks ordered by duration
 */
class OutputPublisher(
    /**
     * General configuration for the publisher
     */
    private val outputPublisherConfiguration: OutputPublisherConfiguration,
    /**
     * LogTracker to print in console depending on the Mode
     */
    private val logTracker: LogTracker
) : Publisher, java.io.Serializable {

    private val TAG = "OutputPublisher"

    override fun publish(report: ExecutionReport) {
        logTracker.log(TAG, "================")
        logTracker.log(TAG, "OutputPublisher")
        logTracker.log(TAG, "publishBuildMetrics: ${outputPublisherConfiguration.publishBuildMetrics}")
        logTracker.log(TAG, "publishTaskMetrics: ${outputPublisherConfiguration.publishTaskMetrics}")
        logTracker.log(TAG, "================")

        if (outputPublisherConfiguration.publishTaskMetrics) {
            report.tasks?.apply {

                val orderedTiming = sort(this, outputPublisherConfiguration.order)
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
                        logTracker.log(TAG, "$shrug ${orderedTiming[i].taskName}: $maskMs : ${orderedTiming[i].state} ")
                    }
                }
            }
        }
    }

    /**
     * Quick Sort algorithm for a list of Tasks tracked.
     *
     * @param items list of tasks tracked
     * @param order orientation of the order
     *
     * @return list of Task ordered
     */
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

    /**
     * Format the duration of a given task to have better readability with high values
     * @param ms quantity of milliseconds to be formatted
     *
     * @return value formatted
     */
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
