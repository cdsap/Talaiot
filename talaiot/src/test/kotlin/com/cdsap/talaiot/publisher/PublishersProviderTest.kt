package com.cdsap.talaiot.publisher

import com.cdsap.talaiot.TalaiotExtension
import com.cdsap.talaiot.logger.LogTracker
import com.cdsap.talaiot.logger.LogTrackerImpl
import com.cdsap.talaiot.mock.ConsolePublisher
import com.cdsap.talaiot.mock.TestPublisher
import com.cdsap.talaiot.provider.PublishersProvider
import com.cdsap.talaiot.publisher.rethinkdb.RethinkDbPublisher
import io.kotlintest.inspectors.forAll
import io.kotlintest.inspectors.forAtLeastOne
import io.kotlintest.specs.BehaviorSpec
import org.gradle.testfixtures.ProjectBuilder

class PublishersProviderTest : BehaviorSpec({
    given("Publisher Provider") {
        val logger = LogTrackerImpl(LogTracker.Mode.SILENT)
        `when`("No configuration is included") {
            val project = ProjectBuilder.builder().build()
            project.extensions.create("talaiot", TalaiotExtension::class.java, project)
            val publishersProvider = PublishersProvider(project, logger, TestExecutor(), TestExecutor())
            then("no publishers are  ") {
                assert(publishersProvider.get().isEmpty())
            }
        }
        `when`("InfluxDbPublisher is included") {
            val project = ProjectBuilder.builder().build()
            val talaiotExtension = project.extensions.create("talaiot", TalaiotExtension::class.java, project)
            talaiotExtension.publishers {
                influxDbPublisher {
                    url = ""
                    taskMetricName = ""
                    dbName = ""
                }
            }
            val publishers = PublishersProvider(project, logger, TestExecutor(), TestExecutor()).get()
            then("instance of InfluxDbPublisher is created") {
                publishers.forAtLeastOne {
                    it is InfluxDbPublisher
                }
            }
        }
        `when`("OutputPublisher is included") {
            val project = ProjectBuilder.builder().build()
            val talaiotExtension = project.extensions.create("talaiot", TalaiotExtension::class.java, project)
            talaiotExtension.publishers {
                outputPublisher {
                }
            }
            val publishers = PublishersProvider(project, logger, TestExecutor(), TestExecutor()).get()
            then("instance of OutputPublisher is created") {
                publishers.forAtLeastOne {
                    it is OutputPublisher
                }
            }
        }
        `when`("TaskDependencyGraphPublisher is included") {
            val project = ProjectBuilder.builder().build()
            val talaiotExtension = project.extensions.create("talaiot", TalaiotExtension::class.java, project)
            talaiotExtension.publishers {
                taskDependencyGraphPublisher {
                    gexf = true
                }
            }
            val publishers = PublishersProvider(project, logger, TestExecutor(), TestExecutor()).get()
            then("instance of TaskDependencyGraphPublisher is created") {
                publishers.forAtLeastOne {
                    it is TaskDependencyGraphPublisher
                }
            }
        }
        `when`("One custom publisher is included") {
            val project = ProjectBuilder.builder().build()
            val talaiotExtension = project.extensions.create("talaiot", TalaiotExtension::class.java, project)
            talaiotExtension.publishers {
                customPublishers(TestPublisher())
            }
            val publishers = PublishersProvider(project, logger, TestExecutor(), TestExecutor()).get()
            then("instance of TestPublisher exists") {
                publishers.forAtLeastOne {
                    it is TestPublisher
                }
            }
        }
        `when`("Two custom publishers are included") {
            val project = ProjectBuilder.builder().build()
            val talaiotExtension = project.extensions.create("talaiot", TalaiotExtension::class.java, project)
            talaiotExtension.publishers {
                customPublishers(
                    TestPublisher(),
                    ConsolePublisher()
                )
            }
            val publishers = PublishersProvider(project, logger, TestExecutor(), TestExecutor()).get()
            then("instance of TestPublisher and ConsolePublisher exists") {
                publishers.forAll {
                    it is TestPublisher
                    it is ConsolePublisher
                }
            }
        }
        `when`("RethinkDb is included") {
            val project = ProjectBuilder.builder().build()
            val talaiotExtension = project.extensions.create("talaiot", TalaiotExtension::class.java, project)
            talaiotExtension.publishers {
                rethinkDbPublisher {
                    buildTableName = "builds"
                }
            }
            val publishers = PublishersProvider(project, logger, TestExecutor(), TestExecutor()).get()
            then("instance of RethinkDbPublisher is created") {
                publishers.forAtLeastOne {
                    it is RethinkDbPublisher
                }
            }
        }

        `when`("Multiple publishers are included") {
            val project = ProjectBuilder.builder().build()
            val talaiotExtension = project.extensions.create("talaiot", TalaiotExtension::class.java, project)
            talaiotExtension.publishers {
                customPublishers(TestPublisher())
                taskDependencyGraphPublisher {
                    gexf = true
                }
                outputPublisher {
                }
                influxDbPublisher {
                    url = ""
                    taskMetricName = ""
                    dbName = ""
                }
            }
            val publishers = PublishersProvider(project, logger, TestExecutor(), TestExecutor()).get()
            then("instance of all publishers are created") {
                publishers.forAll {
                    it is TestPublisher
                    it is InfluxDbPublisher
                    it is OutputPublisher
                    it is TaskDependencyGraphPublisher
                }
            }
        }
    }
})
