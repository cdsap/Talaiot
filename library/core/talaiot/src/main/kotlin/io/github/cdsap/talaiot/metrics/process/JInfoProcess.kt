package io.github.cdsap.talaiot.metrics.process

/* Parse output of jinfo command for a given type of process

 */
class JInfoProcess {
    fun parseJInfoData(content: String): Map<String, Map<String, String>> {
        val mapInfoProcesses = mutableMapOf<String, Map<String, String>>()
        val lines = content.split("\n")
        if (lines.last().trim() == "") {
            lines.dropLast(1)
        }
        val numberOfProcess = lines.size / 2
        var auxIndex = 0
        for (i in 0 until numberOfProcess) {
            val flags = parseVmFlags(lines[auxIndex])
            val process = lines[++auxIndex].split("\\s+".toRegex()).first()
            mapInfoProcesses[process] = flags
            auxIndex++
        }
        return mapInfoProcesses
    }

    private fun parseVmFlags(vmFlags: String): Map<String, String> {
        val flags = mutableMapOf<String, String>()
        vmFlags.trim().split(" ").forEach { flag ->
            if (flag.contains("=")) {
                val property = flag.split("=")
                flags[property[0]] = property[1]
            } else {
                flags[flag] = "true"
            }
        }
        return flags
    }
}
