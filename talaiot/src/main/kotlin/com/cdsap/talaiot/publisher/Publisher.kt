package com.cdsap.talaiot.publisher

import com.cdsap.talaiot.entities.AggregatedMeasurements
import com.cdsap.talaiot.entities.DetailedMeasurements

/**
 * Publisher: Contract required to publish the results of the build.
 * It contains the aggregation of the tasks and metrics calculated in the task.
 * Once the build is finished and processed TalaiotPublisher will call all the
 * implementations of Publisher.
 *
 */
interface Publisher {
    /**
     * Publish the results given one Publisher implementation
     *
     * @param taskMeasurementAggregated: Aggregated entity with the results of the build
     */
    fun publish(measurements: AggregatedMeasurements)

    /**
     * Publish detailed measurements
     */
    fun publishDetailed(measurements: DetailedMeasurements) = Unit

    /**
     * @return true if publisher can publish filtered tasks list
     */
    fun acceptsFilteredTasks(): Boolean = true

    /**
     * @return true if publisher can publish detailed measurements
     */
    fun canPublishDetailedMeasurements(): Boolean = false
}
