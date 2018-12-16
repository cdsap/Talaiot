package com.cdsap.talaiot

import com.cdsap.talaiot.ci.CiConfiguration
import com.cdsap.talaiot.logger.LogTracker
import com.cdsap.talaiot.publisher.PublisherExtension
import org.gradle.api.Project


open class TalaiotExtension(val project: Project) {
    var logger = LogTracker.Mode.INFO
    var publishers: PublisherExtension? = null
    var gitMetrics: Boolean = true
    var performanceMetrics: Boolean = true
    var customMetrics: MutableMap<String, String> = mutableMapOf()
    var ci: CiConfiguration? = null

    fun ci(block: CiConfiguration.() -> Unit) {
        ci = CiConfiguration(project).also(block)
    }

    fun publishers(block: PublisherExtension.() -> Unit) {
        publishers = PublisherExtension().also(block)
    }

    fun customMetrics(vararg pair: Pair<String, String>) {
        pair.forEach {
            customMetrics[it.first] = it.second
        }
    }

    fun customMetrics(pair: Pair<String, String>) {
        customMetrics[pair.first] = pair.second
    }


// Example using Groovy
//fun publishers(closure: Closure<*>) {
//    publishers = PublisherConfiguration()
//    closure.delegate = publishers
//    closure.call()
//}
}