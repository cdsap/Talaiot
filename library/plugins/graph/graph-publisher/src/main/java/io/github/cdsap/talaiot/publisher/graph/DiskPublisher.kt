package io.github.cdsap.talaiot.publisher.graph

import io.github.cdsap.talaiot.logger.LogTracker
import io.github.cdsap.talaiot.publisher.Publisher
import io.github.cdsap.talaiot.publisher.graph.writer.FileWriter


/**
 * Extension of Publisher interface with a FileWriter implementation to write on disk
 *
 */
interface DiskPublisher : Publisher {
    /**
     * LogTracker to print in console depending on the Mode
     */
    var logTracker: LogTracker
    /**
     * File I/O utility
     */
    var fileWriter: FileWriter
}
