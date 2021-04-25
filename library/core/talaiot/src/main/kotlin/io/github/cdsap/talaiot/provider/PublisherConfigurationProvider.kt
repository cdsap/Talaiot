package io.github.cdsap.talaiot.provider

import io.github.cdsap.talaiot.publisher.Publisher

interface PublisherConfigurationProvider {
    fun get(): List<Publisher>
}