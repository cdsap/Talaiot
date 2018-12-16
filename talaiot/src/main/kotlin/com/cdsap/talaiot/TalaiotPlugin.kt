package com.cdsap.talaiot

import org.gradle.api.Plugin
import org.gradle.api.Project

class TalaiotPlugin : Plugin<Project> {

    override fun apply(target: Project) {

        val extension: TalaiotExtension = target.extensions.create("talaiot", TalaiotExtension::class.java, target)
        target.gradle.addBuildListener(TalaiotListener(extension))
    }
}