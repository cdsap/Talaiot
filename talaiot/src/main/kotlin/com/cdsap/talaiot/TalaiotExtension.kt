package com.cdsap.talaiot

import com.cdsap.talaiot.logger.LogTracking
import com.cdsap.talaiot.publisher.PublisherConfiguration
import org.gradle.api.Project


open class TalaiotExtension {
    var logger = LogTracking.Mode.INFO
    var track = ""
    var publisher: PublisherConfiguration? = null

    var customMetrics: MutableMap<String, String> = mutableMapOf()

    fun publisher(block: PublisherConfiguration.() -> Unit) {
        publisher = PublisherConfiguration().also(block)
    }

    fun customMetrics(vararg pair: Pair<String, String>) {
        pair.forEach {
            customMetrics[it.first] = it.second
        }
    }
}