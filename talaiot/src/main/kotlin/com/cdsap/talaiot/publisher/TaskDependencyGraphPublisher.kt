package com.cdsap.talaiot.publisher

import com.cdsap.talaiot.composer.*
import com.cdsap.talaiot.configuration.TaskDependencyGraphConfiguration
import com.cdsap.talaiot.entities.TaskMeasurementAggregated
import com.cdsap.talaiot.logger.LogTracker
import com.cdsap.talaiot.writer.DefaultWriter
import com.cdsap.talaiot.writer.DotWriter
import org.gradle.api.Project
import java.util.concurrent.Executor

class TaskDependencyGraphPublisher(
    val project: Project,
    private val graphConfiguration: TaskDependencyGraphConfiguration,
    val logTracker: LogTracker,
    private val executor: Executor,
    private val composerFactory: ComposerFactory
) : Publisher {

    override fun publish(measurementAggregated: TaskMeasurementAggregated) {
        logTracker.log("================")
        logTracker.log("TaskDependencyGraphPublisher")
        logTracker.log("================")
        val listOfComposer = mutableListOf<Composer>()
        graphConfiguration.apply {
            if (ignoreWhen == null || ignoreWhen?.shouldIgnore() == false) {
                if (html) {
                    logTracker.log("Html Output enabled")
                    listOfComposer.add(
                        composerFactory.createComposer(
                            ComposerType.HTML,
                            logTracker, DefaultWriter(project), executor
                        )
                    )
                }
                if (gexf) {
                    logTracker.log("Gexf Output enabled")
                    listOfComposer.add(
                        composerFactory.createComposer(
                            ComposerType.GEXF,
                            logTracker, DefaultWriter(project), executor
                        )
                    )
                }
                if (dot) {
                    logTracker.log("Dot Output enabled")
                    listOfComposer.add(
                        composerFactory.createComposer(
                            ComposerType.DOT,
                            logTracker, DotWriter(project), executor
                        )
                    )
                }

                listOfComposer.forEach {
                    it.compose(measurementAggregated)
                }

            } else {
                logTracker.log("Execution ignored")

            }
        }
    }
}
