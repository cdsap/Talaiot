package com.cdsap.talaiot.composer

import com.cdsap.talaiot.composer.resources.ResourcesHtml
import com.cdsap.talaiot.entities.TaskLength
import com.cdsap.talaiot.entities.TaskMeasurementAggregated
import com.cdsap.talaiot.entities.TaskMessageState
import com.cdsap.talaiot.logger.LogTracker
import com.cdsap.talaiot.publisher.TestExecutor
import com.cdsap.talaiot.writer.FileWriter
import com.nhaarman.mockitokotlin2.argThat
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import io.kotlintest.specs.BehaviorSpec
import java.lang.StringBuilder

class HtmlComposerTest : BehaviorSpec({
    given("Html Composer") {
        `when`("composing new aggregation ") {
            val logger: LogTracker = mock()
            val fileWriter: FileWriter<String> = mock()
            val executor = TestExecutor()
            val htmlComposer = HtmlComposer(logger, fileWriter, executor)
            then("writer is using the content") {

                val content = "nodes.push({id: 0, title:'app', group:'app', " +
                        "label: 'assemble', " +
                        "value: 0});\n" +
                        "nodes.push({id: 1, title:'app', group:'app', " +
                        "label: 'compileKotlin', " +
                        "value: 1});\n" +
                        "edges.push({from: 1, to: 0});\n"

                htmlComposer.compose(taskMeasurementAggregated())
                verify(fileWriter).prepareFile(
                    argThat {
                        this == StringBuilder().apply {
                            append(ResourcesHtml.HEADER)
                            append(content)
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
