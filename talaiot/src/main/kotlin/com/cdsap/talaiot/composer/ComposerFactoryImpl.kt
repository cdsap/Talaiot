package com.cdsap.talaiot.composer

import com.cdsap.talaiot.logger.LogTracker
import com.cdsap.talaiot.writer.FileWriter
import java.util.concurrent.Executor

class ComposerFactoryImpl
    : ComposerFactory {

    override fun createComposer(
        graphType: ComposerType,
        logTracker: LogTracker,
        fileWriter: FileWriter,
        executor: Executor
    ): Composer = when (graphType) {
        ComposerType.HTML -> HtmlComposer(logTracker, fileWriter, executor)
        ComposerType.DOT -> DotComposer(logTracker, fileWriter, executor)
        ComposerType.GEXF -> GexfComposer(logTracker, fileWriter, executor)
    }
}
