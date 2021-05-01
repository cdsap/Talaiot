package io.github.cdsap.talaiot.plugin.elasticsearch

import groovy.lang.Closure
import io.github.cdsap.talaiot.publisher.PublishersConfiguration
import io.github.cdsap.talaiot.publisher.elasticsearch.ElasticSearchPublisher
import io.github.cdsap.talaiot.publisher.elasticsearch.ElasticSearchPublisherConfiguration
import org.gradle.api.Project

class ElasticSearchConfiguration(project: Project) : PublishersConfiguration(project) {

    var elasticSearchPublisher: ElasticSearchPublisherConfiguration? = null

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
}
