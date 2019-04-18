package com.cdsap.talaiot.publisher.taskdependencygraph.composer


import com.cdsap.talaiot.entities.TaskMeasurementAggregated
import com.cdsap.talaiot.logger.LogTracker
import com.cdsap.talaiot.publisher.taskdependencygraph.resources.ResourcesGexf
import com.cdsap.talaiot.writer.FileWriter
import org.neo4j.driver.v1.AuthTokens
import org.neo4j.driver.v1.Driver
import org.neo4j.driver.v1.GraphDatabase

class Neo4JComposer(
    override var logTracker: LogTracker,
    override var fileWriter: FileWriter
) : ContentComposer {
    private val fileName: String = ""
    private val driver: Driver = GraphDatabase.driver(
        "bolt://localhost:7687",
        AuthTokens.basic("neo4j", "root")
    )
    val session = driver.session()

    override fun compose(measurementAggregated: TaskMeasurementAggregated) {

        val content = contentComposer(
            buildGraph(measurementAggregated), ResourcesGexf.HEADER,
            ResourcesGexf.FOOTER
        )
        writeFile(content, fileName)
        session.close()
        driver.close()
    }

    override fun formatNode(internalId: Int, module: String, taskName: String, numberDependencies: Int): String =
        session
            .run(write("CREATE ($internalId:Task {id:\"$internalId\", name:\"$taskName\"})"))
            .toString()


    override fun formatEdge(
        from: Int,
        to: Int?
    ) = session
        .run(
            write(
                " MATCH (a:Task), (b:Task) WHERE a.id = \"$from\" AND b.id = \"${to}\" " +
                        "CREATE (a)-[r: xx]->(b) " +
                        "RETURN a,b"
            )
        )
        .toString()
}
