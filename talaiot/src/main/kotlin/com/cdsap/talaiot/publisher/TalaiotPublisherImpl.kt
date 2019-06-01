package com.cdsap.talaiot.publisher

import com.cdsap.talaiot.TalaiotExtension
import com.cdsap.talaiot.entities.TaskLength
import com.cdsap.talaiot.entities.TaskMeasurementAggregated
import com.cdsap.talaiot.filter.TaskFilterProcessor
import com.cdsap.talaiot.logger.LogTracker
import com.cdsap.talaiot.provider.Provider
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
    project: Project,
    val logger: LogTracker,
    private val metricsProvider: Provider<Map<String, String>>,
    private val publisherProvider: Provider<List<Publisher>>
) : TalaiotPublisher {
    private val taskFilterProcessor: TaskFilterProcessor

    init {
        val extension: TalaiotExtension? = project.extensions.getByType(TalaiotExtension::class.java)
        taskFilterProcessor = TaskFilterProcessor(logger, extension?.filter)
    }

    override fun publish(taskLengthList: MutableList<TaskLength>) {
        val taskLengthListFiltered = taskLengthList.filter { taskFilterProcessor.taskLengthFilter(it) }
        val metrics = metricsProvider.get()
        val aggregatedData = TaskMeasurementAggregated(metrics, taskLengthList)
        val aggregatedDataFiltered = TaskMeasurementAggregated(metrics, taskLengthListFiltered)
        publisherProvider.get().forEach {
            if (it is TaskDependencyGraphPublisher) {
                it.publish(aggregatedData)
            } else {
                it.publish(aggregatedDataFiltered)
            }
        }
    }
}
