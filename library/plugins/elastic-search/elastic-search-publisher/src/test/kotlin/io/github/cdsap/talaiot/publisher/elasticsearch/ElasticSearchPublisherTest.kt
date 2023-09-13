package io.github.cdsap.talaiot.publisher.elasticsearch

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import io.github.cdsap.talaiot.entities.CustomProperties
import io.github.cdsap.talaiot.entities.Environment
import io.github.cdsap.talaiot.entities.ExecutionReport
import io.github.cdsap.talaiot.entities.TaskLength
import io.github.cdsap.talaiot.entities.TaskMessageState
import io.github.cdsap.talaiot.logger.TestLogTrackerRecorder
import io.kotlintest.Spec
import io.kotlintest.shouldThrow
import io.kotlintest.specs.BehaviorSpec
import org.apache.http.HttpHost
import org.apache.http.util.EntityUtils
import org.elasticsearch.client.ResponseException
import org.elasticsearch.client.RestClient
import org.testcontainers.elasticsearch.KElasticSearchContainer

class ElasticSearchPublisherTest : BehaviorSpec() {

    private val container = KElasticSearchContainer()

    override fun beforeSpec(spec: Spec) {
        super.beforeSpec(spec)
        container.start()
    }

    override fun afterSpec(spec: Spec) {
        super.afterSpec(spec)
        container.stop()
    }

    init {
        given("ElasticSearch configuration") {
            val logger = TestLogTrackerRecorder

            `when`("Tasks and build information is published ") {
                val elasticSearchPublisherConfiguration =
                    ElasticSearchPublisherConfiguration().apply {
                        url = "http://" + container.httpHostAddress
                    }
                val elasticSearchPublisher = ElasticSearchPublisher(
                    elasticSearchPublisherConfiguration, logger
                )
                elasticSearchPublisher.publish(executionReport())

                then("Indices build and tasks have been created and contains metrics, data included") {

                    Thread.sleep(10000)

                    val url = container.httpHostAddress.split(":")
                    val client =
                        RestClient.builder(HttpHost(url[0], url[1].toInt(), "http")).build()
                    val parser = JsonParser()

                    val responseBuild =
                        client.performRequest(
                            org.elasticsearch.client.Request(
                                "GET",
                                "/build/_search?"
                            )
                        )

                    val contentBuild = EntityUtils.toString(responseBuild.entity)
                    val hitsBuild =
                        (parser.parse(contentBuild.toString()) as JsonObject).get("hits").asJsonObject
                    val hitsContentBuild = (hitsBuild.get("hits").asJsonArray)[0].asJsonObject
                    val elementsBuild = hitsContentBuild.get("_source").asJsonObject

                    assert(hitsBuild.get("total").asJsonObject.get("value").asInt == 1)
                    assert(hitsContentBuild.get("_index").asString == "build")
                    assert(elementsBuild.get("cpuCount").asString == "12")
                    assert(elementsBuild.get("requestedTasks").asString == "assemble")

                    val responseTask =
                        client.performRequest(
                            org.elasticsearch.client.Request(
                                "GET",
                                "/task/_search?"
                            )
                        )

                    val contentTask = EntityUtils.toString(responseTask.entity)
                    val hitsTask =
                        (parser.parse(contentTask.toString()) as JsonObject).get("hits").asJsonObject
                    val hitsContentTask = (hitsTask.get("hits").asJsonArray)[0].asJsonObject
                    val elementsTask = hitsContentTask.get("_source").asJsonObject

                    assert(hitsTask.get("total").asJsonObject.get("value").asInt == 1)
                    assert(hitsContentTask.get("_index").asString == "task")
                    assert(elementsTask.get("task").asString == ":assemble")
                    assert(elementsTask.get("state").asString == "EXECUTED")
                    assert(elementsTask.get("metric1").asString == "value1")
                    assert(elementsTask.get("metric2").asString == "value2")
                }
            }
            `when`("Publishing Task metrics are disabled ") {
                val elasticSearchPublisherConfiguration =
                    ElasticSearchPublisherConfiguration().apply {
                        url = "http://" + container.httpHostAddress
                        taskIndexName = "task2"
                        buildIndexName = "build2"
                        publishTaskMetrics = false
                    }
                val elasticSearchPublisher = ElasticSearchPublisher(
                    elasticSearchPublisherConfiguration, logger
                )
                elasticSearchPublisher.publish(executionReport())

                then("Build metrics are retrieved but no tasks metrics") {

                    Thread.sleep(10000)

                    val url = container.httpHostAddress.split(":")
                    val client =
                        RestClient.builder(HttpHost(url[0], url[1].toInt(), "http")).build()
                    val parser = JsonParser()

                    val responseBuild =
                        client.performRequest(
                            org.elasticsearch.client.Request(
                                "GET",
                                "/build2/_search?"
                            )
                        )

                    val contentBuild = EntityUtils.toString(responseBuild.entity)
                    val hitsBuild =
                        (parser.parse(contentBuild.toString()) as JsonObject).get("hits").asJsonObject
                    val hitsContentBuild = (hitsBuild.get("hits").asJsonArray)[0].asJsonObject
                    val elementsBuild = hitsContentBuild.get("_source").asJsonObject

                    assert(hitsBuild.get("total").asJsonObject.get("value").asInt == 1)
                    assert(hitsContentBuild.get("_index").asString == "build2")
                    assert(elementsBuild.get("cpuCount").asString == "12")
                    assert(elementsBuild.get("requestedTasks").asString == "assemble")

                    val exception = shouldThrow<ResponseException> {
                        client.performRequest(
                            org.elasticsearch.client.Request(
                                "GET",
                                "/task2/_search?"
                            )
                        )
                    }
                    assert(exception.message!!.contains("index_not_found_exception"))
                }
            }
            `when`("Publishing Build metrics are disabled ") {
                val elasticSearchPublisherConfiguration =
                    ElasticSearchPublisherConfiguration().apply {
                        url = "http://" + container.httpHostAddress
                        taskIndexName = "task3"
                        buildIndexName = "build3"
                        publishBuildMetrics = false
                    }
                val elasticSearchPublisher = ElasticSearchPublisher(
                    elasticSearchPublisherConfiguration, logger
                )

                elasticSearchPublisher.publish(executionReport())

                then("Task metrics are retrieved but no build metrics") {

                    Thread.sleep(10000)

                    val url = container.httpHostAddress.split(":")
                    val client =
                        RestClient.builder(HttpHost(url[0], url[1].toInt(), "http")).build()
                    val parser = JsonParser()

                    val exception = shouldThrow<ResponseException> {
                        client.performRequest(
                            org.elasticsearch.client.Request(
                                "GET",
                                "/build3/_search?"
                            )
                        )
                    }
                    assert(exception.message!!.contains("index_not_found_exception"))

                    val responseTask =
                        client.performRequest(
                            org.elasticsearch.client.Request(
                                "GET",
                                "/task3/_search?"
                            )
                        )

                    val contentTask = EntityUtils.toString(responseTask.entity)
                    val hitsTask =
                        (parser.parse(contentTask.toString()) as JsonObject).get("hits").asJsonObject
                    val hitsContentTask = (hitsTask.get("hits").asJsonArray)[0].asJsonObject
                    val elementsTask = hitsContentTask.get("_source").asJsonObject

                    assert(hitsTask.get("total").asJsonObject.get("value").asInt == 1)
                    assert(hitsContentTask.get("_index").asString == "task3")
                    assert(elementsTask.get("task").asString == ":assemble")
                    assert(elementsTask.get("state").asString == "EXECUTED")
                    assert(elementsTask.get("metric1").asString == "value1")
                    assert(elementsTask.get("metric2").asString == "value2")
                }
            }
        }
    }

    private fun executionReport(): ExecutionReport {
        return ExecutionReport(
            requestedTasks = "assemble",
            environment = Environment(
                cpuCount = "12", maxWorkers = "4"
            ),
            customProperties = CustomProperties(
                taskProperties = mutableMapOf(
                    "metric1" to "value1",
                    "metric2" to "value2"
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
