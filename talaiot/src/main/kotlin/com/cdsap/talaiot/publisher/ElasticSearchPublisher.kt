package com.cdsap.talaiot.publisher

import com.cdsap.talaiot.configuration.ElasticSearchPublisherConfiguration
import com.cdsap.talaiot.entities.ExecutionReport
import com.cdsap.talaiot.logger.LogTracker
import com.cdsap.talaiot.metrics.DefaultBuildMetricsProvider
import com.cdsap.talaiot.metrics.DefaultTaskDataProvider
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

    private val TAG = "ElasticSearchPublisher"

    override fun publish(report: ExecutionReport) {

        if (validate()) {
            val client = getClient()
            executor.execute {
                logTracker.log(TAG, "================")
                logTracker.log(TAG, "ElasticSearchPublisher")
                logTracker.log(TAG, "publishBuildMetrics: ${elasticSearchPublisherConfiguration.publishBuildMetrics}")
                logTracker.log(TAG, "publishTaskMetrics: ${elasticSearchPublisherConfiguration.publishTaskMetrics}")
                logTracker.log(TAG, "================")

                try {
                    if (elasticSearchPublisherConfiguration.publishBuildMetrics) {
                        logTracker.log(TAG, "Sending Build metrics")
                        sendBuildMetrics(report, client)
                    }
                    if (elasticSearchPublisherConfiguration.publishTaskMetrics) {
                        logTracker.log(TAG, "Sending Task metrics")
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
        val metrics = DefaultBuildMetricsProvider(report).get()
        val response = client.index(
            IndexRequest(elasticSearchPublisherConfiguration.buildIndexName).source(metrics),
            RequestOptions.DEFAULT

        )
        logTracker.log(TAG, "Result Build metrics ${response.toString()}")
    }

    private fun sendTasksMetrics(
        report: ExecutionReport,
        client: RestHighLevelClient
    ) {
        logTracker.log(TAG, "number of tasks report.tasks " + report.tasks?.size)
        report.tasks?.forEach {
            try {
                val response = client.index(
                    IndexRequest(elasticSearchPublisherConfiguration.taskIndexName)
                        .source(DefaultTaskDataProvider(it).get() +report.customProperties.taskProperties ), RequestOptions.DEFAULT
                )
                logTracker.log(TAG, "Result Task metrics ${response}")
            } catch (e: java.lang.Exception) {
                logTracker.error(e.message.toString())
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
