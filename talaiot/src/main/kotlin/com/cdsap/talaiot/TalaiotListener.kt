package com.cdsap.talaiot

import com.cdsap.talaiot.entities.TaskLength
import com.cdsap.talaiot.entities.TaskMessageState
import com.cdsap.talaiot.publisher.TalaiotPublisher
import org.gradle.BuildListener
import org.gradle.BuildResult
import org.gradle.api.Task
import org.gradle.api.execution.TaskExecutionListener
import org.gradle.api.initialization.Settings
import org.gradle.api.invocation.Gradle
import org.gradle.api.tasks.TaskState


class TalaiotListener(
    private val talaiotPublisher: TalaiotPublisher,
    private val extension: TalaiotExtension
) : BuildListener, TaskExecutionListener {

    private val taskLenghtList = mutableListOf<TaskLength>()
    private var listOfTasks: HashMap<String, Long> = hashMapOf()

    override fun settingsEvaluated(settings: Settings) {
    }

    override fun buildFinished(result: BuildResult) {
        if (extension.ignoreWhen == null || extension.ignoreWhen?.shouldIgnore() == false) {
            updateTotalBuild()
            talaiotPublisher.publish(taskLenghtList)
        }
    }

    override fun projectsLoaded(gradle: Gradle) {
    }

    override fun buildStarted(gradle: Gradle) {
    }

    override fun projectsEvaluated(gradle: Gradle) {
        listOfTasks[":total"] = System.currentTimeMillis()
    }

    override fun beforeExecute(task: Task) {
        listOfTasks[task.path] = System.currentTimeMillis()
    }

    override fun afterExecute(task: Task, state: TaskState) {
        val time = System.currentTimeMillis() - (listOfTasks[task.path] as Long)
        taskLenghtList.add(
            TaskLength(
                ms = time,
                taskName = task.path,
                state = when (state.skipMessage) {
                    "UP-TO-DATE" -> TaskMessageState.UP_TO_DATE
                    "FROM-CACHE" -> TaskMessageState.FROM_CACHE
                    "NO-SOURCE" -> TaskMessageState.NO_SOURCE
                    else -> TaskMessageState.EXECUTED
                }
            )
        )
    }

    private fun updateTotalBuild() {
        if (taskLenghtList.size > 1) {
            val aggregatorTask =
                taskLenghtList.last().copy(ms = System.currentTimeMillis() - (listOfTasks[":total"] as Long))
            taskLenghtList.remove(taskLenghtList.last())
            taskLenghtList.add(aggregatorTask)
        } else {
            taskLenghtList.drop(1)
        }
    }

}

