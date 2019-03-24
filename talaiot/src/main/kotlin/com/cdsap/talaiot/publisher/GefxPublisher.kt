package com.cdsap.talaiot.publisher

import com.cdsap.talaiot.entities.TaskLength
import com.cdsap.talaiot.entities.TaskMeasurementAggregated
import com.cdsap.talaiot.entities.TaskMessageState
import com.cdsap.talaiot.resources.TaskDependenciesHtml
import com.cdsap.talaiot.wrotter.Writter
import java.lang.StringBuilder

class GefxPublisher(private val fileWriter: Writter) : Publisher {
    val graphTaskM = mutableSetOf<GraphTask>()
    val g = hashMapOf<String, TaskDependency>()
    val modules = mutableSetOf<String>()

    override fun publish(measurementAggregated: TaskMeasurementAggregated) {
        var count = 0
        var nodes = ""

//        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
//                "<gexf xmlns=\"http://www.gexf.net/1.2draft\" version=\"1.2\">\n" +
//                "    <meta lastmodifieddate=\"2009-03-20\">\n" +
//                "        <creator>Gexf.net</creator>\n" +
//                "        <description>A hello world! file</description>\n" +
//                "    </meta>\n" +
//                "    <graph mode=\"static\" defaultedgetype=\"directed\">\n" +
//                "        <nodes>\n" +
//                "            <node id=\"0\" label=\"Hello\" />\n" +
//                "            <node id=\"1\" label=\"Word\" />\n" +
//                "        </nodes>\n" +
//                "        <edges>\n" +
//                "            <edge id=\"0\" source=\"0\" target=\"1\" />\n" +
//                "        </edges>\n" +
//                "    </graph>\n" +
//                "</gexf>"

        //   val xmlSerializer = XMLSerializer.newSerializer()


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
                1 + (it.taskDependencies.count() * 10)//getCounter(it.taskPath, measurementAggregated.taskMeasurement)
            g[it.taskPath] = dependency
            if (dependency?.taskLength?.state == TaskMessageState.FROM_CACHE) {
                color = ", color: '#ccccff'"
            }

            nodes += "nodes.push({id: ${dependency?.internalId}, title:'${dependency?.module}', group:'${dependency?.module}', label: '${dependency?.taskLength?.taskName}', value: ${dependency?.counter} $color});\n"

        }
        var edges = ""

        g.forEach {
            var taska = g[it.key]
            taska?.taskLength?.taskDependencies?.forEach {
                edges += "edges.push({from: ${taska.internalId}, to: ${g[it]?.internalId}});\n"

            }
        }

        val content = nodes + edges
        // println(contentComponser(content))
        fileWriter.createFile(contentComponser(content), "xx.html")

    }

    fun contentComponser(task: String): String {
        var content = StringBuilder()
        content.append(TaskDependenciesHtml.HEADER)
        content.append(task)
        content.append(
            TaskDependenciesHtml.FOOTER
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


data class TaskDependency(val taskLength: TaskLength, val internalId: Int) {
    var module: String = ""
    var counter: Int = 0
}


data class Node(val id: Int, val label: String)

data class Edge(val id: Int, val source: Int, val target: Int)