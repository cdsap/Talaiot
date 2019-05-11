package com.cdsap.talaiot.configuration.filter

import java.util.regex.Pattern

class ModuleFilterConfiguration {
    var includes: Array<String> = emptyArray()
    var excludes: Array<String> = emptyArray()

    private fun shouldInclude(moduleName: String) =
            includes.find { moduleName.matches(Regex.fromLiteral(Pattern.quote(it))) } != null

    private fun shouldExclude(taskName: String) =
            excludes.find { taskName.matches(Regex.fromLiteral(Pattern.quote(it))) } != null
}