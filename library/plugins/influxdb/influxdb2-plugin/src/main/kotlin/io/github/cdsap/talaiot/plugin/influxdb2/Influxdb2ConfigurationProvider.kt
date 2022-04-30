package io.github.cdsap.talaiot.plugin.influxdb2

import io.github.cdsap.talaiot.logger.LogTrackerImpl
import io.github.cdsap.talaiot.provider.PublisherConfigurationProvider
import io.github.cdsap.talaiot.publisher.Publisher
import io.github.cdsap.talaiot.publisher.influxdb2.InfluxDb2Publisher
import org.gradle.api.Project

class Influxdb2ConfigurationProvider(
    private val project: Project
) : PublisherConfigurationProvider {
    override fun get(): List<Publisher> {
        val publishers = mutableListOf<Publisher>()
        val talaiotExtension = project.extensions.getByName("talaiot") as Influxdb2Extension

        talaiotExtension.publishers?.apply {
            influxDb2Publisher?.let { publisherConfig ->
                publishers.add(
                    InfluxDb2Publisher(
                        publisherConfig,
                        LogTrackerImpl(talaiotExtension.logger)
                    )
                )
            }
            publishers.addAll(customPublishers)
        }
        return publishers
    }
}
