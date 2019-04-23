package com.cdsap.talaiot.writer

import org.gradle.api.Project
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

interface FileWriter {
    var project: Project

    fun prepareFile(content: Any, name: String)

    fun createFile(func: () -> Unit) {
        if (dirExist()) {
            func()
        } else {
            val dir = File("${project.rootDir}/$TALAIOT_OUTPUT_DIR")
            dir.mkdir()
            func()
        }
    }

    fun dirExist() = Files.exists(Paths.get("${project.rootDir}/$TALAIOT_OUTPUT_DIR"))

    companion object {
        const val TALAIOT_OUTPUT_DIR = "talaiot"
    }
}