package io.github.cdsap.talaiot.metrics

import io.github.cdsap.talaiot.entities.ExecutionReport
import io.github.cdsap.talaiot.entities.TaskLength

class DefaultTaskDataProvider(
    private val task: TaskLength,
    private val report: ExecutionReport
) : ValuesProvider {
    override fun get(): Map<String, Any> {
        return mapOf(
            TaskMetrics.State.toKey() to task.state.name,
            TaskMetrics.Module.toKey() to task.module,
            TaskMetrics.RootNode.toKey() to task.rootNode.toString(),
            TaskMetrics.Task.toKey() to task.taskPath,
            TaskMetrics.Value.toKey() to task.ms,
        ) + report.customProperties.taskProperties
    }
}
