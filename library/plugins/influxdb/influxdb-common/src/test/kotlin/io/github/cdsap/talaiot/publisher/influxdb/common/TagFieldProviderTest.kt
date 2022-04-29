package io.github.cdsap.talaiot.publisher.influxdb.common

import io.github.cdsap.talaiot.entities.CustomProperties
import io.github.cdsap.talaiot.entities.Environment
import io.github.cdsap.talaiot.entities.ExecutionReport
import io.github.cdsap.talaiot.metrics.BuildMetrics
import io.github.cdsap.talaiot.metrics.DefaultBuildMetricsProvider
import io.github.cdsap.talaiot.publisher.inflluxdb.common.TagFieldProvider
import io.kotlintest.specs.BehaviorSpec

class TagFieldProviderTest : BehaviorSpec() {

    init {
        given("TagFieldProvider Instance") {

            `when`("No tags included in the configuration") {
                val metrics = mutableMapOf("Metrics1" to "value1", "Metrics2" to "value2")
                val customProperties = CustomProperties()
                customProperties.buildProperties = metrics

                val executionReport = ExecutionReport(
                    customProperties = customProperties,
                    environment = Environment(
                        cpuCount = "12"
                    )
                )

                val tagFieldProvider = createBuildTagFieldProvider(
                    executionReport,
                    emptyList()
                )

                then("tags are empty and fields contain CpuCount build metrics + 2 custom properties") {
                    assert(tagFieldProvider.tags().isEmpty())
                    assert(tagFieldProvider.fields().containsKey(BuildMetrics.CpuCount.toString()))
                    assert(tagFieldProvider.fields().containsKey("Metrics1"))
                    assert(tagFieldProvider.fields().containsKey("Metrics2"))
                }
            }

            `when`("tag CpuCount included in the configuration") {
                val metrics = mutableMapOf("Metrics1" to "value1", "Metrics2" to "value2")
                val customProperties = CustomProperties()
                customProperties.buildProperties = metrics

                val executionReport = ExecutionReport(
                    customProperties = customProperties,
                    environment = Environment(
                        cpuCount = "12"
                    )
                )

                val tagFieldProvider = createBuildTagFieldProvider(
                    executionReport,
                    listOf(BuildMetrics.CpuCount)
                )

                then("tags includes CpuCount") {
                    assert(tagFieldProvider.tags().size == 1)
                    assert(tagFieldProvider.tags().containsKey(BuildMetrics.CpuCount.toString()))
                    assert(!tagFieldProvider.fields().containsKey(BuildMetrics.CpuCount.toString()))
                }
            }

            `when`("tags include Custom Metrics") {
                val metrics = mutableMapOf("Metrics1" to "value1", "Metrics2" to "value2")
                val customProperties = CustomProperties()
                customProperties.buildProperties = metrics

                val executionReport = ExecutionReport(
                    customProperties = customProperties,
                    environment = Environment(
                        cpuCount = "12"
                    )
                )

                val tagFieldProvider = createBuildTagFieldProvider(
                    executionReport,
                    listOf(BuildMetrics.Custom)
                )

                then("tags includes CpuCount") {
                    assert(tagFieldProvider.tags().size == 2)
                    assert(tagFieldProvider.tags().containsKey("Metrics1"))
                    assert(tagFieldProvider.tags().containsKey("Metrics2"))
                    assert(!tagFieldProvider.tags().containsKey(BuildMetrics.CpuCount.toString()))
                    assert(!tagFieldProvider.fields().containsKey("Metrics1"))
                    assert(!tagFieldProvider.fields().containsKey("Metrics2"))
                    assert(tagFieldProvider.fields().containsKey(BuildMetrics.CpuCount.toString()))
                }
            }
        }
    }

    private fun createBuildTagFieldProvider(
        report: ExecutionReport,
        configuration: List<BuildMetrics>
    ) = TagFieldProvider(
        configuration,
        DefaultBuildMetricsProvider(report),
        report.customProperties.buildProperties
    )
}
