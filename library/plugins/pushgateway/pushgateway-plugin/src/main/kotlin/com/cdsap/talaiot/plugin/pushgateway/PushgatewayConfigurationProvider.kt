package com.cdsap.talaiot.plugin.pushgateway

import com.cdsap.talaiot.logger.LogTrackerImpl
import com.cdsap.talaiot.provider.PublisherConfigurationProvider
import com.cdsap.talaiot.publisher.Publisher
import com.cdsap.talaiot.publisher.pushgateway.PushGatewayFormatter
import com.cdsap.talaiot.publisher.pushgateway.PushGatewayPublisher
import com.cdsap.talaiot.request.SimpleRequest
import org.gradle.api.Project
import java.util.concurrent.Executors

class PushgatewayConfigurationProvider(
    val project: Project
) : PublisherConfigurationProvider {
    override fun get(): List<Publisher> {
        val publishers = mutableListOf<Publisher>()
        val talaiotExtension = project.extensions.getByName("talaiot") as PushgatewayExtension

        talaiotExtension.publishers?.apply {
            val logger = LogTrackerImpl(talaiotExtension.logger)
            PushGatewayFormatter()
            publishers.add(
                PushGatewayPublisher(
                    this.pushGatewayPublisher!!,
                    LogTrackerImpl(talaiotExtension.logger),
                    SimpleRequest(logger),
                    Executors.newSingleThreadExecutor(),
                    PushGatewayFormatter()
                )
            )
            publishers.addAll(customPublishers)
        }
        return publishers

    }
}