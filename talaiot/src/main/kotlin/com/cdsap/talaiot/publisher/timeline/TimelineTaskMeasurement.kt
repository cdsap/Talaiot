package com.cdsap.talaiot.publisher.timeline

import com.cdsap.talaiot.entities.TaskMessageState

data class TimelineTaskMeasurement(
    val taskPath: String,
    val stateType: TaskMessageState,
    val critical: Boolean,
    val start: Long,
    val worker: String,
    val end: Long
)
