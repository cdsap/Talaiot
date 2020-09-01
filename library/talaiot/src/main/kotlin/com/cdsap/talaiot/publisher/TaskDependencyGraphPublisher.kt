package com.cdsap.talaiot.publisher

import com.cdsap.talaiot.configuration.TaskDependencyGraphConfiguration
import com.cdsap.talaiot.entities.ExecutionReport
import com.cdsap.talaiot.logger.LogTracker
import com.cdsap.talaiot.publisher.graphpublisher.DiskPublisher
import com.cdsap.talaiot.publisher.graphpublisher.GraphPublisherFactory
import com.cdsap.talaiot.publisher.graphpublisher.GraphPublisherType
import com.cdsap.talaiot.writer.DotWriter
import com.cdsap.talaiot.writer.TaskGraphWriter
import java.util.concurrent.Executor

/**
 * Publisher used to export the task dependency graph in a DAG with different formats.
 * Current formats supported: Html, Gexf and PNG(dot format)
 */
open class TaskDependencyGraphPublisher(
    /**
     * General configuration for the publisher
     */
    private val graphConfiguration: TaskDependencyGraphConfiguration,
    /**
     * LogTracker to print in console depending on the Mode
     */
    private val logTracker: LogTracker,
    /**
     * Executor to schedule a task in Background
     */
    private val executor: Executor,
    /**
     * Factory to create instance of the different publishers
     */
    private val graphPublisherFactory: GraphPublisherFactory
) : Publisher {

    private val TAG = "TaskDependencyGraphPublisher"

    override fun publish(report: ExecutionReport) {
        logTracker.log(TAG, "================")
        logTracker.log(TAG, "TaskDependencyGraphPublisher")
        logTracker.log(TAG, "================")
        val listOfPublishers = mutableListOf<DiskPublisher>()
        graphConfiguration.apply {
            if (ignoreWhen == null || ignoreWhen?.shouldIgnore() == false) {
                if (html) {
                    logTracker.log(TAG, "Html Output enabled")
                    listOfPublishers.add(
                        graphPublisherFactory.createPublisher(
                            GraphPublisherType.HTML,
                            logTracker, TaskGraphWriter(project, logTracker), executor
                        )
                    )
                }
                if (gexf) {
                    logTracker.log(TAG, "Gexf Output enabled")
                    listOfPublishers.add(
                        graphPublisherFactory.createPublisher(
                            GraphPublisherType.GEXF,
                            logTracker, TaskGraphWriter(project, logTracker), executor
                        )
                    )
                }
                if (dot) {
                    logTracker.log(TAG, "Dot Output enabled")
                    listOfPublishers.add(
                        graphPublisherFactory.createPublisher(
                            GraphPublisherType.DOT,
                            logTracker, DotWriter(project, logTracker), executor
                        )
                    )
                }

                val filteredReport = report.copy(tasks = report.unfilteredTasks)
                listOfPublishers.forEach {
                    it.publish(filteredReport)
                }

            } else {
                logTracker.log(TAG, "Execution ignored")

            }
        }
    }
}
