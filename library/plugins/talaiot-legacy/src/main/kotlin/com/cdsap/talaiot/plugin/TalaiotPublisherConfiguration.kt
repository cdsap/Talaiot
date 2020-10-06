package com.cdsap.talaiot.plugin

import com.cdsap.talaiot.configuration.*
import com.cdsap.talaiot.publisher.OutputPublisherConfiguration
import com.cdsap.talaiot.publisher.Publisher
import com.cdsap.talaiot.publisher.hybrid.HybridPublisherConfiguration
import com.cdsap.talaiot.publisher.influxdb.InfluxDbPublisherConfiguration


import groovy.lang.Closure
import org.gradle.api.Project


class TalaiotPublisherConfiguration(
    val project: Project
) {
    internal var elasticSearchPublisher: ElasticSearchPublisherConfiguration? = null
    internal var hybridPublisher: HybridPublisherConfiguration? = null
    internal var influxDbPublisher: InfluxDbPublisherConfiguration? = null
    internal var outputPublisher: OutputPublisherConfiguration? = null
    internal var pushGatewayPublisher: PushGatewayPublisherConfiguration? = null
    internal var rethinkDbPublisher: RethinkDbPublisherConfiguration? = null
    internal var taskDependencyGraphPublisher: TaskDependencyGraphConfiguration? = null

    internal var customPublishers: MutableSet<Publisher> = mutableSetOf()

    /**
     * Enables a [TimelinePublisher] if set to `true`. Disabled by default.
     */
    var timelinePublisher: Boolean = false

    /**
     * Enables a [JsonPublisher] if set to `true`. Disabled by default.
     */
    var jsonPublisher: Boolean = false

    /**
     * Configuration accessor within the [PublishersConfiguration] for the [ElasticSearchPublisher]
     *
     * @param configuration Configuration block for the [ElasticSearchPublisherConfiguration]
     */
    fun elasticSearchPublisher(configuration: ElasticSearchPublisherConfiguration.() -> Unit) {
        elasticSearchPublisher = ElasticSearchPublisherConfiguration().also(configuration)
    }

    /**
     * Configuration accessor within the [PublishersConfiguration] for the [ElasticSearchPublisher]
     *
     * @param closure closure for the [ElasticSearchPublisherConfiguration]
     */
    fun elasticSearchPublisher(closure: Closure<*>) {
        elasticSearchPublisher = ElasticSearchPublisherConfiguration()
        closure.delegate = elasticSearchPublisher
        closure.call()
    }

    /**
     * Configuration accessor within the [PublishersConfiguration] for the [HybridPublisher]
     *
     * @param configuration Configuration block for the [HybridPublisherConfiguration]
     */
    fun hybridPublisher(configuration: HybridPublisherConfiguration.() -> Unit) {
        hybridPublisher = HybridPublisherConfiguration().also(configuration)
    }

    /**
     * Configuration accessor within the [HybridPublisherConfiguration] for the [HybridPublisher]
     *
     * @param closure closure for the [HybridPublisherConfiguration]
     */
    fun hybridPublisher(closure: Closure<*>) {
        hybridPublisher = HybridPublisherConfiguration()
        closure.delegate = hybridPublisher
        closure.call()
    }

    /**
     * Configuration accessor within the [PublishersConfiguration] for the [InfluxDbPublisher]
     *
     * @param configuration Configuration block for the [InfluxDbPublisherConfiguration]
     */
    fun influxDbPublisher(configuration: InfluxDbPublisherConfiguration.() -> Unit) {
        influxDbPublisher = InfluxDbPublisherConfiguration().also(configuration)
    }

    /**
     * Configuration accessor within the [PublishersConfiguration] for the [InfluxDbPublisher]
     *
     * @param closure closure for the [InfluxDbPublisherConfiguration]
     */
    fun influxDbPublisher(closure: Closure<*>) {
        influxDbPublisher = InfluxDbPublisherConfiguration()
        closure.delegate = influxDbPublisher
        closure.call()
    }

    /**
     * Configuration accessor within the [PublishersConfiguration] for the [OutputPublisher]
     *
     * @param configuration Configuration block for the [OutputPublisherConfiguration]
     */
    fun outputPublisher(configuration: OutputPublisherConfiguration.() -> Unit) {
        outputPublisher = OutputPublisherConfiguration().also(configuration)
    }

    /**
     * Configuration accessor within the [PublishersConfiguration] for the [OutputPublisher]
     *
     * @param closure closure for the [OutputPublisherConfiguration]
     */
    fun outputPublisher(closure: Closure<*>) {
        outputPublisher = OutputPublisherConfiguration()
        closure.delegate = outputPublisher
        closure.call()
    }

    /**
     * Configuration accessor within the [PublishersConfiguration] for the [PushGatewayPublisher]
     *
     * @param configuration Configuration block for the [PushGatewayPublisherConfiguration]
     */
    fun pushGatewayPublisher(configuration: PushGatewayPublisherConfiguration.() -> Unit) {
        pushGatewayPublisher = PushGatewayPublisherConfiguration().also(configuration)
    }

    /**
     * Configuration accessor within the [PublishersConfiguration] for the [PushGatewayPublisher]
     *
     * @param closure closure for the [PushGatewayPublisherConfiguration]
     */
    fun pushGatewayPublisher(closure: Closure<*>) {
        pushGatewayPublisher = PushGatewayPublisherConfiguration()
        closure.delegate = pushGatewayPublisher
        closure.call()
    }

    /**
     * Configuration accessor within the [RethinkDbPublisherConfiguration] for the custom implementation for [RethinkDbPublisher]
     *
     * @param configuration instance of your publisher
     */
    fun rethinkDbPublisher(configuration: RethinkDbPublisherConfiguration.() -> Unit) {
        rethinkDbPublisher = RethinkDbPublisherConfiguration().also(configuration)
    }

    /**
     * Configuration accessor within the [RethinkDbPublisherConfiguration] for the [RethinkDbPublisher]
     *
     * @param closure closure for the [RethinkDbPublisherConfiguration]
     */
    fun rethinkDbPublisher(closure: Closure<*>) {
        rethinkDbPublisher = RethinkDbPublisherConfiguration()
        closure.delegate = rethinkDbPublisher
        closure.call()
    }
    /**
     * Configuration accessor within the [PublishersConfiguration] for the [TaskDependencyGraphPublisher]
     *
     * @param configuration Configuration block for the [TaskDependencyGraphConfiguration]
     */
    fun taskDependencyGraphPublisher(configuration: TaskDependencyGraphConfiguration.() -> Unit) {
        taskDependencyGraphPublisher = TaskDependencyGraphConfiguration(project).also(configuration)
    }

    /**
     * Configuration accessor within the [PublishersConfiguration] for the [TaskDependencyGraphPublisher]
     *
     * @param closure closure for the [TaskDependencyGraphConfiguration]
     */
    fun taskDependencyGraphPublisher(closure: Closure<*>) {
        taskDependencyGraphPublisher = TaskDependencyGraphConfiguration(project)
        closure.delegate = taskDependencyGraphPublisher
        closure.call()
    }

    /**
     * Adds the given custom publishers into the publisher list.
     *
     * @param publishers takes N [Publisher]s to be added to the publishers list.
     */
    fun customPublishers(vararg publishers: Publisher) {
        customPublishers.addAll(publishers)
    }
}