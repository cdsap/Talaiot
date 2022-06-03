package io.github.cdsap.talaiot.publisher

import com.nhaarman.mockitokotlin2.argForWhich
import com.nhaarman.mockitokotlin2.inOrder
import com.nhaarman.mockitokotlin2.mock
import io.github.cdsap.talaiot.entities.ExecutionReport
import io.github.cdsap.talaiot.entities.TaskLength
import io.github.cdsap.talaiot.entities.TaskMessageState
import io.github.cdsap.talaiot.logger.LogTracker
import io.kotlintest.specs.BehaviorSpec

class OutputPublisherTest : BehaviorSpec({
    given("OutputPublisher configuration") {
        `when`("There are no tasks tracked") {
            val logTracker: LogTracker = mock()
            val outputPublisherConfiguration = OutputPublisherConfiguration()
            val outputPublisher = OutputPublisher(outputPublisherConfiguration, logTracker)
            then("shouldn't print anything") {
                outputPublisher.publish(ExecutionReport())
                inOrder(logTracker) {
                    verify(logTracker).log("OutputPublisher", "================")
                    verify(logTracker).log("OutputPublisher", "OutputPublisher")
                    verify(logTracker).log("OutputPublisher", "publishBuildMetrics: true")
                    verify(logTracker).log("OutputPublisher", "publishTaskMetrics: true")
                    verify(logTracker).log("OutputPublisher", "================")
                    verifyNoMoreInteractions()
                }
            }
        }
        `when`("There are tasks tracked") {
            val logTracker: LogTracker = mock()
            then("should apply sorting desc") {
                val outputPublisherConfiguration = OutputPublisherConfiguration()
                val outputPublisher = OutputPublisher(outputPublisherConfiguration, logTracker)
                val taskMeasurementAggregated = ExecutionReport(
                    tasks = listOf(
                        TaskLength(
                            20L,
                            "averageTask",
                            ":averageTask",
                            TaskMessageState.EXECUTED,
                            true,
                            "app"
                        ),
                        TaskLength(
                            30L,
                            "slowTask",
                            ":slowTask",
                            TaskMessageState.EXECUTED,
                            true,
                            "app"
                        ),
                        TaskLength(
                            10L,
                            "fastTask",
                            ":fastTask",
                            TaskMessageState.EXECUTED,
                            true,
                            "app"
                        )
                    )
                )
                outputPublisher.publish(taskMeasurementAggregated)
                inOrder(logTracker) {
                    verify(logTracker).log("OutputPublisher", "================")
                    verify(logTracker).log("OutputPublisher", "OutputPublisher")
                    verify(logTracker).log("OutputPublisher", "publishBuildMetrics: true")
                    verify(logTracker).log("OutputPublisher", "publishTaskMetrics: true")
                    verify(logTracker).log("OutputPublisher", "================")
                    verify(logTracker).log(
                        argForWhich { this == "OutputPublisher" },
                        argForWhich {
                            this.contains("fastTask")
                        }
                    )
                    verify(logTracker).log(
                        argForWhich { this == "OutputPublisher" },
                        argForWhich {
                            this.contains("averageTask")
                        }
                    )
                    verify(logTracker).log(
                        argForWhich { this == "OutputPublisher" },
                        argForWhich {
                            this.contains("slowTask")
                        }
                    )
                    verifyNoMoreInteractions()
                }
            }
            then("should apply sorting asc") {
                val outputPublisherConfiguration = OutputPublisherConfiguration()
                outputPublisherConfiguration.order = Order.DESC
                val outputPublisher = OutputPublisher(outputPublisherConfiguration, logTracker)

                val taskMeasurementAggregated = ExecutionReport(
                    tasks = listOf(
                        TaskLength(
                            20L,
                            "averageTask",
                            ":averageTask",
                            TaskMessageState.EXECUTED,
                            true,
                            "app"
                        ),
                        TaskLength(
                            30L,
                            "slowTask",
                            ":slowTask",
                            TaskMessageState.EXECUTED,
                            true,
                            "app",
                        ),
                        TaskLength(
                            10L,
                            "fastTask",
                            ":fastTask",
                            TaskMessageState.EXECUTED,
                            true,
                            "app"
                        )
                    )
                )
                outputPublisher.publish(taskMeasurementAggregated)
                inOrder(logTracker) {
                    verify(logTracker).log("OutputPublisher", "================")
                    verify(logTracker).log("OutputPublisher", "OutputPublisher")
                    verify(logTracker).log("OutputPublisher", "publishBuildMetrics: true")
                    verify(logTracker).log("OutputPublisher", "publishTaskMetrics: true")
                    verify(logTracker).log("OutputPublisher", "================")
                    verify(logTracker).log(
                        argForWhich { this == "OutputPublisher" },
                        argForWhich {
                            this.contains("slowTask")
                        }
                    )
                    verify(logTracker).log(
                        argForWhich { this == "OutputPublisher" },
                        argForWhich {
                            this.contains("averageTask")
                        }
                    )
                    verify(logTracker).log(
                        argForWhich { this == "OutputPublisher" },
                        argForWhich {
                            this.contains("fastTask")
                        }
                    )
                    verifyNoMoreInteractions()
                }
            }
        }
        `when`("There is task tracked with 0 length") {
            val logTracker: LogTracker = mock()
            val outputPublisherConfiguration = OutputPublisherConfiguration()
            val outputPublisher = OutputPublisher(outputPublisherConfiguration, logTracker)
            then("should print the task with 0 length") {
                val taskMeasurementAggregated = ExecutionReport(
                    tasks = listOf(
                        TaskLength(
                            0L,
                            "zeroTask",
                            ":zeroTask",
                            TaskMessageState.EXECUTED,
                            true,
                            "app"
                        )
                    )
                )
                outputPublisher.publish(taskMeasurementAggregated)
                inOrder(logTracker) {
                    verify(logTracker).log("OutputPublisher", "================")
                    verify(logTracker).log("OutputPublisher", "OutputPublisher")
                    verify(logTracker).log("OutputPublisher", "publishBuildMetrics: true")
                    verify(logTracker).log("OutputPublisher", "publishTaskMetrics: true")
                    verify(logTracker).log("OutputPublisher", "================")
                    verify(logTracker).log(
                        argForWhich { this == "OutputPublisher" },
                        argForWhich {
                            this.contains("zeroTask: 0ms")
                        }
                    )
                    verifyNoMoreInteractions()
                }
            }
        }
        `when`("There are different time units on the task tracked") {
            val logTracker: LogTracker = mock()
            val outputPublisherConfiguration = OutputPublisherConfiguration()
            val outputPublisher = OutputPublisher(outputPublisherConfiguration, logTracker)
            then("should print length in the correct unit") {
                val taskMeasurementAggregated = ExecutionReport(
                    tasks = listOf(
                        TaskLength(
                            2_000L,
                            "secTask",
                            ":secTask",
                            TaskMessageState.EXECUTED,
                            true,
                            "app"
                        ),
                        TaskLength(
                            65_000L,
                            "minTask",
                            ":minTask",
                            TaskMessageState.EXECUTED,
                            true,
                            "app"
                        ),
                        TaskLength(
                            10L,
                            "msTask",
                            ":msTask",
                            TaskMessageState.EXECUTED,
                            true,
                            "app"
                        )
                    )
                )
                outputPublisher.publish(taskMeasurementAggregated)
                inOrder(logTracker) {
                    verify(logTracker).log("OutputPublisher", "================")
                    verify(logTracker).log("OutputPublisher", "OutputPublisher")
                    verify(logTracker).log("OutputPublisher", "publishBuildMetrics: true")
                    verify(logTracker).log("OutputPublisher", "publishTaskMetrics: true")
                    verify(logTracker).log("OutputPublisher", "================")
                    verify(logTracker).log(
                        argForWhich { this == "OutputPublisher" },
                        argForWhich {
                            this.contains("msTask: 10ms")
                        }
                    )
                    verify(logTracker).log(
                        argForWhich { this == "OutputPublisher" },
                        argForWhich {
                            this.contains("secTask: 2sec")
                        }
                    )
                    verify(logTracker).log(
                        argForWhich { this == "OutputPublisher" },
                        argForWhich {
                            this.contains("minTask: 1min")
                        }
                    )
                    verifyNoMoreInteractions()
                }
            }
        }
        `when`("There are tasks tracked and the configuration of the Publisher exceeds number of tasks ") {
            val logTracker: LogTracker = mock()
            then("should apply sorting desc") {
                val outputPublisherConfiguration = OutputPublisherConfiguration()
                outputPublisherConfiguration.numberOfTasks = 100
                val outputPublisher = OutputPublisher(outputPublisherConfiguration, logTracker)
                val taskMeasurementAggregated = ExecutionReport(
                    tasks = listOf(
                        TaskLength(
                            20L,
                            "averageTask",
                            ":averageTask",
                            TaskMessageState.EXECUTED,
                            true,
                            "app"
                        ),
                        TaskLength(
                            30L,
                            "slowTask",
                            ":slowTask",
                            TaskMessageState.EXECUTED,
                            true,
                            "app"
                        ),
                        TaskLength(
                            10L,
                            "fastTask",
                            ":fastTask",
                            TaskMessageState.EXECUTED,
                            true,
                            "app"
                        )
                    )
                )
                outputPublisher.publish(taskMeasurementAggregated)
                inOrder(logTracker) {
                    verify(logTracker).log("OutputPublisher", "================")
                    verify(logTracker).log("OutputPublisher", "OutputPublisher")
                    verify(logTracker).log("OutputPublisher", "publishBuildMetrics: true")
                    verify(logTracker).log("OutputPublisher", "publishTaskMetrics: true")
                    verify(logTracker).log("OutputPublisher", "================")
                    verify(logTracker).log(
                        argForWhich { this == "OutputPublisher" },
                        argForWhich {
                            this.contains("fastTask")
                        }
                    )
                    verify(logTracker).log(
                        argForWhich { this == "OutputPublisher" },
                        argForWhich {
                            this.contains("averageTask")
                        }
                    )
                    verify(logTracker).log(
                        argForWhich { this == "OutputPublisher" },
                        argForWhich {
                            this.contains("slowTask")
                        }
                    )
                    verifyNoMoreInteractions()
                }
            }
        }
    }
})
