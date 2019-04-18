package com.cdsap.talaiot.publisher.taskdependencygraph.resources

object ResourcesGexf {
    const val HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<gexf xmlns=\"http://www.gexf.net/1.2draft\" version=\"1.3\">\n" +
            "    <meta>\n" +
            "        <creator>Talaiot</creator>\n" +
            "        <description>Graph Dependency</description>\n" +
            "    </meta>\n" +
            "    <taskdependencygraph mode=\"static\" defaultedgetype=\"directed\">"
    const val FOOTER = "    </taskdependencygraph>\n" +
            "</gexf>"
}
