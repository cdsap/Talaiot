package com.cdsap.talaiot.plugin.pushgateway

import com.cdsap.talaiot.Talaiot
import org.gradle.api.Plugin
import org.gradle.api.Project

class TalaiotPushgatewayPlugin: Plugin<Project> {

    override fun apply(target: Project) {
        Talaiot(
            PushgatewayExtension::class.java,
            PushgatewayConfigurationProvider(
                target
            )
        ).setUpPlugin(target)
    }

}