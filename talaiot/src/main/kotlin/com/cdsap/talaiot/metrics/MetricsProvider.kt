package com.cdsap.talaiot.metrics

import com.cdsap.talaiot.TalaiotExtension
import org.gradle.api.Project

/**
 * Provider for all metrics defined in the main MetricsConfiguration
 */
class MetricsProvider(
    /**
     * Gradle project required to access to the TalaiotExtension
     */
    private val project: Project
) {

    /**
     * Aggregates all metrics depending if there are enabled in the MetricsConfiguration.
     * It access trough the Metrics interface
     *
     * @return collection of Pairs.
     */
    fun get(): Map<String, String> {
        val talaiotExtension = project.extensions.getByName("talaiot") as TalaiotExtension
        val metrics = mutableListOf<Metrics>(BaseMetrics(project, talaiotExtension.generateBuildId))
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
