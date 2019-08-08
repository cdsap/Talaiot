package com.cdsap.talaiot.publisher

import com.cdsap.talaiot.configuration.ElasticSearchPublisherConfiguration
import com.cdsap.talaiot.configuration.InfluxDbPublisherConfiguration
import com.cdsap.talaiot.entities.ExecutionReport
import com.cdsap.talaiot.logger.LogTracker
import org.apache.http.HttpHost
import org.elasticsearch.action.index.IndexRequest
import org.elasticsearch.client.RequestOptions
import org.elasticsearch.client.RestClient
import org.elasticsearch.client.RestClientBuilder
import org.elasticsearch.client.RestHighLevelClient
import java.util.concurrent.Executor

class ElasticSearchPublsiher(
    /**
     * General configuration for the publisher
     */
    private val elasticSearchPublsiherConfiguration: ElasticSearchPublisherConfiguration,
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
        val client = getClient(elasticSearchPublsiherConfiguration)
        report.tasks?.map {

            client.index(
                IndexRequest(elasticSearchPublsiherConfiguration.taskMetricName)
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
        //       val measurements = insertTasks(report)
        //       val buildMeasurement = createBuildPoint(report)

        //       TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun getMapToAny(list: Map<String, String>): Map<String, String> = list

    private fun getClient(elasticSearchPublsiherConfiguration: ElasticSearchPublisherConfiguration): RestHighLevelClient {
        var restClientBuilder = RestClient.builder(HttpHost("localhost", 9200, "http"))
        return RestHighLevelClient(restClientBuilder)


    }

}