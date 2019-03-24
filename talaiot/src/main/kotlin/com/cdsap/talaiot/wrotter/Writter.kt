package com.cdsap.talaiot.wrotter

import org.gradle.api.Project
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

class Writter(val project: Project) {
    var path = project.buildDir

    fun createFile(conent: String, name: String) {
        val fileName = File("${project.rootDir}/$TALAIOT_OUTPUT/$name")
        if (dirExist()) {
            Files.write(fileName.toPath(), conent.toByteArray())

        } else {
            val s = File("${project.rootDir}/$TALAIOT_OUTPUT")
            s.mkdir()
            Files.write(fileName.toPath(), name.toByteArray())
        }
    }


    fun dirExist() = Files.exists(Paths.get("${project.rootDir}/$TALAIOT_OUTPUT"))

    companion object {
        const val TALAIOT_OUTPUT = "talaiot"
    }
}