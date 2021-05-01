package io.github.cdsap.talaiot.plugin.base

import groovy.lang.Closure
import io.github.cdsap.talaiot.TalaiotExtension
import org.gradle.api.Project

open class BaseExtension(project: Project) : TalaiotExtension(project) {
    /**
     * General Publisher configuration included in the build
     */
    var publishers: BaseConfiguration? = null

    fun publishers(block: BaseConfiguration.() -> Unit) {
        publishers = BaseConfiguration(project).also(block)
    }

    fun publishers(closure: Closure<*>) {
        publishers = BaseConfiguration(project)
        closure.delegate = publishers
        closure.call()
    }
}
