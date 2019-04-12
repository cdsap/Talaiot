package com.cdsap.talaiot.publisher.taskDependencyGraph

import com.cdsap.talaiot.configuration.Neo4JConfiguration
import com.cdsap.talaiot.entities.TaskLength
import com.cdsap.talaiot.entities.TaskMeasurementAggregated
import com.cdsap.talaiot.entities.TaskMessageState
import com.cdsap.talaiot.logger.LogTracker
import com.cdsap.talaiot.publisher.GraphPublisher
import com.cdsap.talaiot.publisher.Publisher
import com.cdsap.talaiot.wrotter.Writter
import org.neo4j.driver.v1.*

class Neo4jPublisher(
    private val neo4jConfiguration: Neo4JConfiguration,
    private val logTracker: LogTracker, override var fileWriter: Writter, override var fileName: String
) : GraphPublisher {

    val driver: Driver = GraphDatabase.driver(
        "bolt://localhost:7687",
        AuthTokens.basic("neo4j", "root")
    )
    val session = driver.session()

    override fun publish(measurementAggregated: TaskMeasurementAggregated) {
        buildGraph(measurementAggregated)
        session.close()
        driver.close()
    }

    override fun writeNode(internalId: Int, module: String, taskName: String, numberDependencies: Int): String {
        session.run(
            "CREATE ($internalId:Taskp {name:\"$taskName\"}) "
        )
        return ""
    }

    override fun writeEdge(
        from: Int,
        to: Int?
    ): String {
        session.run(
            "   MATCH (a:Taskp), (b:Taskp) WHERE a.name = \"$name\" AND b.name = \"${it.name}\" " +
                    "CREATE (a)-[r: xx]->(b) " +
                    "RETURN a,b"
        )
        return ""

    }
}