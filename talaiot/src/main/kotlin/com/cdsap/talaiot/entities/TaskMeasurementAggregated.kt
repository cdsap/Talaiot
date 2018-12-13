package com.cdsap.talaiot.entities

data class TaskMeasurementAggregated(
    val values: Map<String, String>,
    val taskMeasurement: List<TaskLength>
)
