package com.cdsap.talaiot.configuration

import groovy.lang.Closure
import org.gradle.api.Project

/**
 * TaskDependencyGraphConfiguration: Configuration extension @see TaskDependencyGraphPublisher
 * Supported outputs:
 * gexf
 * html
 * dot
 */
class TaskDependencyGraphConfiguration(val project: Project) {
    var ignoreWhen: IgnoreWhenConfiguration? = null
    var gexf = false
    var html = false
    var dot = false

    fun ignoreWhen(block: IgnoreWhenConfiguration.() -> Unit) {
        ignoreWhen = IgnoreWhenConfiguration(project).also(block)
    }

    fun ignoreWhen(closure: Closure<*>) {
        ignoreWhen = IgnoreWhenConfiguration(project)
        closure.delegate = ignoreWhen
        closure.call()
    }
}
