package com.cdsap.talaiot.publisher.taskdependencygraph.resources

object ResourcesGexf {
    const val HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<gexf xmlns=\"http://www.gexf.net/1.2draft\" version=\"1.3\">\n" +
            "    <meta>\n" +
            "        <creator>Talaiot</creator>\n" +
            "        <description>Graph Dependency</description>\n" +
            "    </meta>\n" +
            "    <graph mode=\"static\" defaultedgetype=\"directed\">" +
            "       <attributes class=\"node\">\n" +
            "          <attribute id=\"0\" title=\"module\" type=\"string\"/>\n" +
            "          <attribute id=\"1\" title=\"cached\" type=\"boolean\">\n" +
            "                <default>false</default>\n" +
            "           </attribute>\n" +
            "       </attributes>"
    const val FOOTER = "    </taskdependencygraph>\n" +
            "</gexf>"
}
