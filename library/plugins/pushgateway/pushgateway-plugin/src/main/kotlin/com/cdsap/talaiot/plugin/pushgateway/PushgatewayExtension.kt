package com.cdsap.talaiot.plugin.pushgateway

import com.cdsap.talaiot.TalaiotExtension
import com.cdsap.talaiot.publisher.PublishersConfiguration
import groovy.lang.Closure
import org.gradle.api.Project

open class PushgatewayExtension(project: Project) : TalaiotExtension(project) {
    /**
     * General Publisher configuration included in the build
     */
    var publishers: PushgatewayConfiguration? = null

    fun publishers(block: PublishersConfiguration.() -> Unit) {
        publishers = PushgatewayConfiguration(project).also(block)
    }

    fun publishers(closure: Closure<*>) {
        publishers = PushgatewayConfiguration(project)
        closure.delegate = publishers
        closure.call()
    }


}