package com.cdsap.talaiot.configuration


import com.cdsap.talaiot.publisher.Publisher
import groovy.lang.Closure
import org.gradle.api.Project


class PublishersConfiguration(val project: Project) {
    var influxDbPublisher: InfluxDbPublisherConfiguration? = null
    var outputPublisher: OutputPublisherConfiguration? = null
    var taskDependencyGraphPublisher: TaskDependencyGraphConfiguration? = null
    var customPublisher: Publisher? = null

    fun taskDependencyGraphPublisher(configuration: TaskDependencyGraphConfiguration.() -> Unit) {
        taskDependencyGraphPublisher = TaskDependencyGraphConfiguration(project).also(configuration)
    }

    fun influxDbPublisher(configuration: InfluxDbPublisherConfiguration.() -> Unit) {
        influxDbPublisher = InfluxDbPublisherConfiguration().also(configuration)
    }

    fun outputPublisher(configuration: OutputPublisherConfiguration.() -> Unit) {
        outputPublisher = OutputPublisherConfiguration().also(configuration)
    }

    fun customPublisher(configuration: Publisher) {
        customPublisher = configuration
    }

    fun influxDbPublisher(closure: Closure<*>) {
        influxDbPublisher = InfluxDbPublisherConfiguration()
        closure.delegate = influxDbPublisher
        closure.call()
    }

    fun taskDependencyGraphPublisher(closure: Closure<*>) {
        taskDependencyGraphPublisher = TaskDependencyGraphConfiguration(project)
        closure.delegate = taskDependencyGraphPublisher
        closure.call()
    }

    fun outputPublisher(closure: Closure<*>) {
        outputPublisher = OutputPublisherConfiguration()
        closure.delegate = outputPublisher
        closure.call()
    }
}