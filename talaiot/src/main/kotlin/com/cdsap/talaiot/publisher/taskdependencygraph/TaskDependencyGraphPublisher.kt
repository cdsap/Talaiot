package com.cdsap.talaiot.publisher.taskdependencygraph

import com.cdsap.talaiot.configuration.TaskDependencyGraphConfiguration
import com.cdsap.talaiot.entities.TaskMeasurementAggregated
import com.cdsap.talaiot.logger.LogTracker
import com.cdsap.talaiot.publisher.Publisher
import com.cdsap.talaiot.publisher.taskdependencygraph.composer.GexfComposer
import com.cdsap.talaiot.publisher.taskdependencygraph.composer.HtmlComposer
import com.cdsap.talaiot.publisher.taskdependencygraph.composer.Neo4JComposer
import com.cdsap.talaiot.writer.FileWriter

class TaskDependencyGraphPublisher(
    private val graphConfiguration: TaskDependencyGraphConfiguration,
    private val fileWriter: FileWriter,
    val logTracker: LogTracker
) : Publisher {

    override fun publish(measurementAggregated: TaskMeasurementAggregated) {
        graphConfiguration.apply {
            if (html) {
                HtmlComposer(logTracker, fileWriter)
                    .compose(measurementAggregated)
            }

            if (gexf) {
                GexfComposer(logTracker, fileWriter)
                    .compose(measurementAggregated)
            }

            if (neo4j) {
                Neo4JComposer(logTracker, fileWriter)
                    .compose(measurementAggregated)
            }
        }
    }
}