package com.cdsap.talaiot.logger

class LogTracker(private val mode: Mode) {

    enum class Mode {
        SILENT,
        INFO
    }

    fun log(message: String) {
        when (mode) {
            Mode.INFO -> println(message)
            else -> {
            }
        }
    }
}