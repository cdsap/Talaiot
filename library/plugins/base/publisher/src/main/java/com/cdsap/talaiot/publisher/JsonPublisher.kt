package com.cdsap.talaiot.publisher

import com.cdsap.talaiot.entities.ExecutionReport
import com.cdsap.talaiot.publisher.Publisher
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.gradle.api.invocation.Gradle
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter

class JsonPublisher(val gradle: Gradle) : Publisher {
    override fun publish(report: ExecutionReport) {
        val gson: Gson = GsonBuilder().setPrettyPrinting().create()

        val file = File(gradle.rootProject.buildDir, "reports/talaiot/json/data.json")
            .apply {
                mkdirs()
                delete()
                createNewFile()
            }

        BufferedWriter(FileWriter(file)).use {
            gson.toJson(report, it)
            it.flush()
        }
    }
}