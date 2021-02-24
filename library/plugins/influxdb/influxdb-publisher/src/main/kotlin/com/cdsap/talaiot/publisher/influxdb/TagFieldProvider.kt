package com.cdsap.talaiot.publisher.influxdb

import com.cdsap.talaiot.entities.CustomProperties
import com.cdsap.talaiot.entities.Environment
import com.cdsap.talaiot.entities.ExecutionReport
import com.cdsap.talaiot.metrics.BuildMetrics
import com.cdsap.talaiot.metrics.DefaultBuildMetricsProvider
import java.lang.IllegalArgumentException

class TagFieldProvider(
    private val executionReport: ExecutionReport,
    private val tagsConfiguration: List<BuildMetrics>
) {
    private val buildMetrics = DefaultBuildMetricsProvider(executionReport).get()

    fun tags(): Map<String, String> = buildMetrics
        .filter {
            customMetrics(it.key) || defaultMetrics(it.key)
        }.mapValues { it.value.toString() }


    fun fields(): Map<String, Any> {
        val tags = tags()
        return buildMetrics.filter { !tags.containsKey(it.key) }
    }

    private fun customMetrics(key: String) =
        tagsConfiguration.contains(BuildMetrics.Custom) &&
                executionReport.customProperties.buildProperties.contains(key)

    private fun defaultMetrics(key: String) =
        try {
            tagsConfiguration.contains(BuildMetrics.valueOf(key.capitalize()))
        } catch (e: IllegalArgumentException){
            false
        }
}


fun main() {
    val mapa = mutableMapOf("Rorri" to "String", "petting" to "W")
    val customProperties = CustomProperties()
    customProperties.buildProperties = mapa
    val executionReport = ExecutionReport(
        customProperties = customProperties,
        environment = Environment(
            cpuCount = "12"
        )
    )
    val tagFieldProvider = TagFieldProvider(
        executionReport,
        listOf(BuildMetrics.Custom, BuildMetrics.CpuCount)
    )
    tagFieldProvider.tags()

    println("end")
}
