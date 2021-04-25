package io.github.cdsap.talaiot.plugin.graph

import io.github.cdsap.talaiot.TalaiotExtension
import groovy.lang.Closure
import org.gradle.api.Project

open class GraphExtension(project: Project) : TalaiotExtension(project) {
    /**
     * General Publisher configuration included in the build.
     */
    internal var publishers: GraphConfiguration? = null

    fun publishers(block: GraphConfiguration.() -> Unit) {
        publishers = GraphConfiguration(project).also(block)
    }

    fun publishers(closure: Closure<*>) {
        publishers = GraphConfiguration(project)
        closure.delegate = publishers
        closure.call()
    }
}
