package com.cdsap.talaiot.publisher.graphpublisher

import com.cdsap.talaiot.logger.LogTracker
import com.cdsap.talaiot.publisher.TestExecutor
import com.cdsap.talaiot.writer.FileWriter
import com.nhaarman.mockitokotlin2.*
import io.kotlintest.specs.BehaviorSpec

class GraphPublisherFactoryTest : BehaviorSpec({
    given("GraphPublisherFactory Implementation") {
        val graphPublisherFactory = GraphPublisherFactoryImpl()
        `when`("type is Html ") {
            val logger: LogTracker = mock()
            val fileWriter: FileWriter = mock()
            val executor = TestExecutor()
            val publisher = graphPublisherFactory.createPublisher(GraphPublisherType.HTML, logger, fileWriter, executor)
            then("instance of publisher is HtmlPublisher") {
                assert(publisher is HtmlPublisher)
            }
        }
        `when`("type is Gexg ") {
            val logger: LogTracker = mock()
            val fileWriter: FileWriter = mock()
            val executor = TestExecutor()
            val publisher = graphPublisherFactory.createPublisher(GraphPublisherType.GEXF, logger, fileWriter, executor)
            then("instance of publisher is GexfPublisher") {
                assert(publisher is GexfPublisher)
            }
        }
        `when`("type is Dot ") {
            val logger: LogTracker = mock()
            val fileWriter: FileWriter = mock()
            val executor = TestExecutor()
            val publisher = graphPublisherFactory.createPublisher(GraphPublisherType.DOT, logger, fileWriter, executor)
            then("instance of publisher DotPublisher") {
                assert(publisher is DotPublisher)
            }
        }
    }
}
)
