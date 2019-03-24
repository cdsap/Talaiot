package com.cdsap.talaiot.publisher

import com.cdsap.talaiot.entities.TaskLength
import com.cdsap.talaiot.entities.TaskMeasurementAggregated
import com.cdsap.talaiot.entities.TaskMessageState
import com.cdsap.talaiot.resources.TaskDependenciesHtml
import com.cdsap.talaiot.resources.TaskDependenciesXml
import com.cdsap.talaiot.wrotter.Writter
import java.lang.StringBuilder

class GefxPublisher(private val fileWriter: Writter) : Publisher {
    val graphTaskM = mutableSetOf<GraphTask>()
    val g = hashMapOf<String, TaskDependency>()
    val modules = mutableSetOf<String>()

    override fun publish(measurementAggregated: TaskMeasurementAggregated) {
        var count = 0
        var nodes = ""


        nodes += "<nodes>"
        measurementAggregated.taskMeasurement.forEach {
            val dependency = TaskDependency(it, count)

            count++
            val module = getModule(it.taskPath)
            dependency.module = module
            var color = ""
            if (!modules.contains(module)) {
                modules.add(module)
            }
            dependency.counter =
                    1 +
                    (it.taskDependencies.count() * 10)//getCounter(it.taskPath, measurementAggregated.taskMeasurement)
            g[it.taskPath] = dependency
            if (dependency?.taskLength?.state == TaskMessageState.FROM_CACHE) {
                color = ", color: '#ccccff'"
            }
            nodes += "  <node id=\"${dependency?.internalId}\" label=\"${dependency?.taskLength?.taskName}\" />\n"

        }
        nodes += "</nodes>"

        var edges = "<edges>"

        var counterEdges = 0
        g.forEach {
            var taska = g[it.key]
            taska?.taskLength?.taskDependencies?.forEach {
                edges += "  <edge id=\"$counterEdges\" source=\"${taska.internalId}\" target=\"${g[it]?.internalId}\" />\n"
                counterEdges++
            }
        }

        edges +="</edges>"

        val content = nodes + edges
        println(contentComponser(content))
        fileWriter.createFile(contentComponser(content), "xx.gexf")

    }

    fun contentComponser(task: String): String {
        var content = StringBuilder()
        content.append(TaskDependenciesXml.HEADER)
        content.append(task)
        content.append(
            TaskDependenciesXml.FOOTER
        )
        return content.toString()
    }

    fun getModule(path: String): String = path.split(":").toList().dropLast(1).joinToString(separator = ":")
    fun getCounter(path: String, tasks: List<TaskLength>): Int {
        var counter = 1
        tasks.forEach {

            counter += it.taskDependencies.filter { it == path }.count()
        }
        return counter * 10
    }
}


data class Node(val id: Int, val label: String)

data class Edge(val id: Int, val source: Int, val target: Int)