package com.cdsap.talaiot.publisher

import com.cdsap.talaiot.configuration.InfluxDbPublisherConfiguration
import com.cdsap.talaiot.entities.TaskLength
import com.cdsap.talaiot.entities.TaskMeasurementAggregated
import com.cdsap.talaiot.entities.TaskMessageState
import com.cdsap.talaiot.logger.LogTracker
import com.cdsap.talaiot.logger.LogTrackerImpl
import com.cdsap.talaiot.request.Request
import io.kotlintest.specs.BehaviorSpec


class InfluxDbPublisherTest : BehaviorSpec({
    given("InfluxDbPublisher configuration") {
        val logger = LogTrackerImpl(LogTracker.Mode.INFO)

        `when`("There is configuration with metrics and tasks ") {
            val influxDbConfiguration = InfluxDbPublisherConfiguration().apply {
                dbName = "db"
                url = "http://localhost:666"
                urlMetric = "log"
            }
            val testRequest = TestRequest(logger)
            val influxDbPublisher = InfluxDbPublisher(
                influxDbConfiguration, logger, testRequest, TestExecutor()
            )

            then("should contains formatted the url and content the Request") {
                influxDbPublisher.publish(
                    taskMeasurementAggregated = TaskMeasurementAggregated(
                        getMetrics(), listOf(
                            TaskLength(
                                1, "clean", ":clean", TaskMessageState.EXECUTED, false,
                                "app", emptyList()
                            )
                        )
                    )
                )
                assert(testRequest.content == "log,state=EXECUTED,module=app,rootNode=false,task=:clean,metric1=value1,metric2=value2 value=1\n")
                assert(testRequest.url == "http://localhost:666/write?db=db")
            }
        }

        `when`("There is configuration without metrics and tasks ") {
            val influxDbConfiguration = InfluxDbPublisherConfiguration().apply {
                dbName = "db"
                url = "http://localhost:666"
                urlMetric = "log"
            }
            val testRequest = TestRequest(logger)
            val influxDbPublisher = InfluxDbPublisher(
                influxDbConfiguration, logger, testRequest, TestExecutor()
            )

            then("should TestRequest be empty") {
                influxDbPublisher.publish(
                    taskMeasurementAggregated = TaskMeasurementAggregated(
                        emptyMap(), emptyList()

                    )
                )
                assert(testRequest.content.isEmpty())
                assert(testRequest.url.isEmpty())
            }
        }
        `when`("There is configuration with metrics and tags not supported by Line Protocol") {
            val influxDbConfiguration = InfluxDbPublisherConfiguration().apply {
                dbName = "db"
                url = "http://localhost:666"
                urlMetric = "log"
            }
            val testRequest = TestRequest(logger)
            val influxDbPublisher = InfluxDbPublisher(
                influxDbConfiguration, logger, testRequest, TestExecutor()
            )

            then("these metrics should be parsed to correct format ") {
                influxDbPublisher.publish(
                    taskMeasurementAggregated = TaskMeasurementAggregated(
                        mapOf(
                            "me=tric1" to "va====lue1",
                            "metric2" to "val,,   , ue2"
                        ), listOf(TaskLength(1, "clean", ":clean", TaskMessageState.EXECUTED, true, "app", emptyList()))
                    )
                )
                assert(testRequest.content == "log,state=EXECUTED,module=app,rootNode=true,task=:clean,metric1=value1,metric2=value2 value=1\n")
                assert(testRequest.url == "http://localhost:666/write?db=db")
            }
        }

        `when`("There is a min threshold defined for one task") {
            val influxDbConfiguration = InfluxDbPublisherConfiguration().apply {
                dbName = "db"
                url = "http://localhost:666"
                urlMetric = "log"
                threshold {
                    minExecutionTime = 10
                }
            }
            val testRequest = TestRequest(logger)
            val influxDbPublisher = InfluxDbPublisher(
                influxDbConfiguration, logger, testRequest, TestExecutor()
            )

            then(" not calls are done ") {
                influxDbPublisher.publish(
                    taskMeasurementAggregated = TaskMeasurementAggregated(
                        getMetrics(), listOf(
                            TaskLength(
                                1, "clean", ":clean", TaskMessageState.EXECUTED, false,
                                "app", emptyList()
                            )
                        )
                    )
                )
                assert(testRequest.content.isEmpty())
                assert(testRequest.url.isEmpty())
            }
        }

        `when`("There is a min threshold defined for more than one task affecting only one") {
            val influxDbConfiguration = InfluxDbPublisherConfiguration().apply {
                dbName = "db"
                url = "http://localhost:666"
                urlMetric = "log"
                threshold {
                    minExecutionTime = 10
                }
            }
            val testRequest = TestRequest(logger)
            val influxDbPublisher = InfluxDbPublisher(
                influxDbConfiguration, logger, testRequest, TestExecutor()
            )

            then(" request is build only for the tasks within the threshold ") {
                influxDbPublisher.publish(
                    taskMeasurementAggregated = TaskMeasurementAggregated(
                        getMetrics(), listOf(
                            TaskLength(
                                1, "clean", ":clean", TaskMessageState.EXECUTED, false,
                                "app", emptyList()
                            ),
                            TaskLength(
                                100, "clean2", ":clean2", TaskMessageState.EXECUTED, false,
                                "app", emptyList()
                            )
                        )
                    )
                )
                assert(testRequest.content == "log,state=EXECUTED,module=app,rootNode=false,task=:clean2,metric1=value1,metric2=value2 value=100\n")
                assert(testRequest.url == "http://localhost:666/write?db=db")

            }
        }

        `when`("There max a min threshold defined for one task") {
            val influxDbConfiguration = InfluxDbPublisherConfiguration().apply {
                dbName = "db"
                url = "http://localhost:666"
                urlMetric = "log"
                threshold {
                    maxExecutionTime = 100
                }
            }
            val testRequest = TestRequest(logger)
            val influxDbPublisher = InfluxDbPublisher(
                influxDbConfiguration, logger, testRequest, TestExecutor()
            )

            then(" not calls are done ") {
                influxDbPublisher.publish(
                    taskMeasurementAggregated = TaskMeasurementAggregated(
                        getMetrics(), listOf(
                            TaskLength(
                                120, "clean", ":clean", TaskMessageState.EXECUTED, false,
                                "app", emptyList()
                            )
                        )
                    )
                )
                assert(testRequest.content.isEmpty())
                assert(testRequest.url.isEmpty())
            }
        }

        `when`("There is a max threshold defined for more than one task affecting only one") {
            val influxDbConfiguration = InfluxDbPublisherConfiguration().apply {
                dbName = "db"
                url = "http://localhost:666"
                urlMetric = "log"
                threshold {
                    maxExecutionTime = 100
                }
            }
            val testRequest = TestRequest(logger)
            val influxDbPublisher = InfluxDbPublisher(
                influxDbConfiguration, logger, testRequest, TestExecutor()
            )

            then(" request is build only for the tasks within the threshold ") {
                influxDbPublisher.publish(
                    taskMeasurementAggregated = TaskMeasurementAggregated(
                        getMetrics(), listOf(
                            TaskLength(
                                1, "clean", ":clean", TaskMessageState.EXECUTED, false,
                                "app", emptyList()
                            ),
                            TaskLength(
                                120, "clean2", ":clean2", TaskMessageState.EXECUTED, false,
                                "app", emptyList()
                            )
                        )
                    )
                )
                assert(testRequest.content == "log,state=EXECUTED,module=app,rootNode=false,task=:clean,metric1=value1,metric2=value2 value=1\n")
                assert(testRequest.url == "http://localhost:666/write?db=db")

            }
        }

        `when`("There is a max and min threshold defined") {
            val influxDbConfiguration = InfluxDbPublisherConfiguration().apply {
                dbName = "db"
                url = "http://localhost:666"
                urlMetric = "log"
                threshold {
                    maxExecutionTime = 100
                    minExecutionTime = 10
                }
            }
            val testRequest = TestRequest(logger)
            val influxDbPublisher = InfluxDbPublisher(
                influxDbConfiguration, logger, testRequest, TestExecutor()
            )

            then(" just one task is processed because is it in the range ") {
                influxDbPublisher.publish(
                    taskMeasurementAggregated = TaskMeasurementAggregated(
                        getMetrics(), listOf(
                            TaskLength(
                                50, "clean", ":clean", TaskMessageState.EXECUTED, false,
                                "app", emptyList()
                            ),
                            TaskLength(
                                120, "clean2", ":clean2", TaskMessageState.EXECUTED, false,
                                "app", emptyList()
                            ),
                            TaskLength(
                                9, "clean3", ":clean3", TaskMessageState.EXECUTED, false,
                                "app", emptyList()
                            )
                        )
                    )
                )
                assert(testRequest.content == "log,state=EXECUTED,module=app,rootNode=false,task=:clean,metric1=value1,metric2=value2 value=50\n")
                assert(testRequest.url == "http://localhost:666/write?db=db")

            }
        }

        `when`("There is a threshold configuration but not ranges defined") {
            val influxDbConfiguration = InfluxDbPublisherConfiguration().apply {
                dbName = "db"
                url = "http://localhost:666"
                urlMetric = "log"
                threshold {
                }
            }
            val testRequest = TestRequest(logger)
            val influxDbPublisher = InfluxDbPublisher(
                influxDbConfiguration, logger, testRequest, TestExecutor()
            )

            then(" all the tasks are processed") {
                influxDbPublisher.publish(
                    taskMeasurementAggregated = TaskMeasurementAggregated(
                        getMetrics(), listOf(
                            TaskLength(
                                50, "clean", ":clean", TaskMessageState.EXECUTED, false,
                                "app", emptyList()
                            ),
                            TaskLength(
                                120, "clean2", ":clean2", TaskMessageState.EXECUTED, false,
                                "app", emptyList()
                            ),
                            TaskLength(
                                9, "clean3", ":clean3", TaskMessageState.EXECUTED, false,
                                "app", emptyList()
                            )
                        )
                    )
                )
                assert(
                    testRequest.content == "log,state=EXECUTED,module=app,rootNode=false,task=:clean,metric1=value1,metric2=value2 value=50\n" +
                            "log,state=EXECUTED,module=app,rootNode=false,task=:clean2,metric1=value1,metric2=value2 value=120\n" +
                            "log,state=EXECUTED,module=app,rootNode=false,task=:clean3,metric1=value1,metric2=value2 value=9\n"
                )
                assert(testRequest.url == "http://localhost:666/write?db=db")

            }
        }

        `when`("negatives values are defined in the threshold configuration") {
            val influxDbConfiguration = InfluxDbPublisherConfiguration().apply {
                dbName = "db"
                url = "http://localhost:666"
                urlMetric = "log"
                threshold {
                    minExecutionTime = -10
                    maxExecutionTime = -5
                }

            }
            val testRequest = TestRequest(logger)
            val influxDbPublisher = InfluxDbPublisher(
                influxDbConfiguration, logger, testRequest, TestExecutor()
            )

            then(" default values for the threshold are replacing the negatives values") {
                influxDbPublisher.publish(
                    taskMeasurementAggregated = TaskMeasurementAggregated(
                        getMetrics(), listOf(
                            TaskLength(
                                50, "clean", ":clean", TaskMessageState.EXECUTED, false,
                                "app", emptyList()
                            ),
                            TaskLength(
                                120, "clean2", ":clean2", TaskMessageState.EXECUTED, false,
                                "app", emptyList()
                            ),
                            TaskLength(
                                9, "clean3", ":clean3", TaskMessageState.EXECUTED, false,
                                "app", emptyList()
                            )
                        )
                    )
                )
                assert(
                    testRequest.content == "log,state=EXECUTED,module=app,rootNode=false,task=:clean,metric1=value1,metric2=value2 value=50\n" +
                            "log,state=EXECUTED,module=app,rootNode=false,task=:clean2,metric1=value1,metric2=value2 value=120\n" +
                            "log,state=EXECUTED,module=app,rootNode=false,task=:clean3,metric1=value1,metric2=value2 value=9\n"
                )
                assert(testRequest.url == "http://localhost:666/write?db=db")

            }
        }


    }

})

private fun getMetrics(): Map<String, String> {
    return mapOf(
        "metric1" to "value1",
        "metric2" to "value2"
    )
}

class TestRequest(override var logTracker: LogTracker) : Request {

    var url = ""
    var content = ""
    override fun send(url: String, content: String) {
        this.url = url
        this.content = content
    }
}
