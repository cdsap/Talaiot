package io.github.cdsap.talaiot.publisher

import com.rethinkdb.RethinkDB
import com.rethinkdb.net.Connection
import io.github.cdsap.talaiot.configuration.PublisherConfiguration
import io.github.cdsap.talaiot.entities.CustomProperties
import io.github.cdsap.talaiot.entities.ExecutionReport
import io.github.cdsap.talaiot.entities.TaskLength
import io.github.cdsap.talaiot.entities.TaskMessageState
import io.github.cdsap.talaiot.logger.TestLogTrackerRecorder
import io.github.cdsap.talaiot.publisher.hybrid.HybridPublisher
import io.github.cdsap.talaiot.publisher.hybrid.HybridPublisherConfiguration
import io.github.cdsap.talaiot.publisher.influxdb.InfluxDbPublisherConfiguration
import io.github.cdsap.talaiot.publisher.pushgateway.PushGatewayPublisherConfiguration
import io.github.cdsap.talaiot.publisher.rethinkdb.RethinkDbPublisherConfiguration
import io.github.cdsap.talaiot.utils.TestExecutor
import io.kotlintest.Description
import io.kotlintest.Spec
import io.kotlintest.specs.BehaviorSpec
import org.influxdb.dto.Query
import org.testcontainers.influxdb.KInfluxDBContainer
import org.testcontainers.pushgateway.KPushGatewayContainer
import org.testcontainers.rethinkdb.KRethinkDbContainer
import java.net.URL

class HybridPublisherTest : BehaviorSpec() {

    private val database = "talaiot"
    private val container = KInfluxDBContainer().withAuthEnabled(false)
    private val containerRethink = KRethinkDbContainer()
    private val containerPushGateway = KPushGatewayContainer()

    private val r = RethinkDB.r

    override fun beforeSpec(description: Description, spec: Spec) {
        super.beforeSpec(description, spec)
        container.start()
        containerRethink.start()
        containerPushGateway.start()
    }

    override fun afterSpec(spec: Spec) {
        super.afterSpec(spec)
        container.stop()
        containerRethink.stop()
        containerPushGateway.stop()
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
                    url = "http://" + containerPushGateway.httpHostAddress
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
                            durationMs = "10",
                            configurationDurationMs = "1",
                            customProperties = CustomProperties(
                                taskProperties = getMetricsTasks()
                            ),
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
                    assert(combinedBuildValues.matches("""\[.+, 10\.0, 1\.0, false\]""".toRegex()))

                    val taskResult =
                        influxDB.query(Query("select value from $database.rpTalaiot.task"))

                    assert(taskResult.results[0].series == null)
                }
            }
            `when`("Reporting task publisher is PushGateway and reporting build publisher is incorrect") {
                val pushGatewayPublisherConfiguration = PushGatewayPublisherConfiguration().apply {
                    url = "http://" + containerPushGateway.httpHostAddress
                    taskJobName = "tracking"
                }

                val outputPublisherConfiguration = PublisherTestConfiguration()

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
                            customProperties = CustomProperties(taskProperties = getMetricsTasks()),
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
                            customProperties = CustomProperties(taskProperties = getMetricsTasks()),
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
            `when`("Reporting task publisher is RethinkDbPublisher and reporting build publisher is InfluxDb") {
                val influxDbConfiguration = InfluxDbPublisherConfiguration().apply {
                    dbName = database
                    url = container.url
                    taskMetricName = "task"
                    buildMetricName = "build"
                }
                val rethinkDbPublisherConfiguration = RethinkDbPublisherConfiguration().apply {
                    url = "http://" + containerRethink.httpHostAddress
                    dbName = "tracking"
                    taskTableName = "tasks"
                    buildTableName = "build"
                }

                val hybridPublisherConfiguration = HybridPublisherConfiguration().apply {
                    buildPublisher = influxDbConfiguration
                    taskPublisher = rethinkDbPublisherConfiguration
                }
                val hybridPublisher = HybridPublisher(
                    hybridPublisherConfiguration, logger, TestExecutor()
                )

                then("RethinkDbPublisher only reports builds") {
                    hybridPublisher.publish(
                        ExecutionReport(
                            customProperties = CustomProperties(taskProperties = getMetricsTasks()),
                            tasks = listOf(
                                TaskLength(
                                    1, "clean", ":clean", TaskMessageState.EXECUTED, false,
                                    "app", emptyList()
                                )
                            )
                        )
                    )
                    logger.containsLog("RethinkDbPublisher")
                    logger.containsLog("InfluxDbPublisher")

                    val conn = getConnection(rethinkDbPublisherConfiguration.url)
                    val existsTableTasks =
                        r.db(rethinkDbPublisherConfiguration.dbName).tableList()
                            .contains(rethinkDbPublisherConfiguration.taskTableName)
                            .run<Boolean>(conn)
                    val existsTableBuilds =
                        r.db(rethinkDbPublisherConfiguration.dbName).tableList()
                            .contains(rethinkDbPublisherConfiguration.buildTableName)
                            .run<Boolean>(conn)
                    assert(!existsTableBuilds)
                    assert(existsTableTasks)
                }
            }
        }
    }

    fun getMetricsTasks(): MutableMap<String, String> {
        return mutableMapOf(
            "metric1" to "value1",
            "metric2" to "value2"
        )
    }

    private fun getConnection(url: String): Connection {
        val url = URL(url)
        return r.connection().hostname(url.host).port(url.port).connect()
    }

    class PublisherTestConfiguration : PublisherConfiguration {
        override var name: String = "testConf"
        override var publishBuildMetrics: Boolean = true
        override var publishTaskMetrics: Boolean = true
    }
}
