package com.cdsap.talaiot.reporter

import com.agoda.gradle.tracking.entities.TaskMeasurementAggregated
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

@Suppress("UnsafeCast", "ThrowRuntimeException")
class HTTPReporter : Reporter {

    val url = URL(PROMETEHUS)

    override fun send(measurementAggregated: TaskMeasurementAggregated) {

        var content = ""
        measurementAggregated.apply {
            this.taskMeasurment.forEach {
                content += "taskMeasurement{task=\"${it.path}\", " +
                        "user=\"${this.user}\"," +
                        "totalMemory=\"${this.totalMemory}\"," +
                        "freeMemory=\"${this.freeMemory}\"," +
                        "project=\"${this.project}\"," +
                        "maxMemory=\"${this.maxMemory}\"," +
                        "availableProcessors=\"${this.availableProcessors}\"," +
                        "branch=\"${this.branch}\"," +
                        "gradleVerion=\"${this.gradleVersion}\" ," +
                        "os=\"${this.os}\"}${it.ms}\n"
            }
        }

        try {
            println(content)
            with(url.openConnection() as HttpURLConnection) {
                requestMethod = "POST"
                doOutput = true
                val wr = OutputStreamWriter(getOutputStream());
                wr.write(content);
                wr.appendln()
                wr.flush();

                BufferedReader(InputStreamReader(inputStream)).use {
                    val response = StringBuffer()

                    var inputLine = it.readLine()
                    while (inputLine != null) {
                        response.append(inputLine)
                        inputLine = it.readLine()
                    }
                    it.close()
                    println("Response : $response")
                }
            }
        } catch (e: Exception) {
            println("HTTPReporting failed: " + e.message)
        }
    }

    companion object {
        const val METRIC = "gradle_local_build"
        const val PROMETEHUS = "https://prometheus-push.default.svc.agoda.mobi/metrics/job/$METRIC"
    }
}
