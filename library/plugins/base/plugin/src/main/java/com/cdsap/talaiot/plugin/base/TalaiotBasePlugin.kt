package com.cdsap.talaiot.plugin.base

import BaseConfigurationProvider
import com.cdsap.talaiot.Talaiot
import org.gradle.api.Plugin
import org.gradle.api.Project

class TalaiotBasePlugin : Plugin<Project> {

    override fun apply(target: Project) {
        Talaiot(
            BaseExtension::class.java,
            BaseConfigurationProvider(
                target
            )
        ).setUpPlugin(target)
    }

}