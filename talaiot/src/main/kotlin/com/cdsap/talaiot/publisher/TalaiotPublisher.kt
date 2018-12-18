package com.cdsap.talaiot.publisher

import com.cdsap.talaiot.AggregateData
import com.cdsap.talaiot.TalaiotExtension
import com.cdsap.talaiot.entities.TaskLength
import com.cdsap.talaiot.logger.LogTracker
import com.cdsap.talaiot.metrics.MetricsParser
import com.cdsap.talaiot.metrics.MetricsProvider
import com.cdsap.talaiot.request.SimpleRequest
import org.gradle.BuildResult


class TalaiotPublisher(
    result: BuildResult,
    taskLengthList: MutableList<TaskLength>,
    talaiotExtension: TalaiotExtension
) {
    private val logger = LogTracker(talaiotExtension.logger)
    private val availableMetrics = MetricsProvider(talaiotExtension, result).get()
    private val aggregatedData = AggregateData(taskLengthList, MetricsParser(availableMetrics)).build()

    init {
        talaiotExtension.publishers?.apply {
            outputPublisher.apply { OutputPublisher(logger).publish(aggregatedData) }

            influxDbPublisher?.apply {
                let {
                    InfluxDbPublisher(it, logger, SimpleRequest(logger)).publish(aggregatedData)
                }
            }

            customPublisher?.apply {
                publish(aggregatedData)
            }
        }
    }
}
