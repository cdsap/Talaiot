package com.cdsap.talaiot.functional

import io.kotlintest.specs.BehaviorSpec
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome

class OutputsBuildTest : BehaviorSpec({
    given("Build Gradle File") {
        val testProjectDir = TemporaryFolder()
        `when`("Talaiot is included with OutputPublisher") {
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
                    logger = com.cdsap.talaiot.logger.LogTracker.Mode.INFO
                    publishers {
                      outputPublisher {}
                  }
               }
            """
            )
            val result = GradleRunner.create()
                .withProjectDir(testProjectDir.getRoot())
                .withArguments("assemble")
                .withPluginClasspath()
                .build()
            then("logs are shown in the output") {
                assert(result.output.contains("OutputPublisher"))
                assert(result.output.contains("¯\\_(ツ)_/¯"))
                assert(result.task(":assemble")?.outcome == TaskOutcome.SUCCESS)

            }
            testProjectDir.delete()
        }
    }
})
