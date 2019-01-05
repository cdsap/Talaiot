package com.cdsap.talaiot.request

import com.cdsap.talaiot.logger.LogTracker

class AuthRequest(mode: LogTracker) : Request {


    override var logTracker = mode
    override fun send(url: String, content: String) {
//        GlobalScope.launch {
//            val client = HttpClient(OkHttp) {
//                install(BasicAuth) {
//
//                    username = "username"
//                    password = "password"
//
//                }
//            }.post<String>(URL(url)) {
//                body = content
//                build()
//            }
//        }
    }
}
