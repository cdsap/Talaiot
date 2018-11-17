package com.agoda.gradle.tracking


import org.codehaus.groovy.runtime.DefaultGroovyMethods.hasProperty
import org.gradle.BuildListener
import org.gradle.BuildResult
import org.gradle.api.Task
import org.gradle.api.execution.TaskExecutionListener
import org.gradle.api.initialization.Settings
import org.gradle.api.invocation.Gradle
import org.gradle.api.tasks.TaskState
import org.gradle.internal.os.OperatingSystem
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL


class TrackingRecored(val buildTimeTrackerPluginCustom: BuildTimeTrackerPluginCustom)
    : BuildListener, TaskExecutionListener {
    val timing = mutableListOf<TaskLenght>()
    val clock: Clock = Clock()

    override fun settingsEvaluated(settings: Settings) {
    }

    override fun buildFinished(result: BuildResult) {
        buildTimeTrackerPluginCustom.onFinished(result, timing)
    }

    override fun projectsLoaded(gradle: Gradle) {
    }

    override fun buildStarted(gradle: Gradle) {
    }

    override fun projectsEvaluated(gradle: Gradle) {
    }

    override fun beforeExecute(task: Task) {
    }

    override fun afterExecute(task: Task, state: TaskState) {
        timing.add(TaskLenght(
                ms = clock.getTimeInMs(),
                path = task.path)
        )
    }
}

