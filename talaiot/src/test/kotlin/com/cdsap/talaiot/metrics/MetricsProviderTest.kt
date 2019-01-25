package com.cdsap.talaiot.metrics

import com.cdsap.talaiot.TalaiotExtension
import com.nhaarman.mockitokotlin2.mock
import io.kotlintest.specs.BehaviorSpec
import org.gradle.BuildResult
import org.gradle.api.Project
import org.gradle.kotlin.dsl.extra
import org.gradle.testfixtures.ProjectBuilder
import kotlin.reflect.jvm.internal.impl.resolve.calls.inference.CapturedType


class MetricsProviderTest : BehaviorSpec({
    given("Metrics Provider implementation") {

        `when`("there are no metrics configured") {
            val project = ProjectBuilder.builder().build()
            val talaiotExtension = project.extensions.create("talaiot", TalaiotExtension::class.java, project)
            talaiotExtension.metrics.gitMetrics = false
            talaiotExtension.metrics.customMetrics = mutableMapOf()
            talaiotExtension.metrics.gradleMetrics = false
            talaiotExtension.metrics.performanceMetrics = false
            val metrics = MetricsProvider(project).get()
            then("only base metrics are provided") {
                assert(metrics.count() == 4)
                assert(metrics.containsKey("user"))
                assert(metrics.containsKey("project"))
                assert(metrics.containsKey("buildId"))
                assert(metrics.containsKey("os"))
            }
        }
        `when`("git metrics are configured") {
            val project = ProjectBuilder.builder().build()
            val talaiotExtension = project.extensions.create("talaiot", TalaiotExtension::class.java, project)
            talaiotExtension.metrics.gitMetrics = true
            talaiotExtension.metrics.customMetrics = mutableMapOf()
            talaiotExtension.metrics.gradleMetrics = false
            talaiotExtension.metrics.performanceMetrics = false
            val metrics = MetricsProvider(project).get()
            then("git metrics should be included in the list") {
                assert(metrics.count() == 6)
                assert(metrics.containsKey("gitUser"))
                assert(metrics.containsKey("branch"))
            }
        }
        `when`("all the  metrics are configured") {
            val project = ProjectBuilder.builder().build()
            val talaiotExtension = project.extensions.create("talaiot", TalaiotExtension::class.java, project)
            project.gradle.rootProject.extra.set("org.gradle.jvmargs", "-Xmx8G MaxPermSize=512M")
            talaiotExtension.metrics.gitMetrics = true
            talaiotExtension.metrics.customMetrics = mutableMapOf(Pair("1", "2"))
            talaiotExtension.metrics.gradleMetrics = true
            talaiotExtension.metrics.performanceMetrics = true
            val metrics = MetricsProvider(project).get()
            then("all metrics should be included in the list") {
                assert(metrics.containsKey("buildId"))
                assert(metrics.containsKey("user"))
                assert(metrics.containsKey("Xmx"))
                assert(metrics.containsKey("1"))
            }
        }
    }
})
