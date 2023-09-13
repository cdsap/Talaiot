package io.github.cdsap.talaiot.logger

/**
 * Main implementation of the [LogTracker]
 */
class LogTrackerImpl(
    /**
     * Mode applied to the logger
     */
    private val mode: LogTracker.Mode = LogTracker.Mode.INFO
) : LogTracker, java.io.Serializable {

    /**
     * Logger function that check the current node to use the output method to display the results.
     * @param tag reference log
     * @param message message to be displayed
     */
    override fun log(tag: String, message: String) {
        when (mode) {
            LogTracker.Mode.INFO -> println("[$tag]: $message")
            else -> {
            }
        }
    }

    override fun error(message: String) {
        println(message)
    }
}
