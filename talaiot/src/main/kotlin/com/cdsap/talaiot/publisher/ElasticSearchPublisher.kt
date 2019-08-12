package com.cdsap.talaiot.publisher

import com.cdsap.talaiot.configuration.ElasticSearchPublisherConfiguration
import com.cdsap.talaiot.entities.ExecutionReport
import com.cdsap.talaiot.logger.LogTracker
import org.apache.http.HttpHost
import org.elasticsearch.action.index.IndexRequest
import org.elasticsearch.client.RequestOptions
import org.elasticsearch.client.RestClient
import org.elasticsearch.client.RestHighLevelClient
import java.net.URL
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
        logTracker.log("================")
        logTracker.log("ElasticSearchPublisher")
        logTracker.log("publishBuildMetrics: ${elasticSearchPublisherConfiguration.publishBuildMetrics}")
        logTracker.log("publishTaskMetrics: ${elasticSearchPublisherConfiguration.publishTaskMetrics}")
        logTracker.log("================")


        if (validate()) {
            val client = getClient()
            executor.execute {
                try {
                    if (elasticSearchPublisherConfiguration.publishBuildMetrics) {
                        sendBuildMetrics(report, client)
                    }
                    if (elasticSearchPublisherConfiguration.publishTaskMetrics) {
                        sendTasksMetrics(report, client)
                    }

                } catch (e: Exception) {
                    logTracker.error("ElasticSearchPublisher-Error-Executor Runnable: ${e.message}")
                }

            }
        }
    }

    private fun validate(): Boolean {
        if (elasticSearchPublisherConfiguration.url.isEmpty() ||
            elasticSearchPublisherConfiguration.taskIndexName.isEmpty() ||
            elasticSearchPublisherConfiguration.buildIndexName.isEmpty()
        ) {
            logTracker.error(
                "ElasticSearchPublisher not executed. Configuration requires url, taskIndexName and buildIndexName: \n" +
                        "elasticSearchPublisher {\n" +
                        "            url = \"http://localhost:8086\"\n" +
                        "            buildIndexName = \"build\"\n" +
                        "            taskIndexName = \"task\"\n" +
                        "}\n" +
                        "Please update your configuration"
            )
            return false
        }
        return true

    }

    private fun sendBuildMetrics(report: ExecutionReport, client: RestHighLevelClient) {
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

        client.index(
            IndexRequest(elasticSearchPublisherConfiguration.buildIndexName).source(source),
            RequestOptions.DEFAULT
        )
    }

    private fun sendTasksMetrics(
        report: ExecutionReport,
        client: RestHighLevelClient
    ) {
        logTracker.log("number of tasks report.tasks " + report.tasks?.size)
        report.tasks?.forEach {
            try {
                client.index(
                    IndexRequest(elasticSearchPublisherConfiguration.taskIndexName)
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
            } catch (e: java.lang.Exception) {
                logTracker.log("error")
                logTracker.log(e.message.toString())
            }
        }
    }

    private fun getClient(): RestHighLevelClient {
        return if (elasticSearchPublisherConfiguration.url == "localhost") {
            RestHighLevelClient(RestClient.builder(HttpHost("localhost")))
        } else {
            val url = URL(elasticSearchPublisherConfiguration.url)

            val restClientBuilder =
                RestClient.builder(
                    HttpHost(
                        url.host,
                        url.port,
                        url.protocol
                    )
                )
            RestHighLevelClient(restClientBuilder)
        }
    }

}
