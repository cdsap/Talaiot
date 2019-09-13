package com.cdsap.talaiot.provider

import com.cdsap.talaiot.TalaiotExtension
import com.cdsap.talaiot.logger.LogTracker
import com.cdsap.talaiot.publisher.*
import com.cdsap.talaiot.publisher.graphpublisher.GraphPublisherFactoryImpl
import com.cdsap.talaiot.publisher.timeline.TimelinePublisher
import com.cdsap.talaiot.request.SimpleRequest
import org.gradle.api.Project
import java.util.concurrent.Executors

/**
 * Provides the [Publisher]s defined in the [com.cdsap.talaiot.configuration.PublishersConfiguration] of the [TalaiotExtension]
 */
class PublishersProvider(
    /**
     * Gradle Project used to retrieve the extension
     */
    private val project: Project,
    private val logger: LogTracker

) : Provider<List<Publisher>> {

    /**
     * Check the main [TalaiotExtension] which publishers have been enabled.
     * When one publisher is enabled it initialize it with the required parameters
     *
     * @return list of available Publisher for the configuration
     */
    override fun get(): List<Publisher> {
        val publishers = mutableListOf<Publisher>()
        val talaiotExtension = project.extensions.getByName("talaiot") as TalaiotExtension
        val executor = Executors.newSingleThreadExecutor()

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
                        executor,
                        GraphPublisherFactoryImpl()
                    )
                )
            }
            pushGatewayPublisher?.apply {
                publishers.add(
                    PushGatewayPublisher(
                        this,
                        logger,
                        SimpleRequest(logger),
                        executor
                    )
                )
            }
            if (jsonPublisher) {
                publishers.add(JsonPublisher(project.gradle))
            }

            if (timelinePublisher) {
                publishers.add(TimelinePublisher(project.gradle))
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

            customPublisher?.apply {
                this.publisher?.apply {
                    publishers.add(this)

                }
            }
        }
        return publishers
    }
}