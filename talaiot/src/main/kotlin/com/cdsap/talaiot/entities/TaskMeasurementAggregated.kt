package com.cdsap.talaiot.entities

data class TaskMeasurementAggregated(
        val user: String,
        val branch: String,
        val gradleVersion: String,
        val os: String,
        val totalMemory: Long,
        val freeMemory: Long,
        val maxMemory: Long,
        val availableProcessors: Int,
        val project: String,
        val taskMeasurement: List<TaskLength>
)
