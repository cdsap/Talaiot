package com.cdsap.talaiot.metrics


/**
 * Custom metrics defined in the MetricsConfiguration.
 * In case we want to provide customMetrics we will use the custom metrics.
 */
class CustomMetrics(
    /**
     * Collection of pairs representing the custom metrics
     */
    private val metrics: Map<String, String>
) : Metrics {

    override fun get() = metrics
}