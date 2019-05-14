package com.cdsap.talaiot.configuration

import com.cdsap.talaiot.configuration.filter.StringFilter
import groovy.lang.Closure

class FilterConfiguration {
    var tasks: StringFilter? = null
    var modules: StringFilter? = null


    fun tasks(configuration: StringFilter.() -> Unit) {
        tasks = StringFilter().also(configuration)
    }

    fun tasks(closure: Closure<*>) {
        tasks = StringFilter()
        closure.delegate = tasks
        closure.call()
    }


    fun modules(configuration: StringFilter.() -> Unit) {
        modules = StringFilter().also(configuration)
    }

    fun modules(closure: Closure<*>) {
        modules = StringFilter()
        closure.delegate = modules
        closure.call()
    }
}