package io.github.cdsap.talaiot

import io.github.cdsap.talaiot.entities.TaskLength
import io.github.cdsap.talaiot.entities.TaskMessageState
import org.gradle.api.provider.Property
import org.gradle.api.services.BuildService
import org.gradle.api.services.BuildServiceParameters
import org.gradle.tooling.events.FinishEvent
import org.gradle.tooling.events.OperationCompletionListener

abstract class TalaiotBuildService : BuildService<TalaiotBuildService.Params>, AutoCloseable,
    OperationCompletionListener {

    interface Params : BuildServiceParameters {
        val target: Property<String>
    }

    //    private val talaiotTracker = TalaiotTracker()
//    private var configurationEnd: Long? = null
//    private var start: Long? = null
    val taskLengthList = mutableListOf<TaskLength>()

    init {
        println("starte")
        //    configurationEnd = System.currentTimeMillis()
        //   start = System.currentTimeMillis()
        //  println(configurationEnd)
    }

    override fun close() {
        println("out of service")
        taskLengthList.forEach {
            println(it.toString())
        }
//        parameters.target.get().publish(
//            taskLengthList,
//            start!!,
//            start!! * 2,
//            System.currentTimeMillis(),
//            true
//        )
//        parameters.target.get().publish(
//            tas
//            taskLengthList: MutableList<TaskLength>,
//        start: Long,
//        configuraionMs: Long?,
//        end: Long,
//        success: Boolean
//            t
//        )

    }


    override fun onFinish(p0: FinishEvent?) {

        println(p0.toString())

        //    println("onfinish")
        val duration = p0?.result?.endTime!! - p0?.result?.startTime!!
        val end = p0?.result?.endTime!!
        val start = p0?.result?.startTime!!
        val task = p0?.descriptor?.name.toString()
        val state = p0?.displayName.split(" ")[2]
        println("duration $duration")
        println("end $end")
        println("start $start")
        println("task   $task")
        println("state   $state")

        taskLengthList.add(
            taskLength(
                ms = duration,
                task = task,
                state = when (state) {
                    "UP-TO-DATE" -> TaskMessageState.UP_TO_DATE
                    "FROM-CACHE" -> TaskMessageState.FROM_CACHE
                    "NO-SOURCE" -> TaskMessageState.NO_SOURCE
                    else -> TaskMessageState.EXECUTED
                },
                rootNode = false,
                startMs = start,
                stopMs = end,
                workerId = "test worker"
            )
        )

    }
}

    private fun taskLength(
        ms: Long,
        task: String,
        state: TaskMessageState,
        rootNode: Boolean,
        startMs: Long,
        stopMs: Long,
        workerId: String
    ): TaskLength =
        TaskLength(
            ms = ms,
            taskName = task,
            taskPath = task,
            state = state,
            rootNode = rootNode,
            module = getModule(task),
            taskDependencies = emptyList(),
            workerId = workerId,
            startMs = startMs,
            stopMs = stopMs
        )

//
//    private fun taskDependencies(task: Task): List<String> =
//        try {
//            task.taskDependencies.getDependencies(task).map { it.path }
//        } catch (e: TaskDependencyResolveException) {
//            emptyList()
//        }
//
private fun getModule(path: String): String {
    val module = path.split(":")

    return if (module.size > 2) module.toList()
        .dropLast(1)
        .joinToString(separator = ":")
    else "no_module"
}

