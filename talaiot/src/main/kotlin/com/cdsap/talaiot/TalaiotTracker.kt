package com.cdsap.talaiot

import com.cdsap.talaiot.entities.TaskLength
import com.cdsap.talaiot.entities.TaskMessageState
import org.gradle.api.Task
import org.gradle.api.tasks.TaskState
import java.util.*

class TalaiotTracker {
    internal val taskLengthList = mutableListOf<TaskLength>()
    internal var queue = ArrayDeque<NodeArgument>()
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

    fun finishTrackingTask(task: Task, state: TaskState) {

        if (currentNode.task == task.name) {
            val ms = if (currentNode.counter > 1) {
                System.currentTimeMillis() - currentNode.ms
            } else {
                System.currentTimeMillis() - (listOfTasks[task.name] as Long)
            }

            taskLengthList.add(
                TaskLength(
                    ms = ms,
                    taskName = task.name,
                    state = TaskMessageState.EXECUTED,
                    rootNode = true
                )
            )

            if (!queue.isEmpty()) {
                initNodeArgument()
            }
        } else {
            val ms = System.currentTimeMillis() - (listOfTasks[task.name] as Long)
            taskLengthList.add(
                TaskLength(
                    ms = ms,
                    taskName = task.name,
                    state = when (state.skipMessage) {
                        "UP-TO-DATE" -> TaskMessageState.UP_TO_DATE
                        "FROM-CACHE" -> TaskMessageState.FROM_CACHE
                        "NO-SOURCE" -> TaskMessageState.NO_SOURCE
                        else -> TaskMessageState.EXECUTED
                    }
                )
            )
        }

    }
}

data class NodeArgument(val task: String, var ms: Long, var counter: Int)