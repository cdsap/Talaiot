package io.github.cdsap.talaiot.request

import io.github.cdsap.talaiot.logger.LogTracker

/**
 * Type representing the action to send content to an url
 */
interface Request {
    /**
     * LogTracker to print in console depending on the Mode
     */
    var logTracker:LogTracker

    fun send(url: String, content: String)
}
