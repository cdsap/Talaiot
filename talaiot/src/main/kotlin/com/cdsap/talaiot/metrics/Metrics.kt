package com.cdsap.talaiot.metrics

/**
 * Main interface for the Metrics measured during the build. All the different metrics implement this interface
 * and will be collected at the end of the execution
 */
interface Metrics {
    /**
     * Getter for the metrics defined in the implementation of the interface
     *
     * @return collection of Pair of Strings
     */
    fun get(): Map<String, String>
}