package com.cdsap.talaiot.publisher.influxdb

import com.cdsap.talaiot.metrics.Metrics
import com.cdsap.talaiot.metrics.ValuesProvider

class TagFieldProvider(
    tagsConfiguration: List<Metrics>,
    valuesProvider: ValuesProvider,
    private val customMetrics: Map<String, String>
) {

    private val metrics: Map<String, Any> = valuesProvider.get()

    private val tagMetricKeys = tagsConfiguration.mapTo(HashSet()) { it.toKey() }
    private val shouldIncludeCustom = tagsConfiguration.any { it.isCustom }

    fun tags(): Map<String, String> = metrics
        .filter {
            customMetrics(it.key) || it.key in tagMetricKeys
        }.mapValues { it.value.toString() }


    fun fields(): Map<String, Any> {
        val tags = tags()
        return metrics.filter { !tags.containsKey(it.key) }
    }

    private fun customMetrics(key: String) = shouldIncludeCustom && customMetrics.contains(key)
}
