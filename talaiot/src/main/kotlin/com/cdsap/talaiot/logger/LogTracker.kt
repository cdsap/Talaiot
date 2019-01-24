package com.cdsap.talaiot.logger

interface LogTracker {
    enum class Mode {
        SILENT,
        INFO
    }

    fun log(message: String)
}