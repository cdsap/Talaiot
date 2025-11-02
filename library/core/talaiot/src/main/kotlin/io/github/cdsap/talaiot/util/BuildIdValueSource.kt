package io.github.cdsap.talaiot.util

import org.gradle.api.provider.ValueSource
import org.gradle.api.provider.ValueSourceParameters
import java.util.UUID

abstract class BuildIdValueSource : ValueSource<String, ValueSourceParameters.None> {
    override fun obtain(): String {
        return UUID.randomUUID().toString()
    }
}
