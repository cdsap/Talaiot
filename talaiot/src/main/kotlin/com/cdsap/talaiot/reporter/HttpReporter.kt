package com.cdsap.talaiot.reporter

import com.cdsap.talaiot.TalaiotExtension
import com.cdsap.talaiot.entities.TaskMeasurementAggregated
import com.cdsap.talaiot.logger.LogTracking
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.request.post
import io.ktor.client.request.url
import io.ktor.http.URLBuilder
import kotlinx.coroutines.experimental.async


import kotlinx.coroutines.experimental.launch

import java.net.URL
import java.time.Instant
import java.util.concurrent.TimeUnit

class HttpReporter(val talaiotExtension: TalaiotExtension) : Reporter {
    override var logTracker = LogTracking(LogTracking.Mode.INFO)

    override fun send(measurementAggregated: TaskMeasurementAggregated) {
        //HttpCl
         val METRIC =""// talaiotExtension.metric
         val PROMETEHUS = "http://localhost:8086/write?db=influxdb"
        val client = HttpClient(OkHttp)
        var content = ""

        measurementAggregated.apply {
            logTracker.log("================")
            logTracker.log("HttpReporting")
            logTracker.log("================")
            logTracker.log("User ${this.user}")
            logTracker.log("Branch ${this.branch}")
            logTracker.log("Os ${this.os}")
            logTracker.log("Project ${this.project}")
            logTracker.log("Gradle Version ${this.gradleVersion}")

            this.taskMeasurment.forEach {

                //taskMeasurement,task=s value=4443 1434055562000000000
                val time1 = System.currentTimeMillis()
                var time = Instant.ofEpochMilli(System.currentTimeMillis()).plusNanos(System.nanoTime() % 1000_000L)
                time
                System.currentTimeMillis()//TimeUnit.MILLISECONDS.toMicros(System.currentTimeMillis())
                content += "taskMeasurement,task=\"${it.path}\"," +
                        "user=\"${this.user}\"," +
                        "totalMemory=\"${this.totalMemory}\"," +
                        "freeMemory=\"${this.freeMemory}\"," +
                        "project=\"${this.project}\"," +
                        "state=\"${it.state}\"," +
                        "maxMemory=\"${this.maxMemory}\"," +
                        "availableProcessors=\"${this.availableProcessors}\"," +
                        "branch=\"${this.branch}\"," +
                        "gradleVerion=\"${this.gradleVersion}\"," +
                        "os=\"${this.os}\" value=${it.ms}   \n"
             //   1434055562000000000
             //   1543919400647000000
                //1434055562000000000
//                1543920192
                //      91775841661589
                // PROMOTHEUS
//                content += "taskMeasurement{task=\"${it.path}\", " +
//                        "user=\"${this.user}\"," +
//                        "totalMemory=\"${this.totalMemory}\"," +
//                        "freeMemory=\"${this.freeMemory}\"," +
//                        "project=\"${this.project}\"," +
//                        "state=\"${it.state}\"," +
//                        "maxMemory=\"${this.maxMemory}\"," +
//                        "availableProcessors=\"${this.availableProcessors}\"," +
//                        "branch=\"${this.branch}\"," +
//                        "gradleVerion=\"${this.gradleVersion}\" ," +
      //                  "os=\"${this.os}\"}${it.ms}\n"
            }
            logTracker.log("$content")
        }
        try {

            async {
//                client.post<Unit> {
//                    url(URL(PROMETEHUS))
//                    body = content
//                }

                val a = client.post<String>(URL(PROMETEHUS)){
                    body = content
                    build()

            }

            }
            logTracker.log("$PROMETEHUS")
        } catch (e: Exception) {
            logTracker.log("HTTPReporting failed: ${e.message} ")
        }
    }
}

/// query SELECT "value" FROM "taskMeasurement"
///localhost http://localhost:3004/
// docker https://github.com/philhawthorne/docker-influxdb-grafana
