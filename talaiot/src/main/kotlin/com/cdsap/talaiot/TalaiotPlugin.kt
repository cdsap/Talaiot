package com.cdsap.talaiot

import com.cdsap.talaiot.entities.TaskLength
import com.cdsap.talaiot.metrics.MetricsProvider
import com.cdsap.talaiot.metrics.MetricsParser
import com.cdsap.talaiot.publisher.influxdb.InfluxDbPublisher
import com.cdsap.talaiot.publisher.output.OutputPublisher
import org.gradle.BuildResult
import org.gradle.api.Plugin
import org.gradle.api.Project

class TalaiotPlugin : Plugin<Project> {

    override fun apply(target: Project) {

        val extension: TalaiotExtension = target.extensions.create("talaiot", TalaiotExtension::class.java)

        target.gradle.addBuildListener(
            TalaiotListener(
                this
                , extension
            )
        )
    }

    fun onFinished(result: BuildResult, timing: MutableList<TaskLength>, talaiotExtension: TalaiotExtension) {

        val availableMetrics = MetricsProvider(talaiotExtension, result).get()

        val aggregatedData = AggregateData(
            timing, MetricsParser(availableMetrics)
        ).build()

        talaiotExtension.publishers?.apply {
            this.outputPublisher.let { OutputPublisher().publish(aggregatedData) }
            this.influxDbPublisher?.let {
                it.let {
                    InfluxDbPublisher(it).publish(aggregatedData)
                }
            }
            this.customPublisher?.apply {
                this.publish(aggregatedData)
            }
        }
    }
}