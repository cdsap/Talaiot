package io.github.cdsap.talaiot.plugin

import io.github.cdsap.talaiot.Talaiot
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
