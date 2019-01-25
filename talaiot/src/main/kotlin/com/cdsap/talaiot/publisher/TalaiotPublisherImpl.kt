package com.cdsap.talaiot.publisher

import com.cdsap.talaiot.entities.TaskLength
import com.cdsap.talaiot.entities.TaskMeasurementAggregated


class TalaiotPublisherImpl(
    private val publishers: List<Publisher>,
    val metrics: Map<String, String>
) : TalaiotPublisher {


    override fun publish(taskLengthList: MutableList<TaskLength>) {
        val aggregatedData = TaskMeasurementAggregated(metrics, taskLengthList)
        publishers.forEach {
            println("aslslsallas")
            it.publish(aggregatedData)
        }
    }
}

