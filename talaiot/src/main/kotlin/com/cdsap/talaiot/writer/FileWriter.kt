package com.cdsap.talaiot.writer

import org.gradle.api.Project
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

class FileWriter(val project: Project) {

    fun createFile(conent: String, name: String) {
        val fileName = File("${project.rootDir}/$TALAIOT_OUTPUT_DIR/$name")
        if (dirExist()) {
            Files.write(fileName.toPath(), conent.toByteArray())

        } else {
            val s = File("${project.rootDir}/$TALAIOT_OUTPUT_DIR")
            s.mkdir()
            Files.write(fileName.toPath(), name.toByteArray())
        }
    }

    private fun dirExist() = Files.exists(Paths.get("${project.rootDir}/$TALAIOT_OUTPUT_DIR"))

    companion object {
        const val TALAIOT_OUTPUT_DIR = "talaiot"
    }
}