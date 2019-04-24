package com.cdsap.talaiot.publisher.graphpublisher

import com.cdsap.talaiot.logger.LogTracker
import com.cdsap.talaiot.writer.FileWriter
import java.util.concurrent.Executor

/**
 * Generates Disks publishers instances depending on the GraphType passed as argument
 *
 * @see GraphPublisherFactoryImpl
 * @see DiskPublisher
 * @see GraphPublisherType
 */
interface GraphPublisherFactory {


    fun createPublisher(
        graphType: GraphPublisherType,
        logTracker: LogTracker,
        fileWriter: FileWriter,
        executor: Executor
    ): DiskPublisher
}