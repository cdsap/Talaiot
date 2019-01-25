package com.cdsap.talaiot.metrics

import com.cdsap.talaiot.TalaiotExtension
import org.gradle.api.Project

class MetricsProvider(
    private val project: Project
) {

    fun get(): Map<String, String> {
        val metrics = mutableListOf<Metrics>(BaseMetrics(project))
        val talaiotExtension = project.extensions.getByName("talaiot") as TalaiotExtension
        talaiotExtension.metrics.apply {
            if (gitMetrics) {
                metrics.add(GitMetrics())
            }

            if (performanceMetrics) {
                metrics.add(PerformanceMetrics(project))
            }

            if (customMetrics.isNotEmpty()) {
                metrics.add(CustomMetrics(customMetrics))
            }

            if (gradleMetrics) {
                metrics.add(GradleMetrics(project))
            }

        }
        return metrics.fold(mutableMapOf()) { acc, f ->
            (f.get().toMutableMap() + acc.toMutableMap()) as MutableMap<String, String>
        }
    }
}
