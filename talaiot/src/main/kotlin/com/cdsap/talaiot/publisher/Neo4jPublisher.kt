package com.cdsap.talaiot.publisher

import com.cdsap.talaiot.configuration.Neo4JConfiguration
import com.cdsap.talaiot.entities.TaskMeasurementAggregated
import com.cdsap.talaiot.logger.LogTracker
import org.neo4j.driver.v1.*

class Neo4jPublisher(
    private val neo4jConfiguration: Neo4JConfiguration,
    private val logTracker: LogTracker
) : Publisher {
    val graphTaskc = mutableSetOf<GraphTask>()

    override fun publish(measurementAggregated: TaskMeasurementAggregated) {

        val config = Config.defaultConfig()

        val driver = GraphDatabase.driver(
            "bolt://localhost:7687",
            AuthTokens.basic("neo4j", "root")
        )
        measurementAggregated.taskMeasurement.forEach {
            println(it.taskPath)
            it.taskDependencies.forEach {
                println("++$it")
            }

        }

        val transactionWork = TransactionWork { tx: Transaction? ->
            tx.run {
                "CREATE (a:SALSALSAL { Name : \"LPSALPSPALSAPLSALPSAPLAS Young Lad\" }) "

            }

        }

        measurementAggregated.taskMeasurement.forEach {
            var graphTask = GraphTask(name = it.taskPath)
            graphTaskc.add(graphTask)
            //  println(it.taskPath)

            var dependencies = mutableSetOf<GraphTask>()
            it.taskDependencies.forEach {
                val a = it
                val fd = graphTaskc.find { it.name == a }
                fd?.let {
                    dependencies.add(it)
                }
                println("++$it")
            }
            val a = it
            val fa = graphTaskc.find { it.name == a.taskPath }


            fa?.let {
                fa.dependencies = dependencies.toList()


                println("1")
                println(fa.name)
                fa.dependencies.forEach {
                    println("2")
                    println("========${it.name}")

                }
                //session.save(fa)
            }
        }
        val session = driver.session()


        graphTaskc.forEach {
            session.run(
                "CREATE (${it.name}:TaskC {name:\"${it.name}\"}) ")
        }

        graphTaskc.forEach {
            val name = it.name
            it.dependencies.forEach {
                session.run("   MATCH (a:TaskC), (b:TaskC) WHERE a.name = \"$name\" AND b.name = \"${it.name}\" "+
                "CREATE (a)-[r: xx]->(b) "+
                "RETURN a,b")
            }

        }
        session.close()
        driver.close()
    }


}

data class GraphTask(
    val name: String,
    var dependencies: List<GraphTask> = emptyList()
)