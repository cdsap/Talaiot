package io.github.cdsap.talaiot.metrics

import io.github.cdsap.valuesourceprocess.CommandLineWithOutputValue
import org.gradle.api.Project


class GitBranchMetric(val project: Project) : SimpleMetric<String>(
    provider = {
        project.providers.of(CommandLineWithOutputValue::class.java) {
            it.parameters.commands.set("git rev-parse --abbrev-ref HEAD")
        }.get().replace("\n","")
    },
    assigner = { report, value -> report.environment.gitBranch = value }
)

class GitUserMetric(val project: Project) : SimpleMetric<String>(
    provider = {
        project.providers.of(CommandLineWithOutputValue::class.java) {
            it.parameters.commands.set("git config --get user.name")
        }.get().replace("\n","")
    },
    assigner = { report, value -> report.environment.gitUser = value }
)
