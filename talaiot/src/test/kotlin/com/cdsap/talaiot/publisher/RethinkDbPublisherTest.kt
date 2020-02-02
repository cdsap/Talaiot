package com.cdsap.talaiot.publisher

import com.cdsap.talaiot.configuration.RethinkDbPublisherConfiguration
import com.cdsap.talaiot.entities.*

import com.cdsap.talaiot.logger.TestLogTrackerRecorder
import com.cdsap.talaiot.publisher.rethinkdb.RethinkDbPublisher
import com.cdsap.talaiot.request.SimpleRequest
import io.github.rybalkinsd.kohttp.dsl.httpGet
import io.github.rybalkinsd.kohttp.ext.url
import io.kotlintest.Description
import io.kotlintest.Spec
import io.kotlintest.matchers.string.shouldNotContain
import io.kotlintest.specs.BehaviorSpec
import java.net.URL


class RethinkDbPublisherTest : BehaviorSpec() {

    val database = "rethink"
    val container = KRethinkDbContainer()
    override fun beforeSpec(description: Description, spec: Spec) {
        super.beforeSpec(description, spec)
        container.start()
    }

    override fun afterSpec(description: Description, spec: Spec) {
        super.afterSpec(description, spec)
        container.stop()
    }

    val rethinkDb by lazy {
        container
    }

    init {
        given("RethinkDb Publisher instance") {
            val logger = TestLogTrackerRecorder

            `when`("There is configuration with metrics for tasks and builds ") {
                val rethinkDbConfiguration = RethinkDbPublisherConfiguration().apply {
                    url = "http://" + container.httpHostAddress
                }

                val rethinkDb = RethinkDbPublisher(
                    rethinkDbConfiguration, logger, TestExecutor()
                )

                rethinkDb.publish(
                    executionReportData()
                )

                then("RethinkDb contains metrics for bulid and tasks") {

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
                        content?.contains(":app:assemble{instance=\"\",job=\"task\",metric1=\"value1\",metric2=\"value2\",module=\"app\",rootNode=\"false\",state=\"EXECUTED\"} 100")
                            ?: false
                    )
                    assert(
                        content?.contains(":clean{instance=\"\",job=\"task\",metric1=\"value1\",metric2=\"value2\",module=\"app\",rootNode=\"false\",state=\"EXECUTED\"} 1")
                            ?: false
                    )

                    assert(
                        content?.contains("build{cpuCount=\"12\",instance=\"\",job=\"build\",metric1=\"value1\",metric2=\"value2\",requestedTasks=\"assemble\"} 100")
                            ?: false
                    )
                }
            }
            `when`("There is configuration with metrics only to send tasks ") {
                val rethinkDbConfiguration = RethinkDbPublisherConfiguration().apply {
                    url = "http://" + container.httpHostAddress
                    publishBuildMetrics = false
                    buildTableName = "build2"
                    taskTableName = "task2"
                }

                val rethinkDb = RethinkDbPublisher(
                    rethinkDbConfiguration, logger, TestExecutor()
                )

                rethinkDb.publish(
                    executionReportData()
                )

                then("RethinkDb contains metrics for tasks but not for build") {

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
                        content?.contains(":app:assemble{instance=\"\",job=\"task2\",metric1=\"value1\",metric2=\"value2\",module=\"app\",rootNode=\"false\",state=\"EXECUTED\"} 100")
                            ?: false
                    )
                    assert(
                        content?.contains(":clean{instance=\"\",job=\"task2\",metric1=\"value1\",metric2=\"value2\",module=\"app\",rootNode=\"false\",state=\"EXECUTED\"} 1")
                            ?: false
                    )

                    content?.shouldNotContain("build2{cpuCount=\"12\",instance=\"\",job=\"build2\",metric1=\"value1\",metric2=\"value2\",requestedTasks=\"assemble\"} 100")

                }
            }
            `when`("There is configuration with metrics of tasks and build ") {
                val rethinkDbConfiguration = RethinkDbPublisherConfiguration().apply {
                    url = "http://" + container.httpHostAddress
                    buildTableName = "build4"
                    taskTableName = "task4"
                }

                val rethinkDb = RethinkDbPublisher(
                    rethinkDbConfiguration, logger, TestExecutor()
                )

                rethinkDb.publish(
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

                    content?.shouldNotContain(":test_module:clean{instance=\"\",job=\"task4\",metric1=\"value1\",metric2=\"value2\",module=\"app\",rootNode=\"false\",state=\"EXECUTED\"} 100")


                    content?.shouldNotContain(":test_module:app:assemble{instance=\"\",job=\"task4\",metric1=\"value1\",metric2=\"value2\",module=\"app\",rootNode=\"false\",state=\"EXECUTED\"} 1")
                    assert(
                        content?.contains("build4{cpuCount=\"12\",instance=\"\",job=\"build4\",metric1=\"value1\",metric2=\"value2\",requestedTasks=\"assemble\",switch_configurationOnDemand=\"true\",switch_dryRun=\"true\"} 100")
                            ?: false
                    )

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
                taskProperties = getMetrics(),
                buildProperties = getMetrics()
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
                taskProperties = getMetrics(),
                buildProperties = getMetrics()
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


    private fun getMetrics(): MutableMap<String, String> {
        return mutableMapOf(
            "metric1" to "value1",
            "metric2" to "value2"
        )
    }

}

