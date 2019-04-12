package com.cdsap.talaiot.publisher.taskDependencyGraph

import com.cdsap.talaiot.entities.TaskLength

data class TaskDependencyNode(val taskLength: TaskLength, val internalId: Int) {
    var module: String = ""
}