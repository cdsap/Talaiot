package com.cdsap.talaiot.entities

import com.cdsap.talaiot.publisher.json.DetailedMetrics

/**
 * Aggregated entity that represents the detailed final results for one build
 */
data class TaskMeasurementDetailed(
    private val values: Map<String, String>,
    private val taskMeasurement: List<TaskLength>,
    private val details: DetailedMetrics
) : TaskMeasurementAggregated(values, taskMeasurement), DetailedMeasurements {
    override fun details(): DetailedMetrics = details
}
