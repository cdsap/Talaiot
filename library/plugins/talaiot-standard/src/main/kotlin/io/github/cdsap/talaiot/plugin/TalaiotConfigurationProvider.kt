package io.github.cdsap.talaiot.plugin

import io.github.cdsap.talaiot.logger.LogTrackerImpl
import io.github.cdsap.talaiot.provider.PublisherConfigurationProvider
import io.github.cdsap.talaiot.publisher.JsonPublisher
import io.github.cdsap.talaiot.publisher.OutputPublisher
import io.github.cdsap.talaiot.publisher.Publisher
import io.github.cdsap.talaiot.publisher.elasticsearch.ElasticSearchPublisher
import io.github.cdsap.talaiot.publisher.hybrid.HybridPublisher
import io.github.cdsap.talaiot.publisher.influxdb.InfluxDbPublisher
import io.github.cdsap.talaiot.publisher.pushgateway.PushGatewayPublisher
import io.github.cdsap.talaiot.publisher.rethinkdb.RethinkDbPublisher
import org.gradle.api.Project

class TalaiotConfigurationProvider(
    val project: Project
) : PublisherConfigurationProvider {
    override fun get(): List<Publisher> {
        val publishers = mutableListOf<Publisher>()
        val talaiotExtension = project.extensions.getByName("talaiot") as TalaiotPluginExtension
        val logger = LogTrackerImpl(talaiotExtension.logger)

        talaiotExtension.publishers?.apply {
            outputPublisher?.apply {
                println("yes")
                publishers.add(OutputPublisher(this, logger))
            }

            influxDbPublisher?.apply {
                publishers.add(
                    InfluxDbPublisher(
                        this, logger
                    )
                )
            }

            pushGatewayPublisher?.apply {
                publishers.add(
                    PushGatewayPublisher(
                        this, logger
                    )
                )
            }
            if (jsonPublisher) {
                publishers.add(JsonPublisher(project.gradle.rootProject.buildDir))
            }

            elasticSearchPublisher?.apply {
                publishers.add(
                    ElasticSearchPublisher(
                        this, logger
                    )
                )
            }

            hybridPublisher?.apply {
                publishers.add(
                    HybridPublisher(
                        this, logger
                    )
                )
            }

            rethinkDbPublisher?.apply {
                publishers.add(
                    RethinkDbPublisher(
                        this, logger
                    )
                )
            }
        }
        return publishers
    }
}
