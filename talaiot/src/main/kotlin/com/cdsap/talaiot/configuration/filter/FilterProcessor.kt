package com.cdsap.talaiot.configuration.filter

import com.cdsap.talaiot.logger.LogTracker

class FilterProcessor(private val filter: StringFilter, private val logTracker: LogTracker) {


    fun matches(string: String): Boolean {
        var includes = -1
        var excludes = -1
        filter.includes?.let {
            includes = if (listContainsMatchingItem(it, string)) 1 else 0
        }
        filter.excludes?.let {
            excludes = if (listContainsMatchingItem(it, string)) 1 else 0
        }

        //No config provided
        if (includes == -1 && excludes == -1)
            return true
        //Includes not provided,excludes provided
        else if (includes == -1 && excludes > -1)
            return excludes == 0
        //Excludes not provided , includes provided
        else if (excludes == -1 && includes > -1)
            return includes == 1
        //Excludes and includes provided
        else {
            if (excludes == 1 && includes == 1)
                logTracker.log("$string matches with inclusion and exclusion filter")
            return includes == 1 && excludes == 0
        }


    }


    private fun listContainsMatchingItem(regexes: Array<String>, string: String): Boolean {
        return regexes.find { string.matches(it.toRegex()) } != null
    }


}