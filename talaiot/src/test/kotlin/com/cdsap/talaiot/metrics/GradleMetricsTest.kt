package com.cdsap.talaiot.metrics

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.kotlintest.specs.BehaviorSpec
import org.gradle.StartParameter
import org.gradle.api.Project
import org.gradle.api.invocation.Gradle


class GradleMetricsTest : BehaviorSpec({
    given("Gradle Metrics") {
        val gradle: Gradle = mock()
        val project: Project = mock()
        whenever(gradle.startParameter).thenReturn(getStartParameter())
        whenever(gradle.gradleVersion).thenReturn("5.3.1")
        `when`("configuration project is setup with daemon") {
            whenever(project.hasProperty("org.gradle.daemon")).thenReturn(true)
            whenever(project.property("org.gradle.daemon")).thenReturn("true")
            whenever(project.gradle).thenReturn(gradle)

            val gradleMetrics = GradleMetrics(project)
            then("Gradle Metrics process different properties") {
                assert(gradleMetrics.get()["gradleCaching"] == "false")
                assert(gradleMetrics.get()["gradleParallel"] == "true")
                assert(gradleMetrics.get()["gradleProfile"] == "true")
                assert(gradleMetrics.get()["gradleOffline"] == "true")
                assert(gradleMetrics.get()["gradleVersion"] == "5.3.1")
                assert(gradleMetrics.get()["gradleDaemon"] == "true")
            }
        }
        `when`("configuration project is setup without daemon") {
            whenever(project.hasProperty("org.gradle.daemon")).thenReturn(false)

            val gradleMetrics = GradleMetrics(project)
            then("Gradle Metrics process different properties") {
                assert(gradleMetrics.get()["gradleCaching"] == "false")
                assert(gradleMetrics.get()["gradleParallel"] == "true")
                assert(gradleMetrics.get()["gradleProfile"] == "true")
                assert(gradleMetrics.get()["gradleOffline"] == "true")
                assert(gradleMetrics.get()["gradleVersion"] == "5.3.1")
                assert(!gradleMetrics.get().containsKey("gradleDaemon"))
            }
        }
    }
})

fun getStartParameter() = StartParameter().apply {
    isBuildCacheEnabled = false
    isParallelProjectExecutionEnabled = true
    isOffline = true
    isProfile = true
}
