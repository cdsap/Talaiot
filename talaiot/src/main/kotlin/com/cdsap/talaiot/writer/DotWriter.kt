package com.cdsap.talaiot.writer

import com.cdsap.talaiot.logger.LogTracker
import com.cdsap.talaiot.writer.FileWriter.Companion.TALAIOT_OUTPUT_DIR
import guru.nidi.graphviz.engine.Renderer
import org.gradle.api.Project
import java.io.File

/**
 * Implementation of FileWriter using Renderer from GraphViz library
 */
class DotWriter(
    override var project: Project,
    override var logTracker: LogTracker
) : FileWriter {
    override fun prepareFile(content: Any, name: String) {
        val fileName = File("${project.rootDir}/$TALAIOT_OUTPUT_DIR/$name")
        createFile {
            if (content is Renderer) {
                content.toFile(fileName)
            }
        }
    }
}
