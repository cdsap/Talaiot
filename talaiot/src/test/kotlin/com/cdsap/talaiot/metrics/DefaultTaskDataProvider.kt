package com.cdsap.talaiot.metrics

import com.cdsap.talaiot.entities.TaskLength
import com.cdsap.talaiot.entities.TaskMessageState
import com.cdsap.talaiot.report.ExecutionReportProvider
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
                    taskDependencies = emptyList()
                )
                , ExecutionReportProvider.executionReport()
            ).get()

            then("all the values are are mapped") {
                assert(metrics.filter {
                    it.key == "state" && it.value == "EXECUTED"
                }.count() == 1)
                assert(metrics.filter {
                    it.key == "module" && it.value == "module"
                }.count() == 1)
                assert(metrics.filter {
                    it.key == "rootNode" && it.value == "false"
                }.count() == 1)
                assert(metrics.filter {
                    it.key == "task" && it.value == "module:task1"
                }.count() == 1)
                assert(metrics.filter {
                    it.key == "workerId" && it.value == "1"
                }.count() == 1)
                assert(metrics.filter {
                    it.key == "critical" && it.value == "false"
                }.count() == 1)
                assert(metrics.filter {
                    it.key == "rootNode" && it.value == "false"
                }.count() == 1)
                assert(metrics.filter {
                    it.key == "metric1" && it.value == "value1"
                }.count() == 1)
                assert(metrics.filter {
                    it.key == "metric2" && it.value == "value2"
                }.count() == 1)
            }
        }
    }
})
