package com.cdsap.talaiot

import com.cdsap.talaiot.publisher.*
import org.gradle.api.Plugin
import org.gradle.api.Project

class TalaiotPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        val extension: TalaiotExtension = project.extensions.create("talaiot", TalaiotExtension::class.java, project)
        initPlugin(extension, project)
    }

    private fun initPlugin(extension: TalaiotExtension, project: Project) {
        val publisher = TalaiotPublisherImpl(project)
        val listener = TalaiotListener(publisher, extension)
        project.gradle.addBuildListener(listener)
    }
}