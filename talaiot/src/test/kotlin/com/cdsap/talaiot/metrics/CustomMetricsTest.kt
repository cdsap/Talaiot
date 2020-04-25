package com.cdsap.talaiot.metrics

import com.cdsap.talaiot.configuration.MetricsConfiguration
import com.cdsap.talaiot.mock.AdbVersionMetric
import com.cdsap.talaiot.mock.KotlinVersionMetric
import io.kotlintest.specs.BehaviorSpec

class CustomMetricsTest : BehaviorSpec({
    given("metrics configuration") {
        `when`("single custom metric is used") {
            val metricsConfiguration = MetricsConfiguration()
            val metrics = metricsConfiguration.run {
                customMetrics(
                    AdbVersionMetric()
                )
                build()
            }
            then("AdbMetric is included") {
                assert(metrics.count() == 1)
                assert(metrics.count { it is AdbVersionMetric } == 1)
            }
        }
        `when`("two custom metrics are used") {
            val metricsConfiguration = MetricsConfiguration()
            val metrics = metricsConfiguration.run {
                customMetrics(
                    AdbVersionMetric(),
                    KotlinVersionMetric()
                )
                build()
            }
            then("AdbMetric and KotlinVersionMetric are included") {
                assert(metrics.count() == 2)
                assert(metrics.count { it is AdbVersionMetric } == 1)
                assert(metrics.count { it is KotlinVersionMetric } == 1)
            }
        }
        `when`("defaults and custom metrics are used") {
            val metricsConfiguration = MetricsConfiguration()
            val metrics = metricsConfiguration.run {
                defaultMetrics()
                customMetrics(
                    KotlinVersionMetric()
                )
                build()
            }
            then("RootProject, GradleRequested, GradleVersion and KotlinVersionMetric are included") {
                assert(metrics.count { it is RootProjectNameMetric } == 1)
                assert(metrics.count { it is GradleRequestedTasksMetric } == 1)
                assert(metrics.count { it is GradleVersionMetric } == 1)
                assert(metrics.count { it is KotlinVersionMetric } == 1)
            }
        }
    }
})
