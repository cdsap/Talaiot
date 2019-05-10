package com.cdsap.talaiot.configuration

import groovy.lang.Closure
import org.gradle.api.Project

/**
 * TaskDependencyGraphConfiguration: Configuration extension @see TaskDependencyGraphPublisher
 * taskDependencyGraphPublisher{
 *    gexf = true
 *    html = false
 *    dot = true
 *    ignoreWhen{
 *       envName = "CI"
 *       envValue = "false"
 *    }
 * }
 */
class TaskDependencyGraphConfiguration(val project: Project) {
    /**
     * Configuration for the ignoreWhen property.
     */
    var ignoreWhen: IgnoreWhenConfiguration? = null
    /**
     * Configuration for GexfPublisher in the TaskDependencyGraphConfiguration
     */
    var gexf = false
    /**
     * Configuration for HtmlPublisher in the TaskDependencyGraphConfiguration
     */
    var html = false
    /**
     * Configuration for DotPublisher in the TaskDependencyGraphConfiguration
     */
    var dot = false

    /**
     * Configuration within the TaskDependencyGraphConfiguration for the ignoreWhen
     * @param block Lambda with receiver for the IgnoreWhenConfiguration
     */
    fun ignoreWhen(block: IgnoreWhenConfiguration.() -> Unit) {
        ignoreWhen = IgnoreWhenConfiguration(project).also(block)
    }

    /**
     * Configuration within the TaskDependencyGraphConfiguration for the ignoreWhen, Groovy version
     * @param closure closure for the IgnoreWhenConfiguration
     */
    fun ignoreWhen(closure: Closure<*>) {
        ignoreWhen = IgnoreWhenConfiguration(project)
        closure.delegate = ignoreWhen
        closure.call()
    }
}
