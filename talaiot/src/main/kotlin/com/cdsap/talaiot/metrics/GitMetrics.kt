package com.cdsap.talaiot.metrics

import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.IllegalStateException

class GitMetrics : Metrics {
    override fun get(): Map<String, String> {
        val runtime = Runtime.getRuntime()
        val gitMetrics = mutableMapOf<String, String>()
        try {
            val bufferBranch = BufferedReader(
                InputStreamReader(runtime.exec("git rev-parse --abbrev-ref HEAD").inputStream)
            )
            val bufferUserName = BufferedReader(
                InputStreamReader(runtime.exec("git config --get user.name").inputStream)
            )
            val userName = bufferUserName.runCatching { readLine() }
            if (userName.isSuccess) {
                gitMetrics["gitUser"] = userName.getOrElse { "" }
            }
            val branch = bufferBranch.runCatching { readLine() }
            if (branch.isSuccess) {
                gitMetrics["branch"] = branch.getOrElse { "" }

            }
            return gitMetrics
        } catch (e: IllegalStateException) {
            throw IllegalArgumentException(
                "Error getting information about the Git Repository. In case " +
                        "you are not in a Git Repository you can disable the metrics of Git with " +
                        "talaiot{" +
                        "  gitMetrics=false" +
                        "} " +
                        "${e.message}"
            )
        }

    }
}