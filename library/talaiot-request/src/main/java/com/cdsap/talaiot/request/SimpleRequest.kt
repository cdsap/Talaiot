package com.cdsap.talaiot.request

import com.cdsap.talaiot.logger.LogTracker
import io.github.rybalkinsd.kohttp.dsl.httpPost
import io.github.rybalkinsd.kohttp.ext.url
import java.net.URL

/**
 * Simple implementation of request.
 * Using KoHttp to create the request.
 */
class SimpleRequest(mode: LogTracker) : Request {
    override var logTracker = mode
    private val TAG = "SimpleRequest"

    override fun send(url: String, content: String) {
        val urlSpec = URL(url)
        logTracker.log(TAG, "send request to $url")
        try {
            httpPost {
                url(urlSpec)
                if (urlSpec.query != null) {
                    val query = urlSpec.query.split("=")
                    param {
                        query[0] to query[1]
                    }
                }

                body {
                    string(content)
                }
            }.also {
                logTracker.log(TAG, "Response code ${it.code()}")
                if (!it.isSuccessful) {
                    logTracker.log(TAG, "Response code not Successful")
                    logTracker.log(TAG, "Message Response ${it.message()}")
                    logTracker.log(TAG, "Response Body ${it.body()?.string()}")
                }
            }
        } catch (e: Exception) {
            logTracker.log(TAG, e.message ?: "error requesting $url")
        }
    }
}
