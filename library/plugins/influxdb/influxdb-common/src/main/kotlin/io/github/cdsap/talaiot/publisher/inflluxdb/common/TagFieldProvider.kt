package io.github.cdsap.talaiot.publisher.inflluxdb.common

import io.github.cdsap.talaiot.metrics.Metrics
import io.github.cdsap.talaiot.metrics.ValuesProvider

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
