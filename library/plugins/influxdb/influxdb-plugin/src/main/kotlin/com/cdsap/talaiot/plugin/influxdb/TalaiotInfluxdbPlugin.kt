package com.cdsap.talaiot.plugin.influxdb

import com.cdsap.talaiot.Talaiot
import org.gradle.api.Plugin
import org.gradle.api.Project

class TalaiotInfluxdbPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        Talaiot(
            InfluxdbExtension::class.java,
           InfluxdbConfigurationProvider(
                target
            )
        ).setUpPlugin(target)
    }

}