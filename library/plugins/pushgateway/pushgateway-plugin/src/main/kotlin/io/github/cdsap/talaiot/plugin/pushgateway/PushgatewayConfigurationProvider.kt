package io.github.cdsap.talaiot.plugin.pushgateway

import io.github.cdsap.talaiot.logger.LogTrackerImpl
import io.github.cdsap.talaiot.provider.PublisherConfigurationProvider
import io.github.cdsap.talaiot.publisher.Publisher
import io.github.cdsap.talaiot.publisher.pushgateway.PushGatewayPublisher
import org.gradle.api.Project
import java.util.concurrent.Executors

class PushgatewayConfigurationProvider(
    val project: Project
) : PublisherConfigurationProvider {
    override fun get(): List<Publisher> {
        val publishers = mutableListOf<Publisher>()
        val talaiotExtension = project.extensions.getByName("talaiot") as PushgatewayExtension

        talaiotExtension.publishers?.apply {
            pushGatewayPublisher?.let { publisherConfig ->
                val logger = LogTrackerImpl(talaiotExtension.logger)
                publishers.add(
                    PushGatewayPublisher(
                        publisherConfig,
                        logger,
                        Executors.newSingleThreadExecutor()
                    )
                )
            }
            publishers.addAll(customPublishers)
        }
        return publishers
    }
}
