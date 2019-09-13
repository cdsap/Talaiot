package com.cdsap.talaiot.configuration


import com.cdsap.talaiot.publisher.Publisher
import groovy.lang.Closure
import org.gradle.api.Project

/**
 * Main configuration for the publishers.
 *
 * It offers the accessors for Groovy and KTS
 *
 * publishers {
 *    influxDbPublisher {
 *    }
 *    outputPublisher {
 *    }
 *    taskDependencyGraphPublisher {
 *    }
 *    pushGatewayPublisher {
 *    }
 *    customPublisher {
 *    }
 * }
 */
class PublishersConfiguration(
    val project: Project
) {
    /**
     * Access to the configuration of [com.cdsap.talaiot.publisher.InfluxDbPublisher]
     */
    var influxDbPublisher: InfluxDbPublisherConfiguration? = null
    /**
     * Access to the configuration of [com.cdsap.talaiot.publisher.OutputPublisher]
     */
    var outputPublisher: OutputPublisherConfiguration? = null
    /**
     * Access to the configuration of [com.cdsap.talaiot.publisher.PushGatewayPublisher]
     */
    var pushGatewayPublisher: PushGatewayPublisherConfiguration? = null
    /**
     * Access to the configuration of [com.cdsap.talaiot.publisher.TaskDependencyGraphPublisher]
     */
    var taskDependencyGraphPublisher: TaskDependencyGraphConfiguration? = null
    /**
     * Flag to enable [com.cdsap.talaiot.publisher.timeline.TimelinePublisher]
     *
     * Generates an html report with the timeline of task execution
     */
    var timelinePublisher: Boolean = false
    /**
     * Flag to enable [com.cdsap.talaiot.publisher.JsonPublisher]
     *
     * Generates a json representation of [com.cdsap.talaiot.entities.ExecutionReport]
     */
    var jsonPublisher: Boolean = false

    var elasticSearchPublisher: ElasticSearchPublisherConfiguration? = null

    var hybridPublisher: HybridPublisherConfiguration? = null

    /**
     * Definition of a custom Publisher in the PublisherConfiguration. Requires implementation of Publisher.
     *
     * Some users of plugin might need to use a custom publisher to push to internal analytics for example.
     */
    var customPublisher: CustomPublisherConfiguration? = null

    /**
     * Configuration accessor within the [PublishersConfiguration] for the [com.cdsap.talaiot.publisher.TaskDependencyGraphPublisher]
     *
     * @param configuration Configuration block for the [TaskDependencyGraphConfiguration]
     */
    fun taskDependencyGraphPublisher(configuration: TaskDependencyGraphConfiguration.() -> Unit) {
        taskDependencyGraphPublisher = TaskDependencyGraphConfiguration(project).also(configuration)
    }

    /**
     * Configuration accessor within the [PublishersConfiguration] for the [com.cdsap.talaiot.publisher.InfluxDbPublisher]
     *
     * @param configuration Configuration block for the [InfluxDbPublisherConfiguration]
     */
    fun influxDbPublisher(configuration: InfluxDbPublisherConfiguration.() -> Unit) {
        influxDbPublisher = InfluxDbPublisherConfiguration().also(configuration)
    }

    /**
     * Configuration accessor within the [PublishersConfiguration] for the [com.cdsap.talaiot.publisher.PushGatewayPublisher]
     *
     * @param configuration Configuration block for the [PushGatewayPublisherConfiguration]
     */
    fun pushGatewayPublisher(configuration: PushGatewayPublisherConfiguration.() -> Unit) {
        pushGatewayPublisher = PushGatewayPublisherConfiguration().also(configuration)
    }

    /**
     * Configuration accessor within the [PublishersConfiguration] for the [com.cdsap.talaiot.publisher.ElasticSearchPublisher]
     *
     * @param configuration Configuration block for the [ElasticSearchPublisherConfiguration]
     */
    fun elasticSearchPublisher(configuration: ElasticSearchPublisherConfiguration.() -> Unit) {
        elasticSearchPublisher = ElasticSearchPublisherConfiguration().also(configuration)
    }

    /**
     * Configuration accessor within the [PublishersConfiguration] for the [com.cdsap.talaiot.publisher.HybridPublisher]
     *
     * @param configuration Configuration block for the [HybridPublisherConfiguration]
     */
    fun hybridPublisher(configuration: HybridPublisherConfiguration.() -> Unit) {
        hybridPublisher = HybridPublisherConfiguration().also(configuration)
    }

    /**
     * Configuration accessor within the [PublishersConfiguration] for the [com.cdsap.talaiot.publisher.OutputPublisher]
     *
     * @param configuration Configuration block for the [OutputPublisherConfiguration]
     */
    fun outputPublisher(configuration: OutputPublisherConfiguration.() -> Unit) {
        outputPublisher = OutputPublisherConfiguration().also(configuration)
    }

    /**
     * Configuration accessor within the [PublishersConfiguration] for the custom implementation for a Custom [Publisher]
     *
     * Will override another custom publisher instance if present
     *
     * @param configuration instance of your publisher
     */
    fun customPublisher(configuration: CustomPublisherConfiguration.() -> Unit) {
        customPublisher = CustomPublisherConfiguration().also(configuration)
    }

    /**
     * Configuration accessor within the [PublishersConfiguration] for the [com.cdsap.talaiot.publisher.InfluxDbPublisher]
     *
     * @param closure closure for the [InfluxDbPublisherConfiguration]
     */
    fun influxDbPublisher(closure: Closure<*>) {
        influxDbPublisher = InfluxDbPublisherConfiguration()
        closure.delegate = influxDbPublisher
        closure.call()
    }

    /**
     * Configuration accessor within the [PublishersConfiguration] for the [com.cdsap.talaiot.publisher.TaskDependencyGraphPublisher]
     *
     * @param closure closure for the [TaskDependencyGraphConfiguration]
     */
    fun taskDependencyGraphPublisher(closure: Closure<*>) {
        taskDependencyGraphPublisher = TaskDependencyGraphConfiguration(project)
        closure.delegate = taskDependencyGraphPublisher
        closure.call()
    }

    /**
     * Configuration accessor within the [PublishersConfiguration] for the [com.cdsap.talaiot.publisher.ElasticSearchPublisher]
     *
     * @param closure closure for the [ElasticSearchPublisherConfiguration]
     */
    fun elasticSearchPublisher(closure: Closure<*>) {
        elasticSearchPublisher = ElasticSearchPublisherConfiguration()
        closure.delegate = elasticSearchPublisher
        closure.call()
    }

    /**
     * Configuration accessor within the [HybridPublisherConfiguration] for the [com.cdsap.talaiot.publisher.HybridPublisher]
     *
     * @param closure closure for the [HybridPublisherConfiguration]
     */
    fun hybridPublisher(closure: Closure<*>) {
        hybridPublisher = HybridPublisherConfiguration()
        closure.delegate = hybridPublisher
        closure.call()
    }

    /**
     * Configuration accessor within the [PublishersConfiguration] for the [com.cdsap.talaiot.publisher.OutputPublisher]
     *
     * @param closure closure for the [OutputPublisherConfiguration]
     */
    fun outputPublisher(closure: Closure<*>) {
        outputPublisher = OutputPublisherConfiguration()
        closure.delegate = outputPublisher
        closure.call()
    }

    /**
     * Configuration accessor within the [PublishersConfiguration] for the [com.cdsap.talaiot.publisher.PushGatewayPublisher]
     *
     * @param closure closure for the [PushGatewayPublisherConfiguration]
     */
    fun pushGatewayPublisher(closure: Closure<*>) {
        pushGatewayPublisher = PushGatewayPublisherConfiguration()
        closure.delegate = pushGatewayPublisher
        closure.call()
    }
}