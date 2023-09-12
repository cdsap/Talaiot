package io.github.cdsap.talaiot

import io.github.cdsap.talaiot.utils.TemporaryFolder
import io.kotlintest.specs.BehaviorSpec
import org.gradle.testkit.runner.GradleRunner
import java.io.File

class GradleSwitchConfigurationCacheTest : BehaviorSpec() {

    init {
        given("Project with plugins Talaiot and Java") {

            val testProjectDir = TemporaryFolder()
            testProjectDir.create()

            val buildFile = testProjectDir.newFile("build.gradle.kts")
            buildGradle(buildFile)

            `when`("build executes assemble with JsonPublisher and configuration cache") {

                GradleRunner.create()
                    .withProjectDir(testProjectDir.getRoot())
                    .withArguments("assemble", "--configuration-cache")
                    .withPluginClasspath()
                    .build()

                then("Configuration Cache switch is registered") {
                    val file = File("${testProjectDir.getRoot()}/build/reports/talaiot/json/data.json")
                    assert(file.readText().contains("\"configurationCache\": \"true\""))
                }
            }
            `when`("build executes assemble with JsonPublisher without Configuration cache") {

                GradleRunner.create()
                    .withProjectDir(testProjectDir.getRoot())
                    .withArguments("assemble")
                    .withPluginClasspath()
                    .build()

                then("Configuration Cache switch is false") {
                    val file = File("${testProjectDir.getRoot()}/build/reports/talaiot/json/data.json")
                    assert(file.readText().contains("\"configurationCache\": \"false\""))
                }
            }
            `when`("build executes assemble with JsonPublisher with Gradle < 7.1") {

                GradleRunner.create()
                    .withProjectDir(testProjectDir.getRoot())
                    .withArguments("assemble")
                    .withPluginClasspath()
                    .withGradleVersion("7.0.2")
                    .build()

                then("Configuration Cache switch is not registered") {
                    val file = File("${testProjectDir.getRoot()}/build/reports/talaiot/json/data.json")
                    assert(file.readText().contains("\"configurationCache\": \"\""))
                }
            }
            testProjectDir.delete()
        }
    }

    private fun buildGradle(buildFile: File) {
        buildFile.appendText(
            """
                       plugins {
                          id("java")
                          id("io.github.cdsap.talaiot")
                      }


                      talaiot {
                        publishers {
                          jsonPublisher = true
                        }
                      }
            """.trimIndent()
        )
    }
}
