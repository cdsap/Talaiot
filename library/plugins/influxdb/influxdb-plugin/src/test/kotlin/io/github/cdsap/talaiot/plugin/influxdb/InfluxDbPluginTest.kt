package io.github.cdsap.talaiot.plugin.influxdb

import io.github.cdsap.talaiot.utils.TemporaryFolder
import io.kotlintest.Spec
import io.kotlintest.specs.BehaviorSpec
import org.gradle.testkit.runner.GradleRunner
import org.influxdb.dto.Query
import org.testcontainers.influxdb.KInfluxDBContainer

class InfluxDbPluginTest : BehaviorSpec() {

    val container = KInfluxDBContainer().withAuthEnabled(false)

    override fun beforeSpec(spec: Spec) {
        super.beforeSpec(spec)
        container.start()
    }

    override fun afterSpec(spec: Spec) {
        super.afterSpec(spec)
        container.stop()
    }

    val influxDB by lazy {
        container.newInfluxDB
    }

    init {
        given("InfluxDb Talaiot Plugin") {
            val testProjectDir = TemporaryFolder()
            `when`("Project includes the plugin") {
                testProjectDir.create()
                val buildFile = testProjectDir.newFile("build.gradle")
                buildFile.appendText(
                    """
                   plugins {
                      id 'java'
                      id 'io.github.cdsap.talaiot.plugin.influxdb'
                   }

                  talaiot {
                    publishers {
                      influxDbPublisher {
                         dbName = "tracking"
                         url = "${container.url}"
                         taskMetricName = "task"
                         buildMetricName = "build"
                      }
                    }
                  }
            """
                )
                GradleRunner.create()
                    .withProjectDir(testProjectDir.getRoot())
                    .withArguments("assemble")
                    .withPluginClasspath()
                    .build()
                then("there are records in the InfluxDb instance") {
                    // We are testing the plugin and we can't inject
                    // a test scheduler, forcing to sleep
                    Thread.sleep(2000)
                    val taskResultTask =
                        influxDB.query(Query("select *  from tracking.rpTalaiot.task"))
                    val taskResultBuild =
                        influxDB.query(Query("select * from tracking.rpTalaiot.build"))
                    assert(taskResultTask.results.isNotEmpty() && taskResultTask.results[0].series[0].name == "task")
                    assert(taskResultBuild.results.isNotEmpty() && taskResultBuild.results[0].series[0].name == "build")

                }
                testProjectDir.delete()
            }
        }
    }
}