package com.cdsap.talaiot.request

import com.cdsap.talaiot.logger.LogTracker
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.request.post
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.Exception


import java.net.URL

class SimpleRequest(mode: LogTracker) : Request {
    override var logTracker = mode

    override fun send(url: String, content: String) {

        val client = HttpClient(OkHttp)
        GlobalScope.launch {
            try {
                val response = client.post<String>(URL(url)) {
                    body = content
                    build()
                }
                if (response.isNotEmpty()) {
                    logTracker.log(response)
                }
            } catch (e: Exception) {
                logTracker.log(e.message ?: "error requesting $url")
            }
        }
        logTracker.log(url)
    }
}