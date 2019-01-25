package com.cdsap.talaiot

import com.cdsap.talaiot.metrics.*
import com.cdsap.talaiot.publisher.*
import org.gradle.api.Plugin
import org.gradle.api.Project

class TalaiotPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        val extension: TalaiotExtension = project.extensions.create("talaiot", TalaiotExtension::class.java, project)
        initPlugin(extension, project)
    }

    private fun initPlugin(extension: TalaiotExtension, project: Project) {
        if (extension.ignoreWhen?.shouldIgnore() == false) {
            val publishers = PublishersProvider(project).get()
            val metrics = MetricsProvider(project).get()
            val publisher = TalaiotPublisherImpl(publishers, metrics)
            val listener = TalaiotListener(publisher)
            project.gradle.addBuildListener(listener)
        }
    }
}