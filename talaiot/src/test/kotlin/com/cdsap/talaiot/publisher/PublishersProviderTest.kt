package com.cdsap.talaiot.publisher

import com.cdsap.talaiot.TalaiotExtension
import com.cdsap.talaiot.entities.TaskMeasurementAggregated
import io.kotlintest.inspectors.forAll
import io.kotlintest.inspectors.forAtLeastOne
import io.kotlintest.specs.BehaviorSpec
import org.gradle.testfixtures.ProjectBuilder


class PublishersProviderTest : BehaviorSpec({
    given("Publisher Provider") {
        `when`("No configuration is included") {
            val project = ProjectBuilder.builder().build()
            project.extensions.create("talaiot", TalaiotExtension::class.java, project)
            val publishersProvider = PublishersProvider(project)
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
                    urlMetric = ""
                    dbName = ""
                }
            }
            val publishers = PublishersProvider(project).get()
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
            val publishers = PublishersProvider(project).get()
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
            val publishers = PublishersProvider(project).get()
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
                customPublisher(TestPublisher())
            }
            val publishers = PublishersProvider(project).get()
            then("instance of CustomPublisher is created") {
                publishers.forAtLeastOne {
                    it is TestPublisher
                }
            }
        }

        `when`("All publishers are included") {
            val project = ProjectBuilder.builder().build()
            val talaiotExtension = project.extensions.create("talaiot", TalaiotExtension::class.java, project)
            talaiotExtension.publishers {
                customPublisher(TestPublisher())
                taskDependencyGraphPublisher {
                    gexf = true
                }
                outputPublisher {
                }
                influxDbPublisher {
                    url = ""
                    urlMetric = ""
                    dbName = ""
                }
            }
            val publishers = PublishersProvider(project).get()
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
    override fun publish(taskMeasurementAggregated: TaskMeasurementAggregated) {

    }

}