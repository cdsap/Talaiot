package com.cdsap.talaiot.configuration

/**
 * Configuration for the Metrics extensions
 * It will process the default metrics defined and the custom ones.
 * metrics{
 *   gitMetrics = true
 *   performanceMetrics = false
 * }
 */
class MetricsConfiguration {
    /**
     * enable the use of the Git metrics
     */
    var gitMetrics: Boolean = true
    /**
     * enable the use of the performance metrics
     */
    var performanceMetrics: Boolean = true

    var customMetrics: MutableMap<String, String> = mutableMapOf()
    /**
     * enable the use of the Gradle metrics
     */
    var gradleMetrics: Boolean = true

    /**
     * process the metrics defined in the configuration
     * @param pair one or more pairs of strings representing custom the metrics
     */
    fun customMetrics(vararg pair: Pair<String, String>) {
        pair.forEach {
            customMetrics[it.first] = it.second
        }
    }

    /**
     * process the metrics defined in the configuration
     * @param pair one pair of strings representing a custom metric
     */
    fun customMetrics(pair: Pair<String, String>) {
        customMetrics[pair.first] = pair.second
    }

    /**
     * process the metrics defined in the configuration
     * @param metrics a Map of strings representing  custom metrics
     */
    fun customMetrics(metrics: Map<String, String>) {
        metrics.forEach {
            customMetrics[it.key] = it.value
        }
    }
}