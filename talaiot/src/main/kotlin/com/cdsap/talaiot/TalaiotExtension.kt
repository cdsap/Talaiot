package com.cdsap.talaiot

import com.cdsap.talaiot.configuration.IgnoreWhenConfiguration
import com.cdsap.talaiot.logger.LogTracker
import com.cdsap.talaiot.configuration.MetricsConfiguration
import com.cdsap.talaiot.configuration.PublishersConfiguration
import groovy.lang.Closure
import org.gradle.api.Project
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Internal

@Suppress("PropertyName")
open class TalaiotExtension(val project: Project) {
    @get:Internal("Backing property for logger mode")
    internal var _logger: LogTracker.Mode? = null

    @get:Input
    var logger
        get() = _logger ?: LogTracker.Mode.SILENT
        set(value) {
            _logger = value
        }

    @get:Internal("Backing property for Publishers")
    internal var _publishers: PublishersConfiguration? = null

    @get:Input
    var publishers
        get() = _publishers
        set(value) {
            _publishers = value
        }

    var ignoreWhen: IgnoreWhenConfiguration? = null
    var metrics: MetricsConfiguration =
        MetricsConfiguration()

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