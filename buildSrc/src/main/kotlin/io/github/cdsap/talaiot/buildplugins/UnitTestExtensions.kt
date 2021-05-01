package io.github.cdsap.talaiot.buildplugins

import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.api.tasks.testing.TestReport
import org.gradle.kotlin.dsl.*

fun Project.setUpJunitPlatform() = this.run {
    apply {
        val test by tasks.getting(Test::class) {
            useJUnitPlatform { }
        }
    }
}

fun Project.collectUnitTest() = this.run {
        val testTask = if (rootProject.tasks.findByPath("collectUnitTest") == null) {
            rootProject.tasks.create("collectUnitTest", TestReport::class) {
            }
        } else {
            rootProject.tasks["collectUnitTest"]
        }
        val pr = this
        testTask.configure(closureOf<TestReport> {
            group = "Verification"
            description = "Collect all tests"

            val testTask = pr.tasks.find { it.name == "test" }
            testTask?.let { reportOn(testTask) }
            destinationDir = file("${rootProject.buildDir}/reports/tests")
        })

}