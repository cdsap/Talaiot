package io.github.cdsap.talaiot.plugin.elasticsearch

import groovy.lang.Closure
import io.github.cdsap.talaiot.TalaiotExtension
import org.gradle.api.Project

open class ElasticSearchExtension(project: Project) : TalaiotExtension(project) {
    /**
     * General Publisher configuration included in the build
     */
    var publishers: ElasticSearchConfiguration? = null

    fun publishers(block: ElasticSearchConfiguration.() -> Unit) {
        publishers = ElasticSearchConfiguration(project).also(block)
    }

    fun publishers(closure: Closure<*>) {
        publishers = ElasticSearchConfiguration(project)
        closure.delegate = publishers
        closure.call()
    }
}
