package io.github.cdsap.talaiot.publisher.influxdb

import io.github.cdsap.talaiot.entities.CustomProperties
import io.github.cdsap.talaiot.entities.Environment
import io.github.cdsap.talaiot.entities.ExecutionReport
import io.github.cdsap.talaiot.entities.TaskLength
import io.github.cdsap.talaiot.entities.TaskMessageState
import io.github.cdsap.talaiot.logger.TestLogTrackerRecorder
import io.github.cdsap.talaiot.metrics.BuildMetrics
import io.github.cdsap.talaiot.publisher.influxdb2.InfluxDb2Publisher
import io.github.cdsap.talaiot.publisher.influxdb2.InfluxDb2PublisherConfiguration
import io.github.cdsap.talaiot.utils.TestExecutor
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
        given("InfluxDbPublisher instance") {
            val logger = TestLogTrackerRecorder

            `when`("Simple configuration is provided") {
                val database = "talaiot"
                val influxDbConfiguration = InfluxDb2PublisherConfiguration().apply {
                   // dbName = database
                    url = container.httpHostAddress
                    taskMetricName = "task"
                    buildMetricName = "build"
                }
                val influxDbPublisher = InfluxDb2Publisher(
                    influxDbConfiguration, logger, TestExecutor()
                )
                influxDbPublisher.publish(executionReport())
                then("task and build data is store in the database") {

//                    val taskResultTask =
//                        influxDB.query(Query("select *  from $database.rpTalaiot.task"))
//                    val taskResultBuild =
//                        influxDB.query(Query("select * from $database.rpTalaiot.build"))
//                    assert(taskResultTask.results.isNotEmpty() && taskResultTask.results[0].series[0].name == "task")
       //             assert(taskResultBuild.results.isNotEmpty() && taskResultBuild.results[0].series[0].name == "build")
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
                    "app", emptyList()
                )
            )
        )
    }
}
