package io.github.cdsap.talaiot.entities

import java.io.Serializable

data class Processes(
    val listKotlinProcesses: List<io.github.cdsap.jdk.tools.parser.model.Process> = emptyList(),
    val listGradleProcesses: List<io.github.cdsap.jdk.tools.parser.model.Process> = emptyList()
) : Serializable
