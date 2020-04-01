package com.cdsap.talaiot.metrics

import com.cdsap.talaiot.report.ExecutionReportProvider
import io.kotlintest.specs.BehaviorSpec
import junit.framework.Assert.assertTrue

class DefaultBuildMetricsProviderTest : BehaviorSpec({
    given("DefaultBuildMetricsProvider instance") {
        `when`("Environment includes just the build Information") {
            val metrics = DefaultBuildMetricsProvider(ExecutionReportProvider.simpleExecutionReport()).get()
            then("no additional metrics are registered") {
                assertTrue(metrics.filter {
                    it.key == "duration" && it.value == 10L
                }.count() == 1)
                assertTrue(metrics.filter {
                    it.key == "configuration" && it.value == 20L
                }.count() == 1)
                assertTrue(metrics.filter {
                    it.key == "success" && it.value == true
                }.count() == 1)
                assertTrue(metrics.filter {
                    it.key == "osVersion"
                }.count() == 0)

            }
        }
        `when`("Complete execution Report") {
            val metrics = DefaultBuildMetricsProvider(ExecutionReportProvider.completeExecutionReport()).get()
            then("all values are registered") {
                assertTrue(metrics.filter {
                    it.key == "duration" && it.value == 10L
                }.count() == 1)
                assertTrue(metrics.filter {
                    it.key == "configuration" && it.value == 32L
                }.count() == 1)
                assertTrue(metrics.filter {
                    it.key == "cpuCount" && it.value == 4
                }.count() == 1)
                assertTrue(metrics.filter {
                    it.key == "osVersion" && it.value == "Linux 1.4"
                }.count() == 1)
                assertTrue(metrics.filter {
                    it.key == "maxWorkers" && it.value == 2
                }.count() == 1)
                assertTrue(metrics.filter {
                    it.key == "javaRuntime" && it.value == "1.2"
                }.count() == 1)
                assertTrue(metrics.filter {
                    it.key == "locale" && it.value == "EN-us"
                }.count() == 1)
                assertTrue(metrics.filter {
                    it.key == "username" && it.value == "user"
                }.count() == 1)
                assertTrue(metrics.filter {
                    it.key == "publicIp" && it.value == "127.0.0.1"
                }.count() == 1)
                assertTrue(metrics.filter {
                    it.key == "ideVersion" && it.value == "2.1"
                }.count() == 1)
                assertTrue(metrics.filter {
                    it.key == "gradleVersion" && it.value == "6.2.2"
                }.count() == 1)
                assertTrue(metrics.filter {
                    it.key == "gitBranch" && it.value == "git_branch"
                }.count() == 1)
                assertTrue(metrics.filter {
                    it.key == "gitUser" && it.value == "git_user"
                }.count() == 1)
            }
        }
    }
})
