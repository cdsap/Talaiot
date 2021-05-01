package io.github.cdsap.talaiot.plugin

import groovy.lang.Closure
import io.github.cdsap.talaiot.TalaiotExtension
import org.gradle.api.Project

open class TalaiotPluginExtension(project: Project) : TalaiotExtension(project) {
    /**
     * General Publisher configuration included in the build
     */
    var publishers: TalaiotPublisherConfiguration? = null

    fun publishers(block: TalaiotPublisherConfiguration.() -> Unit) {
        publishers = TalaiotPublisherConfiguration(project).also(block)
    }

    fun publishers(closure: Closure<*>) {
        publishers = TalaiotPublisherConfiguration(project)
        closure.delegate = publishers
        closure.call()
    }
}
