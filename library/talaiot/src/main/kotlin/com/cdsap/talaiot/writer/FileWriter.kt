package com.cdsap.talaiot.writer

import com.cdsap.talaiot.logger.LogTracker
import org.gradle.api.Project
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

/**
 * I/O contract related to files
 */
interface FileWriter {

    /**
     * Gradle Project used to retrieve the extension
     */
    var project: Project
    /**
     * LogTracker to print in console depending on the Mode
     */
    var logTracker: LogTracker

    /**
     * Previous step where it will invoke the creation of the file depending on the Publisher and the way we want to
     * write on disk
     *
     * @param content it contains the data formatted to be persisted. The implementation gives the specific type
     * @param name filename
     */
    fun prepareFile(content: Any, name: String)

    /**
     * perform the operation of creating a file and write the content independent of the Publisher
     *
     * @param func lambda to be executed after the file is created. Depending on the implementation of the FileWriter
     * the function will be different
     */
    fun createFile(func: () -> Unit) {
        try {
            if (dirExist()) {
                func()
            } else {
                val dir = File(project.rootDir, TALAIOT_OUTPUT_DIR)
                dir.mkdirs()
                func()
            }
        } catch (e: Exception) {
            logTracker.log("FileWriter", "Error -> ${e.message}")
        }
    }

    fun dirExist() = Files.exists(Paths.get("${project.rootDir}/$TALAIOT_OUTPUT_DIR"))

    companion object {
        const val TALAIOT_OUTPUT_DIR = "build/reports/talaiot"
    }
}