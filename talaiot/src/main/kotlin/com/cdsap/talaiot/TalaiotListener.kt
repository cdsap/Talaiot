package com.cdsap.talaiot

import com.cdsap.talaiot.configuration.FilterConfiguration
import com.cdsap.talaiot.entities.NodeArgument
import com.cdsap.talaiot.entities.TaskLength
import com.cdsap.talaiot.filter.StringFilter
import com.cdsap.talaiot.filter.StringFilterProcessor
import com.cdsap.talaiot.logger.LogTracker
import com.cdsap.talaiot.logger.LogTrackerImpl
import com.cdsap.talaiot.publisher.TalaiotPublisher
import org.gradle.BuildListener
import org.gradle.BuildResult
import org.gradle.api.Task
import org.gradle.api.execution.TaskExecutionListener
import org.gradle.api.initialization.Settings
import org.gradle.api.invocation.Gradle
import org.gradle.api.tasks.TaskState

/**
 * Custom listener that combines the BuildListener and TaskExecutionListener. For each Task we need to record information
 * like duration or state, it's helped by the TalaiotTracker to track the information.
 * Once the build is finished it will publish the data for all the publishers included in the configuration
 */
class TalaiotListener(
    /**
     * Talaiot Publisher with the information of metrics and publishers defined in the configuration
     */
    val talaiotPublisher: TalaiotPublisher,
    /**
     * Extension with the main configuration of the plugin
     */
    private val extension: TalaiotExtension
) : BuildListener, TaskExecutionListener {

    private val logTracker = LogTrackerImpl(LogTracker.Mode.INFO)
    private val talaiotTracker = TalaiotTracker()

    override fun settingsEvaluated(settings: Settings) {
    }

    override fun buildFinished(result: BuildResult) {
        if (shouldPublish()) {
            talaiotPublisher.publish(talaiotTracker.taskLengthList)
        }
    }

    /**
     * it checks if the executions has to be published, checking the  main ignoreWhen configuration and the
     * state of the tracker.
     */
    private fun shouldPublish() = ((extension.ignoreWhen == null || extension.ignoreWhen?.shouldIgnore() == false)
            && talaiotTracker.isTracking)

    override fun projectsLoaded(gradle: Gradle) {
    }

    override fun buildStarted(gradle: Gradle) {
    }

    override fun projectsEvaluated(gradle: Gradle) {
        gradle.startParameter.taskRequests.forEach {
            it.args.forEach {
                talaiotTracker.queue.add(NodeArgument(it, 0, 0))
            }
        }
        if (talaiotTracker.queue.isNotEmpty()) {
            talaiotTracker.initNodeArgument()
        }
    }

    override fun beforeExecute(task: Task) {
        if (taskLengthFilter(extension.filter, task.path))
            talaiotTracker.startTrackingTask(task)
    }

    override fun afterExecute(task: Task, state: TaskState) {
        if (taskLengthFilter(extension.filter, task.path))
            talaiotTracker.finishTrackingTask(task, state)
    }

    private fun taskLengthFilter(filter: FilterConfiguration?, taskPath: String): Boolean {
        var isTaskIncluded = true
        var isModuleIncluded = true
        filter?.let { filter ->

            filter.modules?.let { moduleFilter ->
                isModuleIncluded = executeFilterProcessor(moduleFilter, getModule(taskPath))
            }
            filter.tasks?.let { taskFilter ->
                isTaskIncluded = executeFilterProcessor(taskFilter, taskPath)
            }
        }
        return isTaskIncluded && isModuleIncluded
    }

    private fun getModule(path: String): String {
        val module = path.split(":")

        return if (module.size > 2) module.toList()
            .dropLast(1)
            .joinToString(separator = ":")
        else "no_module"
    }

    private fun executeFilterProcessor(
        filter: StringFilter, argument: String
    ): Boolean {
        return with(StringFilterProcessor(filter, logTracker)) {
            matches(argument)
        }
    }
}

