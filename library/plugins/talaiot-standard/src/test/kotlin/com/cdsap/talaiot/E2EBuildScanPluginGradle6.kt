package com.cdsap.talaiot

import com.cdsap.talaiot.metrics.BuildMetrics
import com.cdsap.talaiot.utils.TemporaryFolder
import io.kotlintest.Spec
import io.kotlintest.specs.BehaviorSpec
import org.gradle.testkit.runner.GradleRunner
import org.influxdb.dto.Query
import org.testcontainers.influxdb.KInfluxDBContainer
import java.io.File


class E2EBuildScanPluginGradle6 : BehaviorSpec() {

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
        given("Project with plugins Talaiot, Java and Build Scan") {

            val testProjectDir = TemporaryFolder()
            testProjectDir.create()

            val settingsGradle = testProjectDir.newFile("settings.gradle")
            settingsGradle(settingsGradle)

            val buildFile = testProjectDir.newFile("build.gradle")
            buildGradle(buildFile)

            `when`("build executes assemble and --scan with InfluxDbPublisher") {

                GradleRunner.create()
                    .withProjectDir(testProjectDir.getRoot())
                    .withArguments("assemble", "--scan")
                    .withGradleVersion("6.0")
                    .withPluginClasspath()
                    .build()

                then("InfluxDb instance doesn't contain build metric GradleScanLink") {
                    Thread.sleep(2000)
                    val taskResultBuild =
                        influxDB.query(Query("select ${BuildMetrics.GradleScanLink}  from tracking.rpTalaiot.build"))
                    val columns =
                        taskResultBuild.results.joinToString { it.series.joinToString { it.columns.joinToString() } }
                    assert(columns == "time, ${BuildMetrics.GradleScanLink}")

                    val values =
                        taskResultBuild.results.joinToString { it.series.joinToString { it.values.joinToString() } }
                    assert(values.contains("https://gradle.com/s/"))



                }

            }
            testProjectDir.delete()
        }

    }

    private fun settingsGradle(settingsGradle: File) {
        settingsGradle.appendText(
            """
                        plugins {
                          id "com.gradle.enterprise" version "3.6"
                        } """.trimIndent()
        )
    }

    private fun buildGradle(buildFile: File) {
        buildFile.appendText(
            """
                        plugins {
                          id 'java'
                          id 'com.cdsap.talaiot'
                      }
                      
                      gradleEnterprise {
                        buildScan {
                           termsOfServiceUrl = "https://gradle.com/terms-of-service"
                           termsOfServiceAgree = "yes"
                        }
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
                      }  """.trimIndent()
        )
    }
}
