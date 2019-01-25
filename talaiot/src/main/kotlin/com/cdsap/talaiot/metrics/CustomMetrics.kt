package com.cdsap.talaiot.metrics


class CustomMetrics(private val metrics: Map<String, String>) : Metrics {

    override fun get() = metrics
}