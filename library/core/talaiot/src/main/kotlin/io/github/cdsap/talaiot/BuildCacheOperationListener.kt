package io.github.cdsap.talaiot

import io.github.cdsap.talaiot.entities.CacheInfo
import io.github.cdsap.talaiot.entities.ExecutedGradleTaskInfo
import io.github.cdsap.talaiot.entities.ExecutedTasksInfo
import io.github.cdsap.talaiot.provider.Provider
import org.gradle.api.internal.tasks.execution.ExecuteTaskBuildOperationType
import org.gradle.caching.internal.operations.BuildCacheRemoteLoadBuildOperationType
import org.gradle.internal.operations.BuildOperationDescriptor
import org.gradle.internal.operations.BuildOperationListener
import org.gradle.internal.operations.OperationFinishEvent
import org.gradle.internal.operations.OperationIdentifier
import org.gradle.internal.operations.OperationProgressEvent
import org.gradle.internal.operations.OperationStartEvent
import java.util.concurrent.ConcurrentHashMap

internal class BuildCacheOperationListener : BuildOperationListener, Provider<ExecutedTasksInfo> {
    private val taskCacheDownloadResults = ConcurrentHashMap<OperationIdentifier, BuildCacheRemoteLoadBuildOperationType.Result>()
    private val tasksMap = ConcurrentHashMap<OperationIdentifier, TaskExecutionResults>()
    override fun get(): ExecutedTasksInfo {
        val tasksList = tasksMap.map { (taskIdentifier, executionResult) ->
            val isCacheEnabled = executionResult.result.cachingDisabledReasonCategory == null
            val remoteCacheInfo = computeCacheInfo(isCacheEnabled, taskCacheDownloadResults[taskIdentifier]?.isHit)

            val isLocalCacheHit = if (isCacheEnabled) {
                remoteCacheInfo !is CacheInfo.CacheHit && executionResult.result.skipMessage == "FROM-CACHE"
            } else {
                null
            }
            val localCacheInfo = computeCacheInfo(isCacheEnabled, isLocalCacheHit)

            executionResult.name to ExecutedGradleTaskInfo(
                taskIdentifier.id,
                executionResult.name,
                isCacheEnabled,
                localCacheInfo = localCacheInfo,
                remoteCacheInfo = remoteCacheInfo
            )
        }.toMap()
        return ExecutedTasksInfo(tasksList)
    }

    private fun computeCacheInfo(cacheEnabled: Boolean, cacheHit: Boolean?): CacheInfo {
        return if (cacheEnabled) {
            if (cacheHit == true) {
                CacheInfo.CacheHit
            } else {
                CacheInfo.CacheMiss
            }
        } else {
            CacheInfo.CacheDisabled
        }
    }

    override fun progress(identifier: OperationIdentifier, progressEvent: OperationProgressEvent) {}

    override fun finished(descriptor: BuildOperationDescriptor, finishEvent: OperationFinishEvent) {
        when (val result = finishEvent.result) {
            is ExecuteTaskBuildOperationType.Result -> {
                descriptor.id?.let {
                    tasksMap[it] =
                        TaskExecutionResults(descriptor.name, result)
                }
            }
            is BuildCacheRemoteLoadBuildOperationType.Result -> {
                taskCacheDownloadResults[descriptor.parentId!!] = result
            }
        }
    }

    override fun started(descriptor: BuildOperationDescriptor, startEvent: OperationStartEvent) {
    }
}

private data class TaskExecutionResults(val name: String, val result: ExecuteTaskBuildOperationType.Result)
