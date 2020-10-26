package com.cdsap.talaiot.metrics

import com.cdsap.talaiot.entities.TaskLength
import com.cdsap.talaiot.entities.TaskMessageState
import com.cdsap.talaiot.report.ExecutionReportProvider
import io.kotlintest.shouldBe
import io.kotlintest.specs.BehaviorSpec

class DefaultTaskMetricsProviderTest : BehaviorSpec({
    given("DefaultTaskMetricsProvider instance") {
        `when`("TasLenght information is given") {
            val metrics = DefaultTaskDataProvider(
                TaskLength(
                    ms = 1000L,
                    taskName = "task1",
                    taskPath = "module:task1",
                    state = TaskMessageState.EXECUTED,
                    module = "module",
                    workerId = "1",
                    critical = false,
                    rootNode = false,
                    taskDependencies = emptyList(),
                    isCacheEnabled = true,
                    isLocalCacheHit = false,
                    isLocalCacheMiss = true,
                    isRemoteCacheHit = true,
                    isRemoteCacheMiss = false
                )
                , ExecutionReportProvider.executionReport()
            ).get()

            then("all the values are are mapped") {
                val expectedMetrics: Map<String, Any> = mapOf(
                    "state" to "EXECUTED",
                    "module" to "module",
                    "rootNode" to "false",
                    "task" to "module:task1",
                    "workerId" to "1",
                    "critical" to "false",
                    "rootNode" to "false",
                    "metric1" to "value1",
                    "metric2" to "value2",
                    "value" to 1000L,
                    "cacheEnabled" to true,
                    "localCacheHit" to false,
                    "localCacheMiss" to true,
                    "remoteCacheHit" to true,
                    "remoteCacheMiss" to false
                )

                metrics.shouldBe(expectedMetrics)
            }
        }
    }
})
