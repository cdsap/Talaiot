package com.cdsap.talaiot.publisher.timeline

import com.cdsap.talaiot.entities.ExecutionReport
import com.cdsap.talaiot.publisher.Publisher
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.malinskiy.marathon.report.debug.timeline.Data
import com.malinskiy.marathon.report.debug.timeline.ExecutionResult
import com.malinskiy.marathon.report.debug.timeline.Measure
import org.gradle.api.invocation.Gradle
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.InputStream

class TimelinePublisher(val gradle: Gradle) : Publisher {
    override fun publish(report: ExecutionReport) {
        val measures = report.tasks
            ?.filter { !it.rootNode }
            ?.groupBy { it.workerId }
        val timelineMeasures = measures?.map {
            Measure(it.key, it.value.map { task ->
                Data(
                    task.taskPath,
                    task.state,
                    task.critical,
                    task.startMs,
                    task.workerId,
                    task.stopMs
                )
            })
        } ?: emptyList()

        val executionResult = ExecutionResult(timelineMeasures)

        val gson: Gson = GsonBuilder().setPrettyPrinting().create()

        val html = File(gradle.rootProject.buildDir, "reports/talaiot/timeline/index.html")
            .apply {
                mkdirs()
                delete()
                createNewFile()
            }



        BufferedWriter(FileWriter(html)).use {
            var reader = inputStreamFromResources("timeline/index_0.html").bufferedReader()
            it.append(reader.readText())
            reader.close()

            gson.toJson(executionResult, it)

            reader = inputStreamFromResources("timeline/index_2.html").bufferedReader()
            it.append(reader.readText())
            reader.close()

            it.flush()
        }

        inputStreamFromResources("timeline/chart.css").copyTo(
            File(
                gradle.rootProject.buildDir,
                "reports/talaiot/timeline/chart.css"
            ).outputStream()
        )
        inputStreamFromResources("timeline/chart.js").copyTo(
            File(
                gradle.rootProject.buildDir,
                "reports/talaiot/timeline/chart.js"
            ).outputStream()
        )
    }

    private fun inputStreamFromResources(path: String): InputStream =
        TimelinePublisher::class.java.classLoader.getResourceAsStream(path)
}