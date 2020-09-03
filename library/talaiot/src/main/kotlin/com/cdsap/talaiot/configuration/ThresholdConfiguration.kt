package com.cdsap.talaiot.configuration

/**
 * Task filtering configuration
 * If specified Talaiot will report only tasks that are between [minExecutionTime] and [maxExecutionTime]
 */
class ThresholdConfiguration {
    /**
     * Minimum execution time of one task to be consider valid to be reported
     */
    var minExecutionTime: Long = 0
        set(value) {
            field = if (value < 0) {
                0
            } else {
                value
            }
        }

    /**
     * Maximum execution time of one task to be consider valid to be reported
     */
    var maxExecutionTime: Long = Long.MAX_VALUE
        set(value) {
            field = if (value < 0) {
                Long.MAX_VALUE
            } else {
                value
            }
        }
}
