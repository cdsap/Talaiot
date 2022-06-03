package io.github.cdsap.talaiot.publisher.pushgateway

import io.github.cdsap.talaiot.entities.CustomProperties
import io.github.cdsap.talaiot.entities.Environment
import io.github.cdsap.talaiot.entities.ExecutionReport
import io.github.cdsap.talaiot.entities.TaskLength
import io.github.cdsap.talaiot.entities.TaskMessageState
import io.github.cdsap.talaiot.metrics.BuildMetrics
import io.github.cdsap.talaiot.metrics.DefaultBuildMetricsProvider
import io.github.cdsap.talaiot.metrics.TaskMetrics
import io.kotlintest.specs.BehaviorSpec

class PushGatewayLabelProviderTest : BehaviorSpec() {
    init {

        given("PushGatewayLabelProvider instance with simple execution report") {
            val report = executionReportData()
            val labelProvider = PushGatewayLabelProvider(report)
            `when`("task label names are retrieved") {
                val taskLabelNames = labelProvider.taskLabelNames()
                then("Default task metric labels are present") {
                    assert(taskLabelNames.contains(TaskMetrics.Module.name))
                    assert(taskLabelNames.contains(TaskMetrics.State.name))
                }
            }
            `when`("task label values are retrieved") {
                val taskLabelNames = labelProvider.taskLabelValues(
                    TaskLength(
                        1, "clean", ":clean", TaskMessageState.EXECUTED, false,
                        "app"
                    )
                )
                then(" Task metric values are present") {
                    assert(taskLabelNames.contains("app"))
                    assert(taskLabelNames.contains(TaskMessageState.EXECUTED.name))
                }
            }
            `when`("report includes custom task metrics and task labels are retrieved") {
                val taskLabelNames = labelProvider.taskLabelNames()
                then("custom task label names metrics are present") {
                    assert(taskLabelNames.contains("metric1"))
                    assert(taskLabelNames.contains("metric2"))
                }
            }
            `when`("report includes custom task metrics and task values are retrieved") {
                val taskLabelNames = labelProvider.taskLabelValues(
                    TaskLength(
                        1, "clean", ":clean", TaskMessageState.EXECUTED, false,
                        "app"
                    )
                )
                then("custom task label values metrics are present") {
                    assert(taskLabelNames.contains("value1"))
                    assert(taskLabelNames.contains("value2"))
                }
            }
            `when`("build label names are retrieved") {
                val buildLabelNames = labelProvider.buildLabelNames(DefaultBuildMetricsProvider(report).get())
                then("Default build metric labels are present when they are not null") {
                    assert(buildLabelNames.contains(BuildMetrics.RequestedTasks.toKey()))
                    assert(!buildLabelNames.contains(BuildMetrics.GitUser.toKey()))
                    assert(!buildLabelNames.contains(BuildMetrics.GitBranch.toKey()))
                    assert(!buildLabelNames.contains(BuildMetrics.Hostname.toKey()))
                }
            }
            `when`("build label values are retrieved") {
                val buildLabelNames = labelProvider.buildLabelValues(DefaultBuildMetricsProvider(report).get())
                then("Default values metric labels are present") {
                    assert(buildLabelNames.contains("assemble"))
                }
            }
            `when`("report includes custom build metrics and build labels are retrieved") {
                val buildLabelNames = labelProvider.buildLabelNames(DefaultBuildMetricsProvider(report).get())
                then("custom build label names metrics are present") {
                    assert(buildLabelNames.contains("metric3"))
                    assert(buildLabelNames.contains("metric4"))
                }
            }
            `when`("report includes custom build metrics and build values are retrieved") {
                val buildLabelNames = labelProvider.buildLabelValues(DefaultBuildMetricsProvider(report).get())
                then("custom build label values metrics are present") {
                    assert(buildLabelNames.contains("value3"))
                    assert(buildLabelNames.contains("value4"))
                }
            }
        }
    }

    private fun executionReportData(): ExecutionReport {
        return ExecutionReport(
            durationMs = "100",
            configurationDurationMs = "10",
            requestedTasks = "assemble",
            environment = Environment(
                cpuCount = "12", maxWorkers = "4"
            ),
            customProperties = CustomProperties(
                taskProperties = mutableMapOf(
                    "metric1" to "value1",
                    "metric2" to "value2"
                ),
                buildProperties = mutableMapOf(
                    "metric3" to "value3",
                    "metric4" to "value4"
                )
            ),
            tasks = listOf(
                TaskLength(
                    1, "clean", ":clean", TaskMessageState.EXECUTED, false,
                    "app"
                ),
                TaskLength(
                    100, "assemble", ":app:assemble", TaskMessageState.EXECUTED, false,
                    "app"
                )
            )
        )
    }
}
