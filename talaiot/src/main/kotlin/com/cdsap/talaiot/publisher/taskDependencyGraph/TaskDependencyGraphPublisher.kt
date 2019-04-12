package com.cdsap.talaiot.publisher.taskDependencyGraph

import com.cdsap.talaiot.entities.TaskMeasurementAggregated
import com.cdsap.talaiot.publisher.GraphPublisher
import com.cdsap.talaiot.publisher.taskDependencyGraph.resources.ResourcesTaskDependencyGraph
import com.cdsap.talaiot.wrotter.Writter

/*
  TaskDependencyGraphPublisher with vis.js format
 */
class TaskDependencyGraphPublisher(override var fileWriter: Writter) : GraphPublisher {


    override var fileName: String = "taskDependency.html"
    private val header = ResourcesTaskDependencyGraph.HEADER
    private val footer = ResourcesTaskDependencyGraph.FOOTER

    override fun publish(measurementAggregated: TaskMeasurementAggregated) {
        val graph = buildGraph(measurementAggregated)
        val content = contentComposer(graph, header, footer)
        writeFile(content)
    }

    override fun writeNode(
        internalId: Int,
        module: String,
        taskName: String,
        numberDependencies: Int
    ) = "nodes.push({id: $internalId, title:'$module', group:'$module', " +
            "label: '$taskName', " +
            "value: $numberDependencies});\n"

    override fun writeEdge(
        from: Int,
        to: Int?
    ) = "edges.push({from: $from, to: $to});\n"

    override fun mask(vertices: String, edges: String): String = vertices + edges

}

