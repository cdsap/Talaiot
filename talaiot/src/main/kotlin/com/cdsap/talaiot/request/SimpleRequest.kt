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

    override fun send(url: String, content: String) {
        val urlSpec = URL(url)
        println("1")
        println(url)
        println("aloaoaoaoa")

        logTracker.log(url)
        try {
          //  val query = urlSpec.query.split("=")
            httpPost {
                url(urlSpec)

//                if (query.isNotEmpty()) {
//                    param {
//                        query[0] to query[1]
//                    }
//                }

                body {
                    println(content)

                    string(content)
                }
            }.also {
                logTracker.log(it.message())
            }
        } catch (e: Exception) {
            println("error")
           // logTracker.log("${e.toString()}")
            logTracker.log("${e.stackTrace.toString()}")
            logTracker.log(e.message ?: "error requesting $url")
        }
    }
}