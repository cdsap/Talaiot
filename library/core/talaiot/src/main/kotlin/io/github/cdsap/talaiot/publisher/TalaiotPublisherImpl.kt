package io.github.cdsap.talaiot.publisher


import io.github.cdsap.talaiot.TalaiotExtension
import io.github.cdsap.talaiot.configuration.BuildFilterConfiguration
import io.github.cdsap.talaiot.entities.CacheInfo
import io.github.cdsap.talaiot.entities.ExecutedTasksInfo
import io.github.cdsap.talaiot.entities.ExecutionReport
import io.github.cdsap.talaiot.entities.TaskLength
import io.github.cdsap.talaiot.filter.BuildFilterProcessor
import io.github.cdsap.talaiot.filter.TaskFilterProcessor
import io.github.cdsap.talaiot.logger.LogTracker
import io.github.cdsap.talaiot.provider.Provider
import io.github.cdsap.talaiot.provider.PublisherConfigurationProvider

/**
 * Implementation of TalaiotPublisher.
 * It will retrieve all the metrics trough the MetricsProvider and the Publishers defined in the configuration
 * trough the PublisherProvider.
 * At the publishing phase it will aggregate the data of in a TaskMeasurementAggregated to publish the result
 * on each publisher retrieved.
 * Before the publishing phase we will apply the TaskFilterProcessor. Filtering doesn't apply to
 * the TaskDependencyGraphPublisher
 */
class TalaiotPublisherImpl(
    extension: TalaiotExtension,
    logger:LogTracker,
    private val metricsProvider: Provider<ExecutionReport>,
    private val publisherProvider: PublisherConfigurationProvider,
    private val executedTasksInfo: ExecutedTasksInfo
) : TalaiotPublisher {
    private val taskFilterProcessor: TaskFilterProcessor = TaskFilterProcessor(logger, extension.filter)
    private val buildFilterProcessor: BuildFilterProcessor = BuildFilterProcessor(logger, extension.filter?.build ?: BuildFilterConfiguration())

    override fun publish(
        taskLengthList: MutableList<TaskLength>,
        start: Long,
        configuraionMs: Long?,
        end: Long,
        success: Boolean
    ) {
        val tasksLengthWithCacheInfo = addCacheInfoToTaskLength(taskLengthList, executedTasksInfo)
        val report = metricsProvider.get().apply {
            tasks = tasksLengthWithCacheInfo.filter { taskFilterProcessor.taskLengthFilter(it) }
            unfilteredTasks = tasksLengthWithCacheInfo
            this.beginMs = start.toString()
            this.endMs = end.toString()
            this.success = success

            this.durationMs = (end - start).toString()

            this.configurationDurationMs = when {
                configuraionMs != null -> (configuraionMs - start).toString()
                else -> "undefined"
            }

            this.estimateCriticalPath()
        }

        if (buildFilterProcessor.shouldPublishBuild(report)) {
            publisherProvider.get().forEach {
                it.publish(report)
            }
        }
    }

    private fun addCacheInfoToTaskLength(
        taskLengthList: MutableList<TaskLength>,
        executedTasksInfo: ExecutedTasksInfo
    ): List<TaskLength> {
        return taskLengthList.map { taskLength ->
            executedTasksInfo.executedTasksInfo[taskLength.taskPath]?.let { cacheInfo ->
                taskLength.copy(
                    isCacheEnabled = cacheInfo.isCacheEnabled,
                    isLocalCacheHit = cacheInfo.localCacheInfo is CacheInfo.CacheHit,
                    isLocalCacheMiss = cacheInfo.localCacheInfo is CacheInfo.CacheMiss,
                    isRemoteCacheHit = cacheInfo.remoteCacheInfo is CacheInfo.CacheHit,
                    isRemoteCacheMiss = cacheInfo.remoteCacheInfo is CacheInfo.CacheMiss
                )
            } ?: taskLength
        }
    }
}