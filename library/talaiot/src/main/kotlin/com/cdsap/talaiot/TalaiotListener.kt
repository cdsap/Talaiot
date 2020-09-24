package com.cdsap.talaiot

import com.cdsap.talaiot.entities.ExecutedTasksInfo
import com.cdsap.talaiot.entities.NodeArgument
import com.cdsap.talaiot.logger.LogTrackerImpl
import com.cdsap.talaiot.provider.MetricsProvider
import com.cdsap.talaiot.provider.Provider
import com.cdsap.talaiot.provider.PublisherConfigurationProvider
import com.cdsap.talaiot.publisher.TalaiotPublisherImpl
import com.cdsap.talaiot.util.TaskAbbreviationMatcher
import com.cdsap.talaiot.util.TaskName
import org.gradle.BuildListener
import org.gradle.BuildResult
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.execution.TaskExecutionListener
import org.gradle.api.initialization.Settings
import org.gradle.api.internal.GradleInternal
import org.gradle.api.invocation.Gradle

import org.gradle.api.tasks.TaskState
import org.gradle.internal.scan.time.BuildScanBuildStartedTime
import org.gradle.internal.work.WorkerLeaseService
import org.gradle.invocation.DefaultGradle
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
    private val publisherConfigurationProvider: PublisherConfigurationProvider
) : BuildListener, TaskExecutionListener {

    private val talaiotTracker = TalaiotTracker()
    private var start: Long = 0L
    private var configurationEnd: Long? = null

    override fun settingsEvaluated(settings: Settings) {
    }

    override fun buildFinished(result: BuildResult) {
        if (shouldPublish()) {
            val end = System.currentTimeMillis()
            val logger = LogTrackerImpl(extension.logger)
            val executor = Executors.newSingleThreadExecutor()
            val heavyExecutor = Executors.newSingleThreadExecutor()

            val executedTasksInfo = tasksInfoProvider.get()
            TalaiotPublisherImpl(
                extension,
                logger,
                MetricsProvider(project, result, executedTasksInfo),
                publisherConfigurationProvider,
                executedTasksInfo
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
    private fun shouldPublish() = ((extension.ignoreWhen == null || extension.ignoreWhen?.shouldIgnore() == false)
            && talaiotTracker.isTracking)

    override fun projectsLoaded(gradle: Gradle) {
    }

    override fun buildStarted(gradle: Gradle) {
        //This never gets called because we're registering after the build has already started
    }

    override fun projectsEvaluated(gradle: Gradle) {
        start = assignBuildStarted(gradle)
        configurationEnd = System.currentTimeMillis()
        if(gradle.startParameter.isConfigureOnDemand){
            initQueue(gradle)
        } else {
            gradle.gradle.taskGraph.addTaskExecutionGraphListener {
                initQueue(gradle)
            }
        }
    }

    private fun initQueue(gradle: Gradle) {
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