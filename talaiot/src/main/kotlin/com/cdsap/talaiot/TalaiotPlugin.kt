package com.cdsap.talaiot

import com.cdsap.talaiot.entities.TaskLength
import com.cdsap.talaiot.metrics.BaseMetrics
import com.cdsap.talaiot.metrics.CustomMetrics
import com.cdsap.talaiot.metrics.MetricsProvider
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
        val influxDbPublisher = talaiotExtension.publisher.takeIf {
            it != null
        }?.influxDbPublisher
        val outputPublisher = talaiotExtension.publisher.takeIf {
            it != null
        }?.outputPublisher

        val a = AggregateData(
            timing, MetricsProvider(
                BaseMetrics(result),
                CustomMetrics(talaiotExtension)
            )
        ).build()

        if (outputPublisher != null) {
            OutputPublisher().publish(a)
        }

        if (influxDbPublisher != null) {
            InfluxDbPublisher(influxDbPublisher).publish(a)
        }
    }
}