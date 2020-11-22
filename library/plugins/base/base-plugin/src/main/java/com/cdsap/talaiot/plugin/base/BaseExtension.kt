package com.cdsap.talaiot.plugin.base

import com.cdsap.talaiot.TalaiotExtension
import groovy.lang.Closure
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