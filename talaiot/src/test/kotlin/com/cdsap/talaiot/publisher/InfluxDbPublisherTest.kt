package com.cdsap.talaiot.publisher

import com.cdsap.talaiot.configuration.InfluxDbPublisherConfiguration
import com.cdsap.talaiot.entities.TaskLength
import com.cdsap.talaiot.entities.TaskMeasurementAggregated
import com.cdsap.talaiot.entities.TaskMessageState
import com.cdsap.talaiot.logger.LogTracker
import com.cdsap.talaiot.request.Request
import io.kotlintest.properties.assertAll
import io.kotlintest.specs.BehaviorSpec
import junit.framework.Assert.assertTrue
import org.gradle.api.Project
import org.gradle.internal.impldep.org.junit.Assert.assertThat
import org.gradle.testfixtures.ProjectBuilder


class InfluxDbPublisherTest : BehaviorSpec({
    given("InfluxDbPublisher configuration") {
        val logger = LogTracker(LogTracker.Mode.INFO)

        `when`("There is configuration with metrics and tasks ") {
            val influxDbCon = InfluxDbPublisherConfiguration().apply {
                dbName = "db"
                url = "http://localhost:666"
                urlMetric = "log"
            }
            val testRequest = TestRequest(logger)
            val influxDbPublisher = InfluxDbPublisher(
                influxDbCon, logger, testRequest
            )

            then("should contains formatted the url and content the Request") {
                influxDbPublisher.publish(
                    measurementAggregated = TaskMeasurementAggregated(
                        mapOf(
                            "metric1" to "value1",
                            "metric2" to "value2"
                        ), listOf(TaskLength(1, ":clean", TaskMessageState.NO_MESSAGE_STATE))
                    )
                )
                assert(testRequest.content == "log,task=\":clean\",metric1=\"value1\",metric2=\"value2\"  value=1\n")
                assert(testRequest.url == "http://localhost:666/write?db=db")
            }
        }

        `when`("There is configuration without metrics and tasks ") {
            val influxDbCon = InfluxDbPublisherConfiguration().apply {
                dbName = "db"
                url = "http://localhost:666"
                urlMetric = "log"
            }
            val testRequest = TestRequest(logger)
            val influxDbPublisher = InfluxDbPublisher(
                influxDbCon, logger, testRequest
            )

            then("should TestRequest be empty") {
                influxDbPublisher.publish(
                    measurementAggregated = TaskMeasurementAggregated(
                        emptyMap(), emptyList()

                    )
                )
                assert(testRequest.content.isEmpty())
                assert(testRequest.url.isEmpty())
            }
        }
    }

})

class TestRequest(override var logTracker: LogTracker) : Request {

    var url = ""
    var content = ""
    override fun send(url: String, content: String) {
        this.url = url
        this.content = content
    }
}

