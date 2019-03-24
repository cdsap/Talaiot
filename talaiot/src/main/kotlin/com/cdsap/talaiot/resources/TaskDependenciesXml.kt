package com.cdsap.talaiot.resources

object TaskDependenciesXml {
    const val HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\\n\" +\n" +
            "   <gexf xmlns=\"http://www.gexf.net/1.2draft\" version=\"1.2\">\n" +
            "    <meta lastmodifieddate=\"2009-03-20\">\n" +
            "   <creator>Talaiot</creator>\n" +
            "  <description>Graph Depenency</description>\\\n" +
            " </meta>\n" +
            "  <graph mode=\"static\" defaultedgetype=\"directed\">"
    const val FOOTER = "      var container = document.getElementById('mynetwork');\n" +
            "      var data = {\n" +
            "        nodes: nodes,\n" +
            "        edges: edges\n" +
            "      };\n" +
            "\n" +
            "      var options = {\n" +
            "        physics: {\n" +
            "          enabled: false\n" +
            "        },\n" +
            "        layout: {\n" +
            "          improvedLayout: true,\n" +
            "          hierarchical: {\n" +
            "            enabled: true,\n" +
            "            levelSeparation: 200,\n" +
            "            nodeSpacing: 600,\n" +
            "            treeSpacing: 200,\n" +
            "            blockShifting: true,\n" +
            "            edgeMinimization: false,\n" +
            "            parentCentralization: true,\n" +
            "            direction: 'LR', // UD, DU, LR, RL\n" +
            "            sortMethod: 'hubsize' // hubsize, directed\n" +
            "          }\n" +
            "        },\n" +
            "        nodes: {\n" +
            "          shape: 'dot',\n" +
            "          margin: 10,\n" +
            "          color: {\n" +
            "            border: '#2B7CE9',\n" +
            "            background: '#97C2FC',\n" +
            "            highlight: {\n" +
            "              border: '#cc4CE9',\n" +
            "              background: '#D2E5FF'\n" +
            "            },\n" +
            "            hover: {\n" +
            "              border: '#2B7CE9',\n" +
            "              background: '#D2E5FF'\n" +
            "            }\n" +
            "          },\n" +
            "\n" +
            "        },\n" +
            "        edges: {\n" +
            "          smooth: true,\n" +
            "          arrows: {\n" +
            "            from: true\n" +
            "          },\n" +
            "          color: {\n" +
            "            color: '#848484',\n" +
            "            highlight: '#cc2233',\n" +
            "            hover: '#848484',\n" +
            "            inherit: 'from',\n" +
            "            opacity: 1.0\n" +
            "          },\n" +
            "        }\n" +
            "      };\n" +
            "      network = new vis.Network(container, data, options);\n" +
            "    }\n" +
            "</script>\n"
}