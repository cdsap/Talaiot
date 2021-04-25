package io.github.cdsap.talaiot.publisher.graph.writer

import io.github.cdsap.talaiot.logger.LogTracker
import io.github.cdsap.talaiot.publisher.graph.writer.FileWriter.Companion.TALAIOT_OUTPUT_DIR
import guru.nidi.graphviz.engine.Renderer
import org.gradle.api.Project
import java.io.File

/**
 * Implementation of FileWriter using Renderer from GraphViz library
 */
class DotWriter(
    override var project: Project,
    override var logTracker:LogTracker
) : FileWriter {
    override fun prepareFile(content: Any, name: String) {
        val path = listOf(project.rootDir, TALAIOT_OUTPUT_DIR, "taskgraph").joinToString(separator = File.separator)
        val dir = File(path).apply {
            mkdirs()
        }
        val file = File(dir, name)

        createFile {
            if (content is Renderer) {
                content.toFile(file)
            }
        }
    }
}
