package com.cdsap.talaiot.publisher

import com.cdsap.talaiot.TalaiotExtension
import com.cdsap.talaiot.entities.TaskLength
import com.cdsap.talaiot.entities.TaskMeasurementAggregated
import com.cdsap.talaiot.filter.TaskFilterProcessor
import com.cdsap.talaiot.logger.LogTracker
import com.cdsap.talaiot.metrics.MetricsProvider
import org.gradle.api.Project

/**
 * Implementation of TalaiotPublisher.
 * It will retrieve all the metrics trough the MetricsProvider and the Publishers defined in the configuration
 * trough the PublisherProvider.
 * At the publishing phase it will aggregate the data of in a TaskMeasurementAggregated to publish the result
 * on each publisher retrieved.
 * Before the publishing phase we will apply the TaskFilterProcessor. Filtering doesn't apply to
 * the TaskDependencyGraphPublisher
 */
class TalaiotPublisherImpl(
    val project: Project,
    extension: TalaiotExtension,
    val logger: LogTracker
) : TalaiotPublisher {
    private val taskFilterProcessor: TaskFilterProcessor

    init {
        taskFilterProcessor = TaskFilterProcessor(logger, extension.filter)

    }

    override fun provideMetrics(): Map<String, String> = MetricsProvider(project).get()

    override fun providePublishers(): List<Publisher> = PublishersProvider(project, logger).get()

    override fun publish(taskLengthList: MutableList<TaskLength>) {
        val taskLengthListFiltered = taskLengthList.filter { taskFilterProcessor.taskLengthFilter(it) }
        val metrics = provideMetrics()
        val aggregatedData = TaskMeasurementAggregated(metrics, taskLengthList)
        val aggregatedDataFiltered = TaskMeasurementAggregated(metrics, taskLengthListFiltered)
        providePublishers().forEach {
            if (it is TaskDependencyGraphPublisher) {
                it.publish(aggregatedData)
            } else {
                it.publish(aggregatedDataFiltered)
            }
        }
    }
}
