package com.cdsap.talaiot

import com.cdsap.talaiot.entities.NodeArgument
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
        talaiotTracker.startTrackingTask(task)
    }

    override fun afterExecute(task: Task, state: TaskState) {
        talaiotTracker.finishTrackingTask(task, state)
    }
}

