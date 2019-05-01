package com.cdsap.talaiot.publisher

import com.cdsap.talaiot.entities.TaskMeasurementAggregated

/**
 * Publisher: Contract required to publish the results of the build.
 * It contains the aggregation of the tasks and metrics calculated in the task.
 * Once the build is finished and processed TalaiotPublisher will call all the
 * implementations of Publisher.
 *
 * @see TalaiotPublisher
 */
interface Publisher {
    fun publish(taskMeasurementAggregated: TaskMeasurementAggregated)
}
