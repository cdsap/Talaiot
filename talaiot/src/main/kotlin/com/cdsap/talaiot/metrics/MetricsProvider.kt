package com.cdsap.talaiot.metrics

import com.cdsap.talaiot.TalaiotExtension
import org.gradle.BuildResult

class MetricsProvider(
    private val talaiotExtension: TalaiotExtension,
    private val result: BuildResult
) {

    fun get(): List<Metrics> {
        val metrics = mutableListOf<Metrics>(BaseMetrics(result))

        if (talaiotExtension.metrics.gitMetrics) {
            metrics.add(GitMetrics())
        }

        if (talaiotExtension.metrics.performanceMetrics) {
            metrics.add(PerformanceMetrics())
        }

        if (talaiotExtension.metrics.customMetrics.isNotEmpty()) {
            metrics.add(CustomMetrics(talaiotExtension))
        }

        return metrics
    }
}
