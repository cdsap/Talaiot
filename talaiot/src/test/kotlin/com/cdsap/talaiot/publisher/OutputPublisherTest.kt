package com.cdsap.talaiot.publisher

import com.cdsap.talaiot.entities.TaskLength
import com.cdsap.talaiot.entities.TaskMeasurementAggregated
import com.cdsap.talaiot.entities.TaskMessageState
import com.cdsap.talaiot.logger.LogTracker
import com.nhaarman.mockitokotlin2.argForWhich
import com.nhaarman.mockitokotlin2.inOrder
import com.nhaarman.mockitokotlin2.mock
import io.kotlintest.specs.BehaviorSpec

class OutputPublisherTest : BehaviorSpec({
    given("OutputPublisher configuration") {
        `when`("There are no tasks tracked") {
            val logTracker: LogTracker = mock()
            val outputPublisher = OutputPublisher(logTracker)
            then("shouldn't print anything") {
                outputPublisher.publish(TaskMeasurementAggregated(emptyMap(), emptyList()))
                inOrder(logTracker) {
                    verify(logTracker).log("================")
                    verify(logTracker).log("OutputReporting")
                    verify(logTracker).log("================")
                    verifyNoMoreInteractions()
                }
            }
        }
        `when`("There are tasks tracked") {
            val logTracker: LogTracker = mock()
            val outputPublisher = OutputPublisher(logTracker)
            then("should apply sorting") {
                val taskMeasurementAggregated = TaskMeasurementAggregated(
                    emptyMap(),
                    listOf(
                        TaskLength(20L, ":averageTask", TaskMessageState.EXECUTED),
                        TaskLength(30L, ":slowTask", TaskMessageState.EXECUTED),
                        TaskLength(10L, ":fastTask", TaskMessageState.EXECUTED)
                    )
                )
                outputPublisher.publish(taskMeasurementAggregated)
                inOrder(logTracker) {
                    verify(logTracker).log("================")
                    verify(logTracker).log("OutputReporting")
                    verify(logTracker).log("================")
                    verify(logTracker).log(argForWhich {
                        this.contains(":fastTask")
                    })
                    verify(logTracker).log(argForWhich {
                        this.contains(":averageTask")
                    })
                    verify(logTracker).log(argForWhich {
                        this.contains(":slowTask")
                    })
                    verifyNoMoreInteractions()

                }
            }
        }
        `when`("There is task tracked with 0 length") {
            val logTracker: LogTracker = mock()
            val outputPublisher = OutputPublisher(logTracker)
            then("should print the task with 0 length") {
                val taskMeasurementAggregated = TaskMeasurementAggregated(
                    emptyMap(),
                    listOf(TaskLength(0L, ":zeroTask", TaskMessageState.EXECUTED))
                )
                outputPublisher.publish(taskMeasurementAggregated)
                inOrder(logTracker) {
                    verify(logTracker).log("================")
                    verify(logTracker).log("OutputReporting")
                    verify(logTracker).log("================")
                    verify(logTracker).log(argForWhich {
                        this.contains(":zeroTask : 0 ms")
                    })
                    verifyNoMoreInteractions()
                }
            }
        }
        `when`("There are different time units on the task tracked") {
            val logTracker: LogTracker = mock()
            val outputPublisher = OutputPublisher(logTracker)
            then("should print length in the correct unit") {
                val taskMeasurementAggregated = TaskMeasurementAggregated(
                    emptyMap(),
                    listOf(
                        TaskLength(2_000L, ":secTask", TaskMessageState.EXECUTED),
                        TaskLength(65_000L, ":minTask", TaskMessageState.EXECUTED),
                        TaskLength(10L, ":msTask", TaskMessageState.EXECUTED)
                    )
                )
                outputPublisher.publish(taskMeasurementAggregated)
                inOrder(logTracker) {
                    verify(logTracker).log("================")
                    verify(logTracker).log("OutputReporting")
                    verify(logTracker).log("================")
                    verify(logTracker).log(argForWhich {
                        this.contains(":msTask : 10 ms")
                    })
                    verify(logTracker).log(argForWhich {
                        this.contains(":secTask : 2 sec")
                    })
                    verify(logTracker).log(argForWhich {
                        this.contains(":minTask : 1 min")
                    })
                    verifyNoMoreInteractions()
                }
            }
        }
    }
}
)