package com.cdsap.talaiot.publisher.influxdb

import com.cdsap.talaiot.metrics.BuildMetrics
import com.cdsap.talaiot.metrics.Metrics
import com.cdsap.talaiot.metrics.ValuesProvider

class TagFieldProvider(
    private val tagsConfiguration: List<Metrics>,
    valuesProvider: ValuesProvider,
    private val customMetrics: Map<String, String>,
    private val keyMapper: (String) -> Metrics
) {

    private val metrics: Map<String, Any> = valuesProvider.get()

    fun tags(): Map<String, String> = metrics
        .filter {
            customMetrics(it.key) || defaultMetrics(it.key)
        }.mapValues { it.value.toString() }


    fun fields(): Map<String, Any> {
        val tags = tags()
        return metrics.filter { !tags.containsKey(it.key) }
    }

    private fun customMetrics(key: String) =
        tagsConfiguration.contains(BuildMetrics.Custom) && customMetrics.contains(key)

    private fun defaultMetrics(key: String) =
        try {
            tagsConfiguration.contains(keyMapper(key))
        } catch (e: IllegalArgumentException){
            false
        }
}
