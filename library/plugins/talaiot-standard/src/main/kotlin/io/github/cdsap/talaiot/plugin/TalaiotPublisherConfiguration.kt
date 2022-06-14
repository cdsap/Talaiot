package io.github.cdsap.talaiot.plugin

import groovy.lang.Closure
import io.github.cdsap.talaiot.publisher.OutputPublisherConfiguration
import io.github.cdsap.talaiot.publisher.elasticsearch.ElasticSearchPublisherConfiguration
import io.github.cdsap.talaiot.publisher.hybrid.HybridPublisherConfiguration
import io.github.cdsap.talaiot.publisher.influxdb.InfluxDbPublisherConfiguration
import io.github.cdsap.talaiot.publisher.pushgateway.PushGatewayPublisherConfiguration
import io.github.cdsap.talaiot.publisher.rethinkdb.RethinkDbPublisherConfiguration
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
}
