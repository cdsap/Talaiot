package com.cdsap.talaiot.publisher.graphpublisher

import com.cdsap.talaiot.publisher.graphpublisher.resources.ResourcesGexf
import com.cdsap.talaiot.logger.LogTracker
import com.cdsap.talaiot.mock.TaskMeasurementAggregatedMock
import com.cdsap.talaiot.publisher.TestExecutor
import com.cdsap.talaiot.writer.FileWriter
import com.nhaarman.mockitokotlin2.argThat
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import io.kotlintest.specs.BehaviorSpec
import java.lang.StringBuilder

class GexfPublisherTest : BehaviorSpec({
    given("Gexf DiskPublisher") {
        `when`("composing new aggregation ") {
            val logger: LogTracker = mock()
            val fileWriter: FileWriter = mock()
            val executor = TestExecutor()
            val gexfPublisher = GexfPublisher(logger, fileWriter, executor)
            then("writer is using the content") {

                val content = "       <node id=\"0\" label=\"assemble\">\n" +
                        "              <attvalues>\n" +
                        "                     <attvalue for=\"0\" value=\"app\"/>\n" +
                        "                     <attvalue for=\"1\" value=\"EXECUTED\"/>\n" +
                        "              </attvalues>\n" +
                        "       </node>\n" +
                        "       <node id=\"1\" label=\"compileKotlin\">\n" +
                        "              <attvalues>\n" +
                        "                     <attvalue for=\"0\" value=\"app\"/>\n" +
                        "                     <attvalue for=\"1\" value=\"EXECUTED\"/>\n" +
                        "              </attvalues>\n" +
                        "       </node>\n" +
                        "       <edge id=\"0\" source=\"1\" target=\"0\" />\n"

                gexfPublisher.publish(TaskMeasurementAggregatedMock.taskMeasurementAggregated())
                verify(fileWriter).prepareFile(
                    argThat {
                        this == StringBuilder().apply {
                            append(ResourcesGexf.HEADER)
                            append(content)
                            append(ResourcesGexf.FOOTER)
                        }.toString()
                    }, argThat {
                        this == "gexfTaskDependency.gexf"
                    }
                )
            }
        }
    }
}
)