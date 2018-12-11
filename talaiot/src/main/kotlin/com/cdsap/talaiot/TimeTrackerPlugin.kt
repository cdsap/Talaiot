package com.cdsap.talaiot

import com.cdsap.talaiot.entities.TaskLength
import com.cdsap.talaiot.reporter.InfluxDbPublisher
import com.cdsap.talaiot.reporter.InfluxDbPublisherConfiguration
import com.cdsap.talaiot.reporter.OutputPublisher
import org.gradle.BuildResult
import org.gradle.api.Plugin
import org.gradle.api.Project

class TimeTrackerPlugin : Plugin<Project> {

    override fun apply(target: Project) {

        val extension: TalaiotExtension  = target.extensions.create("talaiot", TalaiotExtension::class.java)

        target.gradle.addBuildListener(TaskTrackingListener(this
            ,extension))
    }

    fun onFinished(result: BuildResult, timing: MutableList<TaskLength>, talaiotExtension: TalaiotExtension) {
        val influxDbPublisher = talaiotExtension.publisher.takeIf {
        it !=null
        }?.influxDbPublisher
        val outputPublisher = talaiotExtension.publisher.takeIf {
            it != null
        }?.outputPublisher

        val a = AggregateData(result, timing).build()

        if(outputPublisher!=null){
            OutputPublisher().send(a)
        }

        if(influxDbPublisher!=null){
            InfluxDbPublisher(influxDbPublisher).send(a)
        }
    }
}