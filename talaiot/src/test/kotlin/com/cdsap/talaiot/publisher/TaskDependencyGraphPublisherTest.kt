package com.cdsap.talaiot.publisher

import com.cdsap.talaiot.composer.Composer
import com.cdsap.talaiot.composer.ComposerFactory
import com.cdsap.talaiot.composer.ComposerType
import com.cdsap.talaiot.configuration.TaskDependencyGraphConfiguration
import com.cdsap.talaiot.entities.TaskLength
import com.cdsap.talaiot.entities.TaskMeasurementAggregated
import com.cdsap.talaiot.entities.TaskMessageState
import com.cdsap.talaiot.logger.LogTracker
import com.nhaarman.mockitokotlin2.*
import io.kotlintest.specs.BehaviorSpec
import org.gradle.api.Project
import org.gradle.kotlin.dsl.extra
import org.gradle.testfixtures.ProjectBuilder
import java.util.concurrent.Executor

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
            val composerFactory: ComposerFactory = mock()
            val taskDependencyGraphPublisher =
                getGraphPublisher(project, taskDependencyGraphPublisherConfiguration, composerFactory)
            taskDependencyGraphPublisher.publish(
                taskMeasurementAggregated()
            )
            then("no composer is instanciated ") {

                verify(composerFactory, never()).createComposer(
                    argThat { this == ComposerType.GEXF },
                    any(),
                    any(),
                    any()
                )
                verify(composerFactory, never()).createComposer(
                    argThat { this == ComposerType.DOT },
                    any(),
                    any(),
                    any()
                )
                verify(composerFactory, never()).createComposer(
                    argThat { this == ComposerType.HTML },
                    any(),
                    any(),
                    any()
                )

            }
        }
        `when`("configuration shouldn't be ignored and html is enabled") {
            val project = ProjectBuilder.builder().build()
            val taskDependencyGraphPublisherConfiguration = TaskDependencyGraphConfiguration(project).apply {
                html = true
            }
            val composerFactory: ComposerFactory = mock()
            val taskDependencyGraphPublisher =
                getGraphPublisher(project, taskDependencyGraphPublisherConfiguration, composerFactory)

            taskDependencyGraphPublisher.publish(
                taskMeasurementAggregated()
            )

            then("HtmlComposer is called") {
                verify(composerFactory).createComposer(
                    argThat { this == ComposerType.HTML },
                    any(),
                    any(),
                    any()
                )

            }

        }
        `when`("configuration shouldn't be ignored and gexf is enabled") {
            val project = ProjectBuilder.builder().build()
            val taskDependencyGraphPublisherConfiguration = TaskDependencyGraphConfiguration(project).apply {
                gexf = true
            }
            val composerFactory: ComposerFactory = mock()
            val taskDependencyGraphPublisher =
                getGraphPublisher(project, taskDependencyGraphPublisherConfiguration, composerFactory)
            taskDependencyGraphPublisher.publish(
                taskMeasurementAggregated()
            )

            then("GexfComposer is called") {
                verify(composerFactory).createComposer(
                    argThat { this == ComposerType.GEXF },
                    any(),
                    any(),
                    any()
                )
            }

        }
        `when`("configuration shouldn't be ignored and dot is enabled") {
            val project = ProjectBuilder.builder().build()
            val taskDependencyGraphPublisherConfiguration = TaskDependencyGraphConfiguration(project).apply {
                dot = true
            }
            val composerFactory: ComposerFactory = mock()
            val taskDependencyGraphPublisher =
                getGraphPublisher(project, taskDependencyGraphPublisherConfiguration, composerFactory)
            taskDependencyGraphPublisher.publish(
                taskMeasurementAggregated()
            )

            then("DotComposer is called") {
                verify(composerFactory).createComposer(
                    argThat { this == ComposerType.DOT },
                    any(),
                    any(),
                    any()
                )
            }
        }


        `when`("configuration shouldn't be ignored and all options are enabled") {
            val project = ProjectBuilder.builder().build()
            val taskDependencyGraphPublisherConfiguration = TaskDependencyGraphConfiguration(project).apply {
                gexf = true
                html = true
                dot = true
            }
            val composerFactory: ComposerFactory = mock()
            val taskDependencyGraphPublisher =
                getGraphPublisher(project, taskDependencyGraphPublisherConfiguration, composerFactory)
            taskDependencyGraphPublisher.publish(
                taskMeasurementAggregated()
            )

            then("All Composers are called") {
                verify(composerFactory, atLeast(1)).createComposer(
                    argThat { this == ComposerType.GEXF },
                    any(),
                    any(),
                    any()
                )
                verify(composerFactory, atLeast(1)).createComposer(
                    argThat { this == ComposerType.DOT },
                    any(),
                    any(),
                    any()
                )
                verify(composerFactory, atLeast(1)).createComposer(
                    argThat { this == ComposerType.HTML },
                    any(),
                    any(),
                    any()
                )
            }

        }
    }

})


private fun getGraphPublisher(
    project: Project,
    taskDependencyGraphPublisherConfiguration: TaskDependencyGraphConfiguration,
    composerFactory: ComposerFactory
): TaskDependencyGraphPublisher {
    val testExecutor: Executor = mock()
    val logger: LogTracker = mock()
    val composer: Composer = mock()
    whenever(composerFactory.createComposer(any(), any(), any(), any())).thenReturn(composer)
    doNothing().`when`(composer).compose(any())

    return TaskDependencyGraphPublisher(
        project,
        taskDependencyGraphPublisherConfiguration,
        logger,
        testExecutor,
        composerFactory
    )
}

private fun taskMeasurementAggregated(): TaskMeasurementAggregated {
    return TaskMeasurementAggregated(
        mapOf(
            "me=tric1" to "va====lue1",
            "metric2" to "val,,   , ue2"
        ), listOf(TaskLength(1, "clean", ":clean", TaskMessageState.EXECUTED, true, "app", emptyList()))
    )
}

