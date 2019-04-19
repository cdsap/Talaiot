package com.cdsap.talaiot.publisher.taskdependencygraph.composer


import com.cdsap.talaiot.entities.TaskMeasurementAggregated
import com.cdsap.talaiot.logger.LogTracker
import com.cdsap.talaiot.publisher.taskdependencygraph.resources.ResourcesGexf
import com.cdsap.talaiot.writer.FileWriter
import guru.nidi.graphviz.attribute.Color
import guru.nidi.graphviz.engine.Format
import guru.nidi.graphviz.engine.Graphviz
import guru.nidi.graphviz.attribute.RankDir
import guru.nidi.graphviz.attribute.Style
import guru.nidi.graphviz.model.Factory.*
import guru.nidi.graphviz.model.Node
import java.io.File


class DotComposer(
    val logger: LogTracker,
    writter: FileWriter
) : ContentComposer<Node, Node> {
    override var logTracker: LogTracker
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
        set(value) {}
    override var fileWriter: FileWriter
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
        set(value) {}


    override fun formatNode(
        internalId: Int,
        module: String,
        taskName: String,
        numberDependencies: Int,
        cached: Boolean
    ): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    private val fileName: String = ""

    override fun compose(measurementAggregated: TaskMeasurementAggregated) {

        val content = contentComposer(
            buildGraph(measurementAggregated), ResourcesGexf.HEADER,
            ResourcesGexf.FOOTER
        )
        writeFile(content, fileName)
        val g = graph("example1").directed()
            .graphAttr().with(RankDir.LEFT_TO_RIGHT)
            .with(
                node
            )
            .with(
                node("a").with().with(Color.RED).link(node("b")),
                //       node("b").link(to(node("c")).with(Style.DASHED))
            )
        Graphviz.fromGraph(g).width(200).render(Format.PNG).toFile(File("example/ex1i.png"))

    }

//    override fun formatNode(internalId: Int, module: String, taskName: String, numberDependencies: Int): String =
//        session
//            .run(write("CREATE ($internalId:Task {id:\"$internalId\", name:\"$taskName\"})"))
//            .toString()


    override fun formatEdge(
        from: Int,
        to: Int?
    ): Node = ""
}
