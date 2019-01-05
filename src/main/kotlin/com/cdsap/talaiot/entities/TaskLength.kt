package com.cdsap.talaiot.entities

data class TaskLength(
    val ms: Long,
    val path: String,
    val state: TaskMessageState
)
