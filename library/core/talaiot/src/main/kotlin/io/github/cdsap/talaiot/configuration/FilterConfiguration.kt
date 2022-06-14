package io.github.cdsap.talaiot.configuration

import groovy.lang.Closure
import io.github.cdsap.talaiot.filter.StringFilter

/**
 * Configuration to specify the filters for the tasks that should be processed by the publisher or for the build in general.
 * Task filters included: Task Name, Module Name and Threshold duration of the execution.
 *
 * Used in OutputConfiguration and InfluxDbConfiguration.
 * Not used in TaskDependencyGraphConfiguration due to requirement to display dependency of the task build.
 *
 * Build filter include: success of the build, exclude requested tasks or include requested tasks
 * Build filter affect all Publishers.
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
 *  }
 *  build {
 *      // Publish build only with specified result. Omit to publish both successful and failed builds.
 *      success = true
 *      requestedTasks {
 *          // Publish only if at least one requested task matches "includes" filter.
 *          // Has higher priority over "excludes" filter
 *          includes = arrayOf(":app:assemble.*")
 *          // Do not publish report if all requested task matches "excludes" filter.
 *          excludes = arrayOf(":app:generate.*")
 *      }
 * }
 */
class FilterConfiguration : java.io.Serializable {

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

    /**
     * A set of criteria to decide if entire build should be published or not.
     */
    val build: BuildFilterConfiguration = BuildFilterConfiguration()

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

    fun build(configuration: BuildFilterConfiguration.() -> Unit) {
        build.also(configuration)
    }

    fun build(closure: Closure<*>) {
        closure.delegate = build
        closure.call()
    }
}
