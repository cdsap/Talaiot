package com.cdsap.talaiot.composer

import com.cdsap.talaiot.logger.LogTracker
import com.cdsap.talaiot.writer.FileWriter
import java.util.concurrent.Executor

interface ComposerFactory {
    fun createComposer(
        graphType: ComposerType,
        logTracker: LogTracker,
        fileWriter: FileWriter,
        executor: Executor
    ): Composer
}