package com.cdsap.talaiot.logger

class LogTrackerImpl(private val mode: LogTracker.Mode) : LogTracker {


    override fun log(message: String) {
        when (mode) {
            LogTracker.Mode.INFO -> println(message)
            else -> {
            }
        }
    }
}