package com.cdsap.talaiot

import com.cdsap.talaiot.logger.LogTracker
import com.cdsap.talaiot.publisher.PublisherExtension


open class TalaiotExtension {
    var logger = LogTracker.Mode.INFO
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