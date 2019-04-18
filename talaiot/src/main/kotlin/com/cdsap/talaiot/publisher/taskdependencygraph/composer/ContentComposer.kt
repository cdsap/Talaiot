package com.cdsap.talaiot.publisher.taskdependencygraph.composer

import com.cdsap.talaiot.entities.TaskMeasurementAggregated
import com.cdsap.talaiot.logger.LogTracker
import com.cdsap.talaiot.entities.TaskDependencyNode
import com.cdsap.talaiot.writer.FileWriter
import java.lang.StringBuilder

interface ContentComposer {
    var logTracker: LogTracker
    var fileWriter: FileWriter

    fun compose(taskMeasurementAggregated: TaskMeasurementAggregated)

    fun contentComposer(task: String, header: String, footer: String) = StringBuilder().apply {
        append(header)
        append(task)
        append(footer)
    }.toString()

    fun writeFile(contentFile: String, name: String) = fileWriter.createFile(contentFile, name)


    fun mask(vertices: String, edges: String): String = vertices + edges

    fun formatNode(
        internalId: Int,
        module: String,
        taskName: String,
        numberDependencies: Int
    ): String

    fun formatEdge(
        from: Int,
        to: Int?
    ): String

    fun write(statement: String): String {
        logTracker.log(statement)
        return statement
    }

    private fun getModule(path: String): String = path
        .split(":")
        .toList()
        .dropLast(1)
        .joinToString(separator = ":")

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
                nodes += formatNode(internalId, module, taskLength.taskName, taskLength.taskDependencies.count())
            }
            it.taskDependencies.forEach {
                edges += formatEdge(from = dependency.internalId, to = dependencies[it]?.internalId)
            }
            count++
        }
        return nodes + edges
    }
}