package com.cdsap.talaiot.provider

import com.cdsap.talaiot.TalaiotExtension
import com.cdsap.talaiot.logger.LogTracker
import com.cdsap.talaiot.publisher.graphpublisher.GraphPublisherFactoryImpl
import com.cdsap.talaiot.publisher.InfluxDbPublisher
import com.cdsap.talaiot.publisher.OutputPublisher
import com.cdsap.talaiot.publisher.Publisher
import com.cdsap.talaiot.publisher.TaskDependencyGraphPublisher
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
    val project: Project,
    val logger: LogTracker

) : Provider<List<Publisher>> {

    /**
     * Check the main TalaiotExtension which Publishers have been enabled.
     * When one publisher is enabled it initialize it with the required parameters
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
                        SimpleRequest(logger),
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

            customPublisher?.apply {
                publishers.add(this)
            }
        }
        return publishers
    }
}