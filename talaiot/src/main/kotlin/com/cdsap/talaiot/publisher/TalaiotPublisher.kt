package com.cdsap.talaiot.publisher

import com.cdsap.talaiot.entities.TaskLength

interface TalaiotPublisher {
    fun provideMetrics(): Map<String, String>
    fun providePublishers(): List<Publisher>

    fun publish(taskLengthList: MutableList<TaskLength>)
}