package io.github.cdsap.talaiot.publisher.graph

import com.nhaarman.mockitokotlin2.mock
import io.github.cdsap.talaiot.logger.LogTracker
import io.github.cdsap.talaiot.publisher.Publisher
import io.github.cdsap.talaiot.publisher.graph.writer.FileWriter
import io.github.cdsap.talaiot.utils.TestExecutor
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
