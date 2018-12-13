package com.cdsap.talaiot.entities

class Clock {
    private val startedTime: Long = System.currentTimeMillis()

    fun getTimeInMs(): Long = System.currentTimeMillis() - startedTime
}
