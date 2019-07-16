package com.cdsap.talaiot.publisher

import com.cdsap.talaiot.entities.TaskLength

/**
 * Represents the whole information required for the plugin to be executed combining the metrics and publishers
 */
interface TalaiotPublisher {
   
    fun publish(
        taskLengthList: MutableList<TaskLength>,
        start: Long,
        configuraionMs: Long?,
        end: Long,
        success: Boolean
    )
}