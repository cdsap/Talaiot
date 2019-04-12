package com.cdsap.talaiot.publisher

import com.cdsap.talaiot.entities.TaskMeasurementAggregated
import com.cdsap.talaiot.publisher.taskDependencyGraph.TaskDependencyNode
import com.cdsap.talaiot.wrotter.Writter
import java.lang.StringBuilder

interface GraphPublisher : Publisher {
    var fileWriter: Writter
    var fileName: String


    fun writeFile(contentFile: String) = fileWriter.createFile(contentFile, fileName)

    fun contentComposer(task: String, header: String, footer: String) = StringBuilder().apply {
        append(header)
        append(task)
        append(footer)
    }.toString()

    fun mask(vertices: String, edges: String): String = vertices + edges

    fun writeNode(
        internalId: Int,
        module: String,
        taskName: String,
        numberDependencies: Int
    ): String

    fun writeEdge(
        from: Int,
        to: Int?
    ): String

    fun buildGraph(taskMeasurementAggregated: TaskMeasurementAggregated): String {
        var count = 0
        var nodes = ""
        var edges = ""
        val dependencies = hashMapOf<String, TaskDependencyNode>()

        taskMeasurementAggregated.taskMeasurement.forEach {
            val dependency = TaskDependencyNode(it, count)
            dependency.module = getModule(it.taskPath)
            dependencies[it.taskPath] = dependency
            with(dependency) {
                nodes += writeNode(internalId, module, taskLength.taskName, taskLength.taskDependencies.count())
            }
            it.taskDependencies.forEach {
                edges += writeEdge(from = dependency.internalId, to = dependencies[it]?.internalId)
            }
            count++
        }
        return nodes + edges
    }

    private fun getModule(path: String): String = path
        .split(":")
        .toList()
        .dropLast(1)
        .joinToString(separator = ":")
}