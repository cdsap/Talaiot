package com.cdsap.talaiot

import com.cdsap.talaiot.configuration.IgnoreWhenConfiguration
import com.cdsap.talaiot.logger.LogTracker
import com.cdsap.talaiot.metrics.MetricsConfiguration
import com.cdsap.talaiot.configuration.PublishersConfiguration
import org.gradle.api.Project


open class TalaiotExtension(val project: Project) {
    var logger = LogTracker.Mode.INFO
    var publishers: PublishersConfiguration? = null
    var ignoreWhen: IgnoreWhenConfiguration? = null
    var metrics: MetricsConfiguration = MetricsConfiguration()

    fun ignoreWhen(block: IgnoreWhenConfiguration.() -> Unit) {
        ignoreWhen = IgnoreWhenConfiguration(project).also(block)
    }

    fun publishers(block: PublishersConfiguration.() -> Unit) {
        publishers = PublishersConfiguration().also(block)
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