package com.cdsap.talaiot.publisher

import com.cdsap.talaiot.TalaiotExtension
import com.cdsap.talaiot.publisher.graphpublisher.GraphPublisherFactoryImpl
import com.cdsap.talaiot.logger.LogTrackerImpl
import com.cdsap.talaiot.request.SimpleRequest
import org.gradle.api.Project
import java.util.concurrent.Executors

/**
 * Provides the Publishers defined in the  PublisherConfiguration of the Extension
 */
class PublishersProvider(
    /**
     * Gradle Project used to retrieve the extension
     */
    val project: Project
) {

    /**
     * Check the main TalaiotExtension which Publishers have been enabled.
     * When one publisher is enabled it initialize it with the required parameters
     * @return list of available Publisher for the configuration
     */
    fun get(): List<Publisher> {
        val publishers = mutableListOf<Publisher>()
        val talaiotExtension = project.extensions.getByName("talaiot") as TalaiotExtension
        val logger = LogTrackerImpl(talaiotExtension.logger)
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
                        SimpleRequest(logger),
                        executor
                    )
                )
            }
            taskDependencyGraphPublisher?.apply {
                publishers.add(
                    TaskDependencyGraphPublisher(
                        project,
                        this,
                        logger,
                        executor,
                        GraphPublisherFactoryImpl()
                    )
                )
            }

            customPublisher?.apply {
                publishers.add(this)
            }
        }
        return publishers
    }
}