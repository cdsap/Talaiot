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

class TaskDependencyGraphPublisher(
    val project: Project,
    private val graphConfiguration: TaskDependencyGraphConfiguration,
    val logTracker: LogTracker,
    private val executor: Executor,
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
                            logTracker, DefaultWriter(project), executor
                        )
                    )
                }
                if (gexf) {
                    logTracker.log("Gexf Output enabled")
                    listOfPublishers.add(
                        graphPublisherFactory.createPublisher(
                            GraphPublisherType.GEXF,
                            logTracker, DefaultWriter(project), executor
                        )
                    )
                }
                if (dot) {
                    logTracker.log("Dot Output enabled")
                    listOfPublishers.add(
                        graphPublisherFactory.createPublisher(
                            GraphPublisherType.DOT,
                            logTracker, DotWriter(project), executor
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
