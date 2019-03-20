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
        providePublishers().forEach {
            val aggregatedData = TaskMeasurementAggregated(provideMetrics(), taskLengthList)
            it.publish(aggregatedData)
        }
    }
}

