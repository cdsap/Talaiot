package io.github.cdsap.talaiot.publisher.pushgateway

import io.github.cdsap.talaiot.entities.CustomProperties
import io.github.cdsap.talaiot.entities.Environment
import io.github.cdsap.talaiot.entities.ExecutionReport
import io.github.cdsap.talaiot.entities.Switches
import io.github.cdsap.talaiot.entities.TaskLength
import io.github.cdsap.talaiot.entities.TaskMessageState
import io.github.cdsap.talaiot.logger.TestLogTrackerRecorder
import io.github.rybalkinsd.kohttp.dsl.httpGet
import io.github.rybalkinsd.kohttp.ext.url
import io.kotlintest.Spec
import io.kotlintest.specs.BehaviorSpec
import org.testcontainers.pushgateway.KPushGatewayContainer
import java.net.URL

class PushGatewayPublisherTest : BehaviorSpec() {

    val container = KPushGatewayContainer()
    override fun beforeSpec(spec: Spec) {
        super.beforeSpec(spec)
        container.start()
    }

    override fun afterSpec(spec: Spec) {
        super.afterSpec(spec)
        container.stop()
    }

    val pushGateway by lazy {
        container
    }

    init {
        given("PushGateway Publisher instance") {
            val logger = TestLogTrackerRecorder

            `when`("There is configuration with metrics for tasks and builds ") {
                val pushGatewayConfiguration = PushGatewayPublisherConfiguration().apply {
                    url = "http://" + container.httpHostAddress
                }

                val pushGateway = PushGatewayPublisher(
                    pushGatewayConfiguration, logger
                )

                pushGateway.publish(
                    executionReportData()
                )

                then("Pushgateway contains metrics for build and tasks") {

                    Thread.sleep(2000)
                    val urlSpec = URL("http://" + container.httpHostAddress + "/metrics")

                    val a = httpGet {
                        url(urlSpec)
                        if (urlSpec.query != null) {
                            val query = urlSpec.query.split("=")
                            param {
                                query[0] to query[1]
                            }
                        }
                    }
                    val content = a.body()?.string()
                    assert(
                        content?.contains("gradle_task_assemble{Module=\"app\",State=\"EXECUTED\",instance=\"\",job=\"task\",metric1=\"value1\",metric2=\"value2\"} 100")
                            ?: false
                    )
                    assert(
                        content?.contains("gradle_task_clean{Module=\"app\",State=\"EXECUTED\",instance=\"\",job=\"task\",metric1=\"value1\",metric2=\"value2\"} 1")
                            ?: false
                    )
                    assert(
                        content?.contains("gradle_build_total_time{instance=\"\",job=\"build\",metric3=\"value3\",metric4=\"value4\",requestedTasks=\"assemble\"} 100")
                            ?: false
                    )
                }
            }

            `when`("There is configuration with metrics only to send tasks ") {
                val pushGatewayConfiguration = PushGatewayPublisherConfiguration().apply {
                    url = "http://" + container.httpHostAddress
                    publishBuildMetrics = false
                    buildJobName = "build2"
                    taskJobName = "task2"
                }

                val pushGateway = PushGatewayPublisher(
                    pushGatewayConfiguration, logger
                )

                pushGateway.publish(
                    executionReportData()
                )

                then("Pushgateway contains metrics for tasks but not for build") {
                    Thread.sleep(2000)
                    val urlSpec = URL("http://" + container.httpHostAddress + "/metrics")

                    val a = httpGet {
                        url(urlSpec)
                        if (urlSpec.query != null) {
                            val query = urlSpec.query.split("=")
                            param {
                                query[0] to query[1]
                            }
                        }
                    }
                    val content = a.body()?.string()

                    assert(
                        content?.contains("gradle_task_clean{Module=\"app\",State=\"EXECUTED\",instance=\"\",job=\"task2\",metric1=\"value1\",metric2=\"value2\"} 1")
                            ?: false
                    )
                    assert(
                        content?.contains("gradle_task_assemble{Module=\"app\",State=\"EXECUTED\",instance=\"\",job=\"task2\",metric1=\"value1\",metric2=\"value2\"} 100")
                            ?: false
                    )

                    assert(!(content?.contains("job=\"build2\",") ?: true))
                }
            }
            `when`("There is configuration with metrics only to send build ") {
                val pushGatewayConfiguration = PushGatewayPublisherConfiguration().apply {
                    url = "http://" + container.httpHostAddress
                    publishTaskMetrics = false
                    buildJobName = "build3"
                    taskJobName = "task3"
                }

                val pushGateway = PushGatewayPublisher(
                    pushGatewayConfiguration, logger
                )

                pushGateway.publish(
                    executionReportData()
                )

                then("Pushgateway contains metrics for build but not for task") {
                    Thread.sleep(2000)
                    val urlSpec = URL("http://" + container.httpHostAddress + "/metrics")

                    val a = httpGet {
                        url(urlSpec)
                        if (urlSpec.query != null) {
                            val query = urlSpec.query.split("=")
                            param {
                                query[0] to query[1]
                            }
                        }
                    }
                    val content = a.body()?.string()

                    assert(
                        !(
                            content?.contains("gradle_task_assemble{Module=\"app\",State=\"EXECUTED\",instance=\"\",job=\"task3\",metric1=\"value1\",metric2=\"value2\"} 100")
                                ?: true
                            )
                    )
                    assert(
                        !(
                            content?.contains("gradle_task_clean{Module=\"app\",State=\"EXECUTED\",instance=\"\",job=\"task3\",metric1=\"value1\",metric2=\"value2\"} 1")

                                ?: true
                            )
                    )
                    assert(
                        content?.contains("gradle_build_total_time{instance=\"\",job=\"build3\",metric3=\"value3\",metric4=\"value4\",requestedTasks=\"assemble\"} 100")
                            ?: false
                    )
                }
            }

            `when`("There is configuration with custom metrics of tasks and build ") {
                val pushGatewayConfiguration = PushGatewayPublisherConfiguration().apply {
                    url = "http://" + container.httpHostAddress
                    buildJobName = "build4"
                    taskJobName = "task4"
                }

                val pushGateway = PushGatewayPublisher(
                    pushGatewayConfiguration, logger
                )

                pushGateway.publish(
                    executionReportDataSpecialData()
                )

                then("metrics are formatted") {

                    val urlSpec = URL("http://" + container.httpHostAddress + "/metrics")

                    val a = httpGet {
                        url(urlSpec)
                        if (urlSpec.query != null) {
                            val query = urlSpec.query.split("=")
                            param {
                                query[0] to query[1]
                            }
                        }
                    }
                    val content = a.body()?.string()

                    content?.contains("metric1=\"value1\",metric2=\"value2\",module=\":test-module\",rootNode=\"false\",state=\"EXECUTED\",task=\":test-module:clean\",value=\"1\",workerId=\"\"} 100")

                    content?.contains(":test_module:app:assemble{instance=\"\",job=\"task4\",metric1=\"value1\",metric2=\"value2\",module=\":test-module\",rootNode=\"false\",state=\"EXECUTED\",task=\":test-module:app:assemble\",value=\"100\",workerId=\"\"} 1")

                    content?.contains("build4{configuration=\"0\",cpuCount=\"12\",duration=\"100\",instance=\"\",job=\"build4\",maxWorkers=\"4\",metric1=\"value1\",metric2=\"value2\",requestedTasks=\"assemble\",success=\"false\",switch_configurationOnDemand=\"true\",switch_dryRun=\"true\"} 100")
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

    private fun executionReportDataSpecialData(): ExecutionReport {
        return ExecutionReport(
            durationMs = "100",
            requestedTasks = "assemble",
            environment = Environment(
                switches = Switches(
                    configurationOnDemand = "true",
                    dryRun = "true"
                ),
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
                    1, "clean", ":test-module:clean", TaskMessageState.EXECUTED, false,
                    ":test-module"
                ),
                TaskLength(
                    100, "assemble", ":test-module:app:assemble", TaskMessageState.EXECUTED, false,
                    ":test-module"
                )
            )
        )
    }
}
