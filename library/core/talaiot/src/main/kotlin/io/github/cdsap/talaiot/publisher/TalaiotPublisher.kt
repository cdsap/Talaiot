package io.github.cdsap.talaiot.publisher

import io.github.cdsap.talaiot.entities.TaskLength

/**
 * Represents the whole information required for the plugin to be executed combining the metrics and publishers
 */
interface TalaiotPublisher : java.io.Serializable {

    fun publish(
        taskLengthList: MutableList<TaskLength>,
        start: Long,
        configuraionMs: Long?,
        end: Long,
        success: Boolean
    )
}
