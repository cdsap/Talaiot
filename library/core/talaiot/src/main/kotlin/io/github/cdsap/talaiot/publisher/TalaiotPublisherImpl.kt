package io.github.cdsap.talaiot.publisher

import io.github.cdsap.talaiot.entities.ExecutionReport
import io.github.cdsap.talaiot.entities.TaskLength
import io.github.cdsap.talaiot.filter.BuildFilterProcessor
import io.github.cdsap.talaiot.filter.TaskFilterProcessor

/**
 * Implementation of [TalaiotPublisher].
 * Once the [TalaiotBuildService] is closed we need to publish the build information based on the configuration.
 * This configuration is composed by:
 *   - Filtering task
 *   - Build Publishing Filter
 * Finally, it completes the [ExecutionReport] information with the general build info and
 * publishes the build information with the publishers provided
 */
class TalaiotPublisherImpl(
    private val executionReport: ExecutionReport,
    private val taskFilterProcessor: TaskFilterProcessor,
    private val buildFilterProcessor: BuildFilterProcessor
) : TalaiotPublisher, java.io.Serializable {

    override fun publish(
        taskLengthList: MutableList<TaskLength>,
        start: Long,
        configuraionMs: Long?,
        end: Long,
        success: Boolean,
        duration: Long,
        publisherProvider: List<Publisher>
    ) {
        executionReport.tasks = taskLengthList.filter { taskFilterProcessor.taskLengthFilter(it) }
        executionReport.unfilteredTasks = taskLengthList
        executionReport.beginMs = start.toString()
        executionReport.endMs = end.toString()
        executionReport.success = success
        executionReport.durationMs = duration.toString()
        executionReport.configurationDurationMs = configuraionMs.toString()

        if (buildFilterProcessor.shouldPublishBuild(executionReport)) {
            publisherProvider.forEach {
                it.publish(executionReport)
            }
        }
    }
}
