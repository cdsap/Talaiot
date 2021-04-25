package io.github.cdsap.talaiot.publisher.graph

import io.github.cdsap.talaiot.logger.LogTracker
import io.github.cdsap.talaiot.utils.TestExecutor
import io.github.cdsap.talaiot.publisher.graph.writer.FileWriter
import com.nhaarman.mockitokotlin2.*
import io.kotlintest.specs.BehaviorSpec

class DotPublisherTest : BehaviorSpec({
    given("Dot Publisher instance") {
        `when`("composing new aggregation ") {
            val logger:LogTracker = mock()
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
