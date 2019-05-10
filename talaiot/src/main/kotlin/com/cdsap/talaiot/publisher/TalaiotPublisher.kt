package com.cdsap.talaiot.publisher

import com.cdsap.talaiot.entities.TaskLength

/**
 * Represents the whole information required for the plugin to be executed combining the metrics and publishers
 */
interface TalaiotPublisher {
    /**
     * Retrieve the metrics defined in the build.
     *
     * @return a map of pairs of strings and their values represented by metrics
     */
    fun provideMetrics(): Map<String, String>

    /**
     * Retrieve the list of publishers defined in the build
     *
     * @return list of publishers
     */
    fun providePublishers(): List<Publisher>

    /**
     * Main function to publish the results. All the publishers will be called here when the aggregation
     * of data is finished
     *
     * @param taskLengthList List of tasks tracked during the execution
     */
    fun publish(taskLengthList: MutableList<TaskLength>)
}