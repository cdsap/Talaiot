package io.github.cdsap.talaiot.publisher.pushgateway

import io.prometheus.client.CollectorRegistry
import io.prometheus.client.Gauge

interface PushGatewayCollector {

    fun collect()

    fun gaugeBuild(
        name: String,
        help: String,
        value: Double,
        registry: CollectorRegistry,
        labelsNames: Array<String>,
        labelsValues: Array<String>
    ) = Gauge.build()
        .name(name)
        .help(help)
        .labelNames(*labelsNames)
        .register(registry)
        .labels(*labelsValues)
        .set(value)
}
