package com.cdsap.talaiot.request

import com.cdsap.talaiot.logger.LogTracker
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.request.post
import kotlinx.coroutines.experimental.GlobalScope
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch


import java.net.URL

class Request(
    private val url: String,
    private val content: String,
    logTracker: LogTracker
) {
    init {
        val client = HttpClient(OkHttp)
        GlobalScope.launch {
            val response = client.post<String>(URL(url)) {
                body = content
                build()
            }
            println(response)
        }
        logTracker.log(url)

    }
}