package io.github.cdsap.talaiot.publisher.timeline

import io.github.cdsap.talaiot.entities.TaskMessageState

data class TimelineTaskMeasurement(
    val taskPath: String,
    val stateType: TaskMessageState,
    val critical: Boolean,
    val start: Long,
    val worker: String,
    val end: Long
)
