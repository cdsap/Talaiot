package com.cdsap.talaiot.entities

/**
 * Main Entity to contain the information of the Task tracked
 */
data class TaskLength(
    /**
     * Duration of the execution of the task in milliseconds
     */
    val ms: Long,
    /**
     * Task name of the task tracked
     */
    val taskName: String,
    /**
     * Task name with the path of the task tracked, it contains the modules or logic structure applied:
     * android:core:lib:assembleDebug
     */
    val taskPath: String,
    /**
     * Custom task execution result
     */
    val state: TaskMessageState,
    /**
     * In case we are executing a general task like assembleDebug, the length of the execution is not tracked, Talaiot
     * aggregates the execution of the different tasks and need to inform that a given task it was invoke as main task
     * to execute
     */
    val rootNode: Boolean = false,
    /**
     * module which the task belongs
     */
    val module: String,
    /**
     * List of dependencies required to be executed before the current task
     */
    val taskDependencies: List<String>,
    /**
     * String id of gradle worker that executed this task
     */
    val workerId: String = "",
    /**
     * Timestamp of start in millis
     */
    val startMs: Long = 0L,
    /**
     * Timestamp of finish in millis
     */
    val stopMs: Long = 0L,
    /**
     * task is on the critical path of execution
     */
    var critical: Boolean = false
)
