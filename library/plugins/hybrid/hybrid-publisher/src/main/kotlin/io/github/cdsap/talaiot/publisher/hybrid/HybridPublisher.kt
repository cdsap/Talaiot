package io.github.cdsap.talaiot.publisher.hybrid

import io.github.cdsap.talaiot.configuration.PublisherConfiguration
import io.github.cdsap.talaiot.entities.ExecutionReport
import io.github.cdsap.talaiot.logger.LogTracker
import io.github.cdsap.talaiot.publisher.Publisher
import io.github.cdsap.talaiot.publisher.elasticsearch.ElasticSearchPublisher
import io.github.cdsap.talaiot.publisher.elasticsearch.ElasticSearchPublisherConfiguration
import io.github.cdsap.talaiot.publisher.influxdb.InfluxDbPublisher
import io.github.cdsap.talaiot.publisher.influxdb.InfluxDbPublisherConfiguration
import io.github.cdsap.talaiot.publisher.pushgateway.PushGatewayPublisher
import io.github.cdsap.talaiot.publisher.pushgateway.PushGatewayPublisherConfiguration
import io.github.cdsap.talaiot.publisher.rethinkdb.RethinkDbPublisher
import io.github.cdsap.talaiot.publisher.rethinkdb.RethinkDbPublisherConfiguration

class HybridPublisher(
    private val hybridPublisherConfiguration: HybridPublisherConfiguration,
    private val logTracker: LogTracker
) : Publisher, java.io.Serializable {

    private val TAG = "HybridPublisher"

    override fun publish(report: ExecutionReport) {
        logTracker.log(TAG, "================")
        logTracker.log(TAG, "HybridPublisher")
        logTracker.log(TAG, "publishBuildMetrics: ${hybridPublisherConfiguration.publishBuildMetrics}")
        logTracker.log(TAG, "publishTaskMetrics: ${hybridPublisherConfiguration.publishTaskMetrics}")
        logTracker.log(TAG, "================")

        if (validate()) {
            hybridPublisherConfiguration.buildPublisher?.let {
                it.publishTaskMetrics = false
                getPublisher(it)?.publish(report)
            }

            hybridPublisherConfiguration.taskPublisher?.let {
                it.publishBuildMetrics = false
                getPublisher(it)?.publish(report)
            }
        }
    }

    private fun validate(): Boolean {
        if (hybridPublisherConfiguration.buildPublisher == null && hybridPublisherConfiguration.taskPublisher == null) {
            logTracker.error("HybridPublisher-Error: BuildPublisher and TaskPublisher are null. Not publisher will be executed ")
            return false
        }
        return true
    }

    private fun getPublisher(publisherConfiguration: PublisherConfiguration): Publisher? =
        when (publisherConfiguration) {
            is InfluxDbPublisherConfiguration -> {
                InfluxDbPublisher(
                    publisherConfiguration,
                    logTracker
                )
            }
            is PushGatewayPublisherConfiguration -> {
                PushGatewayPublisher(
                    publisherConfiguration,
                    logTracker
                )
            }

            is ElasticSearchPublisherConfiguration -> {
                ElasticSearchPublisher(
                    publisherConfiguration,
                    logTracker
                )
            }

            is RethinkDbPublisherConfiguration -> {
                RethinkDbPublisher(
                    publisherConfiguration,
                    logTracker
                )
            }

            else -> {
                logTracker.error(
                    "HybridPublisher: Not supported Publisher. Current Publishers supported by HybridPublisher: " +
                        "InfluxDbPublisher, PushGatewayPublisher, ElasticSearchPublisher and RethinkDbPublisher"
                )
                null
            }
        }
}
