package com.cdsap.talaiot.publisher.graph.writer

import com.cdsap.talaiot.logger.LogTracker
import com.cdsap.talaiot.publisher.graph.writer.FileWriter.Companion.TALAIOT_OUTPUT_DIR
import org.gradle.api.Project
import java.io.File
import java.nio.file.Files

/**
 * Implementation of FileWriter to writes bytes to a file
 */
class TaskGraphWriter(
    override var project: Project,
    override var logTracker: LogTracker
) : FileWriter {
    override fun prepareFile(content: Any, name: String) {
        val path = listOf(project.rootDir, TALAIOT_OUTPUT_DIR, "taskgraph").joinToString(separator = File.separator)
        val dir = File(path).apply {
            mkdirs()
        }
        val file = File(dir, name)

        createFile {
            if (content is String) {
                Files.write(file.toPath(), content.toByteArray())
            }
        }
    }
}