package com.cdsap.talaiot.metrics

import com.cdsap.talaiot.TalaiotExtension
import org.gradle.BuildResult

class MetricsProvider(
    private val talaiotExtension: TalaiotExtension,
    private val result: BuildResult
) {

    fun get(): List<Metrics> {
        val metrics = mutableListOf<Metrics>(BaseMetrics(result))

        if (talaiotExtension.gitMetrics) {
            metrics.add(GitMetrics())
        }

        if (talaiotExtension.performanceMetrics) {
            metrics.add(PerformanceMetrics())
        }

        if (!talaiotExtension.customMetrics.isEmpty()) {
            metrics.add(CustomMetrics(talaiotExtension))
        }

        return metrics
    }
}
