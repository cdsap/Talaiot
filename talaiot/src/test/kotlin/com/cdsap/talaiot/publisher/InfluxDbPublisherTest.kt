package com.cdsap.talaiot.publisher

import com.cdsap.talaiot.configuration.InfluxDbPublisherConfiguration
import com.cdsap.talaiot.entities.CustomProperties
import com.cdsap.talaiot.entities.ExecutionReport


import com.cdsap.talaiot.entities.TaskLength
import com.cdsap.talaiot.entities.TaskMessageState
import com.cdsap.talaiot.logger.LogTracker
import com.cdsap.talaiot.logger.TestLogTrackerRecorder
import com.cdsap.talaiot.publisher.graphpublisher.KInfluxDBContainer
import com.cdsap.talaiot.request.Request
import io.kotlintest.Description
import io.kotlintest.Spec
import io.kotlintest.specs.BehaviorSpec
import org.influxdb.dto.Query


class InfluxDbPublisherTest : BehaviorSpec() {

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
        given("InfluxDbPublisher configuration") {
            val logger = TestLogTrackerRecorder

            `when`("There is configuration with metrics and tasks ") {
                val influxDbConfiguration = InfluxDbPublisherConfiguration().apply {
                    dbName = database
                    url = container.url
                    taskMetricName = "task"
                    buildMetricName = "build"
                }
                val testRequest = TestRequest(logger)
                val influxDbPublisher = InfluxDbPublisher(
                    influxDbConfiguration, logger, testRequest, TestExecutor()
                )

                then("should push all task present") {
                    influxDbPublisher.publish(
                        ExecutionReport(
                            customProperties = CustomProperties(getMetrics()),

                            tasks = listOf(
                                TaskLength(
                                    1, "clean", ":clean", TaskMessageState.EXECUTED, false,
                                    "app", emptyList(), critical =
                                )
                            )
                        )
                    )
                    val taskResult = influxDB.query(Query("select value,state,module,rootNode,task,metric1,metric2 from $database.rpTalaiot.task"))

                    val combinedTaskColumns =
                        taskResult.results.joinToString { it.series.joinToString { it.columns.joinToString() } }
                    assert(combinedTaskColumns == "time, value, state, module, rootNode, task, metric1, metric2")

                    val combinedTaskValues =
                        taskResult.results.joinToString { it.series.joinToString { it.values.joinToString() } }
                    assert(combinedTaskValues.matches("""\[.+, 1\.0, EXECUTED, app, false, :clean, value1, value2\]""".toRegex()))

                    val buildResult = influxDB.query(Query("select \"duration\",configuration,success from $database.rpTalaiot.build"))

                    val combinedBuildColumns =
                        buildResult.results.joinToString { it.series.joinToString { it.columns.joinToString() } }
                    assert(combinedBuildColumns == "time, duration, configuration, success")

                    val combinedBuildValues =
                        buildResult.results.joinToString { it.series.joinToString { it.values.joinToString() } }
                    assert(combinedBuildValues.matches("""\[.+, 0\.0, 0\.0, false\]""".toRegex()))
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

class TestRequest(
    override var logTracker: LogTracker
) : Request {

    var url = ""
    var content = ""
    override fun send(url: String, content: String) {
        this.url = url
        this.content = content
    }
}
