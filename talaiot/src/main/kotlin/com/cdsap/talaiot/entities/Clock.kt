package com.cdsap.talaiot.entities

class Clock {
    val startedTime: Long

    init {
        startedTime = System.currentTimeMillis()
    }

    fun getTimeInMs(): Long = System.currentTimeMillis() - startedTime
}
