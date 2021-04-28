package io.github.cdsap.talaiot.publisher.graph

import io.github.cdsap.talaiot.publisher.graph.resources.ResourcesHtml
import io.github.cdsap.talaiot.logger.LogTracker
import io.github.cdsap.talaiot.publisher.graph.TaskMeasurementAggregatedMock.taskMeasurementAggregated
import io.github.cdsap.talaiot.publisher.graph.resources.ResourcesHtml.LEGEND_HEADER
import io.github.cdsap.talaiot.utils.TestExecutor
import io.github.cdsap.talaiot.publisher.graph.writer.FileWriter
import com.nhaarman.mockitokotlin2.argThat
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import io.kotlintest.specs.BehaviorSpec
import java.lang.StringBuilder

class HtmlPublisherTest : BehaviorSpec({
    given("Html DiskPublisher") {
        `when`("composing new aggregation ") {
            val logger:LogTracker = mock()
            val fileWriter: FileWriter = mock()
            val executor = TestExecutor()
            val htmlPublisher = HtmlPublisher(logger, fileWriter, executor)
            then("writer is using the content") {

                val content = "      nodes.push({id: 0, title:'assemble', group:'app', " +
                        "label: 'assemble', " +
                        "value: 0});\n" +
                        "      nodes.push({id: 1, title:'compileKotlin', group:'app', " +
                        "label: 'compileKotlin', " +
                        "value: 1});\n" +
                        "      edges.push({from: 1, to: 0});\n"

                val legend = LEGEND_HEADER +
                        "      nodes.push({id: 10001, x: x, y: y, label: 'app', group: 'app', value: 1, " +
                        "fixed: true, physics:false});\n"
                htmlPublisher.publish(taskMeasurementAggregated())
                verify(fileWriter).prepareFile(
                    argThat {
                        this is String &&
                                this == StringBuilder().apply {
                            append(ResourcesHtml.HEADER)
                            append(content)
                            append(legend)
                            append(ResourcesHtml.FOOTER)
                        }.toString()

                    }, argThat {
                        this == "htmlTaskDependency.html"
                    }
                )
            }
        }
    }
}
)
