package io.github.cdsap.talaiot.plugin.rethinkdb

import groovy.lang.Closure
import io.github.cdsap.talaiot.publisher.PublishersConfiguration
import io.github.cdsap.talaiot.publisher.rethinkdb.RethinkDbPublisher
import io.github.cdsap.talaiot.publisher.rethinkdb.RethinkDbPublisherConfiguration
import org.gradle.api.Project

class RethinkdbConfiguration(project: Project) : PublishersConfiguration(project) {

    var rethinkDbPublisher: RethinkDbPublisherConfiguration? = null

    /**
     * Configuration accessor within the [PublishersConfiguration] for the [RethinkDbPublisher]
     *
     * @param configuration Configuration block for the [RethinkDbPublisherConfiguration]
     */
    fun rethinkDbPublisher(configuration: RethinkDbPublisherConfiguration.() -> Unit) {
        rethinkDbPublisher = RethinkDbPublisherConfiguration().also(configuration)
    }

    /**
     * Configuration accessor within the [PublishersConfiguration] for the [RethinkDbPublisher]
     *
     * @param closure closure for the [RethinkDbPublisherConfiguration]
     */
    fun rethinkDbPublisher(closure: Closure<*>) {
        rethinkDbPublisher = RethinkDbPublisherConfiguration()
        closure.delegate = rethinkDbPublisher
        closure.call()
    }
}
