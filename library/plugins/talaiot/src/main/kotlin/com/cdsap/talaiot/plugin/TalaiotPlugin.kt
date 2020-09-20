package com.cdsap.talaiot.plugin

import com.cdsap.talaiot.Talaiot
import org.gradle.api.Plugin
import org.gradle.api.Project

class TalaiotPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        Talaiot(
            TalaiotPluginExtension::class.java,
            TalaiotConfigurationProvider(
                target
            )
        ).setUpPlugin(target)
    }

}