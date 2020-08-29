package com.talaiot.buildplugins

import org.gradle.plugin.devel.GradlePluginDevelopmentExtension

/**
 * Plugin Extension to configure a [TalaiotPlugin]
 * In this extension we define the parameters required for naming and publishing.
 *
 * talaiotPlugin {
 *     idPlugin = "com.cdsap.talaiot"
 *     artifact = "talaiot"
 *     group = "com.cdsap"
 *     mainClass = "com.cdsap.talaiot.TalaiotPlugin"
 *     version = "1.3.6-SNAPSHOT"
 * }
 *
 *
 */
open class TalaiotPluginExtension {
    /**
     * Plugin's Gradle id. This id will be used by the final consumer and the [GradlePluginDevelopmentExtension]
     * example: apply plugin: "com.cdsap.talaiot.plugin.base"
     */
    var idPlugin : String? = null

    /**
     *  Deployment for snapshots/maven local requires inform about the artifact coordinates of the
     *  new artifact
     */
    var artifact : String? = null
    /**
     *  Deployment for snapshots/maven local requires inform about the group coordinates of the
     *  new artifact. If it's not informed uses the default group of the type of plugins [TalaiotPlugin]
     */
    var group : String? = null

    /**
     * Main Class of the plugin, required by the [GradlePluginDevelopmentExtension]
     */
    var mainClass: String? = null
    /**
     * Specific version for the plugin
     * If it's not informed will use version from [Versions]
     */
    var version : String? = null
}