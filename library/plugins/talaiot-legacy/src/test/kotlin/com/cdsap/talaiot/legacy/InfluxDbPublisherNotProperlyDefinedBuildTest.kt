package com.cdsap.talaiot.legacy

import com.cdsap.talaiot.utils.TemporaryFolder
import io.kotlintest.specs.BehaviorSpec
import org.gradle.testkit.runner.GradleRunner

class InfluxDbPublisherNotProperlyDefinedBuildTest : BehaviorSpec({
    given("Build Gradle File") {
        val testProjectDir = TemporaryFolder()
        `when`("Talaiot is included with InfluxDbPublisher but missing the url") {
            testProjectDir.create()
            val buildFile = testProjectDir.newFile("build.gradle")
            buildFile.appendText(
                """
                   plugins {
                      id 'java'
                      id 'com.cdsap.talaiot'
                   }

                  talaiot{
                    publishers {
                      influxDbPublisher {
                           dbName = "tracking"
                      }
                  }
               }
            """
            )
            val result = GradleRunner.create()
                .withProjectDir(testProjectDir.getRoot())
                .withArguments("assemble")
                .withPluginClasspath()
                .build()
            then("logs displays the InfluxDbPublisher error") {
                assert(result.output.contains("InfluxDbPublisher not executed. Configuration requires url, dbName, taskMetricName and buildMetricName"))

            }
            testProjectDir.delete()
        }
        `when`("Talaiot is included with InfluxDbPublisher and using default values for taskMetricName and buildMetricName") {
            testProjectDir.create()
            val buildFile = testProjectDir.newFile("build.gradle")
            buildFile.appendText(
                """
                   plugins {
                      id 'java'
                      id 'com.cdsap.talaiot'
                   }

                  talaiot{
                    publishers {
                      influxDbPublisher {
                           dbName = "tracking"
                           url = "http://url.influxdb"
                      }
                  }
               }
            """
            )
            val result = GradleRunner.create()
                .withProjectDir(testProjectDir.getRoot())
                .withArguments("assemble")
                .withPluginClasspath()
                .build()
            then("no error shown because using default values") {
                assert(!result.output.contains("InfluxDbPublisher not executed. Configuration requires url, dbName, taskMetricName and buildMetricName"))
            }
            testProjectDir.delete()
        }
        `when`("Talaiot is included with InfluxDbPublisher and it's overridden default value for taskMetricName with empty value") {
            testProjectDir.create()
            val buildFile = testProjectDir.newFile("build.gradle")
            buildFile.appendText(
                """
                   plugins {
                      id 'java'
                      id 'com.cdsap.talaiot'
                   }

                  talaiot{
                    publishers {
                      influxDbPublisher {
                           dbName = "tracking"
                           url = "http://url.influxdb"
                           taskMetricName = ""
                      }
                  }
               }
            """
            )
            val result = GradleRunner.create()
                .withProjectDir(testProjectDir.getRoot())
                .withArguments("assemble")
                .withPluginClasspath()
                .build()
            then("logs displays the InfluxDbPublisher error") {
                assert(result.output.contains("InfluxDbPublisher not executed. Configuration requires url, dbName, taskMetricName and buildMetricName"))
            }
            testProjectDir.delete()
        }
    }
})
