package com.cdsap.talaiot.publisher.taskdependencygraph.composer

import com.cdsap.talaiot.entities.TaskMeasurementAggregated
import com.cdsap.talaiot.logger.LogTracker
import com.cdsap.talaiot.publisher.taskdependencygraph.resources.ResourcesHtml
import com.cdsap.talaiot.writer.FileWriter

class HtmlComposer(
    val logger: LogTracker,
    val writter: FileWriter
) : ContentComposer(logger, writter) {
    private val fileName: String = "taskDependency.html"

    override fun compose(taskMeasurementAggregated: TaskMeasurementAggregated) {
        val content = contentComposer(
            buildGraph(taskMeasurementAggregated), ResourcesHtml.HEADER,
            ResourcesHtml.HEADER
        )
        writeFile(content, fileName)
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

    //override fun mask(vertices: String, edges: String): String = vertices + edges
}