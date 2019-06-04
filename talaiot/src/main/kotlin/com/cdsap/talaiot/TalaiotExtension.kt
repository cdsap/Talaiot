package com.cdsap.talaiot

import com.cdsap.talaiot.configuration.FilterConfiguration
import com.cdsap.talaiot.configuration.IgnoreWhenConfiguration
import com.cdsap.talaiot.logger.LogTracker
import com.cdsap.talaiot.configuration.MetricsConfiguration
import com.cdsap.talaiot.configuration.PublishersConfiguration
import groovy.lang.Closure
import org.gradle.api.Project

/**
 * Main configuration file for the Talaiot Plugin
 */
@Suppress("PropertyName")
open class TalaiotExtension(val project: Project) {
    /**
     * General Logger for the whole plugin
     */
    var logger = LogTracker.Mode.SILENT
    /**
     * Flag to specify the generation of the unique build id.
     * In some cases could generate high cardinality problems like in basic InfluxDb setups, disabled by default
     */
    var generateBuildId = false
    /**
     * General Publisher configuration included in the build
     */
    var publishers: PublishersConfiguration? = null
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

    fun ignoreWhen(block: IgnoreWhenConfiguration.() -> Unit) {
        ignoreWhen = IgnoreWhenConfiguration(project).also(block)
    }

    fun publishers(block: PublishersConfiguration.() -> Unit) {
        publishers = PublishersConfiguration(project, filter).also(block)
    }

    fun metrics(block: MetricsConfiguration.() -> Unit) {
        metrics = MetricsConfiguration().also(block)
    }

    fun publishers(closure: Closure<*>) {
        publishers = PublishersConfiguration(project, filter)
        closure.delegate = publishers
        closure.call()
    }

    fun logger(string: String) {
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
