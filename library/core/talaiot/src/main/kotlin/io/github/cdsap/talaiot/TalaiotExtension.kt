package io.github.cdsap.talaiot

import groovy.lang.Closure
import io.github.cdsap.talaiot.configuration.FilterConfiguration
import io.github.cdsap.talaiot.configuration.IgnoreWhenConfiguration
import io.github.cdsap.talaiot.configuration.MetricsConfiguration
import io.github.cdsap.talaiot.logger.LogTracker
import org.gradle.api.Project

/**
 * Main configuration file for the [TalaiotPlugin]
 */
@Suppress("PropertyName")
open class TalaiotExtension(val project: Project) {
    /**
     * General Logger for the whole plugin
     */
    var logger = LogTracker.Mode.SILENT
    /**
     * Configuration for ignoring the execution of the plugin in the build
     */
    var ignoreWhen: IgnoreWhenConfiguration? = null

    /**
     * Metrics general configuration
     */
    var metrics: MetricsConfiguration = MetricsConfiguration()

    /**
     * Filtering task configuration, you can filter the reporting of the tasks
     * by name, module and threshold time execution
     */
    var filter: FilterConfiguration? = null

    /**
     * Execute the action of the publishers in a different thread
     *
     */
    var publishOnNewThread: Boolean = false

    fun ignoreWhen(block: IgnoreWhenConfiguration.() -> Unit) {
        ignoreWhen = IgnoreWhenConfiguration(project).also(block)
    }

    fun metrics(block: MetricsConfiguration.() -> Unit) {
        metrics = MetricsConfiguration().also(block)
    }

    fun ignoreWhen(closure: Closure<*>) {
        ignoreWhen = IgnoreWhenConfiguration(project)
        closure.delegate = ignoreWhen
        closure.call()
    }

    fun metrics(closure: Closure<*>) {
        metrics = MetricsConfiguration()
        closure.delegate = metrics
        closure.call()
    }

    fun filter(configuration: FilterConfiguration.() -> Unit) {
        filter = FilterConfiguration().also(configuration)
    }

    fun filter(closure: Closure<*>) {
        filter = FilterConfiguration()
        closure.delegate = filter
        closure.call()
    }
}
