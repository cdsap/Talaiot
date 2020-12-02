package com.talaiot.buildplugins

import org.gradle.plugin.devel.GradlePluginDevelopmentExtension
import com.gradle.publish.PluginBundleExtension

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
open class TalaiotPluginConfiguration : BaseConfiguration() {
    /**
     * Plugin's Gradle id. This id will be used by the final consumer and the [GradlePluginDevelopmentExtension]
     * example: apply plugin: "com.cdsap.talaiot.plugin.base"
     */
    var idPlugin: String? = null

    /**
     * Main Class of the plugin, required by the [GradlePluginDevelopmentExtension]
     */
    var mainClass: String? = null

    /**
     * Display Name property for the plugin [PluginBundleExtension]]
     */
    var displayName: String? = null
}