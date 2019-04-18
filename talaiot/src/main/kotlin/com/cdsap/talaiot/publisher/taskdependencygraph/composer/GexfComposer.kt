package com.cdsap.talaiot.publisher.taskdependencygraph.composer

import com.cdsap.talaiot.entities.TaskMeasurementAggregated
import com.cdsap.talaiot.logger.LogTracker
import com.cdsap.talaiot.publisher.taskdependencygraph.resources.ResourcesGexf
import com.cdsap.talaiot.writer.FileWriter

class GexfComposer(
    override var logTracker: LogTracker,
    override var fileWriter: FileWriter
) : ContentComposer {
    val fileName: String = "talaiot.gexf"
    private var internalCounterEdges = 0

    override fun compose(measurementAggregated: TaskMeasurementAggregated) {
        val content = contentComposer(
            buildGraph(measurementAggregated), ResourcesGexf.HEADER,
            ResourcesGexf.FOOTER
        )
        writeFile(content, fileName)
    }

    override fun formatNode(internalId: Int, module: String, taskName: String, numberDependencies: Int): String =
        write("<node id=\"$internalId\" label=\"$taskName\"/>\n")

    override fun formatEdge(
        from: Int,
        to: Int?
    ) = write("<edge id=\"${internalCounterEdges++}\" source=\"$from\" target=\"$to\" />\n")

    override fun mask(vertices: String, edges: String) = "<nodes>$vertices</nodes><edges>$edges</edges>"
}
