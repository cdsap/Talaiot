package com.cdsap.talaiot

import com.cdsap.talaiot.configuration.IgnoreWhenConfiguration
import com.cdsap.talaiot.logger.LogTracker
import com.cdsap.talaiot.metrics.MetricsConfiguration
import com.cdsap.talaiot.configuration.PublishersConfiguration
import groovy.lang.Closure
import org.gradle.api.Project


open class TalaiotExtension(val project: Project) {
    var logger = LogTracker.Mode.SILENT
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

    fun publishers(closure: Closure<*>) {
        publishers = PublishersConfiguration()
        closure.delegate = publishers
        closure.call()
    }

    fun logger(string: String) {

    }

    fun ignoreWjen(closure: Closure<*>) {
        ignoreWhen = IgnoreWhenConfiguration(project)
        closure.delegate = ignoreWhen
        closure.call()
    }

    fun metrics(closure: Closure<*>) {
        metrics = MetricsConfiguration()
        closure.delegate = metrics
        closure.call()

    }

}