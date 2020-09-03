package com.cdsap.talaiot.publisher.graphpublisher

import com.cdsap.talaiot.logger.LogTracker
import com.cdsap.talaiot.publisher.Publisher
import com.cdsap.talaiot.writer.FileWriter


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
