package io.github.cdsap.talaiot.metrics

import io.github.cdsap.talaiot.entities.TaskLength
import io.github.cdsap.talaiot.entities.TaskMessageState
import io.github.cdsap.talaiot.report.ExecutionReportProvider
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
                    rootNode = false
                ),
                ExecutionReportProvider.executionReport()
            ).get()

            then("all the values are are mapped") {
                val expectedMetrics: Map<String, Any> = mapOf(
                    "state" to "EXECUTED",
                    "module" to "module",
                    "rootNode" to "false",
                    "task" to "module:task1",
                    "metric1" to "value1",
                    "metric2" to "value2",
                    "value" to 1000L
                )
                metrics.shouldBe(expectedMetrics)
            }
        }
    }
})
