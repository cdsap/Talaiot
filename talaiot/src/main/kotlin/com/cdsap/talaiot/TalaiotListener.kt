package com.cdsap.talaiot

import com.cdsap.talaiot.publisher.TalaiotPublisher
import org.gradle.BuildListener
import org.gradle.BuildResult
import org.gradle.api.Task
import org.gradle.api.execution.TaskExecutionListener
import org.gradle.api.initialization.Settings
import org.gradle.api.invocation.Gradle
import org.gradle.api.tasks.TaskState


class TalaiotListener(
    val talaiotPublisher: TalaiotPublisher,
    private val extension: TalaiotExtension
) : BuildListener, TaskExecutionListener {

    private val talaiotTracker = TalaiotTracker()

    override fun settingsEvaluated(settings: Settings) {
    }

    override fun buildFinished(result: BuildResult) {
        if (extension.ignoreWhen == null || extension.ignoreWhen?.shouldIgnore() == false) {
            talaiotPublisher.publish(talaiotTracker.taskLengthList)
        }
    }

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
        talaiotTracker.initNodeArgument()
    }

    override fun beforeExecute(task: Task) {
        talaiotTracker.startTrackingTask(task)
    }

    override fun afterExecute(task: Task, state: TaskState) {
        talaiotTracker.finishTrackingTask(task, state)
    }
}

