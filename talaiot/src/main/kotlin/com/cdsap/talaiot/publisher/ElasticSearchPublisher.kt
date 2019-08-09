package com.cdsap.talaiot.publisher

import com.cdsap.talaiot.configuration.ElasticSearchPublisherConfiguration
import com.cdsap.talaiot.entities.ExecutionReport
import com.cdsap.talaiot.logger.LogTracker
import org.apache.http.HttpHost
import org.elasticsearch.action.index.IndexRequest
import org.elasticsearch.client.RequestOptions
import org.elasticsearch.client.RestClient
import org.elasticsearch.client.RestHighLevelClient
import java.util.concurrent.Executor

class ElasticSearchPublisher(
    /**
     * General configuration for the publisher
     */
    private val elasticSearchPublisherConfiguration: ElasticSearchPublisherConfiguration,
    /**
     * LogTracker to print in console depending on the Mode
     */
    private val logTracker: LogTracker,
    /**
     * Executor to schedule a task in Background
     */
    private val executor: Executor
) : Publisher {

    override fun publish(report: ExecutionReport) {
        if (validate()) {
            val client = getClient()
            executor.execute {
                try {
                    sendTasksMetrics(report, client)
                    sendBuildMetrics(report, client)
                } catch (e: Exception) {
                    println("ElasticSearchPublisher-Error-Executor Runnable: ${e.message}")
                }

            }
        }
    }

    private fun validate(): Boolean {
        if (elasticSearchPublisherConfiguration.url.isEmpty() ||
            elasticSearchPublisherConfiguration.dbName.isEmpty() ||
            elasticSearchPublisherConfiguration.taskMetricName.isEmpty() ||
            elasticSearchPublisherConfiguration.buildMetricName.isEmpty()
        ) {
            println(
                "InfluxDbPublisher not executed. Configuration requires url, dbName, taskMetricName and buildMetricName: \n" +
                        "influxDbPublisher {\n" +
                        "            dbName = \"tracking\"\n" +
                        "            url = \"http://localhost:8086\"\n" +
                        "            buildMetricName = \"build\"\n" +
                        "            taskMetricName = \"task\"\n" +
                        "}\n" +
                        "Please update your configuration"
            )
            return false
        }
        return true

    }

    private fun sendBuildMetrics(report: ExecutionReport, client: RestHighLevelClient) {
        logTracker.log("================")
        logTracker.log("ElasticSearchPublisher")
        logTracker.log("================")

        val source = mutableMapOf<String, Any>()
        source.putAll(report.customProperties.buildProperties)
        source.putAll(report.flattenBuildEnv().toMutableMap())
        source.apply {
            report.environment.osVersion?.let { "osVersion" to it }
            report.environment.maxWorkers?.let { "maxWorkers" to it.toLong() }
            report.environment.javaRuntime?.let { "javaRuntime" to it }
            report.environment.javaVmName?.let { "javaVmName" to it }
            report.environment.javaXmsBytes?.let { "javaXmsBytes" to it.toLong() }
            report.environment.javaXmxBytes?.let { "javaXmxBytes" to it.toLong() }
            report.environment.javaMaxPermSize?.let { "javaMaxPermSize" to it.toLong() }
            report.environment.totalRamAvailableBytes?.let { "totalRamAvailableBytes" to it.toLong() }

            report.environment.cpuCount?.let { "cpuCount" to it.toLong() }
            report.environment.locale?.let { "locale" to it }
            report.environment.username?.let { "username" to it }
            report.environment.publicIp?.let { "publicIp" to it }
            report.environment.defaultChartset?.let { "defaultCharset" to it }
            report.environment.ideVersion?.let { "ideVersion" to it }
            report.environment.gradleVersion?.let { "gradleVersion" to it }
            report.environment.gitBranch?.let { "gitBranch" to it }
            report.environment.gitUser?.let { "gitUser" to it }
            report.cacheRatio?.let { "cacheRatio" to it.toDouble() }

            report.beginMs?.let { "start" to it.toDouble() }
            report.rootProject?.let { "rootProject" to it }
            report.requestedTasks?.let { "requestedTasks" to it }
            report.scanLink?.let { "scanLink" to it }
        }

        val result = client.index(
            IndexRequest(elasticSearchPublisherConfiguration.buildMetricName).source(source),
            RequestOptions.DEFAULT
        )

        println(result.result)

    }

    private fun sendTasksMetrics(
        report: ExecutionReport,
        client: RestHighLevelClient
    ) {
        report.tasks?.map {
            val result = client.index(
                IndexRequest(elasticSearchPublisherConfiguration.taskMetricName)
                    .source(
                        mapOf(
                            "state" to it.state.name,
                            "module" to it.module,
                            "rootNode" to it.rootNode,
                            "task" to it.taskPath,
                            "workerId" to it.workerId,
                            "critical" to it.critical,
                            "value" to it.ms
                        ) + report.customProperties.taskProperties
                    )
                ,
                RequestOptions.DEFAULT
            )
        }
    }

    private fun getClient(): RestHighLevelClient {
        var restClientBuilder =
            RestClient.builder(HttpHost(
                "localhost",
                9200,
                "http"))
        return RestHighLevelClient(restClientBuilder)


    }

}