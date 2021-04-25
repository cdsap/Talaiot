package io.github.cdsap.talaiot.plugin.graph

import io.github.cdsap.talaiot.utils.TemporaryFolder
import io.kotlintest.specs.BehaviorSpec
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import java.io.File

class DependencyGraphPublisherTest : BehaviorSpec({

    given("Graph Talaiot Plugin") {
        val testProjectDir = TemporaryFolder()
        `when`("Project includes the plugin") {
            testProjectDir.create()
            val buildFile = testProjectDir.newFile("build.gradle")
            buildFile.appendText(
                """
                   plugins {
                      id 'java'
                      id 'io.github.cdsap.talaiot.plugin.graph'
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
                assert(File("${testProjectDir.getRoot()}/build/reports/talaiot/taskgraph/htmlTaskDependency.html").exists())
                assert(File("${testProjectDir.getRoot()}/build/reports/talaiot/taskgraph/gexfTaskDependency.gexf").exists())
                assert(result.task(":assemble")?.outcome == TaskOutcome.SUCCESS)
            }
            testProjectDir.delete()
        }
    }
})
