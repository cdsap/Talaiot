package com.cdsap.talaiot.functional

import io.kotlintest.specs.BehaviorSpec
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome

class InfluxDbPublisherBuildTest : BehaviorSpec({
    given("Build Gradle File") {
        val testProjectDir = TemporaryFolder()
        `when`("Talaiot is included with InfluxDbPublisher") {
            testProjectDir.create()
            var buildFile = testProjectDir.newFile("build.gradle")
            buildFile.appendText(
                """
                   plugins {
                      id 'java'
                      id 'talaiot'
                   }

                  talaiot{
                    logger = com.cdsap.talaiot.logger.LogTracker.Mode.INFO
                    publishers {
                      influxDbPublisher {
                           dbName = "tracking"
                           url = "http://localhost:8086"
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
            then("no logs are shown in the output") {
                assert(result.output.contains("InfluxDbPublisher"))
                assert(result.output.contains("tracking"))
                assert(result.task(":assemble")?.outcome == TaskOutcome.SUCCESS)

            }
            testProjectDir.delete()
        }
    }
})
