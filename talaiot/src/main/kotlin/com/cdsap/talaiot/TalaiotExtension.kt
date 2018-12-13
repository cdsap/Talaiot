package com.cdsap.talaiot

import com.cdsap.talaiot.logger.LogTracking
import com.cdsap.talaiot.publisher.PublisherConfiguration
import com.cdsap.talaiot.publisher.output.OutputPublisher
import groovy.lang.Closure
import org.gradle.api.Project


open class TalaiotExtension {
    var logger = LogTracking.Mode.INFO
    var track = ""
    var publisher: PublisherConfiguration? = null
    var gitMetrics: Boolean = true
    var performanceMetrics: Boolean = true


    var customMetrics: MutableMap<String, String> = mutableMapOf()

    fun publisher(block: PublisherConfiguration.() -> Unit) {
        publisher = PublisherConfiguration().also(block)
    }


    fun publisher(closure: Closure<*>) {
        publisher = PublisherConfiguration()
        closure.delegate = publisher
        closure.call()
    }

    fun customMetrics(vararg pair: Pair<String, String>) {
        pair.forEach {
            customMetrics[it.first] = it.second
        }
    }
}