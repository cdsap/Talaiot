package io.github.cdsap.talaiot.plugin.influxdb2

import groovy.lang.Closure
import io.github.cdsap.talaiot.TalaiotExtension
import org.gradle.api.Project

open class Influxdb2Extension(project: Project) : TalaiotExtension(project) {
    /**
     * General Publisher configuration included in the build
     */
    var publishers: Influxdb2Configuration? = null

    fun publishers(block: Influxdb2Configuration.() -> Unit) {
        publishers = Influxdb2Configuration(project).also(block)
    }

    fun publishers(closure: Closure<*>) {
        publishers = Influxdb2Configuration(project)
        closure.delegate = publishers
        closure.call()
    }
}
