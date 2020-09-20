package com.cdsap.talaiot.publisher.graphpublisher

import com.cdsap.talaiot.logger.LogTracker
import com.cdsap.talaiot.mock.TaskMeasurementAggregatedMock
import com.cdsap.talaiot.utils.TestExecutor
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

                dotPublisher.publish(TaskMeasurementAggregatedMock.taskMeasurementAggregated())
                verify(fileWriter).prepareFile(any(), any())
            }
        }
    }
}
)
