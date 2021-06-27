package io.github.cdsap.talaiot.plugin.pushgateway

import groovy.lang.Closure
import io.github.cdsap.talaiot.TalaiotExtension
import org.gradle.api.Project

open class PushgatewayExtension(project: Project) : TalaiotExtension(project) {
    /**
     * General Publisher configuration included in the build
     */
    var publishers: PushgatewayConfiguration? = null

    fun publishers(block: PushgatewayConfiguration.() -> Unit) {
        publishers = PushgatewayConfiguration(project).also(block)
    }

    fun publishers(closure: Closure<*>) {
        publishers = PushgatewayConfiguration(project)
        closure.delegate = publishers
        closure.call()
    }
}
