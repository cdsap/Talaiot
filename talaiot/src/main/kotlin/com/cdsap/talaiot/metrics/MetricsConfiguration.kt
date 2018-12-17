package com.cdsap.talaiot.metrics

class MetricsConfiguration {
    var gitMetrics: Boolean = true
    var performanceMetrics: Boolean = true
    var customMetrics: MutableMap<String, String> = mutableMapOf()

    fun customMetrics(vararg pair: Pair<String, String>) {
        pair.forEach {
            customMetrics[it.first] = it.second
        }
    }

    fun customMetrics(pair: Pair<String, String>) {
        customMetrics[pair.first] = pair.second
    }

}