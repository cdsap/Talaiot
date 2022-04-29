package io.github.cdsap.talaiot

import io.github.cdsap.talaiot.configuration.BuildFilterConfiguration
import io.github.cdsap.talaiot.entities.ExecutedTasksInfo
import io.github.cdsap.talaiot.entities.ExecutionReport
import io.github.cdsap.talaiot.entities.NodeArgument
import io.github.cdsap.talaiot.filter.BuildFilterProcessor
import io.github.cdsap.talaiot.filter.TaskFilterProcessor
import io.github.cdsap.talaiot.logger.LogTrackerImpl
import io.github.cdsap.talaiot.metrics.base.Metric
import io.github.cdsap.talaiot.provider.MetricsPostBuildProvider
import io.github.cdsap.talaiot.provider.Provider
import io.github.cdsap.talaiot.provider.PublisherConfigurationProvider
import io.github.cdsap.talaiot.publisher.TalaiotPublisherImpl
import io.github.cdsap.talaiot.util.TaskAbbreviationMatcher
import io.github.cdsap.talaiot.util.TaskName
import org.gradle.BuildListener
import org.gradle.BuildResult
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.execution.TaskExecutionListener
import org.gradle.api.initialization.Settings
import org.gradle.api.internal.GradleInternal
import org.gradle.api.invocation.Gradle
import org.gradle.api.tasks.TaskState
import org.gradle.internal.InternalBuildListener
import org.gradle.internal.scan.time.BuildScanBuildStartedTime
import org.gradle.internal.work.WorkerLeaseService
import org.gradle.invocation.DefaultGradle
import java.lang.IllegalStateException
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

/**
 * Custom listener that combines the [BuildListener] and [TaskExecutionListener]. For each [Task] we need to
 * store information (such as duration and state) in [TalaiotTracker].
 *
 * Upon completion of the build this listener will publish the execution data using the publishers specified
 * via the configuration
 */
class TalaiotListener(
    /**
     * Gradle project that this listener is attached to
     *
     * Might be needed by metrics and/or providers
     */
    val project: Project,
    /**
     * Talaiot plugin extension. Contains main configuration of the plugin
     */
    private val extension: TalaiotExtension,
    private val tasksInfoProvider: Provider<ExecutedTasksInfo>,
    private val publisherConfigurationProvider: PublisherConfigurationProvider,
    private val metrics: List<Metric<*, *>>,
    private val executionReport: ExecutionReport
) : InternalBuildListener, TaskExecutionListener {

    private val talaiotTracker = TalaiotTracker()
    private var start: Long = 0L
    private var configurationEnd: Long? = null
    val logger = LogTrackerImpl(extension.logger)

    override fun settingsEvaluated(settings: Settings) {
    }

    override fun buildFinished(result: BuildResult) {
        println("buildFinished")
        project.gradle.buildOperationListenerManager().removeListener(tasksInfoProvider as BuildCacheOperationListener)

        if (shouldPublish()) {
            val end = System.currentTimeMillis()

            val executor = Executors.newSingleThreadExecutor()
            val heavyExecutor = Executors.newSingleThreadExecutor()
            val taskFilterProcessor = TaskFilterProcessor(logger, extension.filter)
            val buildFilterProcessor =
                BuildFilterProcessor(logger, extension.filter?.build ?: BuildFilterConfiguration())

            val executedTasksInfo = tasksInfoProvider.get()
            TalaiotPublisherImpl(
                MetricsPostBuildProvider(result, executedTasksInfo, metrics, executionReport, project),
                publisherConfigurationProvider,
                executedTasksInfo,
                taskFilterProcessor,
                buildFilterProcessor
            ).publish(
                taskLengthList = talaiotTracker.taskLengthList,
                success = result.success(),
                start = start,
                configuraionMs = configurationEnd,
                end = end
            )

            try {
                executor.shutdown()
                while (!executor.awaitTermination(3L, TimeUnit.SECONDS)) {
                    logger.log("Talaiot", "Shutting down executor. Not yet. Still waiting for termination")
                }
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
    }

    /**
     * This method checks if the executions has to be published by checking the main [TalaiotExtension.ignoreWhen]
     * configuration and the state of the [TalaiotTracker]
     */
    private fun shouldPublish() = (
            (extension.ignoreWhen == null || extension.ignoreWhen?.shouldIgnore() == false) &&
                    talaiotTracker.isTracking
            )

    override fun projectsLoaded(gradle: Gradle) {
    }

    override fun projectsEvaluated(gradle: Gradle) {
        start = assignBuildStarted(gradle)
        configurationEnd = System.currentTimeMillis()
        if (gradle.startParameter.isConfigureOnDemand) {
            initQueue(gradle)
        } else {
            gradle.gradle.taskGraph.addTaskExecutionGraphListener {
                initQueue(gradle)
            }
        }
    }

    private fun initQueue(gradle: Gradle) {
        try {
            val executedTasks = gradle.taskGraph.allTasks.map { TaskName(name = it.name, path = it.path) }
            val taskAbbreviationMatcher = TaskAbbreviationMatcher(executedTasks)
            gradle.startParameter.taskRequests.forEach {
                it.args.forEach { task ->
                    talaiotTracker.queue.add(NodeArgument(taskAbbreviationMatcher.findRequestedTask(task), 0, 0))
                }
            }
            if (talaiotTracker.queue.isNotEmpty()) {
                talaiotTracker.initNodeArgument()
            }
        } catch (e: IllegalStateException) {
            logger.log("Talaiot", "Tracking not available because ${e.message}")
        }
    }

    private fun assignBuildStarted(gradle: Gradle): Long {
        val buildStartedTimeService = (gradle as GradleInternal).services.get(BuildScanBuildStartedTime::class.java)
        return buildStartedTimeService?.let { it.buildStartedTime } ?: System.currentTimeMillis()
    }

    override fun beforeExecute(task: Task) {
        talaiotTracker.startTrackingTask(task)
    }

    override fun afterExecute(task: Task, state: TaskState) {
        val currentWorkerLease =
            (task.project.gradle as DefaultGradle).services.get(WorkerLeaseService::class.java).currentWorkerLease
        val workerName = currentWorkerLease.displayName
        talaiotTracker.finishTrackingTask(task, state, workerName)
    }
}

private fun BuildResult.success() = when {
    failure != null -> false
    else -> true
}
