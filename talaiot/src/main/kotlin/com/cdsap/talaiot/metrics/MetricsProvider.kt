package com.cdsap.talaiot.metrics

class MetricsProvider(
    private val baseMetricsProvider: BaseMetrics,
    private val customMetricsProvider: CustomMetrics
) : Metrics {
    override fun get() = baseMetricsProvider.get() + customMetricsProvider.get()

}
