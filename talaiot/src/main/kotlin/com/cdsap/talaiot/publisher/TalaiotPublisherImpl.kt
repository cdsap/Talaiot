package com.cdsap.talaiot.publisher

import com.cdsap.talaiot.entities.TaskLength
import com.cdsap.talaiot.entities.TaskMeasurementAggregated
import com.cdsap.talaiot.metrics.MetricsProvider
import org.gradle.api.Project


class TalaiotPublisherImpl(
    val project: Project
) : TalaiotPublisher {
    override fun provideMetrics(): Map<String, String> = MetricsProvider(project).get()

    override fun providePublishers(): List<Publisher> = PublishersProvider(project).get()

    override fun publish(taskLengthList: MutableList<TaskLength>) {
        val aggregatedData = TaskMeasurementAggregated(provideMetrics(), taskLengthList)
        providePublishers().forEach {
            it.publish(aggregatedData)
        }
    }
}

