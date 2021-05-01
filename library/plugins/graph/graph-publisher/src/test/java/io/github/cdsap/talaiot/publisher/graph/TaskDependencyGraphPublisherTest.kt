package io.github.cdsap.talaiot.publisher.graph

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.argThat
import com.nhaarman.mockitokotlin2.atLeast
import com.nhaarman.mockitokotlin2.doNothing
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.github.cdsap.talaiot.logger.LogTracker
import io.github.cdsap.talaiot.publisher.graph.TaskMeasurementAggregatedMock.taskMeasurementAggregatedWrongMetricsFormat
import io.kotlintest.specs.BehaviorSpec
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
            val graphPublisherFactory: GraphPublisherFactory = mock()
            val taskDependencyGraphPublisher =
                getGraphPublisher(taskDependencyGraphPublisherConfiguration, graphPublisherFactory)
            taskDependencyGraphPublisher.publish(
                taskMeasurementAggregatedWrongMetricsFormat()
            )
            then("no publisher is created ") {

                verify(graphPublisherFactory, never()).createPublisher(
                    argThat { this == GraphPublisherType.GEXF },
                    any(),
                    any(),
                    any()
                )
                verify(graphPublisherFactory, never()).createPublisher(
                    argThat { this == GraphPublisherType.DOT },
                    any(),
                    any(),
                    any()
                )
                verify(graphPublisherFactory, never()).createPublisher(
                    argThat { this == GraphPublisherType.HTML },
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
            val graphPublisherFactory: GraphPublisherFactory = mock()
            val taskDependencyGraphPublisher =
                getGraphPublisher(taskDependencyGraphPublisherConfiguration, graphPublisherFactory)

            taskDependencyGraphPublisher.publish(
                taskMeasurementAggregatedWrongMetricsFormat()
            )

            then("HtmlPublisher is called") {
                verify(graphPublisherFactory).createPublisher(
                    argThat { this == GraphPublisherType.HTML },
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
            val graphPublisherFactory: GraphPublisherFactory = mock()
            val taskDependencyGraphPublisher =
                getGraphPublisher(taskDependencyGraphPublisherConfiguration, graphPublisherFactory)
            taskDependencyGraphPublisher.publish(
                taskMeasurementAggregatedWrongMetricsFormat()
            )

            then("GexfPublisher is called") {
                verify(graphPublisherFactory).createPublisher(
                    argThat { this == GraphPublisherType.GEXF },
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
            val graphPublisherFactory: GraphPublisherFactory = mock()
            val taskDependencyGraphPublisher =
                getGraphPublisher(taskDependencyGraphPublisherConfiguration, graphPublisherFactory)
            taskDependencyGraphPublisher.publish(
                taskMeasurementAggregatedWrongMetricsFormat()
            )

            then("DotPublisher is called") {
                verify(graphPublisherFactory).createPublisher(
                    argThat { this == GraphPublisherType.DOT },
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
            val graphPublisherFactory: GraphPublisherFactory = mock()
            val taskDependencyGraphPublisher =
                getGraphPublisher(taskDependencyGraphPublisherConfiguration, graphPublisherFactory)
            taskDependencyGraphPublisher.publish(
                taskMeasurementAggregatedWrongMetricsFormat()
            )

            then("All publishers are called") {
                verify(graphPublisherFactory, atLeast(1)).createPublisher(
                    argThat { this == GraphPublisherType.GEXF },
                    any(),
                    any(),
                    any()
                )
                verify(graphPublisherFactory, atLeast(1)).createPublisher(
                    argThat { this == GraphPublisherType.DOT },
                    any(),
                    any(),
                    any()
                )
                verify(graphPublisherFactory, atLeast(1)).createPublisher(
                    argThat { this == GraphPublisherType.HTML },
                    any(),
                    any(),
                    any()
                )
            }
        }
    }
})

private fun getGraphPublisher(
    taskDependencyGraphPublisherConfiguration: TaskDependencyGraphConfiguration,
    graphPublisherFactory: GraphPublisherFactory
): TaskDependencyGraphPublisher {
    val testExecutor: Executor = mock()
    val logger: LogTracker = mock()
    val publisher: DiskPublisher = mock()
    whenever(graphPublisherFactory.createPublisher(any(), any(), any(), any())).thenReturn(publisher)
    doNothing().`when`(publisher).publish(any())

    return TaskDependencyGraphPublisher(
        taskDependencyGraphPublisherConfiguration,
        logger,
        testExecutor,
        graphPublisherFactory
    )
}
