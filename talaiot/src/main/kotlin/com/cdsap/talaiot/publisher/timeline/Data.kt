package com.malinskiy.marathon.report.debug.timeline

import com.cdsap.talaiot.entities.TaskMessageState

data class Data(val taskPath: String,
                val stateType: TaskMessageState,
                val critical: Boolean,
                val start: Long,
                val worker: String,
                val end: Long)
