package io.github.cdsap.talaiot.publisher

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import io.github.cdsap.talaiot.entities.ExecutionReport
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
