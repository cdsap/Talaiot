package com.cdsap.talaiot.request

import com.cdsap.talaiot.logger.LogTracker
import io.github.rybalkinsd.kohttp.dsl.httpPost
import okhttp3.RequestBody
import okhttp3.Response


class SimpleRequest(mode: LogTracker) : Request {
    override var logTracker = mode

    override fun send(url: String, content: String) {
        println("sdldldldldldl")
        println(url)
        val response: Response = httpPost {

            host = "http://localhost"
            port = 8086
            path = "write?db=tracking"


            body {
                RequestBody.create(null, content)
            }


        }
        println(response.message())
        // val response: Response = httpPost {

        //  }
    }
//        val response: Response = httPost{
//    }
//    ]
//    val client = HttpClient(OkHttp)
//
//
//    GlobalScope.launch
//    {
//        try {
//            val response = client.post<String>(URL(url)) {
//                body = content
//                build()
//            }
//            if (response.isNotEmpty()) {
//                logTracker.log(response)
//            }
//        } catch (e: Exception) {
//            logTracker.log(e.message ?: "error requesting $url")
//        }
//    }
//    logTracker.log(url)
//}
}