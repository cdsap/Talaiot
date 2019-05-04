package com.cdsap.talaiot.entities

/**
 * Tasks included in the Gradle build as start commands need to track information to aggregate the data. AssembleDebug
 * for example won't return the overall execution of these tasks but we want to track it. It contains an internal counter
 * and the duration.
 */
data class NodeArgument(
    /**
     * Task retrieved from the queue representing the main task
     */
    val task: String,
    /**
     * Duration of the execution
     */
    var ms: Long,
    /**
     * Internal counter representing the tasks executed depending on the counter
     */
    var counter: Int
)
