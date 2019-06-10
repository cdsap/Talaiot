package com.cdsap.talaiot.functional

import io.kotlintest.specs.BehaviorSpec
import org.gradle.testkit.runner.GradleRunner

class InfluxDbPublisherNotProperlyDefinedBuildTest : BehaviorSpec({
    given("Build Gradle File") {
        val testProjectDir = TemporaryFolder()
        `when`("Talaiot is included with InfluxDbPublisher but missing the url") {
            testProjectDir.create()
            var buildFile = testProjectDir.newFile("build.gradle")
            buildFile.appendText(
                """
                   plugins {
                      id 'java'
                      id 'talaiot'
                   }

                  talaiot{
                    publishers {
                      influxDbPublisher {
                           dbName = "tracking"
                           urlMetric = "tracking"
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
                assert(result.output.contains("InfluxDbPublisher not executed. Configuration requires url, dbName and urlMetrics"))
            }
            testProjectDir.delete()
        }
    }
})
