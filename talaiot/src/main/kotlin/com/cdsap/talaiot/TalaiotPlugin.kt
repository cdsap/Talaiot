package com.cdsap.talaiot

import com.cdsap.talaiot.metrics.*
import com.cdsap.talaiot.publisher.*
import org.gradle.api.Plugin
import org.gradle.api.Project

class TalaiotPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        val extension: TalaiotExtension = target.extensions.create("talaiot", TalaiotExtension::class.java, target)
        println("1")
        println(extension.ignoreWhen)
        println(extension.toString())
        initPlugin(extension)
    }

    private fun initPlugin(extension: TalaiotExtension) {
        println(extension)
        println("1")
        println(extension.publishers)
        println("1")
        println(extension.publishers?.influxDbPublisher)
        // if (extension.ignoreWhen?.shouldIgnore() == false) {
        val publishers = PublishersProvider(extension).get()
        val metrics = MetricsProvider(extension.project).get()
        val publisher = TalaiotPublisherImpl(publishers, metrics)
        val listener = TalaiotListener(publisher)
        extension.project.gradle.addBuildListener(listener)
        //   }
    }
}