package com.cdsap.talaiot

import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Talaiot main [Plugin].
 *
 * Talaiot is a simple and extensible plugin for teams that use Gradle Build System. It stores information about
 * your Gradle tasks and helps you detect problems and bottlenecks of your builds. For every tracked task and build
 * it will add additional information defined by default and custom metrics
 * specified in [com.cdsap.talaiot.configuration.MetricsConfiguration].
 *
 * usage:
 * plugins {
 *   id("talaiot")
 * }
 */
class TalaiotPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        val extension: TalaiotExtension = target.extensions.create("talaiot", TalaiotExtension::class.java, target)
        initPlugin(extension, target)
    }

    /**
     * Initialization of the plugin. The plugin needs to receive callbacks
     * from the [org.gradle.api.execution.TaskExecutionListener]
     * and [org.gradle.BuildListener] to start tracking the information of the tasks.
     *
     * Additionally we need the a list of metrics and providers that will be used during the execution.
     *
     * @param extension Talaiot extension that contains the configuration
     * @param project Gradle project used to to retrieve buildProperties and build information.
     */
    private fun initPlugin(extension: TalaiotExtension, project: Project) {
        val listener = TalaiotListener(project, extension)
        project.gradle.addBuildListener(listener)
    }
}