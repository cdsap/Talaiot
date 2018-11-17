package com.agoda.gradle.tracking

import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL


class OutputStrategy : ReportingStrategy {

    override fun send(measurementAggregated: TaskMeasurementAggregated) {
        measurementAggregated.apply {
            println("================")
            println("OutputReporting")
            println("================")
            println("User ${this.user}")
            println("Branch ${this.branch}")
            println("Os ${this.os}")
            println("Gradle Version ${this.gradleVersion}")
            val orderedTiming = quicksort(this.taskMeasurment)
            val max = orderedTiming.last().ms
            MAX_UNIT.length
            quicksort(this.taskMeasurment).forEach {
                val x = (it.ms * MAX_UNIT.length) / max
                val s = MAX_UNIT.substring(0, x.toInt())
                println("$s ${it.path} ---- ${it.ms}")
            }
        }
    }

    fun quicksort(items: List<TaskLenght>): List<TaskLenght> {
        if (items.count() < 2) {
            return items
        }
        val pivot = items[items.count() / 2].ms
        val equal = items.filter { it.ms == pivot }
        val less = items.filter { it.ms < pivot }
        val greater = items.filter { it.ms > pivot }
        return quicksort(less) + equal + quicksort(greater)
    }

    companion object {
        const val MAX_UNIT = "¯\\_(ツ)_/¯ ¯\\_(ツ)_/¯ ¯\\_(ツ)_/¯ ¯\\_(ツ)_/¯ ¯\\_(ツ)_/¯"
    }
}