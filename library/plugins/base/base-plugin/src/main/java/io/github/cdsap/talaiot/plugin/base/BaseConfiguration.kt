package io.github.cdsap.talaiot.plugin.base

import groovy.lang.Closure
import io.github.cdsap.talaiot.publisher.OutputPublisherConfiguration
import io.github.cdsap.talaiot.publisher.PublishersConfiguration
import org.gradle.api.Project

class BaseConfiguration(project: Project) : PublishersConfiguration(project) {
    internal var outputPublisher: OutputPublisherConfiguration? = null

    /**
     * Enables a [TimelinePublisher] if set to `true`. Disabled by default.
     */
    var timelinePublisher: Boolean = false

    /**
     * Enables a [JsonPublisher] if set to `true`. Disabled by default.
     */
    var jsonPublisher: Boolean = false

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
}
