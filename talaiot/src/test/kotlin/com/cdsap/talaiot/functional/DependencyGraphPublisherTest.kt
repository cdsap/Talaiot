package com.cdsap.talaiot.functional

import io.kotlintest.specs.BehaviorSpec
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import java.io.File

class DependencyGraphPublisherTest : BehaviorSpec({

    given("Build Gradle File") {
        val testProjectDir = TemporaryFolder()
        `when`("Talaiot is included with TaskDependencyGraph") {
            testProjectDir.create()
            var buildFile = testProjectDir.newFile("build.gradle")
            buildFile.appendText(
                """
                   plugins {
                      id 'java'
                      id 'talaiot'
                   }

                  talaiot {
                    publishers {
                      taskDependencyGraphPublisher {
                          html = true
                          gexf = true
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
            then("html and gexf files are generated") {
                assert(File("${testProjectDir.getRoot()}/talaiot/htmlTaskDependency.html").exists())
                assert(File("${testProjectDir.getRoot()}/talaiot/gexfTaskDependency.gexf").exists())
                assert(result.task(":assemble")?.outcome == TaskOutcome.SUCCESS)
            }
            testProjectDir.delete()
        }
    }
})
