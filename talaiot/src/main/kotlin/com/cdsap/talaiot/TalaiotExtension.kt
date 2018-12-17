package com.cdsap.talaiot

import com.cdsap.talaiot.ci.IgnoreWhen
import com.cdsap.talaiot.logger.LogTracker
import com.cdsap.talaiot.metrics.MetricsConfiguration
import com.cdsap.talaiot.publisher.PublisherExtension
import org.gradle.api.Project


open class TalaiotExtension(val project: Project) {
    var logger = LogTracker.Mode.INFO
    var publishers: PublisherExtension? = null
    var ignoreWhen: IgnoreWhen? = null
    var metrics: MetricsConfiguration = MetricsConfiguration()

    fun ignoreWhen(block: IgnoreWhen.() -> Unit) {
        ignoreWhen = IgnoreWhen(project).also(block)
    }

    fun publishers(block: PublisherExtension.() -> Unit) {
        publishers = PublisherExtension().also(block)
    }

    fun metrics(block: MetricsConfiguration.() -> Unit) {
        metrics = MetricsConfiguration().also(block)
    }

// Example using Groovy
//fun publishers(closure: Closure<*>) {
//    publishers = PublisherConfiguration()
//    closure.delegate = publishers
//    closure.call()
//}
}