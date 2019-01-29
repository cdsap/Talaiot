package com.cdsap.talaiot

import com.cdsap.talaiot.configuration.IgnoreWhenConfiguration
import com.nhaarman.mockitokotlin2.*
import io.kotlintest.specs.BehaviorSpec
import org.gradle.api.Project
import org.gradle.api.invocation.Gradle
import org.gradle.api.plugins.ExtensionContainer

class TalaiotPluginTest : BehaviorSpec({
    given(("a Talaiot Plugin")) {
        `when`("configuration is applied ") {
            then("listener is added to the build") {
                val talaiotPlug = TalaiotPlugin()
                val project: Project = mock()
                val gradle: Gradle = mock()
                val extension = TalaiotExtension(project)
                val ignore = IgnoreWhenConfiguration(project)
                ignore.envName = "1"
                ignore.envValue = "2"
                val extensionContainer: ExtensionContainer = mock()
                extension.ignoreWhen = ignore
                whenever(extensionContainer.create("talaiot", TalaiotExtension::class.java, project)).thenReturn(
                    extension
                )
                whenever(gradle.rootProject).thenReturn(project)
                whenever(gradle.gradleVersion).thenReturn("5.1")
                whenever(project.gradle).thenReturn(gradle)
                whenever(project.name).thenReturn("test1")
                whenever(project.extensions).thenReturn(extensionContainer)
                whenever(extensionContainer.getByName("talaiot")).thenReturn(extension)
                talaiotPlug.apply(project)
                verify(project.gradle).addBuildListener(any())

            }
        }
    }
}
)
