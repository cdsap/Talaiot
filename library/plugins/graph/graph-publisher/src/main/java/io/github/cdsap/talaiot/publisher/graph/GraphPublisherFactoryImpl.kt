package io.github.cdsap.talaiot.publisher.graph

import io.github.cdsap.talaiot.logger.LogTracker
import io.github.cdsap.talaiot.publisher.graph.writer.FileWriter
import java.util.concurrent.Executor

class GraphPublisherFactoryImpl :
    GraphPublisherFactory {

    override fun createPublisher(
        graphType: GraphPublisherType,
        logTracker: LogTracker,
        fileWriter: FileWriter,
        executor: Executor
    ): DiskPublisher = when (graphType) {
        GraphPublisherType.HTML -> HtmlPublisher(
            logTracker,
            fileWriter,
            executor
        )
        GraphPublisherType.DOT -> DotPublisher(
            logTracker,
            fileWriter,
            executor
        )
        GraphPublisherType.GEXF -> GexfPublisher(
            logTracker,
            fileWriter,
            executor
        )
    }
}
