package io.github.cdsap.talaiot.publisher.influxdb2

import com.influxdb.client.InfluxDBClient
import com.influxdb.client.InfluxDBClientFactory
import com.influxdb.client.WriteApiBlocking
import com.influxdb.client.domain.WritePrecision
import com.influxdb.client.write.Point
import io.github.cdsap.talaiot.entities.ExecutionReport
import io.github.cdsap.talaiot.logger.LogTracker
import io.github.cdsap.talaiot.metrics.DefaultBuildMetricsProvider
import io.github.cdsap.talaiot.metrics.DefaultTaskDataProvider
import io.github.cdsap.talaiot.publisher.Publisher
import io.github.cdsap.talaiot.publisher.inflluxdb.common.TagFieldProvider
import java.util.concurrent.Executor

/**
 * Publisher using InfluxDb (Flux) and LineProtocol format to send the metrics
 */
class InfluxDb2Publisher(
    /**
     * General configuration for the publisher
     */
    private val influxDbPublisherConfiguration: InfluxDb2PublisherConfiguration,
    /**
     * LogTracker to print in console depending on the Mode
     */
    private val logTracker: LogTracker,
    /**
     * Executor to schedule a task in Background
     */
    private val executor: Executor
) : Publisher {

    private val TAG = "InfluxDb2Publisher"

    override fun publish(report: ExecutionReport) {
        if (influxDbPublisherConfiguration.url.isEmpty() ||
            influxDbPublisherConfiguration.token.isEmpty() ||
            influxDbPublisherConfiguration.org.isEmpty() ||
            influxDbPublisherConfiguration.bucket.isEmpty() ||
            influxDbPublisherConfiguration.taskMetricName.isEmpty() ||
            influxDbPublisherConfiguration.buildMetricName.isEmpty()
        ) {
            logTracker.error(
                "InfluxDbPublisher not executed. Configuration requires url, token, org, bucket taskMetricName and buildMetricName: \n" +
                    "influxDbPublisher {\n" +
                    "            url = \"http://localhost:8086\"\n" +
                    "            token = \"<token>\"\n" +
                    "            bucket = \"myBucketName\"\n" +
                    "            org = \"MyOrg\"\n" +
                    "            buildMetricName = \"build\"\n" +
                    "            taskMetricName = \"task\"\n" +
                    "}\n" +
                    "Please update your configuration"
            )
            return
        }

        try {
            val influxDBClient: InfluxDBClient =
                InfluxDBClientFactory.create(
                    influxDbPublisherConfiguration.url,
                    influxDbPublisherConfiguration.token.toCharArray()
                )

            executor.execute {
                logTracker.log(TAG, "================")
                logTracker.log(TAG, "InfluxDb2Publisher")
                logTracker.log(
                    TAG,
                    "publishBuildMetrics: ${influxDbPublisherConfiguration.publishBuildMetrics}"
                )
                logTracker.log(
                    TAG,
                    "publishTaskMetrics: ${influxDbPublisherConfiguration.publishTaskMetrics}"
                )
                logTracker.log(TAG, "================")

                try {
                    val writeApi: WriteApiBlocking = influxDBClient.writeApiBlocking
                    if (influxDbPublisherConfiguration.publishTaskMetrics) {
                        val measurements = createTaskPoints(report)
                        measurements?.let {
                            writeApi.writePoints(
                                influxDbPublisherConfiguration.bucket,
                                influxDbPublisherConfiguration.org,
                                it
                            )
                        }
                    }

                    if (influxDbPublisherConfiguration.publishBuildMetrics) {
                        val buildMeasurement = createBuildPoint(report)
                        writeApi.writePoint(
                            influxDbPublisherConfiguration.bucket,
                            influxDbPublisherConfiguration.org,
                            buildMeasurement
                        )
                    }
                } catch (e: Exception) {
                    logTracker.log(TAG, "InfluxDb2Publisher-Error-Executor Runnable: ${e.message}")
                }
            }
            influxDBClient.close()
        } catch (e: Exception) {
            logTracker.log(TAG, "InfluxDb2Publisher-Error ${e.stackTrace}")
        }
    }

    private fun createTaskPoints(report: ExecutionReport): List<Point>? {
        return report.tasks?.map { task ->
            val tagFieldProvider = TagFieldProvider(
                influxDbPublisherConfiguration.taskTags,
                DefaultTaskDataProvider(task, report),
                report.customProperties.taskProperties
            )
            Point.measurement(influxDbPublisherConfiguration.taskMetricName)
                .time(System.currentTimeMillis(), WritePrecision.MS)
                .addTags(tagFieldProvider.tags())
                .addFields(tagFieldProvider.fields())
        }
    }

    private fun createBuildPoint(report: ExecutionReport): Point {
        val tagFieldProvider = TagFieldProvider(
            influxDbPublisherConfiguration.buildTags,
            DefaultBuildMetricsProvider(report),
            report.customProperties.buildProperties
        )
        return Point.measurement(influxDbPublisherConfiguration.buildMetricName)
            .time(report.endMs?.toLong() ?: System.currentTimeMillis(), WritePrecision.MS)
            .addTags(tagFieldProvider.tags())
            .addFields(tagFieldProvider.fields())
    }
}
