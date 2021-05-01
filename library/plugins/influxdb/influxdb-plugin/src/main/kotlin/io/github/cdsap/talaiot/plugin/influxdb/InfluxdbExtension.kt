package io.github.cdsap.talaiot.plugin.influxdb

import groovy.lang.Closure
import io.github.cdsap.talaiot.TalaiotExtension
import org.gradle.api.Project

open class InfluxdbExtension(project: Project) : TalaiotExtension(project) {
    /**
     * General Publisher configuration included in the build
     */
    var publishers: InfluxdbConfiguration? = null

    fun publishers(block: InfluxdbConfiguration.() -> Unit) {
        publishers = InfluxdbConfiguration(project).also(block)
    }

    fun publishers(closure: Closure<*>) {
        publishers = InfluxdbConfiguration(project)
        closure.delegate = publishers
        closure.call()
    }
}
