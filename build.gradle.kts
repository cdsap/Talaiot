subprojects {
    // Address https://github.com/gradle/gradle/issues/4823: Force parent project evaluation before sub-project evaluation for Kotlin build scripts
    if (gradle.startParameter.isConfigureOnDemand
        && buildscript.sourceFile?.extension?.toLowerCase() == "kts"
        && parent != rootProject) {
        generateSequence(parent) { project -> project.parent.takeIf { it != rootProject } }
            .forEach { evaluationDependsOn(it.path) }
    }
}