package io.github.cdsap.talaiot.filter

import io.github.cdsap.talaiot.logger.LogTracker

class StringFilterProcessor(
    private val filter: StringFilter,
    private val logTracker: LogTracker,
) {
    private val tag = "StringFilterProcessor"

    fun matches(string: String): Boolean {
        var includes = -1
        var excludes = -1
        filter.includes?.let {
            includes = if (listContainsMatchingItem(it, string)) 1 else 0
        }
        filter.excludes?.let {
            excludes = if (listContainsMatchingItem(it, string)) 1 else 0
        }

        return if (includes == -1 && excludes == -1) {
            true
        } else if (includes == -1 && excludes > -1) {
            excludes == 0
        } else if (excludes == -1 && includes > -1) {
            includes == 1
        } else {
            if (excludes == 1 && includes == 1) {
                logTracker.log(tag, "$string matches with inclusion and exclusion filter")
            }
            includes == 1 && excludes == 0
        }
    }

    private fun listContainsMatchingItem(
        regexes: Array<String>,
        string: String,
    ): Boolean =
        regexes.find {
            string.matches(it.toRegex())
        } != null
}
