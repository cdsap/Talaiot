package com.cdsap.talaiot

import com.cdsap.talaiot.publisher.*
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Talaiot main Plugin.
 * Talaiot is a simple and extensible plugin targeting teams using Gradle Build System. It records the duration of
 * your Gradle tasks helping to understand problems of the build and detecting bottlenecks. For every record,
 * it will add additional information defined by default or custom metrics.
 *
 * usage:
 * plugins{
 *   id("talaiot")
 * }
 */
class TalaiotPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        val extension: TalaiotExtension = target.extensions.create("talaiot", TalaiotExtension::class.java, target)
        initPlugin(extension, target)
    }

    /**
     * Initialization of the plugin. The plugin requires a hook in the TaskExecutionListener and BuildListener to start
     * tracking the information of the tasks.
     * Additionally we need the information of which metrics and providers will be used in the execution.
     *
     * @param extension Talaiot extension that contains the configuration
     * @param project Gradle project used to to retrieve properties or general configurations.
     */
    private fun initPlugin(extension: TalaiotExtension, project: Project) {
        val publisher = TalaiotPublisherImpl(project)
        val listener = TalaiotListener(publisher, extension)
        project.gradle.addBuildListener(listener)
    }
}