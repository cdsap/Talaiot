package com.cdsap.talaiot.publisher.graphpublisher

import com.cdsap.talaiot.entities.TaskLength
import com.cdsap.talaiot.entities.TaskMeasurementAggregated
import com.cdsap.talaiot.entities.TaskMessageState
import com.cdsap.talaiot.logger.LogTracker
import com.cdsap.talaiot.publisher.TestExecutor
import com.cdsap.talaiot.writer.FileWriter
import com.nhaarman.mockitokotlin2.*
import io.kotlintest.specs.BehaviorSpec

class DotPublisherTest : BehaviorSpec({
    given("Dot Publisher instance") {
        `when`("composing new aggregation ") {
            val logger: LogTracker = mock()
            val fileWriter: FileWriter = mock()
            val executor = TestExecutor()
            val dotPublisher = DotPublisher(logger, fileWriter, executor)
            then("writer is using the content") {

                dotPublisher.publish(taskMeasurementAggregated())
                verify(fileWriter, times(2)).prepareFile(any(), any())
            }
        }
    }
}
)

private fun taskMeasurementAggregated(): TaskMeasurementAggregated {
    return TaskMeasurementAggregated(
        emptyMap(),
        listOf(
            TaskLength(
                1,
                "assemble",
                "assemble",
                TaskMessageState.EXECUTED,
                true,
                "app",
                emptyList()
            ),
            TaskLength(
                2,
                "compileKotlin",
                "compileKotlin",
                TaskMessageState.EXECUTED,
                false,
                "app",
                listOf("assemble")

            )
        )
    )
}
