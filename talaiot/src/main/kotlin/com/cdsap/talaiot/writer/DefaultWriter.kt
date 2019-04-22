package com.cdsap.talaiot.writer

import com.cdsap.talaiot.writer.FileWriter.Companion.TALAIOT_OUTPUT_DIR
import org.gradle.api.Project
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

class DefaultWriter(override var project: Project) : FileWriter<String> {
    override fun prepareFile(content: String, name: String) {
        val fileName = File("${project.rootDir}/$TALAIOT_OUTPUT_DIR/$name")
        createFile {
            Files.write(fileName.toPath(), content.toByteArray())
        }
    }
}