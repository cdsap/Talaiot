package com.cdsap.talaiot.publisher

import com.cdsap.talaiot.entities.TaskLength
import com.cdsap.talaiot.entities.TaskMeasurementAggregated
import com.cdsap.talaiot.metrics.MetricsProvider
import org.gradle.api.Project

/**
 * Implementation of TalaiotPublisher.
 * It will retrieve all the metrics trough the MetricsProvider and the Publishers defined in the configuration
 * trough the PublisherProvider.
 * At the publishing phase it will aggregate the data of in a TaskMeasurementAggregated to publish the result
 * on each publisher retrieved.
 */
class TalaiotPublisherImpl(
    val project: Project
) : TalaiotPublisher {
    override fun provideMetrics(): Map<String, String> = MetricsProvider(project).get()

    override fun providePublishers(): List<Publisher> = PublishersProvider(project).get()

    override fun publish(taskLengthList: MutableList<TaskLength>) {
        providePublishers().forEach {
            val aggregatedData = TaskMeasurementAggregated(provideMetrics(), taskLengthList)
            it.publish(aggregatedData)
        }
    }
}

