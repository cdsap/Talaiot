package com.cdsap.talaiot

import com.cdsap.talaiot.logger.LogTracking
import com.cdsap.talaiot.publisher.PublisherExtension
import org.gradle.api.Project


open class TalaiotExtension(val name: String, val project: Project) {
    var logger = LogTracking.Mode.INFO
    var publishers: PublisherExtension? = null
    var gitMetrics: Boolean = true
    var performanceMetrics: Boolean = true
    var customMetrics: MutableMap<String, String> = mutableMapOf()


    fun publishers(block: PublisherExtension.() -> Unit) {
        publishers = PublisherExtension().also(block)
    }

    fun customMetrics(vararg pair: Pair<String, String>) {
        pair.forEach {
            customMetrics[it.first] = it.second
        }
    }

    // Example using Groovy
    //fun publishers(closure: Closure<*>) {
    //    publishers = PublisherConfiguration()
    //    closure.delegate = publishers
    //    closure.call()
    //}
}