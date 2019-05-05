package com.cdsap.talaiot.configuration

/**
 * Configuration to specify thresholds to allow more flexibility at time which tasks we want to process in the
 * publishers
 * Used initially in the InfluxDbPublisher
 */
class ThresholdConfiguration {
    /**
     * Minimum execution time of one task to be consider valid to be reported
     */
    var minExecutionTime: Long? = null
    /**
     * Maximum execution time of one task to be consider valid to be reported
     */
    var maxExecutionTime: Long? = null
}
