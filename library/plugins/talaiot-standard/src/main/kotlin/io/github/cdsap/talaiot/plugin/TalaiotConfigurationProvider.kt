package io.github.cdsap.talaiot.plugin

import io.github.cdsap.talaiot.logger.LogTrackerImpl
import io.github.cdsap.talaiot.provider.PublisherConfigurationProvider
import io.github.cdsap.talaiot.publisher.JsonPublisher
import io.github.cdsap.talaiot.publisher.OutputPublisher
import io.github.cdsap.talaiot.publisher.Publisher
import io.github.cdsap.talaiot.publisher.elasticsearch.ElasticSearchPublisher
import io.github.cdsap.talaiot.publisher.graph.GraphPublisherFactoryImpl
import io.github.cdsap.talaiot.publisher.graph.TaskDependencyGraphPublisher
import io.github.cdsap.talaiot.publisher.hybrid.HybridPublisher
import io.github.cdsap.talaiot.publisher.influxdb.InfluxDbPublisher
import io.github.cdsap.talaiot.publisher.pushgateway.PushGatewayPublisher
import io.github.cdsap.talaiot.publisher.rethinkdb.RethinkDbPublisher
import io.github.cdsap.talaiot.publisher.timeline.TimelinePublisher
import org.gradle.api.Project
import java.util.concurrent.Executors

class TalaiotConfigurationProvider(
    val project: Project
) : PublisherConfigurationProvider {
    override fun get(): List<Publisher> {
        val publishers = mutableListOf<Publisher>()
        val talaiotExtension = project.extensions.getByName("talaiot") as TalaiotPluginExtension
        val logger = LogTrackerImpl(talaiotExtension.logger)
        val executor = Executors.newSingleThreadExecutor()
        val heavyExecutor = Executors.newSingleThreadExecutor()
        talaiotExtension.publishers?.apply {
            outputPublisher?.apply {
                publishers.add(OutputPublisher(this, logger))
            }

            influxDbPublisher?.apply {
                publishers.add(
                    InfluxDbPublisher(
                        this,
                        logger,
                        executor
                    )
                )
            }
            taskDependencyGraphPublisher?.apply {
                publishers.add(
                    TaskDependencyGraphPublisher(
                        this,
                        logger,
                        heavyExecutor,
                        GraphPublisherFactoryImpl()
                    )
                )
            }
            pushGatewayPublisher?.apply {
                publishers.add(
                    PushGatewayPublisher(
                        this,
                        logger,
                        executor
                    )
                )
            }
            if (jsonPublisher) {
                publishers.add(JsonPublisher(project.gradle.rootProject.buildDir))
            }

            if (timelinePublisher) {
                publishers.add(TimelinePublisher(project.gradle.rootProject.buildDir))
            }

            elasticSearchPublisher?.apply {
                publishers.add(
                    ElasticSearchPublisher(
                        this,
                        logger,
                        executor
                    )
                )
            }

            hybridPublisher?.apply {
                publishers.add(
                    HybridPublisher(
                        this,
                        logger,
                        executor
                    )
                )
            }

            rethinkDbPublisher?.apply {
                publishers.add(
                    RethinkDbPublisher(
                        this,
                        logger,
                        executor
                    )
                )
            }

            publishers.addAll(customPublishers)
        }
        return publishers
    }
}
