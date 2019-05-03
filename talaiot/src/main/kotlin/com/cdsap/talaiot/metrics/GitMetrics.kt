package com.cdsap.talaiot.metrics

import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.IllegalStateException

/**
 * GitMetrics provided for the builds.
 * Metrics included:
 *  -- GitUser
 *  -- Branch
 * It requires Git repository
 *
 */
class GitMetrics : Metrics {

    /**
     * Retrieve the values for the GitMetrics defined
     *
     * @throws IllegalStateException in case no Git repository found
     * @return collection of GitMetrics
     */
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
            val userName = bufferUserName.readLine()
            if (userName != null) {
                gitMetrics["gitUser"] = userName
            }
            val branch = bufferBranch.readLine()
            if (branch != null) {
                gitMetrics["branch"] = branch
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