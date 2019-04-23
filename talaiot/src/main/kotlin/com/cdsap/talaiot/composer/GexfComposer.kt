package com.cdsap.talaiot.composer

import com.cdsap.talaiot.entities.TaskMeasurementAggregated
import com.cdsap.talaiot.logger.LogTracker
import com.cdsap.talaiot.composer.resources.ResourcesGexf
import com.cdsap.talaiot.writer.FileWriter
import java.util.concurrent.Executor
import java.util.concurrent.ExecutorService

class GexfComposer(
    override var logTracker: LogTracker,
    override var fileWriter: FileWriter,
    private val executor: Executor
) : DefaultComposer(logTracker, fileWriter) {

    private val fileName: String = "gexfTaskDependency.gexf"
    private var internalCounterEdges = 0

    override fun compose(taskMeasurementAggregated: TaskMeasurementAggregated) {
        executor.execute {
            val content = contentComposer(
                buildGraph(taskMeasurementAggregated), ResourcesGexf.HEADER,
                ResourcesGexf.FOOTER
            )
            writeFile(content, fileName)
        }
    }

    override fun formatNode(
        internalId: Int,
        module: String,
        taskName: String,
        numberDependencies: Int,
        cached: Boolean
    ): String =
        write(
            "<node id=\"$internalId\" label=\"$taskName\">\n" +
                    "           <attvalues>\n" +
                    "                    <attvalue for=\"0\" value=\"$module\"/>\n" +
                    "                    <attvalue for=\"1\" value=\"$cached\"/>\n" +
                    "           </attvalues>\n" +
                    "</node>\n"
        )

    override fun formatEdge(
        from: Int,
        to: Int?
    ) = write("<edge id=\"${internalCounterEdges++}\" source=\"$from\" target=\"$to\" />\n")
}
