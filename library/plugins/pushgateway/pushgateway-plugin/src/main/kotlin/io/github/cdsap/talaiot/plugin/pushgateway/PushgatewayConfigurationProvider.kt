package io.github.cdsap.talaiot.plugin.pushgateway

import io.github.cdsap.talaiot.logger.LogTrackerImpl
import io.github.cdsap.talaiot.provider.PublisherConfigurationProvider
import io.github.cdsap.talaiot.publisher.Publisher
import io.github.cdsap.talaiot.publisher.pushgateway.PushGatewayPublisher
import org.gradle.api.Project

class PushgatewayConfigurationProvider(
    val project: Project
) : PublisherConfigurationProvider {
    override fun get(): List<Publisher> {
        val publishers = mutableListOf<Publisher>()
        println("w")
        val talaiotExtension = project.extensions.getByName("talaiot") as PushgatewayExtension

        talaiotExtension.publishers?.apply {
            pushGatewayPublisher?.let { publisherConfig ->
                val logger = LogTrackerImpl(talaiotExtension.logger)
                publishers.add(
                    PushGatewayPublisher(
                        publisherConfig,
                        logger
                    )
                )
            }
            publishers.addAll(customPublishers)
        }
        return publishers
    }
}
