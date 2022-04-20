package io.github.cdsap.talaiot.publisher.pushgateway

import io.github.cdsap.talaiot.entities.ExecutionReport
import io.github.cdsap.talaiot.metrics.DefaultBuildMetricsProvider
import io.prometheus.client.CollectorRegistry

class PushGatewayBuildCollector(
    val report: ExecutionReport,
    private val registry: CollectorRegistry,
    private val pushgatewayLabelProvider: PushGatewayLabelProvider
) : PushGatewayCollector {

    override fun collect() {
        val defaultMetrics = DefaultBuildMetricsProvider(report).get()

        val labelsNames = pushgatewayLabelProvider.buildLabelNames(defaultMetrics).toTypedArray()
        val labelsValues = pushgatewayLabelProvider.buildLabelValues(defaultMetrics).toTypedArray()

        if (report.durationMs != null) {
            gaugeBuild(
                "gradle_build_total_time",
                "build total time",
                report.durationMs!!.toDouble(),
                registry,
                labelsNames,
                labelsValues
            )
        }
        if (report.configurationDurationMs != null) {
            gaugeBuild(
                "gradle_build_configuration_time",
                "Configuration time",
                report.configurationDurationMs!!.toDouble(),
                registry,
                labelsNames,
                labelsValues
            )
        }
        if (report.cacheRatio != null) {
            gaugeBuild(
                "gradle_build_cache_ratio",
                "Cache ratio",
                report.cacheRatio!!.toDouble(),
                registry,
                labelsNames,
                labelsValues
            )
        }
    }
}
