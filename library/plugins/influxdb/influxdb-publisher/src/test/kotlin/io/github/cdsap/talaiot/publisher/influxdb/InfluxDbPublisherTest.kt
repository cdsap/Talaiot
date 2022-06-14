package io.github.cdsap.talaiot.publisher.influxdb

import io.github.cdsap.talaiot.entities.CustomProperties
import io.github.cdsap.talaiot.entities.Environment
import io.github.cdsap.talaiot.entities.ExecutionReport
import io.github.cdsap.talaiot.entities.TaskLength
import io.github.cdsap.talaiot.entities.TaskMessageState
import io.github.cdsap.talaiot.logger.TestLogTrackerRecorder
import io.github.cdsap.talaiot.metrics.BuildMetrics
import io.kotlintest.Spec
import io.kotlintest.specs.BehaviorSpec
import org.influxdb.dto.Query
import org.testcontainers.influxdb.KInfluxDBContainer

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
                    influxDbConfiguration, logger
                )
                influxDbPublisher.publish(executionReport())

                then("task and build data is store in the database") {
                    Thread.sleep(2000)

                    val taskResultTask =
                        influxDB.query(Query("select *  from $database.rpTalaiot.task"))
                    println(taskResultTask.results)
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
                    influxDbConfiguration, logger
                )
                influxDbPublisher.publish(executionReport())
                then("database contains only build information") {
                    Thread.sleep(2000)

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
                    influxDbConfiguration, logger
                )
                influxDbPublisher.publish(executionReport())
                then("database contains only task information") {
                    Thread.sleep(2000)

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
                    influxDbConfiguration, logger
                )
                influxDbPublisher.publish(executionReport())
                then("database contains custom metrics linked to the task execution") {
                    Thread.sleep(2000)

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
                    influxDbConfiguration, logger
                )
                influxDbPublisher.publish(executionReport())
                then("database contains custom metrics linked to the build execution") {
                    Thread.sleep(2000)

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
                    influxDbConfiguration, logger
                )

                then("build metrics are sent and task metrics doesn't") {
                    influxDbPublisher.publish(executionReport())
                    Thread.sleep(2000)

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
            `when`("custom metrics are included as tags") {
                val databaseTags = "databaseTags"
                val influxDbConfiguration = InfluxDbPublisherConfiguration().apply {
                    dbName = databaseTags
                    url = container.url
                    taskMetricName = "task"
                    buildMetricName = "build"
                    publishTaskMetrics = false
                    buildTags = listOf(BuildMetrics.Custom, BuildMetrics.MaxWorkers)
                }
                val influxDbPublisher = InfluxDbPublisher(
                    influxDbConfiguration, logger
                )

                then("build metrics are sent and task metrics doesn't") {
                    influxDbPublisher.publish(executionReport())
                    Thread.sleep(2000)

                    val buildResult =
                        influxDB.query(Query("select * from $databaseTags.rpTalaiot.build group by *"))
                    assert(buildResult.results[0].series[0].tags.containsKey(BuildMetrics.MaxWorkers.toString()))
                    assert(buildResult.results[0].series[0].tags.containsKey("metric3"))
                    assert(buildResult.results[0].series[0].tags.containsKey("metric4"))
                    assert(!buildResult.results[0].series[0].columns.contains(BuildMetrics.MaxWorkers.toString()))
                    assert(!buildResult.results[0].series[0].columns.contains("metric3"))
                    assert(!buildResult.results[0].series[0].columns.contains("metric4"))
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
