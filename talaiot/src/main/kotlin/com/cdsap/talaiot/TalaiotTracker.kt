package com.cdsap.talaiot

import com.cdsap.talaiot.entities.NodeArgument
import com.cdsap.talaiot.entities.TaskLength
import com.cdsap.talaiot.entities.TaskMessageState
import org.gradle.api.Task
import org.gradle.api.tasks.TaskState
import java.util.*

/**
 * Tracker of executed tasks during the build
 * It tracks duration, name, path, dependencies, task state and invoke mode(rootNode)
 */
class TalaiotTracker {
    /**
     * List of tasks executed during the build and tracked
     */
    val taskLengthList = mutableListOf<TaskLength>()
    /**
     * Queue that represents the tasks used in the gradlew/gradle to start the build. We need to register these
     * tasks because we want to aggregate the overall duration for these tasks.
     *
     * Example:
     * ./gradlew assembleDebug test
     * will generate two items in the queue, once the TaskListener will inform about the assembleDebug, the duration is
     * 0 and we want to aggregate the information for all the tasks dependent on assembleDebug.
     *
     */
    var queue = ArrayDeque<NodeArgument>()
    /**
     * HashTable to track the execution time of the task
     */
    private var listOfTasks: HashMap<String, Long> = hashMapOf()
    /**
     * current item retrieved from the queue
     */
    private var currentNode = NodeArgument("", 0, 0)
    /**
     * in cases where Gradle executes the default tasks we don't want to track the build
     */
    var isTracking = false

    /**
     * retrieve the element from the queue
     */
    fun initNodeArgument() {
        isTracking = true
        currentNode = queue.pop()
        currentNode.ms = System.currentTimeMillis()
    }

    /**
     * init the task tracked in the HashTable and increase the counter of the current node
     */
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
    fun finishTrackingTask(task: Task, state: TaskState, workerName: String) {
        val currentTimeMillis = System.currentTimeMillis()
        if (((currentNode.task == task.name) || ((currentNode.task != task.name && currentNode.task == task.path)))) {
            val durationMs = if (currentNode.counter > 1) {
                currentTimeMillis - currentNode.ms
            } else {
                currentTimeMillis - (listOfTasks[task.name] as Long)
            }

            taskLengthList.add(
                taskLength(
                    ms = durationMs,
                    task = task,
                    state = TaskMessageState.EXECUTED,
                    rootNode = currentNode.task != "clean",
                    startMs = currentTimeMillis - durationMs,
                    stopMs = currentTimeMillis,
                    workerId = workerName
                )
            )

            if (!queue.isEmpty()) {
                initNodeArgument()
            }
        } else {
            val durationMs = currentTimeMillis - (listOfTasks[task.name] as Long)
            taskLengthList.add(
                taskLength(
                    ms = durationMs,
                    task = task,
                    state = when (state.skipMessage) {
                        "UP-TO-DATE" -> TaskMessageState.UP_TO_DATE
                        "FROM-CACHE" -> TaskMessageState.FROM_CACHE
                        "NO-SOURCE" -> TaskMessageState.NO_SOURCE
                        else -> TaskMessageState.EXECUTED
                    },
                    rootNode = false,
                    startMs = currentTimeMillis - durationMs,
                    stopMs = currentTimeMillis,
                    workerId = workerName
                )
            )
        }

    }

    /**
     * Helper function to return one TaskLength instance of the current task
     *
     * @param ms duration of the execution
     * @param task current task
     * @param state Custom TaskState
     * @param rootNode is the current task a rootNode
     * @param startMs timestamp of start
     * @param stopMs timestamp of stop
     * @param workerId unique id of the gradle [org.gradle.workers.internal.Worker] that executed this task
     *
     * @return instance of TaskLength for the current task
     */
    private fun taskLength(
        ms: Long,
        task: Task,
        state: TaskMessageState,
        rootNode: Boolean,
        startMs: Long,
        stopMs: Long,
        workerId: String
    ): TaskLength =
        TaskLength(
            ms = ms,
            taskName = task.name,
            taskPath = task.path,
            state = state,
            rootNode = rootNode,
            module = getModule(task.path),
            taskDependencies = taskDependencies(task),
            workerId = workerId,
            startMs = startMs,
            stopMs = stopMs
        )


    /**
     * Retrieve the dependencies of a given task
     *
     * @param task current task
     *
     * @return list of task paths depending of the given task
     */
    private fun taskDependencies(task: Task) = task.taskDependencies.getDependencies(task).map { it.path }

    /**
     * Get the current module of a given task
     *
     * @param path complete of a given task
     *
     * @return module of the task, in case it doesn't exist it will return "no_module"
     */
    private fun getModule(path: String): String {
        val module = path.split(":")

        return if (module.size > 2) module.toList()
            .dropLast(1)
            .joinToString(separator = ":")
        else "no_module"
    }
}

