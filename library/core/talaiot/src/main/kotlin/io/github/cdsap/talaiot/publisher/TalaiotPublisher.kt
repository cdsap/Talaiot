package io.github.cdsap.talaiot.publisher

import io.github.cdsap.talaiot.entities.TaskLength

/**
 * Represents the whole information required for the plugin to be executed combining the metrics and publishers
 */
interface TalaiotPublisher : java.io.Serializable {

    fun publish(
        taskLengthList: MutableList<TaskLength>,
        start: Long,
        configuration: Long,
        end: Long,
        success: Boolean,
        duration: Long,
        publishers: List<Publisher>,
        configurationCacheHit: Boolean,
        gradleStat: String,
        kotlinStat: String,
        gradleInfo: String,
        kotlinInfo: String,
        processProcessMetrics: Boolean
    )
}
