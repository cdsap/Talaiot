package com.cdsap.talaiot.request

import com.cdsap.talaiot.logger.LogTracker
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.request.post
import kotlinx.coroutines.experimental.GlobalScope
import kotlinx.coroutines.experimental.launch


import java.net.URL

class SimpleRequest(mode: LogTracker) : com.cdsap.talaiot.request.Request {
    override var logTracker = mode

    override fun send(url: String, content: String) {
        val client = HttpClient(OkHttp)
        GlobalScope.launch {
            val response = client.post<String>(URL(url)) {
                body = content
                build()
            }
            if (response.isNotEmpty()) {
                logTracker.log(response)
            }
        }
        logTracker.log(url)
    }
}