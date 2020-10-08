package com.cdsap.talaiot.plugin.graph

import com.cdsap.talaiot.TalaiotExtension
import com.cdsap.talaiot.publisher.PublishersConfiguration
import groovy.lang.Closure
import org.gradle.api.Project

class GraphExtension(project: Project) : TalaiotExtension(project) {
    /**
     * General Publisher configuration included in the build.
     */
    internal var publishers: GraphConfiguration? = null

    fun publishers(block: PublishersConfiguration.() -> Unit) {
        publishers = GraphConfiguration(project).also(block)
    }

    fun publishers(closure: Closure<*>) {
        publishers = GraphConfiguration(project)
        closure.delegate = publishers
        closure.call()
    }
}