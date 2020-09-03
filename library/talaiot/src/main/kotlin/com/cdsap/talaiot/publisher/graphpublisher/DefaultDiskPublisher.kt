package com.cdsap.talaiot.publisher.graphpublisher

import com.cdsap.talaiot.entities.ExecutionReport
import com.cdsap.talaiot.entities.TaskDependencyNode
import com.cdsap.talaiot.entities.TaskMessageState
import com.cdsap.talaiot.logger.LogTracker
import com.cdsap.talaiot.writer.FileWriter

/**
 * Abstract class implementing DiskPublisher.
 * Use this class if you need to compose the output of one GraphPublisher writing in Disk the next format:
 *   HEADER + CONTENT + LEGEND + FOOTER
 * Consumers should implement how to write the legend, nodes and edges.
 *
 */
abstract class DefaultDiskPublisher(
    override var logTracker: LogTracker,
    override var fileWriter: FileWriter
) : DiskPublisher {


    /**
     * Abstract function to format the information related with the node.
     * Depending on the Publisher we may want to format the node representation on different way.
     * Node here represent a tracked Task with Talaiot
     *
     * @param internalId internalId for the node
     * @param module module which belongs the task
     * @param taskName name of the task tracked
     * @param numberDependencies number of task dependencies
     * @param cached represents if the task has been cached during the execution
     *
     * @return a String with the node/task formatted
     */
    abstract fun formatNode(
        internalId: Int,
        module: String,
        taskName: String,
        numberDependencies: Int,
        taskMessageState: TaskMessageState
    ): String

    /**
     * Abstract function to format the information related with the edge.
     *
     * @param from origin of the relation, internalId
     * @param to destination of the relation, internalId
     *
     * @return a String with the edge formatted
     */
    abstract fun formatEdge(
        from: Int,
        to: Int?
    ): String


    /**
     * It builds a String with the different parts of the required format defined. This format is composed by a header
     * a content and a footer. Optionally it can be defined a legend. Used by the HtmlPublisher
     *
     * @param task content related with the results formatted of the tracked build
     * @param header header of the file
     * @param legend legend to represent the information included in the graph, optional.
     * @param footer footer of the file
     *
     * @return a String with all the information composed
     */
    fun contentComposer(task: String, header: String, legend: String? = null, footer: String) = StringBuilder().apply {
        append(header)
        append(task)
        legend?.let {
            append(legend)
        }
        append(footer)
    }.toString()


    fun writeFile(contentFile: String, name: String) = fileWriter.prepareFile(contentFile, name)

    /**
     * Once we have the results of the build(metrics + tasks), we need to build graph with the relation between the tasks.
     * The implementations of the abstract class will require the construction of the graph. The format of the graph is
     * given by the formatNode and formatEdge of this class.
     *
     * @param measurements Aggregated entity with the results of the build
     *
     * @return a String with the nodes and edges formatted by the implementations of the class
     */
    fun buildGraph(report: ExecutionReport): String {
        var count = 0
        var nodes = ""
        var edges = ""
        val dependencies = hashMapOf<String, TaskDependencyNode>()

        report.tasks?.forEach {
            val dependency = TaskDependencyNode(it, count)
            dependencies[it.taskPath] = dependency
            with(dependency) {
                nodes += formatNode(
                    internalId, taskLength.module, taskLength.taskName, taskLength.taskDependencies.count(),
                    taskLength.state
                )
            }
            it.taskDependencies.forEach { task ->
                edges += formatEdge(from = dependency.internalId, to = dependencies[task]?.internalId)
            }

            count++
        }

        return nodes + edges
    }
}