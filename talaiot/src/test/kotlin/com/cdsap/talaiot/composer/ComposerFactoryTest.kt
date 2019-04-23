package com.cdsap.talaiot.composer

import com.cdsap.talaiot.logger.LogTracker
import com.cdsap.talaiot.publisher.TestExecutor
import com.cdsap.talaiot.writer.FileWriter
import com.nhaarman.mockitokotlin2.*
import io.kotlintest.specs.BehaviorSpec

class ComposerFactoryTest : BehaviorSpec({
    given("ComposerFactory Implementation") {
        val composerFactory = ComposerFactoryImpl()
        `when`("type is Html ") {
            val logger: LogTracker = mock()
            val fileWriter: FileWriter = mock()
            val executor = TestExecutor()
            val composer = composerFactory.createComposer(ComposerType.HTML, logger, fileWriter, executor)
            then("instance of composer is HtmlComposer") {
                assert(composer is HtmlComposer)
            }
        }
        `when`("type is Gexg ") {
            val logger: LogTracker = mock()
            val fileWriter: FileWriter = mock()
            val executor = TestExecutor()
            val composer = composerFactory.createComposer(ComposerType.GEXF, logger, fileWriter, executor)
            then("instance of composer is GexfComposer") {
                assert(composer is GexfComposer)
            }
        }
        `when`("type is Dot ") {
            val logger: LogTracker = mock()
            val fileWriter: FileWriter = mock()
            val executor = TestExecutor()
            val composer = composerFactory.createComposer(ComposerType.DOT, logger, fileWriter, executor)
            then("instance of composer DotComposer") {
                assert(composer is DotComposer)
            }
        }
    }
}
)
