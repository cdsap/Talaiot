package com.cdsap.talaiot.request

import com.cdsap.talaiot.logger.LogTracker
import io.github.rybalkinsd.kohttp.dsl.httpPost
import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.Response
import okhttp3.internal.Util
import java.lang.Exception
import java.net.URL


class SimpleRequest(mode: LogTracker) : Request {
    override var logTracker = mode

    override fun send(url: String, content: String) {
        val urlSpec = URL(url)
        logTracker.log(url)
        try {
            val query = urlSpec.query.split("=")
            val response: Response = httpPost {
                host = urlSpec.host
                port = urlSpec.port
                path = urlSpec.path
                param {
                    query[0] to query[1]
                }
                body {
                    RequestBody.create(null, content)
                }
            }
            logTracker.log(response.message())
        } catch (e: Exception) {
            logTracker.log(e.message ?: "error requesting $url")
        }
    }
}