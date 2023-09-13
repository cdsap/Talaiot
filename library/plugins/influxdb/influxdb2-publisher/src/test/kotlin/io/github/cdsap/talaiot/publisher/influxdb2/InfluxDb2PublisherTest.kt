package io.github.cdsap.talaiot.publisher.influxdb2

import com.influxdb.client.InfluxDBClientFactory
import io.github.cdsap.talaiot.entities.CustomProperties
import io.github.cdsap.talaiot.entities.Environment
import io.github.cdsap.talaiot.entities.ExecutionReport
import io.github.cdsap.talaiot.entities.TaskLength
import io.github.cdsap.talaiot.entities.TaskMessageState
import io.github.cdsap.talaiot.logger.TestLogTrackerRecorder
import io.kotlintest.Spec
import io.kotlintest.specs.BehaviorSpec
import org.testcontainers.influxdb2.KInfluxDb2Container

class InfluxDb2PublisherTest : BehaviorSpec() {

    val container = KInfluxDb2Container()

    override fun beforeSpec(spec: Spec) {
        super.beforeSpec(spec)
        container.start()
    }

    override fun afterSpec(spec: Spec) {
        super.afterSpec(spec)
        container.stop()
    }

    init {
        given("InfluxDb2Publisher instance") {
            val logger = TestLogTrackerRecorder

            `when`("Simple configuration is provided") {
                val influxDbConfiguration = InfluxDb2PublisherConfiguration().apply {
                    url = container.url
                    bucket = "test-bucket"
                    token = "test-token"
                    org = "test-org"
                    taskMetricName = "task"
                    buildMetricName = "build"
                }
                val influxDbPublisher = InfluxDb2Publisher(
                    influxDbConfiguration, logger
                )
                val influxDBClient = InfluxDBClientFactory.create(container.url, "test-token".toCharArray(), "test-org")
                influxDbPublisher.publish(executionReport())

                then("build and task data is stored correctly on the InfluxDb2 Instance") {
                    val queryApi = influxDBClient.queryApi
                    val fluxBuild =
                        "from(bucket:\"test-bucket\") |> range(start: 0) |> filter(fn: (r) => r._measurement == \"build\")"
                    val tablesBuild = queryApi.query(fluxBuild)

                    assert(tablesBuild.filter { it.records.filter { it.field == "cpuCount" && it.value.toString() == "12" }.size == 1 }.size == 1)
                    assert(tablesBuild.filter { it.records.filter { it.field == "metric3" && it.value == "value3" }.size == 1 }.size == 1)
                    assert(tablesBuild.filter { it.records.filter { it.field == "duration" && it.value.toString() == "10" }.size == 1 }.size == 1)

                    val fluxTask =
                        "from(bucket:\"test-bucket\") |> range(start: 0) |> filter(fn: (r) => r._measurement == \"task\")"

                    val tablesTask = queryApi.query(fluxTask)

                    assert(
                        tablesTask[0].records.filter {
                            it.values.filter {
                                it.key == "task" && it.value == ":assemble"
                            }.size == 1
                        }.size == 1
                    )
                    assert(
                        tablesTask[0].records.filter {
                            it.values.filter {
                                it.key == "metric1" && it.value == "value1"
                            }.size == 1
                        }.size == 1
                    )
                    influxDBClient.close()
                }
            }

            `when`("bucket doesn't exist") {
                val influxDbConfiguration = InfluxDb2PublisherConfiguration().apply {
                    url = container.url
                    bucket = "test-bucket3"
                    token = "test-token"
                    org = "test-org"
                    taskMetricName = "task"
                    buildMetricName = "build"
                }
                val influxDbPublisher = InfluxDb2Publisher(
                    influxDbConfiguration, logger
                )
                influxDbPublisher.publish(executionReport())

                then("Talaiot creates the bucket") {
                    val influxDBClient =
                        InfluxDBClientFactory.create(container.url, "test-token".toCharArray(), "test-org")
                    val queryApi = influxDBClient.queryApi
                    val fluxBuild =
                        "from(bucket:\"test-bucket3\") |> range(start: 0) |> filter(fn: (r) => r._measurement == \"build\")"
                    val tablesBuild = queryApi.query(fluxBuild)

                    assert(tablesBuild.filter { it.records.filter { it.field == "duration" && it.value.toString() == "10" }.size == 1 }.size == 1)
                    influxDBClient.close()
                }
            }
        }
    }

    private fun executionReport(): ExecutionReport {
        return ExecutionReport(
            requestedTasks = "assemble",
            durationMs = "10",
            success = true,
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
                    1, "assemble", ":assemble", TaskMessageState.EXECUTED, false,
                    "app"
                )
            )
        )
    }
}
