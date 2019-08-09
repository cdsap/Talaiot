package com.cdsap.talaiot.publisher

import com.cdsap.talaiot.configuration.*
import com.cdsap.talaiot.entities.ExecutionReport
import com.cdsap.talaiot.logger.LogTracker
import com.cdsap.talaiot.request.SimpleRequest
import java.util.concurrent.Executor

class HybridPublisher(
    val hybridPublisherConfiguration: HybridPublisherConfiguration,
    private val logTracker: LogTracker,
    /**
     * Executor to schedule a task in Background
     */
    private val executor: Executor
) : Publisher {
    override fun publish(report: ExecutionReport) {
        hybridPublisherConfiguration.buildPublisher?.let {
            getPublisher(it)?.publish(report)
        }

        hybridPublisherConfiguration.taskPublisher?.let {
            getPublisher(it)?.publish(report)
        }
    }


    fun getPublisher(publisherConfiguration: PublisherConfiguration): Publisher? {
        when (publisherConfiguration) {
            is InfluxDbPublisherConfiguration -> {
                return InfluxDbPublisher(
                    publisherConfiguration,
                    logTracker,
                    executor
                )

            }
            is PushGatewayPublisherConfiguration -> {
                return PushGatewayPublisher(
                    publisherConfiguration,
                    logTracker,
                    SimpleRequest(logTracker),
                    executor
                )

            }

            is ElasticSearchPublisherConfiguration -> {
                return ElasticSearchPublisher(
                    publisherConfiguration,
                    logTracker,
                    executor
                )

            }

            is Publisher -> {
                return publisherConfiguration
            }

            else -> {
                return null
            }
        }

    }

}