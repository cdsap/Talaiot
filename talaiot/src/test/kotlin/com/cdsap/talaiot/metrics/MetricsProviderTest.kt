package com.cdsap.talaiot.metrics

import com.cdsap.talaiot.TalaiotExtension
import io.kotlintest.specs.BehaviorSpec
import org.gradle.kotlin.dsl.extra
import org.gradle.testfixtures.ProjectBuilder


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
                assert(metrics.count() == 3)
                assert(metrics.containsKey("user"))
                assert(metrics.containsKey("project"))
                assert(metrics.containsKey("os"))
            }
        }
        `when`("performance metrics are configured") {
            val project = ProjectBuilder.builder().build()
            val talaiotExtension = project.extensions.create("talaiot", TalaiotExtension::class.java, project)
            talaiotExtension.metrics.gitMetrics = false
            talaiotExtension.metrics.customMetrics = mutableMapOf()
            talaiotExtension.metrics.gradleMetrics = false
            talaiotExtension.metrics.performanceMetrics = true
            val metrics = MetricsProvider(project).get()
            then("performance metrics should be included in the list") {
                assert(metrics.containsKey("availableProcessors"))
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
                assert(metrics.containsKey("user"))
                assert(metrics.containsKey("Xmx"))
                assert(metrics.containsKey("1"))
            }
        }
        `when`("build Id generation is disabled in the default behaviour") {
            val project = ProjectBuilder.builder().build()
            val talaiotExtension = project.extensions.create("talaiot", TalaiotExtension::class.java, project)
            talaiotExtension.metrics.gitMetrics = false
            talaiotExtension.metrics.gradleMetrics = false
            talaiotExtension.metrics.performanceMetrics = false
            val metrics = MetricsProvider(project).get()
            then("base metrics is not including buildId") {
                assert(!metrics.containsKey("buildId"))
                assert(metrics.containsKey("user"))
                assert(metrics.containsKey("project"))
                assert(metrics.containsKey("os"))
            }
        }
        `when`("build Id generation is enabled") {
            val project = ProjectBuilder.builder().build()
            val talaiotExtension = project.extensions.create("talaiot", TalaiotExtension::class.java, project)
            talaiotExtension.generateBuildId =  true
            talaiotExtension.metrics.gitMetrics = false
            talaiotExtension.metrics.gradleMetrics = false
            talaiotExtension.metrics.performanceMetrics = false
            val metrics = MetricsProvider(project).get()
            then("base metrics is including buildId") {
                assert(metrics.containsKey("buildId"))
                assert(metrics.containsKey("user"))
                assert(metrics.containsKey("project"))
                assert(metrics.containsKey("os"))
            }
        }

    }
})
