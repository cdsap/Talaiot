package com.cdsap.talaiot.entities

/**
 *  Wrapper of [TaskLength], adding an [Int] to be identified in a HashTables
 */
data class TaskDependencyNode(
    /**
     * Task wrapped
     */
    val taskLength: TaskLength,
    /**
     * Internal Id of the tasks used in the Hash
     */
    val internalId: Int
)