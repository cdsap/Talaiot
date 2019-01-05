package com.cdsap.talaiot.request

import com.cdsap.talaiot.logger.LogTracker

interface Request {
    var logTracker: LogTracker

    fun send(url: String, content: String)
}
