package com.agoda.gradle.tracking

class Clock {
    val startedTime: Long

    init {
        startedTime = System.currentTimeMillis()
    }

    fun getTimeInMs(): Long = System.currentTimeMillis() - startedTime
}
