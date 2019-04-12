package com.cdsap.talaiot.publisher.taskDependencyGraph

import com.cdsap.talaiot.entities.TaskMeasurementAggregated
import com.cdsap.talaiot.publisher.GraphPublisher
import com.cdsap.talaiot.publisher.taskDependencyGraph.resources.ResourcesGexf
import com.cdsap.talaiot.wrotter.Writter

/*
 GefxPublisher, exports format gefx
 */
class GefxPublisher(override var fileWriter: Writter) : GraphPublisher {

    override var fileName: String = "gefx.xml"
    private val header = ResourcesGexf.HEADER
    private val footer = ResourcesGexf.FOOTER
    private var internalCounterEdges = 0

    override fun publish(measurementAggregated: TaskMeasurementAggregated) {
        val graph = buildGraph(measurementAggregated)
        val content = contentComposer(graph, header, footer)
        writeFile(content)
    }

    override fun writeNode(internalId: Int, module: String, taskName: String, numberDependencies: Int): String =
        "<node id=\"$internalId\" label=\"$taskName\"/>\n"


    override fun writeEdge(
        from: Int,
        to: Int?
    ) = "<edge id=\"$internalCounterEdges\" source=\"$from\" target=\"$to\" />\n"

    override fun mask(vertices: String, edges: String) = "<nodes>$vertices</nodes><edges>$edges</edges>"
}