package io.github.cdsap.talaiot.plugin.influxdb

import io.github.cdsap.talaiot.logger.LogTrackerImpl
import io.github.cdsap.talaiot.provider.PublisherConfigurationProvider
import io.github.cdsap.talaiot.publisher.Publisher
import io.github.cdsap.talaiot.publisher.influxdb.InfluxDbPublisher
import org.gradle.api.Project

class InfluxdbConfigurationProvider(
    val project: Project
) : PublisherConfigurationProvider {
    override fun get(): List<Publisher> {
        val publishers = mutableListOf<Publisher>()
        val talaiotExtension = project.extensions.getByName("talaiot") as InfluxdbExtension

        talaiotExtension.publishers?.apply {
            influxDbPublisher?.let { publisherConfig ->
                publishers.add(
                    InfluxDbPublisher(
                        publisherConfig,
                        LogTrackerImpl(talaiotExtension.logger)
                    )
                )
            }
        }
        return publishers
    }
}
