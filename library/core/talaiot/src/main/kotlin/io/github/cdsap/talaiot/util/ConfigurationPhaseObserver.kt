package io.github.cdsap.talaiot.util

import org.gradle.api.provider.ValueSource
import org.gradle.api.provider.ValueSourceParameters
import java.util.concurrent.atomic.AtomicBoolean

abstract class ConfigurationPhaseObserver : ValueSource<Boolean, ValueSourceParameters.None> {
    override fun obtain(): Boolean {
        return configurationExecuted.getAndSet(false)
    }
    companion object {
        private val configurationExecuted = AtomicBoolean()
        fun init() {
            configurationExecuted.set(true)
        }
    }
}
