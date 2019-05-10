package com.cdsap.talaiot.publisher.graphpublisher

import com.cdsap.talaiot.logger.LogTracker
import com.cdsap.talaiot.writer.FileWriter
import java.util.concurrent.Executor

/**
 * Generates Disks publishers instances depending on the GraphType passed as argument
 *
 */
interface GraphPublisherFactory {

    /**
     * Creates the DiskPublisher depending on the GraphType
     *
     * @param graphType Type of the default Graphs implemented by the publisher
     * @param logTracker LogTracker to print in console depending on the Mode
     * @param fileWriter File I/O utility
     * @param executor Executor to schedule a task in Background
     * @return an instance for one implementation of DiskPublisher
     */
    fun createPublisher(
        graphType: GraphPublisherType,
        logTracker: LogTracker,
        fileWriter: FileWriter,
        executor: Executor
    ): DiskPublisher
}