package com.cdsap.talaiot.publisher

import com.cdsap.talaiot.TalaiotExtension
import com.cdsap.talaiot.logger.LogTrackerImpl
import com.cdsap.talaiot.request.SimpleRequest
import com.cdsap.talaiot.publisher.taskdependencygraph.TaskDependencyGraphPublisher
import com.cdsap.talaiot.writer.FileWriter
import org.gradle.api.Project
import java.util.concurrent.Executors

class PublishersProvider(val project: Project) {
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
                        Executors.newSingleThreadExecutor()
                    )
                )
            }
            taskDependencyGraphPublisher?.apply {
                publishers.add(
                    TaskDependencyGraphPublisher(
                        this,
                        FileWriter(project),
                        logger
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