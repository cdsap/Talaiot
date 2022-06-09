package io.github.cdsap.talaiot

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.argThat
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyZeroInteractions
import com.nhaarman.mockitokotlin2.whenever
import io.github.cdsap.talaiot.configuration.BuildFilterConfiguration
import io.github.cdsap.talaiot.configuration.MetricsConfiguration
import io.github.cdsap.talaiot.entities.ExecutionReport
import io.github.cdsap.talaiot.entities.TaskLength
import io.github.cdsap.talaiot.entities.TaskMessageState
import io.github.cdsap.talaiot.filter.BuildFilterProcessor
import io.github.cdsap.talaiot.filter.TaskFilterProcessor
import io.github.cdsap.talaiot.logger.LogTrackerImpl
import io.github.cdsap.talaiot.plugin.TalaiotPluginExtension
import io.github.cdsap.talaiot.provider.PublisherConfigurationProvider
import io.github.cdsap.talaiot.publisher.OutputPublisherConfiguration
import io.github.cdsap.talaiot.publisher.Publisher
import io.github.cdsap.talaiot.publisher.TalaiotPublisherImpl
import io.kotlintest.shouldBe
import io.kotlintest.specs.BehaviorSpec
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionContainer

class TalaiotPublisherImplTest : BehaviorSpec({

    given("TalaiotPublisher implementation") {
        val logger = LogTrackerImpl()

        `when`("publisher is included and one task is executed") {
            val project: Project = mock()
            val extension = TalaiotPluginExtension(project).apply {
                publishers {
                    outputPublisher
                }
                metricsConfiguration()
            }

            val publishers: PublisherConfigurationProvider = mock()
            val outputPublisher: Publisher = mock()
            whenever(publishers.get()).thenReturn(listOf(outputPublisher))
            val executionReport = ExecutionReport()
            setUpMockExtension(project, extension)

            talaiotPublisherImpl(
                extension, logger, project, executionReport, publishers.get()
            ).publish(
                mutableListOf(getSingleTask()), 0, 100, 200, true
            )
            then("outputPublisher is publishing one task result ") {
                assert(publishers.get().size == 1)
                verify(publishers.get()[0]).publish(argThat { this.tasks!!.size == 1 })
            }
        }
        `when`("more than one publisher is included ") {
            val project: Project = mock()
            val extension = TalaiotPluginExtension(project).apply {
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
            val publishers: PublisherConfigurationProvider = mock()
            val outputPublisher: Publisher = mock()
            val influxDbPublisher: Publisher = mock()
            whenever(publishers.get()).thenReturn(listOf(outputPublisher, influxDbPublisher))
            val executionReport = ExecutionReport()
            setUpMockExtension(project, extension)

            talaiotPublisherImpl(
                extension, logger, project, executionReport, publishers.get()
            ).publish(
                getTasks(), 0, 100, 200, true
            )

            then("two publishers are processed ") {
                assert(publishers.get().size == 2)
                verify(publishers.get()[0]).publish(
                    argThat {
                        this.tasks!!.size == 2
                    }
                )
            }
        }
        `when`("one filter has been configured") {
            val project: Project = mock()
            val extension = TalaiotPluginExtension(project).apply {
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

            val publishers: PublisherConfigurationProvider = mock()
            val outputPublisher: Publisher = mock()
            val influxDbPublisher: Publisher = mock()
            whenever(publishers.get()).thenReturn(listOf(outputPublisher, influxDbPublisher))

            val executionReport = ExecutionReport()
            setUpMockExtension(project, extension)
            talaiotPublisherImpl(
                extension, logger, project, executionReport, publishers.get()
            ).publish(
                getTasks(), 0, 100, 200, true
            )

            then("two publishers are processed and one task has been filtered ") {
                assert(publishers.get().size == 2)
                verify(publishers.get()[0]).publish(
                    argThat {
                        this.tasks!!.size == 1
                    }
                )
            }
        }

        `when`("one filter has been configured with graph publisher") {
            val project: Project = mock()
            val extension = TalaiotPluginExtension(project).apply {
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
            val outputPublisher: Publisher = mock()
            val influxDbPublisher: Publisher = mock()

            val publishers: PublisherConfigurationProvider = mock()
            whenever(publishers.get()).thenReturn(listOf(outputPublisher, influxDbPublisher))

            val executionReport = ExecutionReport()
            setUpMockExtension(project, extension)

            talaiotPublisherImpl(
                extension, logger, project, executionReport, publishers.get()
            ).publish(
                getTasks(), 0, 100, 200, true
            )

            then("two publishers are processed and one task has been filtered ") {
                assert(publishers.get().size == 2)
                verify(publishers.get()[0]).publish(
                    argThat {
                        this.tasks!!.size == 1
                    }
                )
            }
        }

        `when`("build filter configured to publish only successful build") {
            val project: Project = mock()
            val extension = TalaiotPluginExtension(project).apply {
                filter {
                    build {
                        success = true
                    }
                }
                publishers {
                    jsonPublisher = true
                }
            }
            setUpMockExtension(project, extension)

            val executionReport = ExecutionReport()
            val publishers: PublisherConfigurationProvider = mock()
            val jsonPublisher: Publisher = mock()
            whenever(publishers.get()).thenReturn(listOf(jsonPublisher))
            talaiotPublisherImpl(
                extension, logger, project, executionReport, publishers.get()
            ).publish(
                getTasks(), 0, 100, 200, true
            )

            then("successful build is published") {

                verify(publishers.get()[0]).publish(any())
            }
        }
        `when`("build filter configured to publish only successful build and build fails") {
            val project: Project = mock()
            val extension = TalaiotPluginExtension(project).apply {
                filter {
                    build {
                        success = true
                    }
                }
                publishers {
                    jsonPublisher = true
                }
            }
            setUpMockExtension(project, extension)

            val executionReport = ExecutionReport()
            val publishers: PublisherConfigurationProvider = mock()
            val jsonPublisher: Publisher = mock()
            whenever(publishers.get()).thenReturn(listOf(jsonPublisher))
            talaiotPublisherImpl(
                extension, logger, project, executionReport, publishers.get()
            ).publish(
                getTasks(), 0, 100, 200, false
            )

            then("failed build is not published") {
                verifyZeroInteractions(publishers.get()[0])
            }
        }

        `when`("build filter configured to exclude requested tasks") {
            val project: Project = mock()
            val extension = TalaiotPluginExtension(project).apply {
                filter {
                    build {
                        requestedTasks {
                            excludes = arrayOf(":module:taskA")
                        }
                    }
                }
            }
            setUpMockExtension(project, extension)
            val publishers: PublisherConfigurationProvider = mock()
            val outputPublisher: Publisher = mock()
            whenever(publishers.get()).thenReturn(listOf(outputPublisher))
            val executionReport = ExecutionReport()
            whenever(publishers.get()).thenReturn(listOf(outputPublisher))

            talaiotPublisherImpl(
                extension, logger, project, executionReport, publishers.get()
            ).publish(
                getTasks(), 0, 100, 200, false
            )

            then("build with a different task is published") {
                verify(publishers.get()[0]).publish(any())
            }
        }
        `when`("build filter configured to exclude requested tasks for all tasks") {
            val project: Project = mock()
            val extension = TalaiotPluginExtension(project).apply {
                filter {
                    build {
                        requestedTasks {
                            excludes = arrayOf(":module:taskA")
                        }
                    }
                }
            }
            setUpMockExtension(project, extension)
            val publishers: PublisherConfigurationProvider = mock()
            val outputPublisher: Publisher = mock()
            whenever(publishers.get()).thenReturn(listOf(outputPublisher))
            val report = ExecutionReport(requestedTasks = ":module:taskA")

            talaiotPublisherImpl(
                extension, logger, project, report, publishers.get()
            ).publish(
                taskLengthList = getTasks(), start = 0, configuraionMs = 100, end = 200, success = true
            )
            then("no information is published") {
                verifyZeroInteractions(publishers.get()[0])
            }
        }

        `when`("build filter configured to include requested tasks") {
            val project: Project = mock()
            val extension = TalaiotPluginExtension(project).apply {
                filter {
                    build {
                        requestedTasks {
                            includes = arrayOf(":module:taskA")
                        }
                    }
                }
            }
            setUpMockExtension(project, extension)
            val report = ExecutionReport(requestedTasks = ":module:taskB")
            val publishers: PublisherConfigurationProvider = mock()
            val outputPublisher: Publisher = mock()
            whenever(publishers.get()).thenReturn(listOf(outputPublisher))

            talaiotPublisherImpl(
                extension, logger, project, report, publishers.get()
            ).publish(
                taskLengthList = getTasks(), start = 0, configuraionMs = 100, end = 200, success = false
            )
            then("build with a different task is not published") {

                verifyZeroInteractions(publishers.get()[0])
            }
        }
        `when`("build filter configured to include requested tasks and the task is the same") {
            val project: Project = mock()
            val extension = TalaiotPluginExtension(project).apply {
                filter {
                    build {
                        requestedTasks {
                            includes = arrayOf(":module:taskA")
                        }
                    }
                }
            }
            setUpMockExtension(project, extension)
            val report = ExecutionReport(requestedTasks = ":module:taskA")
            val publishers: PublisherConfigurationProvider = mock()
            val outputPublisher: Publisher = mock()
            whenever(publishers.get()).thenReturn(listOf(outputPublisher))

            talaiotPublisherImpl(
                extension, logger, project, report, publishers.get()
            ).publish(
                taskLengthList = getTasks(), start = 0, configuraionMs = 100, end = 200, success = true
            )
            then("build with the same task is published") {
                verify(publishers.get()[0]).publish(any())
            }
        }

        `when`("build filter configured to include and exclude tasks") {
            val project: Project = mock()
            val extension = TalaiotPluginExtension(project).apply {
                filter {
                    build {
                        success = true
                        requestedTasks {
                            excludes = arrayOf(":module:taskB")
                            includes = arrayOf(":module:taskA")
                        }
                    }
                }
            }
            setUpMockExtension(project, extension)

            val report = ExecutionReport(requestedTasks = ":module:taskA :module:taskB")
            val publishers: PublisherConfigurationProvider = mock()
            val outputPublisher: Publisher = mock()
            whenever(publishers.get()).thenReturn(listOf(outputPublisher))
            talaiotPublisherImpl(
                extension, logger, project, report, publishers.get()
            ).publish(
                taskLengthList = getTasks(), start = 0, configuraionMs = 100, end = 200, success = true
            )

            then("build with at least one task included is published") {

                verify(publishers.get()[0]).publish(any())
            }
        }
        `when`("build filter configured to include and exclude tasks and all tasks are filtered ") {
            val project: Project = mock()
            val publishers: PublisherConfigurationProvider = mock()
            val report = ExecutionReport(requestedTasks = ":module:taskB")
            val outputPublisher: Publisher = mock()
            whenever(publishers.get()).thenReturn(listOf(outputPublisher))
            val extension = TalaiotPluginExtension(project).apply {
                filter {
                    build {
                        success = true
                        requestedTasks {
                            excludes = arrayOf(":module:taskB")
                            includes = arrayOf(":module:taskA")
                        }
                    }
                }
            }
            setUpMockExtension(project, extension)

            talaiotPublisherImpl(
                extension, logger, project, report, publishers.get()
            ).publish(
                taskLengthList = getTasks(), start = 0, configuraionMs = 100, end = 200, success = true
            )

            then("build with all tasks filtered out is not published") {
                verifyZeroInteractions(publishers.get()[0])
            }
        }

        `when`("tasks cache information is present") {
            val project: Project = mock()
            val extension = TalaiotPluginExtension(project).apply {
                publishers {
                    outputPublisher
                }
                metricsConfiguration()
            }

            setUpMockExtension(project, extension)

            val publishers: PublisherConfigurationProvider = mock()
            val outputPublisher: Publisher = mock()
            whenever(publishers.get()).thenReturn(listOf(outputPublisher))
            val taskFilterProcessor = TaskFilterProcessor(logger, extension.filter)
            val buildFilterProcessor =
                BuildFilterProcessor(logger, extension.filter?.build ?: BuildFilterConfiguration())

            val executionReport = ExecutionReport()

            val publisher = TalaiotPublisherImpl(
                executionReport, publishers.get(), taskFilterProcessor, buildFilterProcessor
            )

            publisher.publish(
                mutableListOf(getSingleTask("a"), getSingleTask("b"), getSingleTask("c")), 0, 100, 200, true
            )
            then("should publish cache information for each task") {
                val reportCaptor = argumentCaptor<ExecutionReport>()
                verify(publishers.get()[0]).publish(reportCaptor.capture())

                val expectedTaskA = TaskLength(
                    ms = 1,
                    taskName = "a",
                    taskPath = ":app:a",
                    state = TaskMessageState.EXECUTED,
                    rootNode = false,
                    module = "app",
                    startMs = 0,
                    stopMs = 0
                )

                val expectedTasks = listOf(
                    expectedTaskA,
                    expectedTaskA.copy(
                        taskName = "b", taskPath = ":app:b"
                    ),
                    expectedTaskA.copy(
                        taskName = "c", taskPath = ":app:c"
                    )
                )
                reportCaptor.firstValue.tasks.shouldBe(expectedTasks)
            }
        }
    }
})

private fun talaiotPublisherImpl(
    extension: TalaiotPluginExtension,
    logger: LogTrackerImpl,
    project: Project,
    executionReport: ExecutionReport = ExecutionReport(),
    publisherList: List<Publisher>
): TalaiotPublisherImpl {
    val taskFilterProcessor = TaskFilterProcessor(logger, extension.filter)
    val buildFilterProcessor = BuildFilterProcessor(logger, extension.filter?.build ?: BuildFilterConfiguration())
    setUpMockExtension(project, extension)
    return TalaiotPublisherImpl(
        executionReport, publisherList, taskFilterProcessor, buildFilterProcessor
    )
}

private fun setUpMockExtension(project: Project, extension: TalaiotPluginExtension) {
    val extensionContainer: ExtensionContainer = mock()
    whenever(project.extensions).thenReturn(extensionContainer)
    whenever(project.name).thenReturn("TestProject")
    whenever(extensionContainer.getByType(TalaiotPluginExtension::class.java)).thenReturn(extension)
    whenever(extensionContainer.getByName("talaiot")).thenReturn(extension)
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
        module = "app"
    )
)

private fun getSingleTask(name: String = "a") = TaskLength(
    ms = 1L,
    taskName = name,
    taskPath = ":app:$name",
    state = TaskMessageState.EXECUTED,
    rootNode = false,
    module = "app"
)
