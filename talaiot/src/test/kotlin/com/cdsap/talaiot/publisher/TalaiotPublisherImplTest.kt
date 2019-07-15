package com.cdsap.talaiot.publisher

import com.cdsap.talaiot.TalaiotExtension
import com.cdsap.talaiot.configuration.MetricsConfiguration
import com.cdsap.talaiot.configuration.OutputPublisherConfiguration
import com.cdsap.talaiot.entities.ExecutionReport
import com.cdsap.talaiot.entities.TaskLength
import com.cdsap.talaiot.entities.TaskMessageState
import com.cdsap.talaiot.logger.LogTrackerImpl
import com.cdsap.talaiot.provider.Provider
import com.nhaarman.mockitokotlin2.argThat
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.kotlintest.specs.BehaviorSpec
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionContainer


class TalaiotPublisherImplTest : BehaviorSpec({

    given("TalaiotPublisher implementation") {
        val logger = LogTrackerImpl()

        `when`("publisher is included and one task is executed") {
            val project: Project = mock()
            val extension = TalaiotExtension(project).apply {
                publishers {
                    outputPublisher
                }
                metricsConfiguration()
            }

            setUpMockExtension(project, extension)

            val publishers: Provider<List<Publisher>> = mock()
            val outputPublisher: Publisher = mock()
            whenever(publishers.get()).thenReturn(listOf(outputPublisher))


            val publisher = TalaiotPublisherImpl(
                extension, logger, getMetricsProvider(), publishers
            )
            publisher.publish(
                mutableListOf(getSingleTask()),
                0,
                100,
                200,
                true
            )
            then("outputPublisher is publishing one task result ") {
                assert(publishers.get().size == 1)
                verify(publishers.get()[0]).publish(argThat {
                    this.tasks!!.size == 1
                })

            }
        }
        `when`("more than one publisher is included ") {
            val project: Project = mock()
            val extension = TalaiotExtension(project).apply {
                publishers {
                    outputPublisher
                    influxDbPublisher {
                        dbName = "db"
                        url = ""
                        taskMetricName = ""
                    }
                }
                metricsConfiguration()
            }
            setUpMockExtension(project, extension)

            val publishers: Provider<List<Publisher>> = mock()
            val outputPublisher: Publisher = mock()
            val influxDbPublisher: Publisher = mock()

            whenever(publishers.get()).thenReturn(listOf(outputPublisher, influxDbPublisher))

            TalaiotPublisherImpl(extension, logger, getMetricsProvider(), publishers).publish(
                getTasks(),
                0,
                100,
                200,
                true
            )

            then("two publishers are processed ") {
                assert(publishers.get().size == 2)
                verify(publishers.get()[0]).publish(argThat {
                    this.tasks!!.size == 2
                })

            }
        }
        `when`("one filter has been configured") {
            val project: Project = mock()
            val extension = TalaiotExtension(project).apply {
                filter {
                    tasks {
                        excludes = arrayOf("taskA")
                    }
                }
                publishers {
                    outputPublisher
                    influxDbPublisher {
                        dbName = "db"
                        url = ""
                        taskMetricName = ""
                    }
                }

                metricsConfiguration()
            }
            setUpMockExtension(project, extension)

            val publishers: Provider<List<Publisher>> = mock()
            val outputPublisher: Publisher = mock()
            val influxDbPublisher: Publisher = mock()
            whenever(publishers.get()).thenReturn(listOf(outputPublisher, influxDbPublisher))

            TalaiotPublisherImpl(extension, logger, getMetricsProvider(), publishers).publish(
                getTasks(),
                0,
                100,
                200,
                true
            )

            then("two publishers are processed and one task has been filtered ") {
                assert(publishers.get().size == 2)
                verify(publishers.get()[0]).publish(argThat {
                    this.tasks!!.size == 1
                })
            }
        }

        `when`("one filter has been configur2222ed") {
            val project: Project = mock()
            val extension = TalaiotExtension(project).apply {
                filter {
                    tasks {
                        excludes = arrayOf("taskA")
                    }
                }
                publishers {
                    outputPublisher = OutputPublisherConfiguration()
                    influxDbPublisher {
                        dbName = "db"
                        url = ""
                        taskMetricName = ""
                    }
                }

                metricsConfiguration()
            }
            setUpMockExtension(project, extension)

            val publishers: Provider<List<Publisher>> = mock()
            val graph: TaskDependencyGraphPublisher = mock()
            whenever(publishers.get()).thenReturn(listOf(graph))

            TalaiotPublisherImpl(extension, logger, getMetricsProvider(), publishers).publish(
                getTasks(),
                0,
                100,
                200,
                true
            )

            then("two publishers are processed and one task has been filtered ") {
                assert(publishers.get().size == 1)
                verify(publishers.get()[0]).publish(argThat {
                    this.tasks!!.size == 1
                })
            }
        }
    }

})

private fun setUpMockExtension(project: Project, extension: TalaiotExtension) {
    val extensionContainer: ExtensionContainer = mock()
    whenever(project.extensions).thenReturn(extensionContainer)
    whenever(project.name).thenReturn("TestProject")
    whenever(extensionContainer.getByType(TalaiotExtension::class.java)).thenReturn(extension)
    whenever(extensionContainer.getByName("talaiot")).thenReturn(extension)
}

private fun getMetricsProvider(): Provider<ExecutionReport> {
    return object: Provider<ExecutionReport> {
        override fun get() = ExecutionReport()
    }
}

private fun metricsConfiguration() = MetricsConfiguration()

private fun getTasks() = mutableListOf(
    getSingleTask(),
    TaskLength(
        ms = 1L,
        taskName = "taskA",
        taskPath = ":app:clean",
        state = TaskMessageState.EXECUTED,
        rootNode = false,
        module = "app",
        taskDependencies = emptyList()
    )
)

private fun getSingleTask() = TaskLength(
    ms = 1L,
    taskName = "a",
    taskPath = ":app:a",
    state = TaskMessageState.EXECUTED,
    rootNode = false,
    module = "app",
    taskDependencies = emptyList()
)

