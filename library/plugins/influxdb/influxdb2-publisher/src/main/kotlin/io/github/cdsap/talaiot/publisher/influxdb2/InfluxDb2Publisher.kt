package io.github.cdsap.talaiot.publisher.influxdb2

import com.influxdb.client.InfluxDBClient
import com.influxdb.client.InfluxDBClientFactory
import com.influxdb.client.WriteApi
import com.influxdb.client.domain.WritePrecision
import com.influxdb.client.write.Point
import io.github.cdsap.talaiot.entities.ExecutionReport
import io.github.cdsap.talaiot.logger.LogTracker
import io.github.cdsap.talaiot.metrics.DefaultBuildMetricsProvider
import io.github.cdsap.talaiot.metrics.DefaultTaskDataProvider
import io.github.cdsap.talaiot.publisher.Publisher
import io.github.cdsap.talaiot.publisher.inflluxdb.common.TagFieldProvider

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
    private val logTracker: LogTracker
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
                "InfluxDb2Publisher not executed. Configuration requires url, token, org, bucket taskMetricName and buildMetricName: \n" +
                    "influxDb2Publisher {\n" +
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

            checkBucket(influxDBClient)

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
                val writeApi: WriteApi = influxDBClient.makeWriteApi()
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
            influxDBClient.close()
        } catch (e: Exception) {
            logTracker.log(TAG, "InfluxDb2Publisher-Error ${e.stackTrace}")
        }
    }

    private fun checkBucket(influxDBClient: InfluxDBClient) {
        val bucket = influxDBClient.bucketsApi.findBucketByName(influxDbPublisherConfiguration.bucket)
        if (bucket == null) {
            val orgId = influxDBClient.organizationsApi.findOrganizations()
                .firstOrNull { it.name == influxDbPublisherConfiguration.org }
            if (orgId != null) {
                val newBucket = influxDBClient.bucketsApi.createBucket(
                    influxDbPublisherConfiguration.bucket, orgId.id
                )
            } else {
                logTracker.log(
                    TAG,
                    "InfluxDb2Publisher-Error: Bucket ${influxDbPublisherConfiguration.bucket} " +
                        "doesn't exist. It was not possible create the new bucket because the org  ${influxDbPublisherConfiguration.org}" +
                        " was not found"
                )
            }
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
