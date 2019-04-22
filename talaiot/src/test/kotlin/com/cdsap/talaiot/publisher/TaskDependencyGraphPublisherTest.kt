package com.cdsap.talaiot.publisher

import com.cdsap.talaiot.configuration.TaskDependencyGraphConfiguration
import com.cdsap.talaiot.entities.TaskLength
import com.cdsap.talaiot.entities.TaskMeasurementAggregated
import com.cdsap.talaiot.entities.TaskMessageState
import com.cdsap.talaiot.logger.LogTracker
import com.nhaarman.mockitokotlin2.*
import io.kotlintest.specs.BehaviorSpec
import org.gradle.kotlin.dsl.extra
import org.gradle.testfixtures.ProjectBuilder
import java.util.concurrent.Executor
import java.util.concurrent.ExecutorService

class TaskDependencyGraphPublisherTest : BehaviorSpec({
    given("TaskDependencyGraphPublisher configuration") {
        `when`("configuration should be ignored ") {
            val project = ProjectBuilder.builder().build()
            project.extra.set("test", "true")
            val taskDependencyGraphPublisherConfiguration = TaskDependencyGraphConfiguration(project).apply {
                ignoreWhen {
                    envName = "test"
                    envValue = "true"
                }
                html = true
                gexf = true
            }
            val logger: LogTracker = mock()
            val taskDependencyGraphPublisher = TaskDependencyGraphPublisher(
                project,
                taskDependencyGraphPublisherConfiguration,
                logger,
                TestExecutor()
            )

            then("no composer is instanciated ") {
                taskDependencyGraphPublisher.publish(
                    taskMeasurementAggregated()
                )

                verify(logger).log("TaskDependencyGraphPublisher")
                verify(logger, times(2)).log("================")
                verify(logger).log("Execution ignored")
                verifyNoMoreInteractions(logger)
            }
        }
        `when`("configuration shouldn't be ignored and html is enabled") {
            val project = ProjectBuilder.builder().build()
            val taskDependencyGraphPublisherConfiguration = TaskDependencyGraphConfiguration(project).apply {
                html = true
            }
            val testExecutor: Executor = mock()
            val logger: LogTracker = mock()
            val taskDependencyGraphPublisher = TaskDependencyGraphPublisher(
                project,
                taskDependencyGraphPublisherConfiguration,
                logger,
                testExecutor
            )
            then("HtmlComposer is called") {
                taskDependencyGraphPublisher.publish(
                    taskMeasurementAggregated()
                )
                verify(logger).log("Html Output enabled")
            }

        }
        `when`("configuration shouldn't be ignored and gexf is enabled") {
            val project = ProjectBuilder.builder().build()
            val taskDependencyGraphPublisherConfiguration = TaskDependencyGraphConfiguration(project).apply {
                gexf = true
            }
            val testExecutor: Executor = mock()
            val logger: LogTracker = mock()
            val taskDependencyGraphPublisher = TaskDependencyGraphPublisher(
                project,
                taskDependencyGraphPublisherConfiguration,
                logger,
                testExecutor
            )
            then("GexfComposer is called") {
                taskDependencyGraphPublisher.publish(
                    taskMeasurementAggregated()
                )
                verify(logger).log("Gexf Output enabled")
            }

        }
        `when`("configuration shouldn't be ignored and dot is enabled") {
            val project = ProjectBuilder.builder().build()
            val taskDependencyGraphPublisherConfiguration = TaskDependencyGraphConfiguration(project).apply {
                dot = true
            }
            val testExecutor: Executor = mock()
            val logger: LogTracker = mock()
            val taskDependencyGraphPublisher = TaskDependencyGraphPublisher(
                project,
                taskDependencyGraphPublisherConfiguration,
                logger,
                testExecutor
            )
            then("DotComposer is called") {
                taskDependencyGraphPublisher.publish(
                    taskMeasurementAggregated()
                )
                verify(logger).log("Dot Output enabled")
            }

        }
        `when`("configuration shouldn't be ignored and all options are enabled") {
            val project = ProjectBuilder.builder().build()
            val taskDependencyGraphPublisherConfiguration = TaskDependencyGraphConfiguration(project).apply {
                gexf = true
                html = true
                dot = true
            }
            val testExecutor: Executor = mock()
            val logger: LogTracker = mock()
            val taskDependencyGraphPublisher = TaskDependencyGraphPublisher(
                project,
                taskDependencyGraphPublisherConfiguration,
                logger,
                testExecutor
            )
            then("All Composers are called") {
                taskDependencyGraphPublisher.publish(
                    taskMeasurementAggregated()
                )
                verify(logger).log("Dot Output enabled")
                verify(logger).log("Html Output enabled")
                verify(logger).log("Gexf Output enabled")
            }

        }
    }

})

private fun taskMeasurementAggregated(): TaskMeasurementAggregated {
    return TaskMeasurementAggregated(
        mapOf(
            "me=tric1" to "va====lue1",
            "metric2" to "val,,   , ue2"
        ), listOf(TaskLength(1, "clean", ":clean", TaskMessageState.EXECUTED, true, "app", emptyList()))
    )
}

