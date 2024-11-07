package io.github.cdsap.talaiot.metrics

import io.github.cdsap.valuesourceprocess.CommandLineWithOutputValue
import org.gradle.api.Project

class GitUserMetric(val project: Project) : SimpleMetric<String>(
    provider = {
        project.providers.of(CommandLineWithOutputValue::class.java) {
            it.parameters.commands.set("git config --get user.name")
        }.get().replace("\n", "")
    },
    assigner = { report, value -> report.environment.gitUser = value }
)
