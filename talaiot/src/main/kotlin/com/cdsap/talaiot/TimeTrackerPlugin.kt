package com.cdsap.talaiot

import com.cdsap.talaiot.entities.TaskLenght
import com.cdsap.talaiot.reporter.HttpReporter
import com.cdsap.talaiot.reporter.OutputReporter
import org.gradle.BuildResult
import org.gradle.api.Plugin
import org.gradle.api.Project

class TimeTrackerPlugin : Plugin<Project> {

    private val reporterList = listOf(HttpReporter(), OutputReporter())
    override fun apply(target: Project) {

        val extension: TalaiotExtension  = project.extensions.create("talaiot", TalaiotExtension::class.java, project)
        target.gradle.addBuildListener(TaskTrackingListener(this))
    }

    fun onFinished(result: BuildResult, timing: MutableList<TaskLenght>) {
        val a = AggregateData(result, timing).build()
        reporterList.forEach {
            it.send(a)
        }
    }
}