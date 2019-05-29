package com.cdsap.talaiot.configuration

import com.cdsap.talaiot.filter.StringFilter
import groovy.lang.Closure

/**
 * Configuration to specify the filters for the tasks that should be processed by the publisher.
 * Filters included: Task Name, Module Name and Threshold duration of the execution.
 *
 * Used in OutputConfiguration and InfluxDbConfiguration.
 * Not used in TaskDependencyGraphConfiguration due to requirement to display dependency of the task build.
 *
 * Example:
 *
 *filter{
 *  tasks{
 *      includes = arrayOf("cle.*")
 *      excludes = arrayOf("taskA")
 *  }
 *  modules{
 *      includes = arrayOf("feature.*")
 *      excludes = arrayOf("utils.*")
 *  }
 *  threshold{
 *      maxValue = 3000
 *      minValue = 10
 * }
 */
class FilterConfiguration {

    /**
     * A regex based filter to include and exclude tasks
     */
    var tasks: StringFilter? = null

    /**
     * A regex based filter to include and exclude module
     */
    var modules: StringFilter? = null

    /**
     * A range to filter the duration of execution of the task
     */
    var threshold: ThresholdConfiguration? = null

    fun tasks(configuration: StringFilter.() -> Unit) {
        tasks = StringFilter().also(configuration)
    }

    fun tasks(closure: Closure<*>) {
        tasks = StringFilter()
        closure.delegate = tasks
        closure.call()
    }


    fun modules(configuration: StringFilter.() -> Unit) {
        modules = StringFilter().also(configuration)
    }

    fun modules(closure: Closure<*>) {
        modules = StringFilter()
        closure.delegate = modules
        closure.call()
    }

    fun threshold(configuration: ThresholdConfiguration.() -> Unit) {
        threshold = ThresholdConfiguration().also(configuration)
    }

    fun threshold(closure: Closure<*>) {
        threshold = ThresholdConfiguration()
        closure.delegate = threshold
        closure.call()
    }
}
