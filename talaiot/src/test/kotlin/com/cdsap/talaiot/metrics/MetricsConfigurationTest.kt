package com.cdsap.talaiot.metrics

import com.cdsap.talaiot.TalaiotExtension
import com.cdsap.talaiot.configuration.MetricsConfiguration
import com.cdsap.talaiot.provider.MetricsProvider
import io.kotlintest.specs.BehaviorSpec
import org.gradle.kotlin.dsl.extra
import org.gradle.testfixtures.ProjectBuilder


class MetricsConfigurationTest : BehaviorSpec({
    given("metrics configuration") {

        `when`("defaults are used") {
            val metricsConfiguration = MetricsConfiguration()
            val metrics = metricsConfiguration.build()

            assert(metrics.count { it is RootProjectNameMetric } == 1)
            assert(metrics.count { it is GradleRequestedTasksMetric } == 1)
            assert(metrics.count { it is GradleVersionMetric } == 1)
        }

        `when`("performance metrics are configured") {
            val metricsConfiguration = MetricsConfiguration()
            val metrics = metricsConfiguration.performance().build()

            assert(metrics.count { it is ProcessorCountMetric } == 1)
        }
        `when`("build Id generation is disabled in the default behaviour") {
            val metricsConfiguration = MetricsConfiguration()
            val metrics = metricsConfiguration.performance().build()

            assert(metrics.count { it is BuildIdMetric } == 0)
        }
        `when`("build Id generation is enabled") {
            val metricsConfiguration = MetricsConfiguration()
            metricsConfiguration.generateBuildId = true
            val metrics = metricsConfiguration.performance().build()

            assert(metrics.count { it is BuildIdMetric } == 1)
        }
    }
})
