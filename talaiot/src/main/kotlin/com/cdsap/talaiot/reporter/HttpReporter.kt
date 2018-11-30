package com.cdsap.talaiot.reporter

import com.cdsap.talaiot.entities.TaskMeasurementAggregated
import com.cdsap.talaiot.logger.LogTracking
import io.ktor.client.HttpClient
import io.ktor.client.call.call
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.post
import io.ktor.client.request.url
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.net.URL

@Suppress("UnsafeCast", "ThrowRuntimeException")
class HttpReporter : Reporter {
    val url = URL(PROMETEHUS)
    override var logTracker = LogTracking(LogTracking.Mode.SILENT)

    override fun send(measurementAggregated: TaskMeasurementAggregated) {
        val client = HttpClient(OkHttp)
        var content = ""
        measurementAggregated.apply {
            this.taskMeasurment.forEach {
                content += "taskMeasurement{task=\"${it.path}\", " +
                        "user=\"${this.user}\"," +
                        "totalMemory=\"${this.totalMemory}\"," +
                        "freeMemory=\"${this.freeMemory}\"," +
                        "project=\"${this.project}\"," +
                        "state=\"${it.state}\"," +
                        "maxMemory=\"${this.maxMemory}\"," +
                        "availableProcessors=\"${this.availableProcessors}\"," +
                        "branch=\"${this.branch}\"," +
                        "gradleVerion=\"${this.gradleVersion}\" ," +
                        "os=\"${this.os}\"}${it.ms}\n"
            }
        }
        try {

            GlobalScope.launch {
                client.post<Unit> {
                    url(URL(PROMETEHUS))
                    body = content
                }
                client.call(HttpRequestBuilder())
            }

        } catch (e: Exception) {
            logTracker.log("HTTPReporting failed: ${e.message} ")
        }
    }

    companion object {
        const val METRIC = "gradle_local_build"
        const val PROMETEHUS = "https://prometheus-push.default.svc.agoda.mobi/metrics/job/$METRIC"
    }
}
