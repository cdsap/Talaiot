package io.github.cdsap.talaiot

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.argThat
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyZeroInteractions
import com.nhaarman.mockitokotlin2.whenever
import io.github.cdsap.talaiot.configuration.MetricsConfiguration
import io.github.cdsap.talaiot.entities.CacheInfo
import io.github.cdsap.talaiot.entities.ExecutedGradleTaskInfo
import io.github.cdsap.talaiot.entities.ExecutedTasksInfo
import io.github.cdsap.talaiot.entities.ExecutionReport
import io.github.cdsap.talaiot.entities.TaskLength
import io.github.cdsap.talaiot.entities.TaskMessageState
import io.github.cdsap.talaiot.logger.LogTrackerImpl
import io.github.cdsap.talaiot.plugin.TalaiotConfigurationProvider
import io.github.cdsap.talaiot.plugin.TalaiotPluginExtension
import io.github.cdsap.talaiot.provider.Provider
import io.github.cdsap.talaiot.provider.PublisherConfigurationProvider
import io.github.cdsap.talaiot.publisher.OutputPublisherConfiguration
import io.github.cdsap.talaiot.publisher.Publisher
import io.github.cdsap.talaiot.publisher.TalaiotPublisherImpl
import io.github.cdsap.talaiot.publisher.graph.TaskDependencyGraphPublisher
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

            setUpMockExtension(project, extension)

            val publishers: PublisherConfigurationProvider = mock()
            val outputPublisher: Publisher = mock()
            whenever(publishers.get()).thenReturn(listOf(outputPublisher))

            val publisher = TalaiotPublisherImpl(
                extension, logger, getMetricsProvider(), publishers,
                ExecutedTasksInfo(emptyMap())
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
                verify(publishers.get()[0]).publish(
                    argThat {
                        this.tasks!!.size == 1
                    }
                )
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
            setUpMockExtension(project, extension)

            val publishers: PublisherConfigurationProvider = mock()
            val outputPublisher: Publisher = mock()
            val influxDbPublisher: Publisher = mock()

            whenever(publishers.get()).thenReturn(listOf(outputPublisher, influxDbPublisher))

            TalaiotPublisherImpl(
                extension, logger, getMetricsProvider(), publishers,
                ExecutedTasksInfo(emptyMap())
            ).publish(
                getTasks(),
                0,
                100,
                200,
                true
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
            setUpMockExtension(project, extension)

            val publishers: PublisherConfigurationProvider = mock()
            val outputPublisher: Publisher = mock()
            val influxDbPublisher: Publisher = mock()
            whenever(publishers.get()).thenReturn(listOf(outputPublisher, influxDbPublisher))

            TalaiotPublisherImpl(
                extension, logger, getMetricsProvider(), publishers,
                ExecutedTasksInfo(emptyMap())
            ).publish(
                getTasks(),
                0,
                100,
                200,
                true
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
            setUpMockExtension(project, extension)

            val publishers: PublisherConfigurationProvider = mock()
            val graph: TaskDependencyGraphPublisher = mock()
            whenever(publishers.get()).thenReturn(listOf(graph))

            TalaiotPublisherImpl(
                extension, logger, getMetricsProvider(), publishers,
                ExecutedTasksInfo(emptyMap())
            ).publish(
                getTasks(),
                0,
                100,
                200,
                true
            )

            then("two publishers are processed and one task has been filtered ") {
                assert(publishers.get().size == 1)
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

            then("successful build is published") {
                val publisher: Publisher = mock()
                TalaiotPublisherImpl(
                    extension,
                    logger,
                    getMetricsProvider(),
                    setUpPublisherProvider(publisher),
                    ExecutedTasksInfo(emptyMap())
                ).publish(
                    taskLengthList = getTasks(),
                    start = 0,
                    configuraionMs = 100,
                    end = 200,
                    success = true
                )

                verify(publisher).publish(any())
            }

            then("failed build is not published") {
                val publisher: Publisher = mock()
                TalaiotPublisherImpl(
                    extension,
                    logger,
                    getMetricsProvider(),
                    setUpPublisherProvider(publisher),
                    ExecutedTasksInfo(emptyMap())
                ).publish(
                    taskLengthList = getTasks(),
                    start = 0,
                    configuraionMs = 100,
                    end = 200,
                    success = false
                )

                verifyZeroInteractions(publisher)
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

            then("build with a different task is published") {
                val publisher: Publisher = mock()

                setUpMockExtension(project, extension)
                val publisherProvider: PublisherConfigurationProvider = mock()
                whenever(publisherProvider.get()).thenReturn(
                    listOf(
                        publisher
                    )
                )

                val report = ExecutionReport(requestedTasks = ":module:taskB")

                TalaiotPublisherImpl(
                    extension,
                    logger,
                    SimpleProvider(report),
                    publisherProvider,
                    ExecutedTasksInfo(emptyMap())
                ).publish(
                    taskLengthList = getTasks(),
                    start = 0,
                    configuraionMs = 100,
                    end = 200,
                    success = false
                )

                verify(publisher).publish(any())
            }

            then("build with all tasks filtered out is not published") {
                val publisher: Publisher = mock()
                val publisherProvider = TalaiotConfigurationProvider(project)
                val report = ExecutionReport(requestedTasks = ":module:taskA")

                TalaiotPublisherImpl(
                    extension,
                    logger,
                    SimpleProvider(report),
                    publisherProvider,
                    ExecutedTasksInfo(emptyMap())
                ).publish(
                    taskLengthList = getTasks(),
                    start = 0,
                    configuraionMs = 100,
                    end = 200,
                    success = true
                )

                verifyZeroInteractions(publisher)
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

            then("build with a different task is not published") {
                val publisher: Publisher = mock()
                setUpMockExtension(project, extension)
                val publisherProvider = TalaiotConfigurationProvider(project)
                val report = ExecutionReport(requestedTasks = ":module:taskB")

                TalaiotPublisherImpl(
                    extension,
                    logger,
                    SimpleProvider(report),
                    publisherProvider,
                    ExecutedTasksInfo(emptyMap())
                ).publish(
                    taskLengthList = getTasks(),
                    start = 0,
                    configuraionMs = 100,
                    end = 200,
                    success = false
                )

                verifyZeroInteractions(publisher)
            }

            then("build with the same task is published") {
                val publisher: Publisher = mock()
                val publisherProvider: PublisherConfigurationProvider = mock()
                val report = ExecutionReport(requestedTasks = ":module:taskA")
                whenever(publisherProvider.get()).thenReturn(
                    listOf(
                        publisher
                    )
                )
                TalaiotPublisherImpl(
                    extension,
                    logger,
                    SimpleProvider(report),
                    publisherProvider,
                    ExecutedTasksInfo(emptyMap())
                ).publish(
                    taskLengthList = getTasks(),
                    start = 0,
                    configuraionMs = 100,
                    end = 200,
                    success = true
                )

                verify(publisher).publish(any())
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

            then("build with at least one task included is published") {
                val publisher: Publisher = mock()
                val report = ExecutionReport(requestedTasks = ":module:taskA :module:taskB")

                TalaiotPublisherImpl(
                    extension,
                    logger,
                    SimpleProvider(report),
                    setUpPublisherProvider(publisher),
                    ExecutedTasksInfo(emptyMap())
                ).publish(
                    taskLengthList = getTasks(),
                    start = 0,
                    configuraionMs = 100,
                    end = 200,
                    success = true
                )

                verify(publisher).publish(any())
            }

            then("build with all tasks filtered out is not published") {
                val publisher: Publisher = mock()
                val publisherProvider = TalaiotConfigurationProvider(project)
                val report = ExecutionReport(requestedTasks = ":module:taskB")

                TalaiotPublisherImpl(
                    extension, logger, SimpleProvider(report), publisherProvider,
                    ExecutedTasksInfo(emptyMap())
                ).publish(
                    taskLengthList = getTasks(),
                    start = 0,
                    configuraionMs = 100,
                    end = 200,
                    success = true
                )

                verifyZeroInteractions(publisher)
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

            val publisher = TalaiotPublisherImpl(
                extension, logger, getMetricsProvider(), publishers,
                ExecutedTasksInfo(
                    mapOf(
                        ":app:a" to ExecutedGradleTaskInfo(
                            id = 1L,
                            taskName = ":app:a",
                            isCacheEnabled = false,
                            localCacheInfo = CacheInfo.CacheDisabled,
                            remoteCacheInfo = CacheInfo.CacheDisabled
                        ),
                        ":app:b" to ExecutedGradleTaskInfo(
                            id = 1L,
                            taskName = ":app:b",
                            isCacheEnabled = true,
                            localCacheInfo = CacheInfo.CacheHit,
                            remoteCacheInfo = CacheInfo.CacheDisabled
                        ),
                        ":app:c" to ExecutedGradleTaskInfo(
                            id = 1L,
                            taskName = ":app:c",
                            isCacheEnabled = true,
                            localCacheInfo = CacheInfo.CacheMiss,
                            remoteCacheInfo = CacheInfo.CacheHit
                        )
                    )
                )
            )
            publisher.publish(
                mutableListOf(getSingleTask("a"), getSingleTask("b"), getSingleTask("c")),
                0,
                100,
                200,
                true
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
                    taskDependencies = emptyList(),
                    workerId = "",
                    startMs = 0,
                    stopMs = 0,
                    critical = false,
                    isCacheEnabled = false,
                    isLocalCacheHit = false,
                    isLocalCacheMiss = false,
                    isRemoteCacheHit = false,
                    isRemoteCacheMiss = false
                )

                val expectedTasks = listOf(
                    expectedTaskA,
                    expectedTaskA.copy(
                        taskName = "b", taskPath = ":app:b",
                        isCacheEnabled = true,
                        isLocalCacheHit = true,
                        isLocalCacheMiss = false,
                        isRemoteCacheHit = false,
                        isRemoteCacheMiss = false
                    ),
                    expectedTaskA.copy(
                        taskName = "c", taskPath = ":app:c",
                        isCacheEnabled = true,
                        isLocalCacheHit = false,
                        isLocalCacheMiss = true,
                        isRemoteCacheHit = true,
                        isRemoteCacheMiss = false
                    )
                )
                reportCaptor.firstValue.tasks.shouldBe(expectedTasks)
            }
        }
    }
})

private fun setUpPublisherProvider(publisher: Publisher): PublisherConfigurationProvider {
    val publisherProvider: PublisherConfigurationProvider = mock()
    whenever(publisherProvider.get()).thenReturn(
        listOf(
            publisher
        )
    )
    return publisherProvider
}

private fun setUpMockExtension(project: Project, extension: TalaiotPluginExtension) {
    val extensionContainer: ExtensionContainer = mock()
    whenever(project.extensions).thenReturn(extensionContainer)
    whenever(project.name).thenReturn("TestProject")
    whenever(extensionContainer.getByType(TalaiotPluginExtension::class.java)).thenReturn(extension)
    whenever(extensionContainer.getByName("talaiot")).thenReturn(extension)
}

private fun getMetricsProvider(report: ExecutionReport = ExecutionReport()): Provider<ExecutionReport> {
    return SimpleProvider(report)
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

private fun getSingleTask(name: String = "a") = TaskLength(
    ms = 1L,
    taskName = name,
    taskPath = ":app:$name",
    state = TaskMessageState.EXECUTED,
    rootNode = false,
    module = "app",
    taskDependencies = emptyList()
)

private class SimpleProvider<T>(private val value: T) : Provider<T> {
    override fun get(): T {
        return value
    }
}
