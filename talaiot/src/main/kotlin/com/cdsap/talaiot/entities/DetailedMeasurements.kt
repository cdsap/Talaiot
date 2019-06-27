package com.cdsap.talaiot.entities

import com.cdsap.talaiot.publisher.json.DetailedMetrics

/**
 * Aggregated entity that represents the final results for one build
 */
interface DetailedMeasurements : AggregatedMeasurements {
    fun details(): DetailedMetrics
}
