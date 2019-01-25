package com.cdsap.talaiot.publisher

import com.cdsap.talaiot.entities.TaskLength

interface TalaiotPublisher {
    fun publish(taskLengthList: MutableList<TaskLength>)
}