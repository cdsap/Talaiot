package com.cdsap.talaiot.entities

/**
 * Aggregated entity that represents the final results for one build
 */
open class TaskMeasurementAggregated(
    /**
     * Aggregation of all metrics recorded for a given build
     */
    private val values: Map<String, String>,
    /**
     * List of TaskLength with all the information of the build
     */
    private val taskMeasurement: List<TaskLength>
) : AggregatedMeasurements {
    override fun tasks(): List<TaskLength> = taskMeasurement

    override fun values(): Map<String, String> = values
}
