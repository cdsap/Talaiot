package com.agoda.gradle.tracking

import org.gradle.BuildResult
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.internal.os.OperatingSystem
import java.io.BufferedReader
import java.io.InputStreamReader

class BuildTimeTrackerPluginCustom : Plugin<Project> {

    override fun apply(target: Project) {
        target.gradle.addBuildListener(TrackingRecored(this))
    }

    fun onFinished(result: BuildResult, timing: MutableList<TaskLenght>) {
        val a = AggregateData(result, timing).build()
        HTTPStrategy().send(a)
        OutputStrategy().send(a)
    }
}