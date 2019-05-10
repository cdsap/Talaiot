package com.cdsap.talaiot.publisher.graphpublisher.resources


/**
 * HEADER and VALUE constants used by the HtmlPublisher
 *
 */
object ResourcesHtml {
    const val HEADER = "<!doctype html>\n" +
            "<html>\n" +
            "<head>\n" +
            "  <title>Talaiot</title>\n" +
            "  <script type=\"text/javascript\" src=\"https://cdnjs.cloudflare.com/ajax/libs/vis/4.21.0/vis.min.js\"></script>\n" +
            "  <link href=\"https://cdnjs.cloudflare.com/ajax/libs/vis/4.21.0/vis.min.css\" rel=\"stylesheet\" type=\"text/css\" />\n" +
            "  <style type=\"text/css\">\n" +
            "    #talaiot {\n" +
            "      width: 100%;\n" +
            "      height: 1000px;\n" +
            "      solid lightgray;\n" +
            "    }\n" +
            "  </style>\n" +
            "</head>\n" +
            "\n" +
            "<body onload=\"draw();\">\n" +
            "  <div id=\"talaiot\"></div>\n" +
            "\n" +
            "  <script type=\"text/javascript\">\n" +
            "    var network = null;\n" +
            "    var layoutMethod = \"directed\";\n" +
            "\n" +
            "    function destroy() {\n" +
            "      if (network !== null) {\n" +
            "        network.destroy();\n" +
            "        network = null;\n" +
            "      }\n" +
            "    }\n" +
            "\n" +
            "    function draw() {\n" +
            "      destroy();\n" +
            "      var nodes = [];\n" +
            "      var edges = [];\n"
    const val FOOTER = "      var container = document.getElementById('talaiot');\n" +
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
            "            nodeSpacing: 800,\n" +
            "            treeSpacing: 200,\n" +
            "            blockShifting: true,\n" +
            "            edgeMinimization: false,\n" +
            "            parentCentralization: true,\n" +
            "            direction: 'LR',\n" +
            "            sortMethod: 'hubsize'\n" +
            "          }\n" +
            "        },\n" +
            "        nodes: {\n" +
            "          shape: 'ellipse',\n" +
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

    const val LEGEND_HEADER = "      var x = - talaiot.clientWidth / 2 + 50;\n" +
            "      var y = - talaiot.clientHeight / 2 + 50;\n" +
            "      var step = 70;\n"
}