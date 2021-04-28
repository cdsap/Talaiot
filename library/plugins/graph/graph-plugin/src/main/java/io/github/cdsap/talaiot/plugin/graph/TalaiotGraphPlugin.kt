package io.github.cdsap.talaiot.plugin.graph

import io.github.cdsap.talaiot.Talaiot
import org.gradle.api.Plugin
import org.gradle.api.Project

class TalaiotGraphPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        Talaiot(
            GraphExtension::class.java,
            GraphConfigurationProvider(target)
        ).setUpPlugin(target)
    }
}
