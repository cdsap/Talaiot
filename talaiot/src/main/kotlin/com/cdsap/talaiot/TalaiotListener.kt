package com.cdsap.talaiot

import com.cdsap.talaiot.entities.NodeArgument
import com.cdsap.talaiot.logger.LogTrackerImpl
import com.cdsap.talaiot.provider.DetailedProvider
import com.cdsap.talaiot.provider.MetricsProvider
import com.cdsap.talaiot.provider.PublishersProvider
import com.cdsap.talaiot.publisher.TalaiotPublisherImpl
import org.gradle.BuildListener
import org.gradle.BuildResult
import org.gradle.api.Project
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
     * Gradle project required to access the properties
     */
    val project: Project,
    /**
     * Extension with the main configuration of the plugin
     */
    private val extension: TalaiotExtension
) : BuildListener, TaskExecutionListener {

    private val talaiotTracker = TalaiotTracker()

    // detailed provider has to be instantiated early
    private val detailedProvider = DetailedProvider(project)

    override fun settingsEvaluated(settings: Settings) {
    }

    override fun buildFinished(result: BuildResult) {
        if (shouldPublish()) {
            val logger = LogTrackerImpl(extension.logger)
            TalaiotPublisherImpl(
                extension,
                logger,
                MetricsProvider(project),
                detailedProvider,
                PublishersProvider(project, logger)
            ).publish(talaiotTracker.taskLengthList)
        }
    }

    /**
     * it checks if the executions has to be published, checking the  main ignoreWhen configuration and the
     * state of the tracker
     */
    private fun shouldPublish() = ((extension.ignoreWhen == null || extension.ignoreWhen?.shouldIgnore() == false)
            && talaiotTracker.isTracking)

    override fun projectsLoaded(gradle: Gradle) {
    }

    override fun buildStarted(gradle: Gradle) {
    }

    override fun projectsEvaluated(gradle: Gradle) {
        gradle.startParameter.taskRequests.forEach {
            it.args.forEach { task ->
                talaiotTracker.queue.add(NodeArgument(task, 0, 0))
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

