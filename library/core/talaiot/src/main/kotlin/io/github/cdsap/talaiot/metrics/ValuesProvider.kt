package io.github.cdsap.talaiot.metrics

interface ValuesProvider {
    fun get(): Map<String, Any>
}
