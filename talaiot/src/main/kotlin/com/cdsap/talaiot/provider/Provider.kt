package com.cdsap.talaiot.provider

interface Provider<T> {
    fun get(): T
}
