package com.cdsap.talaiot.metrics

import com.cdsap.talaiot.entities.TaskLength

class DefaultTaskDataProvider(
    private val task: TaskLength
) : ValuesProvider {
    override fun get(): Map<String, Any> = mapOf(
        "state" to task.state.name,
        "module" to task.module,
        "rootNode" to task.rootNode.toString(),
        "task" to task.taskPath,
        "workerId" to task.workerId,
        "critical" to task.critical.toString(),
        "value" to task.ms
    )
}
