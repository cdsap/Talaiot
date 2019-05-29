package com.cdsap.talaiot.configuration


fun FilterConfiguration.getFilterWithMinThreshold() = this.apply {
    threshold {
        minExecutionTime = 10L
    }
}

fun FilterConfiguration.getFilterWithMinMaxThreshold() = this.apply {
    threshold {
        minExecutionTime = 1L
        maxExecutionTime = 100L
    }
}

fun FilterConfiguration.getFilterWithTaskIncludes() = this.apply {
    tasks {
        includes = arrayOf("clean.*")
    }
}

fun FilterConfiguration.getFilterWithTaskExcludes() = this.apply {
    tasks {
        excludes = arrayOf("clean.*")
    }
}

fun FilterConfiguration.getFilterWithTaskExcludesAndIncludes() = this.apply {
    tasks {
        excludes = arrayOf("clean.*")
        includes = arrayOf("cle.*")
    }
}

fun FilterConfiguration.getFilterWithModuleExcludes() = this.apply {
    modules {
        excludes = arrayOf("app")
    }
}

fun FilterConfiguration.getFilterWithModuleIncludes() = this.apply {
    modules {
        includes = arrayOf("app")
    }
}