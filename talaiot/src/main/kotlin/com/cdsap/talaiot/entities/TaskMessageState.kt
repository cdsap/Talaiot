package com.cdsap.talaiot.entities

/**
 * Enum that represents the state of execution of one Task. It differs from TaskState Gradle API: we track if the state
 * it has a skip-message and map it within on model. We added the Executed state.
 *
 */
enum class TaskMessageState {
    FROM_CACHE,
    NO_SOURCE,
    UP_TO_DATE,
    EXECUTED
}
