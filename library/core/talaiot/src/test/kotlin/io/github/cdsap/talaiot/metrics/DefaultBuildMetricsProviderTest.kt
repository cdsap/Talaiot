package io.github.cdsap.talaiot.metrics

import io.github.cdsap.talaiot.report.ExecutionReportProvider
import io.kotlintest.shouldBe
import io.kotlintest.specs.BehaviorSpec
import junit.framework.Assert.assertTrue

class DefaultBuildMetricsProviderTest : BehaviorSpec({
    given("DefaultBuildMetricsProvider instance") {
        `when`("Environment includes just the build Information") {
            val metrics = DefaultBuildMetricsProvider(ExecutionReportProvider.simpleExecutionReport()).get()
            then("no additional metrics are registered") {
                assertTrue(
                    metrics.filter {
                        it.key == "duration" && it.value == 10L
                    }.count() == 1
                )
                assertTrue(
                    metrics.filter {
                        it.key == "configuration" && it.value == 20L
                    }.count() == 1
                )
                assertTrue(
                    metrics.filter {
                        it.key == "success" && it.value == true
                    }.count() == 1
                )
                assertTrue(
                    metrics.filter {
                        it.key == "osVersion"
                    }.count() == 0
                )
            }
        }
        `when`("Complete execution Report") {
            val metrics = DefaultBuildMetricsProvider(ExecutionReportProvider.completeExecutionReport()).get()

            then("all values are registered") {
                metrics.forEach {
                    println(it.key + " " + it.value)
                }
                val expectedMap: Map<String, Any> = mapOf(
                    "start" to "1.590661991331E12".toDouble(),
                    "duration" to 10L,
                    "configuration" to 32L,
                    "success" to true,
                    "buildId" to "12",
                    "rootProject" to "app",
                    "requestedTasks" to "app:assembleDebug",
                    "buildInvocationId" to "123",
                    "osVersion" to "Linux 1.4",
                    "maxWorkers" to 2.toInt(),
                    "javaRuntime" to "1.2",
                    "cpuCount" to 4.toInt(),
                    "locale" to "EN-us",
                    "username" to "user",
                    "defaultCharset" to "default",
                    "ideVersion" to "2.1",
                    "gradleVersion" to "6.2.2",
                    "gitBranch" to "git_branch",
                    "gitUser" to "git_user",
                    "hostname" to "localMachine",
                    "cacheUrl" to "cacheUrl",
                    "cacheStore" to "10",
                    "switch.daemon" to "true",
                    "switch.offline" to "true",
                    "metric3" to "value3",
                    "metric4" to "value4"
                )
                metrics.shouldBe(expectedMap)
            }
        }
    }
})
