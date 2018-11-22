package com.cdsap.talaiot

import com.cdsap.talaiot.entities.TaskLenght
import com.cdsap.talaiot.reporter.HTTPReporter
import com.cdsap.talaiot.reporter.OutputReporter
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