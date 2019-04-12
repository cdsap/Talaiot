package com.cdsap.talaiot.publisher

import com.cdsap.talaiot.TalaiotExtension
import com.cdsap.talaiot.logger.LogTrackerImpl
import com.cdsap.talaiot.request.SimpleRequest
import com.cdsap.talaiot.publisher.taskDependencyGraph.TaskDependencyGraphPublisher
import com.cdsap.talaiot.wrotter.Writter
import org.gradle.api.Project
import java.util.concurrent.ExecutorService
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
//            neo4jPublisher?.apply {
//                publishers.add(
//                    Neo4jPublisher
//                        (
//                        this,
//                        logger
//                    )
//                )
//            }
//
            taskDependencyGraphPublisher?.apply {
                publishers.add(
                    TaskDependencyGraphPublisher
                        (
                        Writter(project)

                    )
                )
            }
//
//            gefxPublisher?.apply {
//                publishers.add(
//                    GefxPublisher
//                        (
//                        Writter(project)
//
//                    )
//                )
//            }



            customPublisher?.apply {
                publishers.add(this)
            }
        }
        return publishers
    }
}