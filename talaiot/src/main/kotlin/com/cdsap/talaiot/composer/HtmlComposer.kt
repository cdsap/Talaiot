package com.cdsap.talaiot.composer

import com.cdsap.talaiot.entities.TaskMeasurementAggregated
import com.cdsap.talaiot.logger.LogTracker
import com.cdsap.talaiot.composer.resources.ResourcesHtml
import com.cdsap.talaiot.writer.FileWriter
import java.util.concurrent.Executor
import java.util.concurrent.ExecutorService

class HtmlComposer(
    override var logTracker: LogTracker,
    override var fileWriter: FileWriter,
    private val executor: Executor
) : DefaultComposer(logTracker, fileWriter) {
    private val fileName: String = "htmlTaskDependency.html"

    override fun compose(taskMeasurementAggregated: TaskMeasurementAggregated) {
        executor.execute {
            val content = contentComposer(
                buildGraph(taskMeasurementAggregated), ResourcesHtml.HEADER,
                ResourcesHtml.FOOTER
            )
            writeFile(content, fileName)
        }
    }

    override fun formatNode(
        internalId: Int,
        module: String,
        taskName: String,
        numberDependencies: Int,
        cached: Boolean
    ): String =
        write(
            "nodes.push({id: $internalId, title:'$module', group:'$module', " +
                    "label: '$taskName', " +
                    "value: $numberDependencies});\n"
        )

    override fun formatEdge(from: Int, to: Int?): String =
        write("edges.push({from: $from, to: $to});\n")

}
