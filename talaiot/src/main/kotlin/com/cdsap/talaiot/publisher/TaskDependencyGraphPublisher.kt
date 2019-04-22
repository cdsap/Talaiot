package com.cdsap.talaiot.publisher

import com.cdsap.talaiot.configuration.TaskDependencyGraphConfiguration
import com.cdsap.talaiot.entities.TaskMeasurementAggregated
import com.cdsap.talaiot.logger.LogTracker
import com.cdsap.talaiot.composer.DotComposer
import com.cdsap.talaiot.composer.GexfComposer
import com.cdsap.talaiot.composer.HtmlComposer
import com.cdsap.talaiot.writer.DefaultWriter
import com.cdsap.talaiot.writer.DotWriter
import org.gradle.api.Project
import java.util.concurrent.Executor

class TaskDependencyGraphPublisher(
    val project: Project,
    private val graphConfiguration: TaskDependencyGraphConfiguration,
    val logTracker: LogTracker,
    private val executor: Executor
) : Publisher {

    override fun publish(measurementAggregated: TaskMeasurementAggregated) {
        logTracker.log("================")
        logTracker.log("TaskDependencyGraphPublisher")
        logTracker.log("================")

        graphConfiguration.apply {
            if (ignoreWhen == null || ignoreWhen?.shouldIgnore() == false) {
                if (html) {
                    logTracker.log("Html Output enabled")
                    HtmlComposer(logTracker, DefaultWriter(project), executor)
                }
                if (gexf) {
                    logTracker.log("Gexf Output enabled")
                    GexfComposer(logTracker, DefaultWriter(project), executor)
                        .compose(measurementAggregated)
                }
                if (dot) {
                    logTracker.log("Dot Output enabled")
                    DotComposer(logTracker, DotWriter(project), executor)
                        .compose(measurementAggregated)
                }
            } else {
                logTracker.log("Execution ignored")

            }
        }
    }
}
