package com.cdsap.talaiot.publisher

import com.cdsap.talaiot.configuration.InfluxDbPublisherConfiguration
import org.testcontainers.influxdb.KInfluxDBContainer
import com.cdsap.talaiot.report.ExecutionReportProvider
import com.cdsap.talaiot.utils.TestExecutor
import com.cdsap.talaiot.logger.TestLogTrackerRecorder
import io.kotlintest.Spec
import io.kotlintest.specs.BehaviorSpec
import org.influxdb.dto.Query


class InfluxDbPublisherTest : BehaviorSpec() {

    val container = KInfluxDBContainer().withAuthEnabled(false)

    override fun beforeSpec(spec: Spec) {
        super.beforeSpec(spec)
        container.start()
    }

    override fun afterSpec(spec: Spec) {
        super.afterSpec(spec)
        container.stop()
    }

    val influxDB by lazy {
        container.newInfluxDB
    }

    init {
        given("InfluxDbPublisher instance") {
            val logger = TestLogTrackerRecorder

            `when`("Simple configuration is provided") {
                val database = "talaiot"
                val influxDbConfiguration = InfluxDbPublisherConfiguration().apply {
                    dbName = database
                    url = container.url
                    taskMetricName = "task"
                    buildMetricName = "build"
                }
                val influxDbPublisher = InfluxDbPublisher(
                    influxDbConfiguration, logger, TestExecutor()
                )
                influxDbPublisher.publish(ExecutionReportProvider.executionReport())
                then("task and build data is store in the database") {

                    val taskResultTask =
                        influxDB.query(Query("select *  from $database.rpTalaiot.task"))
                    val taskResultBuild =
                        influxDB.query(Query("select * from $database.rpTalaiot.build"))
                    assert(taskResultTask.results.isNotEmpty() && taskResultTask.results[0].series[0].name == "task")
                    assert(taskResultBuild.results.isNotEmpty() && taskResultBuild.results[0].series[0].name == "build")
                }
            }

            `when`("configuration defines just send build information") {
                val databaseNoTaskMetrics = "databaseWithoutTasks"
                val influxDbConfiguration = InfluxDbPublisherConfiguration().apply {
                    dbName = databaseNoTaskMetrics
                    url = container.url
                    taskMetricName = "task"
                    buildMetricName = "build"
                    publishTaskMetrics = false
                }
                val influxDbPublisher = InfluxDbPublisher(
                    influxDbConfiguration, logger, TestExecutor()
                )
                influxDbPublisher.publish(ExecutionReportProvider.executionReport())
                then("database contains only build information") {
                    val taskResultTask =
                        influxDB.query(Query("select * from $databaseNoTaskMetrics.rpTalaiot.task"))
                    val taskResultBuild =
                        influxDB.query(Query("select * from $databaseNoTaskMetrics.rpTalaiot.build"))
                    assert(taskResultTask.results.isNotEmpty() && taskResultTask.results[0].series == null)
                    assert(taskResultBuild.results.isNotEmpty() && taskResultBuild.results[0].series[0].name == "build")
                }
            }
            `when`("configuration defines just send task information") {
                val databaseNoBuildMetrics = "databaseWithoutBuild"
                val influxDbConfiguration = InfluxDbPublisherConfiguration().apply {
                    dbName = databaseNoBuildMetrics
                    url = container.url
                    taskMetricName = "task"
                    buildMetricName = "build"
                    publishBuildMetrics = false
                }
                val influxDbPublisher = InfluxDbPublisher(
                    influxDbConfiguration, logger, TestExecutor()
                )
                influxDbPublisher.publish(ExecutionReportProvider.executionReport())
                then("database contains only task information") {
                    val taskResultTask =
                        influxDB.query(Query("select * from $databaseNoBuildMetrics.rpTalaiot.task"))
                    val taskResultBuild =
                        influxDB.query(Query("select * from $databaseNoBuildMetrics.rpTalaiot.build"))
                    assert(taskResultTask.results.isNotEmpty() && taskResultTask.results[0].series[0].name == "task")
                    assert(taskResultBuild.results.isNotEmpty() && taskResultBuild.results[0].series == null)
                }
            }
            `when`("the execution report includes custom task metrics") {
                val databaseTaskMetrics = "databaseTaskBuild"
                val influxDbConfiguration = InfluxDbPublisherConfiguration().apply {
                    dbName = databaseTaskMetrics
                    url = container.url
                    taskMetricName = "task"
                    buildMetricName = "build"
                }
                val influxDbPublisher = InfluxDbPublisher(
                    influxDbConfiguration, logger, TestExecutor()
                )
                influxDbPublisher.publish(ExecutionReportProvider.executionReport())
                then("database contains custom metrics linked to the task execution") {
                    val taskResult =
                        influxDB.query(Query("select value,state,module,rootNode,task,metric1,metric2 from $databaseTaskMetrics.rpTalaiot.task"))
                    val combinedTaskColumns =
                        taskResult.results.joinToString { it.series.joinToString { it.columns.joinToString() } }
                    assert(combinedTaskColumns == "time, value, state, module, rootNode, task, metric1, metric2")

                    val combinedTaskValues =
                        taskResult.results.joinToString { it.series.joinToString { it.values.joinToString() } }
                     assert(combinedTaskValues.matches("""\[.+, 1\.0, EXECUTED, app, false, :assemble, value1, value2\]""".toRegex()))

                }
            }
            `when`("the execution report includes custom build metrics") {
                val databaseBuildMetrics = "databaseBuild"
                val influxDbConfiguration = InfluxDbPublisherConfiguration().apply {
                    dbName = databaseBuildMetrics
                    url = container.url
                    taskMetricName = "task"
                    buildMetricName = "build"
                }
                val influxDbPublisher = InfluxDbPublisher(
                    influxDbConfiguration, logger, TestExecutor()
                )
                influxDbPublisher.publish(ExecutionReportProvider.executionReport())
                then("database contains custom metrics linked to the build execution") {

                    val buildResult =
                        influxDB.query(Query("select configuration, metric3, metric4, success from $databaseBuildMetrics.rpTalaiot.build"))

                    val combinedBuildColumns =
                        buildResult.results.joinToString { it.series.joinToString { it.columns.joinToString() } }
                    assert(combinedBuildColumns == "time, configuration, metric3, metric4, success")

                    val combinedBuildValues =
                        buildResult.results.joinToString { it.series.joinToString { it.values.joinToString() } }
                    assert(combinedBuildValues.matches("""\[.+, 0\.0, value3, value4, true\]""".toRegex()))

                }
            }

            `when`("publishOnlyBuildMetrics is enabled ") {
                val databaseNoMetrics = "databaseWithoutTasks"
                val influxDbConfiguration = InfluxDbPublisherConfiguration().apply {
                    dbName = databaseNoMetrics
                    url = container.url
                    taskMetricName = "task"
                    buildMetricName = "build"
                    publishTaskMetrics = false
                }
                val influxDbPublisher = InfluxDbPublisher(
                    influxDbConfiguration, logger, TestExecutor()
                )

                then("build metrics are sent and task metrics doesn't") {
                    influxDbPublisher.publish(ExecutionReportProvider.executionReport())

                    val buildResult =
                        influxDB.query(Query("select \"duration\",configuration,success from $databaseNoMetrics.rpTalaiot.build"))

                    val combinedBuildColumns =
                        buildResult.results.joinToString { it.series.joinToString { it.columns.joinToString() } }
                    assert(combinedBuildColumns == "time, duration, configuration, success")

                    val combinedBuildValues =
                        buildResult.results.joinToString { it.series.joinToString { it.values.joinToString() } }
                    assert(combinedBuildValues.matches("""\[.+, 10\.0, 0\.0, true\]""".toRegex()))

                    val taskResult = influxDB.query(Query("select value from $databaseNoMetrics.rpTalaiot.task"))

                    assert(taskResult.results[0].series == null)

                }
            }

        }
    }

}
