package com.cdsap.talaiot.plugin.elasticsearch

import com.cdsap.talaiot.publisher.PublishersConfiguration
import com.cdsap.talaiot.publisher.elasticsearch.ElasticSearchPublisher
import com.cdsap.talaiot.publisher.elasticsearch.ElasticSearchPublisherConfiguration
import groovy.lang.Closure
import org.gradle.api.Project

class  ElasticSearchConfiguration(project: Project) : PublishersConfiguration(project) {

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