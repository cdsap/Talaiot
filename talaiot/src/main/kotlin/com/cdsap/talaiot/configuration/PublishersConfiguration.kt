package com.cdsap.talaiot.configuration


import com.cdsap.talaiot.publisher.Neo4jPublisher
import com.cdsap.talaiot.publisher.Publisher
import com.cdsap.talaiot.publisher.TaskDependencyGraphPublisher
import groovy.lang.Closure

class PublishersConfiguration {
    var influxDbPublisher: InfluxDbPublisherConfiguration? = null
    var outputPublisher: OutputPublisherConfiguration? = null
    var customPublisher: Publisher? = null
    var neo4jPublisher: Neo4JConfiguration? = null
    var taskDependencyGraphPublisher: TaskDependencyGraphConfiguration? = null
    var gefxPublisher: GefxConfiguration? = null

    fun influxDbPublisher(configuration: InfluxDbPublisherConfiguration.() -> Unit) {
        influxDbPublisher = InfluxDbPublisherConfiguration().also(configuration)
    }

    fun outputPublisher(configuration: OutputPublisherConfiguration.() -> Unit) {
        outputPublisher = OutputPublisherConfiguration().also(configuration)
    }

    fun customPublisher(configuration: Publisher) {
        customPublisher = configuration
    }

    fun neo4jPublisher(configuration: Neo4JConfiguration.() -> Unit) {
        neo4jPublisher = Neo4JConfiguration().also(configuration)
    }

    fun taskDependencyGraphPublisher(configuration: TaskDependencyGraphConfiguration.() -> Unit) {
        taskDependencyGraphPublisher = TaskDependencyGraphConfiguration().also(configuration)
    }

    fun gefxPublisher(configuration: GefxConfiguration.() -> Unit) {
        gefxPublisher = GefxConfiguration().also(configuration)
    }

    fun influxDbPublisher(closure: Closure<*>) {
        influxDbPublisher = InfluxDbPublisherConfiguration()
        closure.delegate = influxDbPublisher
        closure.call()
    }

    fun outputPublisher(closure: Closure<*>) {
        outputPublisher = OutputPublisherConfiguration()
        closure.delegate = outputPublisher
        closure.call()
    }
}