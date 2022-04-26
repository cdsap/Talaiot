package io.github.cdsap.talaiot.plugin.influxdb2

import io.github.cdsap.talaiot.Talaiot
import org.gradle.api.Plugin
import org.gradle.api.Project

class TalaiotInfluxdb2Plugin : Plugin<Project> {

    override fun apply(target: Project) {
        Talaiot(
            Influxdb2Extension::class.java,
            Influxdb2ConfigurationProvider(
                target
            )
        ).setUpPlugin(target)
    }
}
