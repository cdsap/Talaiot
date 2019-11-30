package com.cdsap.talaiot.publisher.graphpublisher

import com.cdsap.talaiot.entities.ExecutionReport
import com.cdsap.talaiot.entities.TaskMessageState
import com.cdsap.talaiot.logger.LogTracker
import com.cdsap.talaiot.publisher.graphpublisher.resources.ResourcesGexf
import com.cdsap.talaiot.writer.FileWriter
import java.util.concurrent.Executor

/**
 * Publisher used to publish with the Gexf format.
 */
class GexfPublisher(
    override var logTracker: LogTracker,
    override var fileWriter: FileWriter,
    /**
     * Executor to schedule a task in Background
     */
    private val executor: Executor
) : DefaultDiskPublisher(logTracker, fileWriter) {

    private val TAG = "GexfPublisher"

    /**
     * name of the file generated by the publisher
     */
    private val fileName: String = "gexfTaskDependency.gexf"
    /**
     * internal counter to add an id for the edges. It will be used at time to setup the relations
     */
    private var internalCounterEdges = 0

    override fun publish(report: ExecutionReport) {
        executor.execute {
            val content = contentComposer(
                task = buildGraph(report),
                header = ResourcesGexf.HEADER,
                footer = ResourcesGexf.FOOTER
            )
            logTracker.log(TAG, "writing file")
            writeFile(content, fileName)
        }
    }

    override fun formatNode(
        internalId: Int,
        module: String,
        taskName: String,
        numberDependencies: Int,
        taskMessageState: TaskMessageState
    ): String = "       <node id=\"$internalId\" label=\"$taskName\">\n" +
            "              <attvalues>\n" +
            "                     <attvalue for=\"0\" value=\"$module\"/>\n" +
            "                     <attvalue for=\"1\" value=\"${taskMessageState.name}\"/>\n" +
            "              </attvalues>\n" +
            "       </node>\n"


    override fun formatEdge(
        from: Int,
        to: Int?
    ) = "       <edge id=\"${internalCounterEdges++}\" " +
            "source=\"$from\" target=\"$to\" />\n"
}
