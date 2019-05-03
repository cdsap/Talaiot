package com.cdsap.talaiot.entities

/**
 * Aggregated entity that represents the final results for one build
 */
data class TaskMeasurementAggregated(
    /**
     * Aggregation of all metrics recorded for a given build
     */
    val values: Map<String, String>,
    /**
     * List of TaskLength with all the information of the build
     */
    val taskMeasurement: List<TaskLength>
)
