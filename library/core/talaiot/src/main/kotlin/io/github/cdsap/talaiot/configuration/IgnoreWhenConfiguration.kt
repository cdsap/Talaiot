package io.github.cdsap.talaiot.configuration

import org.gradle.api.Project

/**
 * Configuration for the ignoreWhen property. Used in General Talaiot configuration and TaskDependencyGraph.
 * It expects an environment Name and Value variables.
 * In some scenarios we want to ignore the execution of the Publishers, for instance we don't want to execute it in
 * CI environments.
 * We can define this ignore rule:
 * ignoreWhen{
 *    envName = "CI"
 *    envValue = "true"
 * }
 */
class IgnoreWhenConfiguration(private val project: Project) {
    /**
     * name of the environment variable or property project
     */
    var envName: String = ""

    /**
     * value of the environment variable or property project
     */
    var envValue: String = ""

    /**
     * Given a name and value not empty it will check if:
     * 1st: there is an environment variable defined with the same name, in that case will check if the value matches.
     * 2nd: in case there is no environment variable it will check if there is one property in the project with the given
     * name, in that case will check if the value matches
     *
     * @returns Boolean in case the condition is meet
     */
    fun shouldIgnore(): Boolean {
        return if (!envName.isEmpty()) {
            if (System.getenv(envName) != null) {
                System.getenv(envName) == envValue
            } else {
                (project.hasProperty(envName) && project.property(envName) != null) && project.property(envName).toString() == envValue
            }
        } else {
            false
        }
    }
}
