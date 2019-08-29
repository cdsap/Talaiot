package com.cdsap.talaiot.logger

object TestLogTrackerRecorder : LogTracker {


    private val logs = mutableListOf<String>()
    override fun log(tag: String, message: String) {
        logs.add(message)
    }

    override fun error(message: String) {
        logs.add(message)
    }

    fun containsLog(message: String) = logs.contains(message)
}