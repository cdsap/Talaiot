package com.cdsap.talaiot.util

import org.gradle.util.NameMatcher

/**
 * Match task abbreviation to a full task name using list of executed tasks.
 * Fallback to the abbreviation if couldn't find a corresponding full name.
 */
class TaskAbbreviationMatcher(private val executedTasks: List<TaskName>) {
    private val nameMatcher = NameMatcher()

    fun findRequestedTask(taskName: String): String {
        val lastIndex = taskName.lastIndexOf(':')
        return if (lastIndex == -1) {
            val foundTask: String? = nameMatcher.find(
                taskName,
                executedTasks.filter { !it.path.contains(':') }.map { it.name to it.path }.toMap()
            )
            foundTask ?: taskName
        } else {
            val pathWithoutTaskName = taskName.substring(0, lastIndex + 1).let {
                if (it[0] != ':') {
                    ":$it"
                } else {
                    it
                }
            }
            val taskNameWithoutPath = taskName.substring(lastIndex + 1)
            val foundTask: String? = nameMatcher.find(
                taskNameWithoutPath,
                executedTasks.filter { it.path.startsWith(pathWithoutTaskName) }
                    .map { it.name to it.path }
                    .toMap()
            )
            foundTask ?: taskName
        }
    }
}
