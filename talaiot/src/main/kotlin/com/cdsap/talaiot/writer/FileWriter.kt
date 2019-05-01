package com.cdsap.talaiot.writer

import com.cdsap.talaiot.logger.LogTracker
import org.gradle.api.Project
import java.io.File
import java.lang.Exception
import java.nio.file.Files
import java.nio.file.Paths

interface FileWriter {
    var project: Project
    var logTracker: LogTracker

    fun prepareFile(content: Any, name: String)

    fun createFile(func: () -> Unit) {
        try {
            if (dirExist()) {
                func()
            } else {
                val dir = File("${project.rootDir}/$TALAIOT_OUTPUT_DIR")
                dir.mkdir()
                func()
            }
        } catch (e: Exception) {
            logTracker.log("FileWriter: Error -> ${e.message}")
        }
    }

    fun dirExist() = Files.exists(Paths.get("${project.rootDir}/$TALAIOT_OUTPUT_DIR"))

    companion object {
        const val TALAIOT_OUTPUT_DIR = "talaiot"
    }
}