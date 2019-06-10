package com.cdsap.talaiot.functional

import io.kotlintest.specs.BehaviorSpec
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome

class PushGatewayPublisherBuildTest : BehaviorSpec({
    given("Build Gradle File") {
        val testProjectDir = TemporaryFolder()
        `when`("Talaiot is included with PushGateway") {
            testProjectDir.create()
            val settingsFile = testProjectDir.newFile("settings.gradle")
            var buildFile = testProjectDir.newFile("build.gradle")
            buildFile.appendText(
                """
                   plugins {
                      id 'java'
                      id 'talaiot'
                   }

                  talaiot{
                     publishers {
                         pushGatewayPublisher {
                             url = "http://localhost:9091"
                             nameJob = "tracking"
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
                println(result.output)
                assert(result.output.contains("InfluxDbPublisher"))
                assert(result.output.contains("tracking"))
                assert(result.task(":assemble")?.outcome == TaskOutcome.SUCCESS)

            }
            testProjectDir.delete()
        }
    }
})
