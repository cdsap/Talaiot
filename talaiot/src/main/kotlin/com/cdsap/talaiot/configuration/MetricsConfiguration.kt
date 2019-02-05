package com.cdsap.talaiot.configuration

class MetricsConfiguration {
    var gitMetrics: Boolean = true
    var performanceMetrics: Boolean = true
    var customMetrics: MutableMap<String, String> = mutableMapOf()
    var gradleMetrics: Boolean = true

    fun customMetrics(vararg pair: Pair<String, String>) {
        pair.forEach {
            customMetrics[it.first] = it.second
        }
    }

    fun customMetrics(pair: Pair<String, String>) {
        customMetrics[pair.first] = pair.second
    }

    fun customMetrics(metrics: Map<String, String>) {
        metrics.forEach {
            customMetrics[it.key] = it.value
        }
    }
}