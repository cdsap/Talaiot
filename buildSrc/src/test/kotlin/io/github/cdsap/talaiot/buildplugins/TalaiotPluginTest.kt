package io.github.cdsap.talaiot.buildplugins

import org.gradle.testkit.runner.BuildResult
import org.junit.Test
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import java.io.File

class TalaiotPluginTest {

    @Test
    fun pluginIsAppliedCorrectly() {
        val rootFolder = createTempFolder()
        File(rootFolder, "build.gradle").appendText(
            """
             plugins{
                  id 'java'
                  id 'talaiotPlugin'
              }

              talaiotPlugin {
                  idPlugin = "io.github.cdsap.talaiot"
                   artifact = "talaiot"
                   group = "io.github.cdsap"
                   mainClass = "io.github.cdsap.talaiot.TalaiotPlugin"
                   version = "1.3.6-SNAPSHOT"
              }
         """.trimIndent()
        )

        val result = buildResult(rootFolder, "assemble")
        assert(result.task(":assemble")?.outcome == TaskOutcome.SUCCESS)

        rootFolder.deleteRecursively()
    }

    @Test
    fun pluginWithoutExtensionFails() {
        val rootFolder = createTempFolder()
        File(rootFolder, "build.gradle").appendText(
            """
             plugins{
                  id 'java'
                  id 'talaiotPlugin'
              }

         """.trimIndent()
        )

        val result = GradleRunner.create()
            .withProjectDir(rootFolder)
            .withArguments("assemble")
            .withPluginClasspath()
            .buildAndFail()
        assert(result.task(":pluginDescriptors")?.outcome == TaskOutcome.FAILED)

        rootFolder.deleteRecursively()
    }

    @Test
    fun pluginIncludesGradlePluginPublisherTasks() {
        val rootFolder = createTempFolder()
        File(rootFolder, "build.gradle").appendText(
            buildGradleWithTalaiotAndJava().trimIndent()
        )

        val result = buildResult(rootFolder, "tasks")
        assert(result.output.contains("publishPlugins"))

        rootFolder.deleteRecursively()
    }

    @Test
    fun pluginIncludesPublishGeneralTasks() {
        val rootFolder = createTempFolder()
        File(rootFolder, "build.gradle").appendText(
            buildGradleWithTalaiotAndJava().trimIndent()
        )

        val result = buildResult(rootFolder, "tasks")
        assert(result.output.contains("publishToMavenLocal"))

        rootFolder.deleteRecursively()
    }

    @Test
    fun pluginAppliesGenericVersion() {
        val rootFolder = createTempFolder()
        File(rootFolder, "build.gradle").appendText(
            buildGradleWithTalaiotAndCustomRepositoryNoVersion(rootFolder.absolutePath).trimIndent()
        )
        GradleRunner.create()
            .withProjectDir(rootFolder)
            .withArguments("publishTalaiotLibPublicationToTestRepo")
            .withPluginClasspath()
            .build()

        assert(
            File("${rootFolder.absoluteFile}/repo/io/github/cdsap/talaiot//${Constants.TALAIOT_VERSION}")
                .walkTopDown().filter {
                    it.name.contains("talaiot-")
                }.count() > 0
        )
        rootFolder.deleteRecursively()
    }

    @Test
    fun pluginAppliesGenericGroup() {
        val rootFolder = createTempFolder()
        File(rootFolder, "build.gradle").appendText(
            buildGradleWithTalaiotAndCustomRepositoryNoGroup(rootFolder.absolutePath).trimIndent()
        )

        GradleRunner.create()
            .withProjectDir(rootFolder)
            .withArguments("publishTalaiotLibPublicationToTestRepo")
            .withPluginClasspath()
            .build()

        assert(
            File("${rootFolder.absoluteFile}/repo/io/github/cdsap/talaiot/plugin/talaiot/${Constants.TALAIOT_VERSION}")
                .walkTopDown().filter {
                    it.name.contains("talaiot-")
                }.count() > 0
        )
        rootFolder.deleteRecursively()
    }

    @Test
    fun pluginOverrideVersionAndGroup() {
        val rootFolder = createTempFolder()
        File(rootFolder, "build.gradle").appendText(
            buildGradleWithTalaiotAndCustomRepositoryAndVerionGroup(rootFolder.absolutePath).trimIndent()
        )

        GradleRunner.create()
            .withProjectDir(rootFolder)
            .withArguments("publishTalaiotLibPublicationToTestRepo")
            .withPluginClasspath()
            .build()

        assert(File("${rootFolder.absoluteFile}/repo/io/github/cdsap/overridegroup/talaiot/1.3.6/talaiot-1.3.6.jar").exists())
        rootFolder.deleteRecursively()
    }

    private fun createTempFolder(): File {
        val createdFolder = File.createTempFile("test", "", null)
        createdFolder.delete()
        createdFolder.mkdir()
        return createdFolder
    }

    private fun buildResult(rootFolder: File, command: String): BuildResult {
        return GradleRunner.create()
            .withProjectDir(rootFolder)
            .withArguments(command)
            .withPluginClasspath()
            .build()
    }


    private fun buildGradleWithTalaiotAndJava(): String {
        return """
                 plugins{
                      id 'java'
                      id 'talaiotPlugin'
                  }

                  talaiotPlugin {
                      idPlugin = "io.github.cdsap.talaiot"
                       artifact = "talaiot"
                       group = "io.github.cdsap"
                       mainClass = "io.github.cdsap.talaiot.TalaiotPlugin"
                       version = "1.3.6-SNAPSHOT"
                  }

             """
    }

    private fun buildGradleWithTalaiotAndCustomRepositoryAndVerionGroup(testProjectDir: String): String {
        return """
                  plugins{
                      id 'java'
                      id 'talaiotPlugin'
                  }

                  talaiotPlugin {
                       idPlugin = "io.github.cdsap.talaiot"
                       artifact = "talaiot"
                       group = "io.github.cdsap.overridegroup"
                       mainClass = "io.github.cdsap.talaiot.plugin.TalaiotPlugin"
                       version = "1.3.6"
                  }

                   publishing {
                          repositories {
                               maven {
                                 name = "testRepo"
                                 url = uri("$testProjectDir/repo")
                               }
                           }
                   }
                """
    }

    private fun buildGradleWithTalaiotAndCustomRepositoryNoVersion(testProjectDir: String): String {
        return """
                  plugins{
                      id 'java'
                      id 'talaiotPlugin'
                  }

                  talaiotPlugin {
                       idPlugin = "io.github.cdsap.talaiot"
                       artifact = "talaiot"
                       group = "io.github.cdsap"
                       mainClass = "io.github.cdsap.talaiot.plugin.TalaiotPlugin"
                  }

                   publishing {
                          repositories {
                               maven {
                                 name = "testRepo"
                                 url = uri("$testProjectDir/repo")
                               }
                           }
                   }
                """
    }

    private fun buildGradleWithTalaiotAndCustomRepositoryNoGroup(testProjectDir: String): String {
        return """
                  plugins{
                      id 'java'
                      id 'talaiotPlugin'
                  }

                  talaiotPlugin {
                       idPlugin = "io.github.cdsap.talaiot"
                       artifact = "talaiot"
                       mainClass = "io.github.cdsap.talaiot.plugin.TalaiotPlugin"
                  }

                   publishing {
                          repositories {
                               maven {
                                 name = "testRepo"
                                 url = uri("$testProjectDir/repo")
                               }
                           }
                   }
                """
    }
}
