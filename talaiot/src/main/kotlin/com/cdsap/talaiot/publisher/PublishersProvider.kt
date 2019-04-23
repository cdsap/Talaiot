package com.cdsap.talaiot.publisher

import com.cdsap.talaiot.TalaiotExtension
import com.cdsap.talaiot.publisher.graphpublisher.GraphPublisherFactoryImpl
import com.cdsap.talaiot.logger.LogTrackerImpl
import com.cdsap.talaiot.request.SimpleRequest
import org.gradle.api.Project
import java.util.concurrent.Executors

class PublishersProvider(val project: Project) {
    val executor = Executors.newSingleThreadExecutor()
    fun get(): List<Publisher> {
        val publishers = mutableListOf<Publisher>()
        val talaiotExtension = project.extensions.getByName("talaiot") as TalaiotExtension
        val logger = LogTrackerImpl(talaiotExtension.logger)

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