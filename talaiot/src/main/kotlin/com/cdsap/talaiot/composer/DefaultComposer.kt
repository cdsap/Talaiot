package com.cdsap.talaiot.composer

import com.cdsap.talaiot.entities.TaskDependencyNode
import com.cdsap.talaiot.entities.TaskMeasurementAggregated
import com.cdsap.talaiot.entities.TaskMessageState
import com.cdsap.talaiot.logger.LogTracker
import com.cdsap.talaiot.writer.FileWriter
import java.lang.StringBuilder

abstract class DefaultComposer(
    override var logTracker: LogTracker,
    override var fileWriter: FileWriter<String>
) : Composer<String> {


    abstract fun formatNode(
        internalId: Int,
        module: String,
        taskName: String,
        numberDependencies: Int,
        cached: Boolean
    ): String

    abstract fun formatEdge(
        from: Int,
        to: Int?
    ): String


    fun contentComposer(task: String, header: String, footer: String) = StringBuilder().apply {
        append(header)
        append(task)
        append(footer)
    }.toString()

    fun writeFile(contentFile: String, name: String) = fileWriter.prepareFile(contentFile, name)

    fun write(statement: String): String {
        logTracker.log(statement)
        return statement
    }

    fun buildGraph(taskMeasurementAggregated: TaskMeasurementAggregated): String {
        var count = 0
        var nodes = ""
        var edges = ""
        val dependencies = hashMapOf<String, TaskDependencyNode>()

        taskMeasurementAggregated.taskMeasurement.forEach {
            val dependency = TaskDependencyNode(it, count)
            dependencies[it.taskPath] = dependency
            with(dependency) {
                nodes += formatNode(
                    internalId, taskLength.module, taskLength.taskName, taskLength.taskDependencies.count(),
                    taskLength.state == TaskMessageState.FROM_CACHE
                )
            }
            it.taskDependencies.forEach {
                edges += formatEdge(from = dependency.internalId, to = dependencies[it]?.internalId)
            }

            count++
        }
        return nodes + edges
    }
}