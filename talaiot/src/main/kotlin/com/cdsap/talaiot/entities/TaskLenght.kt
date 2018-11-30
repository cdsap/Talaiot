package com.cdsap.talaiot.entities

data class TaskLenght(
    val ms: Long,
    val path: String,
    val state: TaskMessageState
)

enum class TaskMessageState {
    FROM_CACHE,
    NO_SOURCE,
    UP_TO_DATE,
    NO_MESSAGE_STATE
}
