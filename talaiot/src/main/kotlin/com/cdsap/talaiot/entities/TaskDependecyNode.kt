package com.cdsap.talaiot.entities

import com.cdsap.talaiot.entities.TaskLength

data class TaskDependencyNode(val taskLength: TaskLength, val internalId: Int) {
    var module: String = ""
}