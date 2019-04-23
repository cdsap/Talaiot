package com.cdsap.talaiot.publisher.graphpublisher


import com.cdsap.talaiot.entities.TaskMeasurementAggregated
import com.cdsap.talaiot.entities.TaskMessageState
import com.cdsap.talaiot.logger.LogTracker
import com.cdsap.talaiot.writer.FileWriter
import guru.nidi.graphviz.attribute.RankDir
import guru.nidi.graphviz.engine.*
import guru.nidi.graphviz.model.Factory.*
import guru.nidi.graphviz.model.Node
import guru.nidi.graphviz.model.LinkSource
import java.util.concurrent.Executor

class DotPublisher(
    override var logTracker: LogTracker,
    override var fileWriter: FileWriter,
    private val executor: Executor
) : DiskPublisher {
    private val fileName: String = "dotTaskDependency.png"
    private val fileNameXdot: String = "xdotTaskDependency.xdot"

    private fun getLinkSources(measurementAggregated: TaskMeasurementAggregated): List<LinkSource> {
        val mapNodes = mutableMapOf<String, Node>()
        val nodes = mutableListOf<LinkSource>()

        measurementAggregated.taskMeasurement.forEach {

            mapNodes[it.taskPath] = node(it.taskPath)

            val node = node(it.taskPath).with("module", it.module)
                .with("cached", it.state == TaskMessageState.FROM_CACHE)

            it.taskDependencies.forEach { dependency ->
                mapNodes[dependency]?.let { n ->
                    nodes.add(n.link(node))
                }

            }
        }
        return nodes.toList()
    }

    override fun publish(taskMeasurementAggregated: TaskMeasurementAggregated) {
        executor.execute {
            try {

                val g = graph("Talaiot").directed()
                    .strict()
                    .graphAttr().with(RankDir.TOP_TO_BOTTOM)
                    .with(getLinkSources(taskMeasurementAggregated))

                fileWriter.prepareFile(
                    Graphviz.fromGraph(g).fontAdjust(0.90)
                        .render(Format.PNG), fileName
                )

                fileWriter.prepareFile(
                    Graphviz.fromGraph(g)
                        .render(Format.XDOT), fileNameXdot
                )

                Graphviz.releaseEngine()

            } catch (e: Exception) {
                logTracker.log("DotPublisher Error -> ${e.message}")
            }
        }
    }
}
