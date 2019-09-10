package com.cdsap.talaiot.publisher

import com.cdsap.talaiot.configuration.*
import com.cdsap.talaiot.entities.CustomProperties
import com.cdsap.talaiot.entities.ExecutionReport


import com.cdsap.talaiot.entities.TaskLength
import com.cdsap.talaiot.entities.TaskMessageState
import com.cdsap.talaiot.logger.LogTracker
import com.cdsap.talaiot.logger.TestLogTrackerRecorder
import com.cdsap.talaiot.publisher.graphpublisher.KInfluxDBContainer
import com.cdsap.talaiot.request.Request
import com.nhaarman.mockitokotlin2.atLeastOnce
import com.nhaarman.mockitokotlin2.verify
import io.kotlintest.Description
import io.kotlintest.Spec
import io.kotlintest.specs.BehaviorSpec
import org.influxdb.dto.Query


class HybridPublisherTest : BehaviorSpec() {

    val database = "talaiot"
    val container = KInfluxDBContainer().withAuthEnabled(false)
    override fun beforeSpec(description: Description, spec: Spec) {
        super.beforeSpec(description, spec)
        container.start()
    }

    override fun afterSpec(description: Description, spec: Spec) {
        super.afterSpec(description, spec)
        container.stop()
    }

    val influxDB by lazy {
        container.newInfluxDB
    }

    init {
        given("Hybrid configuration") {
            val logger = TestLogTrackerRecorder

            `when`("Reporting task publisher is PushGateway and reporting build publisher is InfluxDb") {
                val influxDbConfiguration = InfluxDbPublisherConfiguration().apply {
                    dbName = database
                    url = container.url
                    taskMetricName = "task"
                    buildMetricName = "build"
                }
                val pushGatewayPublisherConfiguration = PushGatewayPublisherConfiguration().apply {
                    url = "http://localhost:9093"
                    taskJobName = "tracking"
                }

                val hybridPublisherConfiguration = HybridPublisherConfiguration().apply {
                    buildPublisher = influxDbConfiguration
                    taskPublisher = pushGatewayPublisherConfiguration
                }
                val hybridPublisher = HybridPublisher(
                    hybridPublisherConfiguration, logger, TestExecutor()
                )

                then("InfluxDbPublisher only reports builds") {
                    hybridPublisher.publish(
                        ExecutionReport(
                            customProperties = CustomProperties(taskProperties = getMetrics()),
                            tasks = listOf(
                                TaskLength(
                                    1, "clean", ":clean", TaskMessageState.EXECUTED, false,
                                    "app", emptyList()
                                )
                            )
                        )
                    )
                    logger.containsLog("PushGatewayPublisher")
                    logger.containsLog("InfluxDbPublisher")

                    val buildResult =
                        influxDB.query(Query("select \"duration\",configuration,success from $database.rpTalaiot.build"))

                    val combinedBuildColumns =
                        buildResult.results.joinToString { it.series.joinToString { it.columns.joinToString() } }
                    assert(combinedBuildColumns == "time, duration, configuration, success")

                    val combinedBuildValues =
                        buildResult.results.joinToString { it.series.joinToString { it.values.joinToString() } }
                    assert(combinedBuildValues.matches("""\[.+, 0\.0, 0\.0, false\]""".toRegex()))

                    val taskResult = influxDB.query(Query("select value from $database.rpTalaiot.task"))

                    assert(taskResult.results[0].series == null)
                }
            }
            `when`("Reporting task publisher is PushGateway and reporting build publisher is incorrect") {
                val pushGatewayPublisherConfiguration = PushGatewayPublisherConfiguration().apply {
                    url = "http://localhost:9093"
                    taskJobName = "tracking"
                }

                val outputPublisherConfiguration = OutputPublisherConfiguration()

                val hybridPublisherConfiguration = HybridPublisherConfiguration().apply {
                    buildPublisher = outputPublisherConfiguration
                    taskPublisher = pushGatewayPublisherConfiguration
                }
                val hybridPublisher = HybridPublisher(
                    hybridPublisherConfiguration, logger, TestExecutor()
                )

                then("Error is notified") {
                    hybridPublisher.publish(
                        ExecutionReport(
                            customProperties = CustomProperties(taskProperties = getMetrics()),
                            tasks = listOf(
                                TaskLength(
                                    1, "clean", ":clean", TaskMessageState.EXECUTED, false,
                                    "app", emptyList()
                                )
                            )
                        )
                    )
                    logger.containsLog("HybridPublisher: Not supported Publisher. Current Publishers supported by HybridPublisher: ")
                }
            }
            `when`("Reporting task publisher is null and reporting build publisher is null") {

                val hybridPublisherConfiguration = HybridPublisherConfiguration().apply {
                    buildPublisher = null
                    taskPublisher = null
                }
                val hybridPublisher = HybridPublisher(
                    hybridPublisherConfiguration, logger, TestExecutor()
                )

                then("Validation inform the error of null publishers") {
                    hybridPublisher.publish(
                        ExecutionReport(
                            customProperties = CustomProperties(taskProperties = getMetrics()),
                            tasks = listOf(
                                TaskLength(
                                    1, "clean", ":clean", TaskMessageState.EXECUTED, false,
                                    "app", emptyList()
                                )
                            )
                        )
                    )
                    logger.containsLog("HybridPublisher-Error: BuildPublisher and TaskPublisher are null. Not publisher will be executed ")
                }
            }
            `when`("Configuration for the HybridPublisher is specified by receiver") {
                val pushGatewayPublisherConfiguration = PushGatewayPublisherConfiguration().apply {
                    url = "http://localhost:9093"
                    taskJobName = "tracking"
                }

                val hybridPublisherConfiguration = HybridPublisherConfiguration().apply {
                    //task
                    taskPublisher {
                        pushGatewayPublisher {
                            url = "http://localhost:9093"
                            taskJobName = "tracking"
                        }
                        influxDbPublisher {
                            dbName = database
                            url = container.url
                            taskMetricName = "task"
                            buildMetricName = "build"
                        }
                    }
                    buildPublisher = pushGatewayPublisherConfiguration


                }
            }
        }
    }

}

private fun getMetrics(): MutableMap<String, String> {
    return mutableMapOf(
        "metric1" to "value1",
        "metric2" to "value2"
    )
}


class TestCustomPublisher : Publisher {
    override fun publish(report: ExecutionReport) {

    }

}