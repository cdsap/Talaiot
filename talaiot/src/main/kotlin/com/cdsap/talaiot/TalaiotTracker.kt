package com.cdsap.talaiot

import com.cdsap.talaiot.entities.TaskLength
import com.cdsap.talaiot.entities.TaskMessageState
import org.gradle.api.Task
import org.gradle.api.tasks.TaskState
import java.util.*

class TalaiotTracker {
    val taskLengthList = mutableListOf<TaskLength>()
    var queue = ArrayDeque<NodeArgument>()
    private var listOfTasks: HashMap<String, Long> = hashMapOf()
    private var currentNode = NodeArgument("", 0, 0)

    fun initNodeArgument() {
        currentNode = queue.pop()
        currentNode.ms = System.currentTimeMillis()
    }

    fun startTrackingTask(task: Task) {

        listOfTasks[task.name] = System.currentTimeMillis()
        currentNode.counter++
    }

    /**
     * Compute the total time of the task and aggregate the
     * rootNodes launched by the user.
     * Clean is handled as exception, by default task name and task argument on general
     * clean tasks are the same.
     */
    fun finishTrackingTask(task: Task, state: TaskState) {
        if (((currentNode.task == task.name) || ((currentNode.task != task.name && currentNode.task == task.path)))) {
            val ms = if (currentNode.counter > 1) {
                System.currentTimeMillis() - currentNode.ms
            } else {
                System.currentTimeMillis() - (listOfTasks[task.name] as Long)
            }

            taskLengthList.add(
                taskLength(ms, task, TaskMessageState.EXECUTED, currentNode.task != "clean")
            )

            if (!queue.isEmpty()) {
                initNodeArgument()
            }
        } else {
            val ms = System.currentTimeMillis() - (listOfTasks[task.name] as Long)
            taskLengthList.add(
                taskLength(
                    ms, task, when (state.skipMessage) {
                        "UP-TO-DATE" -> TaskMessageState.UP_TO_DATE
                        "FROM-CACHE" -> TaskMessageState.FROM_CACHE
                        "NO-SOURCE" -> TaskMessageState.NO_SOURCE
                        else -> TaskMessageState.EXECUTED
                    }, false
                )
            )
        }

    }

    private fun taskLength(ms: Long, task: Task, state: TaskMessageState, rootNode: Boolean): TaskLength {
        return TaskLength(
            ms = ms,
            taskName = task.name,
            taskPath = task.path,
            module = getModule(task.path),
            state = state,
            rootNode = rootNode,
            taskDependencies = taskDependencies(task)
        )
    }

    private fun taskDependencies(task: Task) = task.taskDependencies.getDependencies(task).map { it.path }
}

data class NodeArgument(val task: String, var ms: Long, var counter: Int)

private fun getModule(path: String): String {
    val module = path.split(":")

    return if (module.size > 2) module.toList()
        .dropLast(1)
        .joinToString(separator = ":")
    else "no_module"
}
