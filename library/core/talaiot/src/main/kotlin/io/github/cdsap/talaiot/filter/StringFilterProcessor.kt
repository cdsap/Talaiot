package io.github.cdsap.talaiot.filter

import io.github.cdsap.talaiot.logger.LogTracker

class StringFilterProcessor(private val filter: StringFilter, private val logTracker: LogTracker) {
    private val TAG = "StringFilterProcessor"

    fun matches(string: String): Boolean {
        var includes = -1
        var excludes = -1
        filter.includes?.let {
            includes = if (listContainsMatchingItem(it, string)) 1 else 0
        }
        filter.excludes?.let {
            excludes = if (listContainsMatchingItem(it, string)) 1 else 0
        }

        // No config provided
        return if (includes == -1 && excludes == -1) {
            true
        } // Includes not provided,excludes provided
        else if (includes == -1 && excludes > -1) {
            excludes == 0
        } // Excludes not provided , includes provided
        else if (excludes == -1 && includes > -1) {
            includes == 1
        } // Excludes and includes provided
        else {
            if (excludes == 1 && includes == 1) {
                logTracker.log(TAG, "$string matches with inclusion and exclusion filter")
            }
            includes == 1 && excludes == 0
        }
    }

    private fun listContainsMatchingItem(regexes: Array<String>, string: String): Boolean {
        return regexes.find { string.matches(it.toRegex()) } != null
    }
}
