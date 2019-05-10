package com.cdsap.talaiot.configuration


import com.cdsap.talaiot.publisher.Publisher
import groovy.lang.Closure
import org.gradle.api.Project

/**
 * Main configuration for the publishers configuration.
 * It offers the accessors for Groovy and KTS
 * publishers{
 *    influxDbPublisher{
 *    }
 *    outputPublisher{
 *    }
 *    taskDependencyGraphPublisher{
 *    }
 *    customDependencies{
 *    }
 * }
 */
class PublishersConfiguration(val project: Project) {
    /**
     * Access to the configuration of InfluxDbPublisher
     */
    var influxDbPublisher: InfluxDbPublisherConfiguration? = null
    /**
     * Access to the configuration of OutputPublisher
     */
    var outputPublisher: OutputPublisherConfiguration? = null
    /**
     * Access to the configuration of TaskDependencyGraphPublisher
     */
    var taskDependencyGraphPublisher: TaskDependencyGraphConfiguration? = null
    /**
     * Definition of the custom Publishers in the PublisherConfiguration. Requires implementation of Publisher
     */
    var customPublisher: Publisher? = null

    /**
     * Configuration within the main PublisherConfiguration for the TaskDependencyGraphPublisher
     * @param configuration Lambda with receiver for the TaskDependencyGraphConfiguration representing the configuration
     * taskDependencyGraphPublisher
     */
    fun taskDependencyGraphPublisher(configuration: TaskDependencyGraphConfiguration.() -> Unit) {
        taskDependencyGraphPublisher = TaskDependencyGraphConfiguration(project).also(configuration)
    }

    /**
     * Configuration within the main PublisherConfiguration for the InfluxDbPublisher
     * @param configuration Lambda with receiver for the InfluxDbPublisherConfiguration representing the configuration
     * influxDbPublisher
     */
    fun influxDbPublisher(configuration: InfluxDbPublisherConfiguration.() -> Unit) {
        influxDbPublisher = InfluxDbPublisherConfiguration().also(configuration)
    }

    /**
     * Configuration within the main PublisherConfiguration for the OutputPublisher
     * @param configuration Lambda with receiver for the OutputPublisherConfiguration representing the configuration
     * outputPublisher
     */
    fun outputPublisher(configuration: OutputPublisherConfiguration.() -> Unit) {
        outputPublisher = OutputPublisherConfiguration().also(configuration)
    }

    /**
     * Configuration within the main PublisherConfiguration for the CustomPublisher
     * @param configuration custom Publisher defined in the configuration
     */
    fun customPublisher(configuration: Publisher) {
        customPublisher = configuration
    }

    /**
     * Configuration within the main PublisherConfiguration for the InfluxDbPublisher, Groovy version
     * @param closure closure for the InfluxDbPublisherConfiguration representing the configuration
     * influxDbPublisher
     */
    fun influxDbPublisher(closure: Closure<*>) {
        influxDbPublisher = InfluxDbPublisherConfiguration()
        closure.delegate = influxDbPublisher
        closure.call()
    }

    /**
     * Configuration within the main PublisherConfiguration for the TaskDependencyGraphPublisher, Groovy version
     * @param closure closure for the TaskDependencyGraphConfiguration representing the configuration
     * taskDependencyGraphPublisher
     */
    fun taskDependencyGraphPublisher(closure: Closure<*>) {
        taskDependencyGraphPublisher = TaskDependencyGraphConfiguration(project)
        closure.delegate = taskDependencyGraphPublisher
        closure.call()
    }

    /**
     * Configuration within the main PublisherConfiguration for the OutputPublisher, Groovy version
     * @param closure closure OutputPublisherConfiguration representing the configuration
     * outputPublisher
     */
    fun outputPublisher(closure: Closure<*>) {
        outputPublisher = OutputPublisherConfiguration()
        closure.delegate = outputPublisher
        closure.call()
    }
}