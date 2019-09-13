package com.cdsap.talaiot.provider

import com.cdsap.talaiot.TalaiotExtension
import com.cdsap.talaiot.entities.ExecutionReport
import com.cdsap.talaiot.logger.LogTracker
import com.cdsap.talaiot.logger.LogTrackerImpl
import com.cdsap.talaiot.publisher.*
import com.cdsap.talaiot.publisher.timeline.TimelinePublisher
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
            val publishersProvider = PublishersProvider(project, logger)
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
            val publishers = PublishersProvider(project, logger).get()
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
            val publishers = PublishersProvider(project, logger).get()
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
            val publishers = PublishersProvider(project, logger).get()
            then("instance of TaskDependencyGraphPublisher is created") {
                publishers.forAtLeastOne {
                    it is TaskDependencyGraphPublisher
                }
            }
        }
        `when`("CustomPublisher is included") {
            val project = ProjectBuilder.builder().build()
            val talaiotExtension = project.extensions.create("talaiot", TalaiotExtension::class.java, project)
            talaiotExtension.publishers {
                customPublisher {
                    publisher = TestPublisher()
                }
            }
            val publishers = PublishersProvider(project, logger).get()
            then("instance of CustomPublisher is created") {
                publishers.forAtLeastOne {
                    it is TestPublisher
                }
            }
        }
        `when`("Pushgateway publisher is included") {
            val project = ProjectBuilder.builder().build()
            val talaiotExtension = project.extensions.create("talaiot", TalaiotExtension::class.java, project)
            talaiotExtension.publishers {
                pushGatewayPublisher {
                    url = "http://urlpushgateway"
                }
            }
            val publishers = PublishersProvider(project, logger).get()
            then("instance of PushgatewayPublisher is created") {
                publishers.forAtLeastOne {
                    it is PushGatewayPublisher
                }
            }
        }
        `when`("JsonPublisher is included") {
            val project = ProjectBuilder.builder().build()
            val talaiotExtension = project.extensions.create("talaiot", TalaiotExtension::class.java, project)
            talaiotExtension.publishers {
                jsonPublisher = true
            }
            val publishers = PublishersProvider(project, logger).get()
            then("instance of JsonPublisher is created") {
                publishers.forAtLeastOne {
                    it is JsonPublisher
                }
            }
        }
        `when`("TimeLinePublisher is included") {
            val project = ProjectBuilder.builder().build()
            val talaiotExtension = project.extensions.create("talaiot", TalaiotExtension::class.java, project)
            talaiotExtension.publishers {
                timelinePublisher = true
            }
            val publishers = PublishersProvider(project, logger).get()
            then("instance of TimelinePublisher is created") {
                publishers.forAtLeastOne {
                    it is TimelinePublisher
                }
            }
        }
        `when`("ElasticSearchPublisher is included") {
            val project = ProjectBuilder.builder().build()
            val talaiotExtension = project.extensions.create("talaiot", TalaiotExtension::class.java, project)
            talaiotExtension.publishers {
                elasticSearchPublisher {
                    url = "http://urlelasticSearch"
                }
            }
            val publishers = PublishersProvider(project, logger).get()
            then("instance of ElasticSearchPublisher is included") {
                publishers.forAtLeastOne {
                    it is ElasticSearchPublisher
                }
            }
        }
        `when`("HybridPublisher is included") {
            val project = ProjectBuilder.builder().build()
            val talaiotExtension = project.extensions.create("talaiot", TalaiotExtension::class.java, project)
            talaiotExtension.publishers {
                hybridPublisher {
                    taskPublisher {
                        elasticSearchPublisher {
                            url = "http://urlelasticSearch"
                        }
                    }
                    buildPublisher {
                        customPublisher {
                            publisher = TestPublisher()
                        }
                    }
                }
            }
            val publishers = PublishersProvider(project, logger).get()
            then("instance of HybridPublisher is included") {
                publishers.forAtLeastOne {
                    it is HybridPublisher
                }
            }
        }

        `when`("Different publishers are included") {
            val project = ProjectBuilder.builder().build()
            val talaiotExtension = project.extensions.create("talaiot", TalaiotExtension::class.java, project)
            talaiotExtension.publishers {
                customPublisher {
                    publisher = TestPublisher()
                }
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
            val publishers = PublishersProvider(project, logger).get()
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

class TestPublisher : Publisher {
    override fun publish(report: ExecutionReport) {

    }
}