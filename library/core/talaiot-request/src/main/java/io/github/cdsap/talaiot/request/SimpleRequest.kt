package io.github.cdsap.talaiot.request

import io.github.cdsap.talaiot.logger.LogTracker
import io.github.rybalkinsd.kohttp.dsl.httpPost
import io.github.rybalkinsd.kohttp.ext.url
import java.net.URL

/**
 * Simple implementation of request.
 * Using KoHttp to create the request.
 */
class SimpleRequest(
    mode: LogTracker,
) : Request {
    override var logTracker = mode
    private val tag = "SimpleRequest"

    override fun send(
        url: String,
        content: String,
    ) {
        val urlSpec = URL(url)
        logTracker.log(tag, "send request to $url")
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
                logTracker.log(tag, "Response code ${it.code()}")
                if (!it.isSuccessful) {
                    logTracker.log(tag, "Response code not Successful")
                    logTracker.log(tag, "Message Response ${it.message()}")
                    logTracker.log(tag, "Response Body ${it.body()?.string()}")
                }
            }
        } catch (e: Exception) {
            logTracker.log(tag, e.message ?: "error requesting $url")
        }
    }
}
