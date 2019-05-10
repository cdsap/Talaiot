package com.cdsap.talaiot.publisher

import com.cdsap.talaiot.configuration.TaskDependencyGraphConfiguration
import com.cdsap.talaiot.entities.TaskMeasurementAggregated
import com.cdsap.talaiot.logger.LogTracker
import com.cdsap.talaiot.publisher.graphpublisher.DiskPublisher
import com.cdsap.talaiot.publisher.graphpublisher.GraphPublisherFactory
import com.cdsap.talaiot.publisher.graphpublisher.GraphPublisherType
import com.cdsap.talaiot.writer.DefaultWriter
import com.cdsap.talaiot.writer.DotWriter
import org.gradle.api.Project
import java.util.concurrent.Executor

/**
 * Publisher used to export the task dependency graph in a DAG with different formats.
 * Current formats supported: Html, Gexf and PNG(dot format)
 */
class TaskDependencyGraphPublisher(
    /**
     * Gradle Project used to retrieve build and root information
     */
    val project: Project,
    /**
     * General configuration for the publisher
     */
    private val graphConfiguration: TaskDependencyGraphConfiguration,
    /**
     * LogTracker to print in console depending on the Mode
     */
    val logTracker: LogTracker,
    /**
     * Executor to schedule a task in Background
     */
    private val executor: Executor,
    /**
     * Factory to create instance of the different publishers
     */
    private val graphPublisherFactory: GraphPublisherFactory
) : Publisher {

    override fun publish(taskMeasurementAggregated: TaskMeasurementAggregated) {
        logTracker.log("================")
        logTracker.log("TaskDependencyGraphPublisher")
        logTracker.log("================")
        val listOfPublishers = mutableListOf<DiskPublisher>()
        graphConfiguration.apply {
            if (ignoreWhen == null || ignoreWhen?.shouldIgnore() == false) {
                if (html) {
                    logTracker.log("Html Output enabled")
                    listOfPublishers.add(
                        graphPublisherFactory.createPublisher(
                            GraphPublisherType.HTML,
                            logTracker, DefaultWriter(project, logTracker), executor
                        )
                    )
                }
                if (gexf) {
                    logTracker.log("Gexf Output enabled")
                    listOfPublishers.add(
                        graphPublisherFactory.createPublisher(
                            GraphPublisherType.GEXF,
                            logTracker, DefaultWriter(project, logTracker), executor
                        )
                    )
                }
                if (dot) {
                    logTracker.log("Dot Output enabled")
                    listOfPublishers.add(
                        graphPublisherFactory.createPublisher(
                            GraphPublisherType.DOT,
                            logTracker, DotWriter(project, logTracker), executor
                        )
                    )
                }

                listOfPublishers.forEach {
                    it.publish(taskMeasurementAggregated)
                }

            } else {
                logTracker.log("Execution ignored")

            }
        }
    }
}
