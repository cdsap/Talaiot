package com.cdsap.talaiot.reporter

import com.agoda.gradle.tracking.entities.TaskLenght
import com.agoda.gradle.tracking.entities.TaskMeasurementAggregated


class OutputReporter : Reporter {

    override fun send(measurementAggregated: TaskMeasurementAggregated) {
        measurementAggregated.apply {
            println("================")
            println("OutputReporting")
            println("================")
            println("User ${this.user}")
            println("Branch ${this.branch}")
            println("Os ${this.os}")
            println("Project ${this.project}")
            println("Gradle Version ${this.gradleVersion}")
            val orderedTiming = sort(taskMeasurment)
            if(!orderedTiming.isEmpty()) {
                val max = orderedTiming.last().ms
                MAX_UNIT.length
                sort(taskMeasurment).forEach {
                    val x = (it.ms * MAX_UNIT.length) / max
                    val s = MAX_UNIT.substring(0, x.toInt())
                    println("$s ${it.path} ---- ${it.ms}")
                }
            }
        }
    }

    fun sort(items: List<TaskLenght>): List<TaskLenght> {
        if (items.count() < 2) {
            return items
        }
        val pivot = items[items.count() / 2].ms
        val equal = items.filter { it.ms == pivot }
        val less = items.filter { it.ms < pivot }
        val greater = items.filter { it.ms > pivot }
        return sort(less) + equal + sort(greater)
    }

    companion object {
        const val MAX_UNIT = "¯\\_(ツ)_/¯ ¯\\_(ツ)_/¯ ¯\\_(ツ)_/¯ ¯\\_(ツ)_/¯ ¯\\_(ツ)_/¯"
    }
}
