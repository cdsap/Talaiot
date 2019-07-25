package com.cdsap.talaiot.logger

/**
 * Main implementation of the [LogTracker]
 */
class LogTrackerImpl(
    /**
     * Mode applied to the logger
     */
    private val mode: LogTracker.Mode = LogTracker.Mode.INFO
) : LogTracker {


    /**
     * Logger function that check the current node to use the output method to display the results.
     * @param message message to be displayed
     */
    override fun log(message: String) {
        when (mode) {
            LogTracker.Mode.INFO -> println(message)
            else -> {
            }
        }
    }
}