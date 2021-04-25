package io.github.cdsap.talaiot.provider

interface Provider<T> {
    fun get(): T
}
