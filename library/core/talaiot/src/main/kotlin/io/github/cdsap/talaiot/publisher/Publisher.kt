package io.github.cdsap.talaiot.publisher

import io.github.cdsap.talaiot.entities.ExecutionReport

/**
 * Publisher: Contract required to publish the results of the build.
 * It contains the aggregation of the tasks and metrics calculated in the task.
 * Once the build is finished and processed TalaiotPublisher will call all the
 * implementations of Publisher.
 *
 */
interface Publisher : java.io.Serializable {
    /**
     * Publish the results given one Publisher implementation
     */
    fun publish(report: ExecutionReport)
}
