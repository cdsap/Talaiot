package io.github.cdsap.talaiot.plugin.rethinkdb

import io.github.cdsap.talaiot.TalaiotExtension
import io.github.cdsap.talaiot.publisher.PublishersConfiguration
import groovy.lang.Closure
import org.gradle.api.Project

open class RethinkdbExtension(project: Project) : TalaiotExtension(project) {
    /**
     * General Publisher configuration included in the build
     */
    var publishers: RethinkdbConfiguration? = null

    fun publishers(block: RethinkdbConfiguration.() -> Unit) {
        publishers = RethinkdbConfiguration(project).also(block)
    }

    fun publishers(closure: Closure<*>) {
        publishers = RethinkdbConfiguration(project)
        closure.delegate = publishers
        closure.call()
    }


}