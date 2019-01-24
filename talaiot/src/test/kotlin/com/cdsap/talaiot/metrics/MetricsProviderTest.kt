package com.cdsap.talaiot.metrics

import com.cdsap.talaiot.TalaiotExtension
import com.nhaarman.mockitokotlin2.mock
import io.kotlintest.specs.BehaviorSpec
import org.gradle.BuildResult
import org.gradle.api.Project


class MetricsProviderTest : BehaviorSpec({
    given("Metrics Provider implementation") {

        `when`("there are no metrics configured") {
            val project: Project = mock()
            val result: BuildResult = mock()
            val talaiotExtension = TalaiotExtension(project)
            talaiotExtension.metrics.gitMetrics = false
            talaiotExtension.metrics.customMetrics = mutableMapOf()
            talaiotExtension.metrics.gradleMetrics = false
            talaiotExtension.metrics.performanceMetrics = false
            val metricsProvider = MetricsProvider(talaiotExtension, result)
            then("only base metrics are provided") {
                assert(metricsProvider.get().count() == 1)
                assert(metricsProvider.get()[0] is BaseMetrics)
            }
        }
        `when`("git metrics are configured") {
            val project: Project = mock()
            val result: BuildResult = mock()
            val talaiotExtension = TalaiotExtension(project)
            talaiotExtension.metrics.gitMetrics = true
            talaiotExtension.metrics.customMetrics = mutableMapOf()
            talaiotExtension.metrics.gradleMetrics = false
            talaiotExtension.metrics.performanceMetrics = false
            val metricsProvider = MetricsProvider(talaiotExtension, result)
            then("git metrics should be included in the list") {
                assert(metricsProvider.get().count() == 2)
                assert(metricsProvider.get().any {
                    it is GitMetrics
                })
            }
        }
        `when`("all the  metrics are configured") {
            val project: Project = mock()
            val result: BuildResult = mock()
            val talaiotExtension = TalaiotExtension(project)
            talaiotExtension.metrics.gitMetrics = true
            talaiotExtension.metrics.customMetrics = mutableMapOf(Pair("1", "2"))
            talaiotExtension.metrics.gradleMetrics = true
            talaiotExtension.metrics.performanceMetrics = true
            val metricsProvider = MetricsProvider(talaiotExtension, result)
            then("all metrics should be included in the list") {
                assert(metricsProvider.get().count() == 5)
                assert(metricsProvider.get().any { it is GitMetrics })
                assert(metricsProvider.get().any { it is PerformanceMetrics })
                assert(metricsProvider.get().any { it is GradleMetrics })
                assert(metricsProvider.get().any { it is CustomMetrics })
                assert(metricsProvider.get().any { it is BaseMetrics })

            }
        }
    }
})
