package io.github.cdsap.talaiot.publisher.graph

import io.github.cdsap.talaiot.configuration.IgnoreWhenConfiguration
import groovy.lang.Closure
import org.gradle.api.Project

/**
 * Configuration for [com.cdsap.talaiot.publisher.graph.TaskDependencyGraphPublisher]
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
     * Flag to enable [com.cdsap.talaiot.publisher.graph.GexfPublisher]
     */
    var gexf = false
    /**
     * Flag to enable [com.cdsap.talaiot.publisher.graph.HtmlPublisher]
     */
    var html = false
    /**
     * Flag to enable [com.cdsap.talaiot.publisher.graph.DotPublisher]
     */
    var dot = false

    /**
     * Kotlin accessor for the [IgnoreWhenConfiguration]
     *
     * @param block Lambda with receiver for the [IgnoreWhenConfiguration]
     */
    fun ignoreWhen(block: IgnoreWhenConfiguration.() -> Unit) {
        ignoreWhen = IgnoreWhenConfiguration(project).also(block)
    }

    /**
     * Groovy accessor for the [IgnoreWhenConfiguration]
     *
     * @param closure closure for the [IgnoreWhenConfiguration]
     */
    fun ignoreWhen(closure: Closure<*>) {
        ignoreWhen = IgnoreWhenConfiguration(project)
        closure.delegate = ignoreWhen
        closure.call()
    }
}
