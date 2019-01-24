package com.cdsap.talaiot

import com.cdsap.talaiot.entities.TaskLength
import com.cdsap.talaiot.entities.TaskMessageState
import com.cdsap.talaiot.metrics.BaseMetrics

import com.cdsap.talaiot.metrics.MetricsParser
import com.nhaarman.mockitokotlin2.mock
import io.kotlintest.specs.BehaviorSpec
import org.gradle.BuildResult


class AggregateDataTest : BehaviorSpec({
    given("Instance of AggregateData") {
        val result: BuildResult = mock()
        val listTaskLength = mutableListOf(
            TaskLength(10L, ":taskA", TaskMessageState.NO_MESSAGE_STATE),
            TaskLength(20L, ":taskB", TaskMessageState.FROM_CACHE)
        )
        val metricsParser = MetricsParser(listOf(BaseMetrics(result)))

        `when`("We build the the aggregation object") {
            val aggregateData = AggregateData(listTaskLength, metricsParser)
            then("an Instance of TaskMeasurementAggregated includes the metrics and tasks") {
                assert(aggregateData.build().taskMeasurement.size == 2)
                assert(aggregateData.build().values.containsKey("project"))
                assert(aggregateData.build().values.containsKey("talaiotVersion"))
            }
        }

    }
})