package com.cdsap.talaiot.publisher.graphpublisher

import com.cdsap.talaiot.logger.LogTracker
import com.cdsap.talaiot.publisher.Publisher
import com.cdsap.talaiot.publisher.TestExecutor
import com.cdsap.talaiot.writer.FileWriter
import com.nhaarman.mockitokotlin2.*
import io.kotlintest.specs.BehaviorSpec

class GraphPublisherFactoryTest : BehaviorSpec({
    given("GraphPublisherFactory Implementation") {
        val graphPublisherFactory = GraphPublisherFactoryImpl()
        `when`("type is Html ") {
            val publisher = initPublisher(graphPublisherFactory, GraphPublisherType.HTML)
            then("instance of publisher is HtmlPublisher") {
                assert(publisher is HtmlPublisher)
            }
        }
        `when`("type is Gexf ") {
            val publisher = initPublisher(graphPublisherFactory, GraphPublisherType.GEXF)
            then("instance of publisher is GexfPublisher") {
                assert(publisher is GexfPublisher)
            }
        }
        `when`("type is Dot ") {
            val publisher = initPublisher(graphPublisherFactory, GraphPublisherType.DOT)
            then("instance of publisher DotPublisher") {
                assert(publisher is DotPublisher)
            }
        }
    }
}
)

private fun initPublisher(
    graphPublisherFactory: GraphPublisherFactory,
    type: GraphPublisherType
): Publisher {
    val logger: LogTracker = mock()
    val fileWriter: FileWriter = mock()
    val executor = TestExecutor()
    return graphPublisherFactory.createPublisher(type, logger, fileWriter, executor)
}
