package com.cdsap.talaiot.resources

object TaskDependenciesXml {
    const val HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\\n\" +\n" +
            "   <gexf xmlns=\"http://www.gexf.net/1.2draft\" version=\"1.3\">\n" +
            "    <meta lastmodifieddate=\"2009-03-20\">\n" +
            "   <creator>Talaiot</creator>\n" +
            "  <description>Graph Depenency</description>\n" +
            " </meta>\n" +
            "  <graph mode=\"static\" defaultedgetype=\"directed\">"
    const val FOOTER = "</graph>\n" +
            "</gexf>"
}