package io.github.cdsap.talaiot.filter

import io.github.cdsap.talaiot.configuration.FilterConfiguration
import io.github.cdsap.talaiot.entities.TaskLength
import io.github.cdsap.talaiot.entities.TaskMessageState
import io.github.cdsap.talaiot.logger.LogTracker
import io.github.cdsap.talaiot.logger.LogTrackerImpl
import io.kotlintest.specs.BehaviorSpec

class TaskFilterProcessorTest : BehaviorSpec({
    given("a TaskFilterProcessor") {
        val logger = LogTrackerImpl(LogTracker.Mode.SILENT)
        `when`("Task Name is excluded") {
            val taskFilter = TaskFilterProcessor(
                logger,
                FilterConfiguration().apply {
                    tasks {
                        excludes = arrayOf("taskToBeExcluded")
                    }
                }
            )
            then("Task is not included after the filter") {
                assert(!taskFilter.taskLengthFilter(getTask("home", "taskToBeExcluded")))
            }
        }
        `when`("Task Name doesn't match in the exclude filter") {
            val taskFilter = TaskFilterProcessor(
                logger,
                FilterConfiguration().apply {
                    tasks {
                        excludes = arrayOf("taskToBeExcluded")
                    }
                }
            )
            then("Task is not included after the filter") {
                assert(taskFilter.taskLengthFilter(getTask("home", "taskToBeIncluded")))
            }
        }
        `when`("Module is excluded") {
            val taskFilter = TaskFilterProcessor(
                logger,
                FilterConfiguration().apply {
                    modules {
                        excludes = arrayOf("home")
                    }
                }
            )
            then("Task is not included after the filter") {
                assert(!taskFilter.taskLengthFilter(getTask("home", "taskToBeIncluded")))
            }
        }
        `when`("Module doesn't match in the exclude filter") {
            val taskFilter = TaskFilterProcessor(
                logger,
                FilterConfiguration().apply {
                    modules {
                        excludes = arrayOf("home")
                    }
                }
            )
            then("Task is not included after the filter") {
                assert(taskFilter.taskLengthFilter(getTask("lib", "taskToBeIncluded")))
            }
        }
        `when`("Threshold filter is applied to a task with short duration") {
            val taskFilter = TaskFilterProcessor(
                logger,
                FilterConfiguration().apply {
                    threshold {
                        minExecutionTime = 20
                    }
                }
            )
            then("Task is not included") {
                assert(!taskFilter.taskLengthFilter(getTask("home", "taskToBeIncluded", 2L)))
            }
        }
        `when`("Threshold filter is applied to a task with long duration") {
            val taskFilter = TaskFilterProcessor(
                logger,
                FilterConfiguration().apply {
                    threshold {
                        minExecutionTime = 200
                    }
                }
            )
            then("Task is included") {
                assert(taskFilter.taskLengthFilter(getTask("lib", "taskToBeIncluded", 300L)))
            }
        }
    }
})

fun getTask(module: String, name: String, duration: Long = 1L) = TaskLength(
    ms = duration,
    taskName = name,
    taskPath = "$module:$name",
    state = TaskMessageState.EXECUTED,
    rootNode = false,
    module = module
)
