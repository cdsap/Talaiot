package io.github.cdsap.talaiot

import io.github.cdsap.talaiot.metrics.BuildMetrics
import io.github.cdsap.talaiot.utils.TemporaryFolder
import io.kotlintest.Spec
import io.kotlintest.specs.BehaviorSpec
import org.gradle.testkit.runner.GradleRunner
import org.influxdb.dto.Query
import org.testcontainers.influxdb.KInfluxDBContainer
import java.io.File

class E2ENoBuildScanPluginIncludedGradle6 : BehaviorSpec() {

    val container: KInfluxDBContainer = KInfluxDBContainer().withAuthEnabled(false)

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

        given("Project with plugins Talaiot and Java") {

            val testProjectDir = TemporaryFolder()
            testProjectDir.create()

            val buildFile = testProjectDir.newFile("build.gradle")
            buildGradle(buildFile)

            `when`("build executes assemble with InfluxDbPublisher") {

                GradleRunner.create()
                    .withProjectDir(testProjectDir.getRoot())
                    .withArguments("assemble")
                    .withGradleVersion("6.0")
                    .withPluginClasspath()
                    .build()

                then("InfluxDb instance doesn't contain build metric GradleScanLink") {
                    Thread.sleep(2000)
                    val taskResultBuild =
                        influxDB.query(Query("select ${BuildMetrics.GradleScanLink}  from tracking.rpTalaiot.build"))
                    println(taskResultBuild.results[0].series == null)
                    println(taskResultBuild.results[0].error == null)
                }
            }
            testProjectDir.delete()
        }
    }

    private fun buildGradle(buildFile: File) {
        buildFile.appendText(
            """
                        plugins {
                          id 'java'
                          id 'io.github.cdsap.talaiot'
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
            """.trimIndent()
        )
    }
}
