package com.cdsap.talaiot.testkit


import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner


import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.util.Collections



import org.gradle.testkit.runner.TaskOutcome.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

class BuildLogicFunctionalTest {
    @get:Rule
    val testProjectDir = TemporaryFolder()
    private var settingsFile: File? = null
    private var buildFile: File? = null

    @Before
    @Throws(IOException::class)
    fun setup() {
        settingsFile = testProjectDir.newFile("settings.gradle")
        buildFile = testProjectDir.newFile("build.gradle")
    }

    @Test
    @Throws(IOException::class)
    fun testHelloWorldTask() {
        writeFile(settingsFile, "rootProject.name = 'hello-world'")
        val buildFileContent = "task helloWorld {" +
                "    doLast {" +
                "        println 'Hello world!'" +
                "    }" +
                "}"
        writeFile(buildFile, buildFileContent)

        val result = GradleRunner.create()
            .withProjectDir(testProjectDir.root)
            .withArguments("helloWorld")
            .build()

        assert(result.output.contains("Hello world!"))
        assert(SUCCESS == result.task(":helloWorld")!!.outcome)
    }

    @Throws(IOException::class)
    private fun writeFile(destination: File?, content: String) {
        var output: BufferedWriter? = null
        try {
            output = BufferedWriter(FileWriter(destination!!))
            output.write(content)
        } finally {
            output?.close()
        }
    }
}