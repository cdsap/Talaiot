package io.github.cdsap.talaiot.publisher.rethinkdb

import com.rethinkdb.RethinkDB
import com.rethinkdb.net.Connection
import com.rethinkdb.net.Cursor
import io.github.cdsap.talaiot.entities.CustomProperties
import io.github.cdsap.talaiot.entities.Environment
import io.github.cdsap.talaiot.entities.ExecutionReport
import io.github.cdsap.talaiot.entities.TaskLength
import io.github.cdsap.talaiot.entities.TaskMessageState
import io.github.cdsap.talaiot.logger.TestLogTrackerRecorder
import io.kotlintest.Description
import io.kotlintest.Spec
import io.kotlintest.inspectors.forAtLeastOne
import io.kotlintest.shouldThrow
import io.kotlintest.specs.BehaviorSpec
import junit.framework.Assert.assertTrue
import org.testcontainers.rethinkdb.KRethinkDbContainer
import java.net.URL

class RethinkDbPublisherTest : BehaviorSpec() {

    val container = KRethinkDbContainer()
    val r = RethinkDB.r

    override fun beforeSpec(description: Description, spec: Spec) {
        super.beforeSpec(description, spec)
        container.start()
    }

    override fun afterSpec(description: Description, spec: Spec) {
        super.afterSpec(description, spec)
        container.stop()
    }

    init {
        given("RethinkDb Publisher instance") {
            val logger = TestLogTrackerRecorder

            `when`("Publisher is sent ") {
                val rethinkDbConfiguration = getBasicRethinkDbConf()
                val rethinkDb = RethinkDbPublisher(
                    rethinkDbConfiguration, logger
                )
                rethinkDb.publish(
                    executionReportData()
                )
                then("RethinkDb Instance contains information for build and tasks") {

                    val conn = getConnection(rethinkDbConfiguration.url)
                    val existsTableTasks =
                        r.db(rethinkDbConfiguration.dbName).tableList().contains(rethinkDbConfiguration.taskTableName)
                            .run<Boolean>(conn)
                    val existsTableBuilds =
                        r.db(rethinkDbConfiguration.dbName).tableList().contains(rethinkDbConfiguration.buildTableName)
                            .run<Boolean>(conn)
                    assertTrue(existsTableBuilds)
                    assertTrue(existsTableTasks)
                }
            }
            `when`("Publisher doesn't include the minimum configuration ") {
                val rethinkDbConfiguration = RethinkDbPublisherConfiguration().apply {
                    dbName = "tracking"
                }

                val rethinkDb = RethinkDbPublisher(
                    rethinkDbConfiguration, logger
                )
                then("Error is thrown pointing the correct configuration") {
                    val exception = shouldThrow<IllegalStateException> {
                        rethinkDb.publish(
                            executionReportData()
                        )
                    }

                    assertTrue(exception.localizedMessage.contains("RethinkDbPublisher not executed. Configuration requires url, dbName, taskTableName and buildTableName:"))
                }
            }
            `when`("Publisher includes custom metrics for Tasks and Build") {
                val rethinkDbConfiguration = getBasicRethinkDbConf()
                val conn = getConnection(rethinkDbConfiguration.url)
                val rethinkDb = RethinkDbPublisher(
                    rethinkDbConfiguration, logger
                )
                rethinkDb.publish(
                    executionReportData()
                )

                then("Metrics are stored for Build and Task Table") {
                    val elementInTaskTable: Cursor<HashMap<String, String>> =
                        r.db(rethinkDbConfiguration.dbName).table(rethinkDbConfiguration.taskTableName).run(conn)
                    val tasks = elementInTaskTable.toList()
                    tasks.forAtLeastOne {
                        it.containsKey("metric1")
                        it.containsKey("value1")
                    }
                    val elementInBuildTable: Cursor<HashMap<String, String>> =
                        r.db(rethinkDbConfiguration.dbName).table(rethinkDbConfiguration.buildTableName).run(conn)
                    val builds = elementInBuildTable.toList()
                    builds.forAtLeastOne {
                        it.containsKey("metric1")
                        it.containsKey("value1")
                    }
                }
            }

            `when`("Build info contains duration, requestedTasks and environment information") {
                val rethinkDbConfiguration = getBasicRethinkDbConf()
                val conn = getConnection(rethinkDbConfiguration.url)
                val rethinkDb = RethinkDbPublisher(
                    rethinkDbConfiguration, logger
                )
                rethinkDb.publish(
                    executionReportData()
                )

                then("Build Info is properly saved") {
                    Thread.sleep(2000)
                    val elementInBuildTable: Cursor<HashMap<String, String>> =
                        r.db(rethinkDbConfiguration.dbName).table(rethinkDbConfiguration.buildTableName).run(conn)
                    val builds = elementInBuildTable.toList()
                    builds.forAtLeastOne {
                        it.containsKey("duration")
                        it.containsKey("100")
                        it.containsKey("requestedTasks")
                        it.containsKey("assemble")
                        it.containsKey("cpuCount")
                        it.containsKey("12")
                    }
                }
            }

            `when`("Task info contains module and rootNode") {
                val rethinkDbConfiguration = getBasicRethinkDbConf()
                val conn = getConnection(rethinkDbConfiguration.url)
                val rethinkDb = RethinkDbPublisher(
                    rethinkDbConfiguration, logger
                )
                rethinkDb.publish(
                    executionReportData()
                )

                then("Task Info is properly saved") {
                    Thread.sleep(3000)
                    val elementInTaskTable: Cursor<HashMap<String, String>> =
                        r.db(rethinkDbConfiguration.dbName).table(rethinkDbConfiguration.taskTableName).run(conn)
                    val tasks = elementInTaskTable.toList()
                    tasks.forAtLeastOne {
                        it.containsKey("module")
                        it.containsKey("app")
                        it.containsKey("rootNode")
                        it.containsKey("false")
                    }
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

    private fun getConnection(url: String): Connection {
        val url = URL(url)
        return r.connection().hostname(url.host).port(url.port).connect()
    }

    private fun getBasicRethinkDbConf() = RethinkDbPublisherConfiguration().apply {
        url = "http://" + container.httpHostAddress
        taskTableName = "tasks"
        buildTableName = "builds"
        dbName = "tracking"
    }
}
