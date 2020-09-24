package com.cdsap.talaiot.publisher

import com.cdsap.talaiot.configuration.PushGatewayPublisherConfiguration
import com.cdsap.talaiot.entities.*

import com.cdsap.talaiot.publisher.pushgateway.PushGatewayFormatter
import com.cdsap.talaiot.publisher.pushgateway.PushGatewayPublisher
import com.cdsap.talaiot.report.ExecutionReportProvider
import com.cdsap.talaiot.request.SimpleRequest
import com.cdsap.talaiot.logger.TestLogTrackerRecorder
import com.cdsap.talaiot.utils.TestExecutor
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
                    pushGatewayConfiguration, logger, SimpleRequest(logger), TestExecutor(), PushGatewayFormatter()
                )

                pushGateway.publish(
                    executionReportData()
                )

                then("Pushgateway contains metrics for bulid and tasks") {

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
                    val content = a.body?.string()
                    assert(
                        content?.contains(":app:assemble{cacheEnabled=\"false\",critical=\"false\",instance=\"\",job=\"task\",localCacheHit=\"false\",localCacheMiss=\"false\",metric1=\"value1\",metric2=\"value2\",module=\"app\",remoteCacheHit=\"false\",remoteCacheMiss=\"false\",rootNode=\"false\",state=\"EXECUTED\",task=\":app:assemble\",value=\"100\",workerId=\"\"} 100")
                            ?: false
                    )
                    assert(
                        content?.contains(":clean{cacheEnabled=\"false\",critical=\"false\",instance=\"\",job=\"task\",localCacheHit=\"false\",localCacheMiss=\"false\",metric1=\"value1\",metric2=\"value2\",module=\"app\",remoteCacheHit=\"false\",remoteCacheMiss=\"false\",rootNode=\"false\",state=\"EXECUTED\",task=\":clean\",value=\"1\",workerId=\"\"} 1")
                            ?: false
                    )
                    assert(
                        content?.contains("build{configuration=\"0\",cpuCount=\"12\",duration=\"100\",instance=\"\",job=\"build\",maxWorkers=\"4\",metric3=\"value3\",metric4=\"value4\",requestedTasks=\"assemble\",success=\"false\"} 100")
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
                    pushGatewayConfiguration, logger, SimpleRequest(logger), TestExecutor(), PushGatewayFormatter()
                )

                pushGateway.publish(
                    executionReportData()
                )

                then("Pushgateway contains metrics for tasks but not for build") {

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
                    val content = a.body?.string()

                    assert(
                        content?.contains(":app:assemble{cacheEnabled=\"false\",critical=\"false\",instance=\"\",job=\"task2\",localCacheHit=\"false\",localCacheMiss=\"false\",metric1=\"value1\",metric2=\"value2\",module=\"app\",remoteCacheHit=\"false\",remoteCacheMiss=\"false\",rootNode=\"false\",state=\"EXECUTED\",task=\":app:assemble\",value=\"100\",workerId=\"\"} 100")
                            ?: false
                    )
                    assert(
                        content?.contains(":clean{cacheEnabled=\"false\",critical=\"false\",instance=\"\",job=\"task2\",localCacheHit=\"false\",localCacheMiss=\"false\",metric1=\"value1\",metric2=\"value2\",module=\"app\",remoteCacheHit=\"false\",remoteCacheMiss=\"false\",rootNode=\"false\",state=\"EXECUTED\",task=\":clean\",value=\"1\",workerId=\"\"} 1")
                            ?: false
                    )

                    assert(!(content?.contains("build2{") ?: true))

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
                    pushGatewayConfiguration, logger, SimpleRequest(logger), TestExecutor(), PushGatewayFormatter()
                )

                pushGateway.publish(
                    executionReportData()
                )

                then("Pushgateway contains metrics for build but not for task") {

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
                    val content = a.body?.string()

                    assert(
                        content?.contains("build3")
                            ?: false
                    )
                    assert(!(content?.contains("task3") ?: true))

                }
            }

            `when`("There is configuration with custom metrics of tasks and build ") {
                val pushGatewayConfiguration = PushGatewayPublisherConfiguration().apply {
                    url = "http://" + container.httpHostAddress
                    buildJobName = "build4"
                    taskJobName = "task4"
                }

                val pushGateway = PushGatewayPublisher(
                    pushGatewayConfiguration, logger, SimpleRequest(logger), TestExecutor(), PushGatewayFormatter()
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
                    val content = a.body?.string()


                    content?.contains("metric1=\"value1\",metric2=\"value2\",module=\":test-module\",rootNode=\"false\",state=\"EXECUTED\",task=\":test-module:clean\",value=\"1\",workerId=\"\"} 100")

                    content?.contains(":test_module:app:assemble{critical=\"false\",instance=\"\",job=\"task4\",metric1=\"value1\",metric2=\"value2\",module=\":test-module\",rootNode=\"false\",state=\"EXECUTED\",task=\":test-module:app:assemble\",value=\"100\",workerId=\"\"} 1")

                    content?.contains("build4{configuration=\"0\",cpuCount=\"12\",duration=\"100\",instance=\"\",job=\"build4\",maxWorkers=\"4\",metric1=\"value1\",metric2=\"value2\",requestedTasks=\"assemble\",success=\"false\",switch_configurationOnDemand=\"true\",switch_dryRun=\"true\"} 100")

                }
            }
        }
    }

    private fun executionReportData(): ExecutionReport {
        return ExecutionReport(
            durationMs = "100",
            requestedTasks = "assemble",
            environment = Environment(
                cpuCount = "12", maxWorkers = "4"
            ),
            customProperties = CustomProperties(
                taskProperties = ExecutionReportProvider.getMetricsTasks(),
                buildProperties = ExecutionReportProvider.getMetricsBuild()
            ),
            tasks = listOf(
                TaskLength(
                    1, "clean", ":clean", TaskMessageState.EXECUTED, false,
                    "app", emptyList()
                ),
                TaskLength(
                    100, "assemble", ":app:assemble", TaskMessageState.EXECUTED, false,
                    "app", emptyList()
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
                taskProperties = ExecutionReportProvider.getMetricsTasks(),
                buildProperties = ExecutionReportProvider.getMetricsBuild()
            ),
            tasks = listOf(
                TaskLength(
                    1, "clean", ":test-module:clean", TaskMessageState.EXECUTED, false,
                    ":test-module", emptyList()
                ),
                TaskLength(
                    100, "assemble", ":test-module:app:assemble", TaskMessageState.EXECUTED, false,
                    ":test-module", emptyList()
                )
            )
        )
    }
}
