package com.cdsap.talaiot.metrics

import com.cdsap.talaiot.entities.ExecutionReport
import com.cdsap.talaiot.entities.TaskLength

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
            TaskMetrics.WorkerId.toKey() to task.workerId,
            TaskMetrics.Critical.toKey() to task.critical.toString(),
            TaskMetrics.Value.toKey() to task.ms,
            TaskMetrics.CacheEnabled.toKey() to task.isCacheEnabled,
            TaskMetrics.LocalCacheHit.toKey() to task.isLocalCacheHit,
            TaskMetrics.LocalCacheMiss.toKey() to task.isLocalCacheMiss,
            TaskMetrics.RemoteCacheHit.toKey() to task.isRemoteCacheHit,
            TaskMetrics.RemoteCacheMiss.toKey() to task.isRemoteCacheMiss

        ) + report.customProperties.taskProperties
    }
}
