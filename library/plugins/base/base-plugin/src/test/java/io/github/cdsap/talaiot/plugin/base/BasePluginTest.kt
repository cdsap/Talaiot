package io.github.cdsap.talaiot.plugin.base

import io.github.cdsap.talaiot.utils.TemporaryFolder
import io.kotlintest.specs.BehaviorSpec
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import java.io.File

class BasePluginTest : BehaviorSpec() {

    init {
        given("Base Talaiot Plugin") {
            val testProjectDir = TemporaryFolder()
            `when`("Project includes the plugin") {
                testProjectDir.create()
                val buildFile = testProjectDir.newFile("build.gradle")
                buildFile.appendText(
                    """
                   plugins {
                      id 'java'
                      id 'io.github.cdsap.talaiot.plugin.base'
                   }

                  talaiot {
                    publishers {
                      jsonPublisher = true
                      }
                  }
            """
                )

                val result = GradleRunner.create()
                    .withProjectDir(testProjectDir.getRoot())
                    .withArguments("assemble")
                    .withPluginClasspath()
                    .build()
                then("json build info exists") {
                    assert(File("${testProjectDir.getRoot()}/build/reports/talaiot/json/data.json").exists())
                    assert(result.task(":assemble")?.outcome == TaskOutcome.SUCCESS)
                }
                testProjectDir.delete()
            }
        }
    }
}
