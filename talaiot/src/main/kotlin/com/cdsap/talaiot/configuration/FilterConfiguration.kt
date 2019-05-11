package com.cdsap.talaiot.configuration

import com.cdsap.talaiot.configuration.filter.ModuleFilterConfiguration
import com.cdsap.talaiot.configuration.filter.TaskFilterConfiguration
import groovy.lang.Closure

class FilterConfiguration {
    var tasks: TaskFilterConfiguration? = null
    var modules: ModuleFilterConfiguration? = null


    fun tasks(configuration: TaskFilterConfiguration.() -> Unit) {
        tasks = TaskFilterConfiguration().also(configuration)
    }

    fun tasks(closure: Closure<*>) {
        tasks = TaskFilterConfiguration()
        closure.delegate = tasks
        closure.call()
    }


    fun modules(configuration: ModuleFilterConfiguration.() -> Unit) {
        modules = ModuleFilterConfiguration().also(configuration)
    }

    fun modules(closure: Closure<*>) {
        modules = ModuleFilterConfiguration()
        closure.delegate = modules
        closure.call()
    }
}