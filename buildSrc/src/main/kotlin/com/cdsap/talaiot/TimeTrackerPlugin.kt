package com.cdsap.talaiot

import com.agoda.gradle.tracking.entities.TaskLenght
import com.agoda.gradle.tracking.reporter.HTTPReporter
import com.agoda.gradle.tracking.reporter.OutputReporter
import org.gradle.BuildResult
import org.gradle.api.Plugin
import org.gradle.api.Project

class TimeTrackerPlugin : Plugin<Project> {

    val reporterList = listOf(HTTPReporter(), OutputReporter())
    override fun apply(target: Project) {
        target.gradle.addBuildListener(TaskTrackingListener(this))
    }

    fun onFinished(result: BuildResult, timing: MutableList<TaskLenght>) {
        val a = AggregateData(result, timing).build()
        reporterList.forEach {
            it.send(a)
        }
    }
}