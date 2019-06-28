package com.cdsap.talaiot.entities

/**
 * Aggregated entity that represents the final results for one build
 */
interface AggregatedMeasurements {
    fun tasks(): List<TaskLength>
    fun values(): Map<String, String>
}
