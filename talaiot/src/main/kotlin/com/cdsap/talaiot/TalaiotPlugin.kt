package com.cdsap.talaiot

import com.cdsap.talaiot.publisher.*
import org.gradle.api.Plugin
import org.gradle.api.Project

class TalaiotPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        val extension: TalaiotExtension = target.extensions.create("talaiot", TalaiotExtension::class.java, target)
        initPlugin(extension,target)
    }

    private fun initPlugin(extension: TalaiotExtension, project: Project) {
        val publisher = TalaiotPublisherImpl(project)
        val listener = TalaiotListener(publisher, extension)
        project.gradle.addBuildListener(listener)
    }
}