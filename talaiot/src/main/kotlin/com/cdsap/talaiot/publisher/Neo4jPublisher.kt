package com.cdsap.talaiot.publisher

import com.cdsap.talaiot.configuration.Neo4JConfiguration
import com.cdsap.talaiot.entities.TaskMeasurementAggregated
import com.cdsap.talaiot.logger.LogTracker
import org.neo4j.driver.v1.*

class Neo4jPublisher(
    private val neo4jConfiguration: Neo4JConfiguration,
    private val logTracker: LogTracker
) : Publisher {
    val graphTaskM = mutableSetOf<GraphTask>()

    override fun publish(measurementAggregated: TaskMeasurementAggregated) {

        val config = Config.defaultConfig()

//        val driver = GraphDatabase.driver(
//            "bolt://localhost:7687",
//            AuthTokens.basic("neo4j", "root")
//        )
        measurementAggregated.taskMeasurement.forEach {
            println(it.taskPath)
            it.taskDependencies.forEach {
                println("++$it")
            }

        }

//        val transactionWork = TransactionWork { tx: Transaction? ->
//            tx.run {
//                "CREATE (a:SALSALSAL { Name : \"LPSALPSPALSAPLSALPSAPLAS Young Lad\" }) "
//
//            }
//
//        }

        measurementAggregated.taskMeasurement.forEach {
            var graphTask = GraphTask(name = it.taskPath)
            graphTaskM.add(graphTask)
            //  println(it.taskPath)

            var dependencies = mutableSetOf<GraphTask>()
            it.taskDependencies.forEach {
                val a = it
                val fd = graphTaskM.find { it.name == a }
                fd?.let {
                    dependencies.add(it)
                }
                println("++$it")
            }
            val a = it
            val fa = graphTaskM.find { it.name == a.taskPath }


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
        //    val session = driver.session()


        var count = 0
        var xx = hashMapOf<String, Int>()
        graphTaskM.forEach {
            if (it.name.contains("presentation:app")) {
                println("nodes.push({id: $count, label: '${it.name}'});")
                xx[it.name] = count
                count++
            }
        }


        graphTaskM.forEach {
            val name = it.name
            it.dependencies.forEach {
                val temp = it.name.split(":").dropLast(1)
                var module = ""
                temp.forEach {
                    module += "it:"
                }

                if (name.contains("presentation:app")) {

                    println("edges.push({from: ${xx[name]}, to: ${xx[it.name]}});")
                }
            }

        }

//        graphTaskM.forEach {
//            session.run(
//                "CREATE (${it.name}:Taskp {name:\"${it.name}\"}) "
//            )
//        }
//
//        graphTaskM.forEach {
//            val name = it.name
//            it.dependencies.forEach {
//                println(
//                    "MATCH (a:Taskp), (b:Taskp) WHERE a.name = \\\"$name\\\" AND b.name = \\\"${it.name}\\\" \"+\n" +
//                            "                \"CREATE (a)-[r: xx]->(b) \"+\n" +
//                            "                \"RETURN a,b\""
//                )
//                session.run(
//                    "   MATCH (a:Taskp), (b:Taskp) WHERE a.name = \"$name\" AND b.name = \"${it.name}\" " +
//                            "CREATE (a)-[r: xx]->(b) " +
//                            "RETURN a,b"
//                )
//            }
//
//        }
//        session.close()
//        driver.close()
    }


}

data class GraphTask(
    val name: String,
    var dependencies: List<GraphTask> = emptyList()
)