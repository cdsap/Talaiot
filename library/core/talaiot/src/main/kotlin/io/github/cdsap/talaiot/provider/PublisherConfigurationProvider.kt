package io.github.cdsap.talaiot.provider

import io.github.cdsap.talaiot.publisher.Publisher

interface PublisherConfigurationProvider : java.io.Serializable {
    fun get(): List<Publisher>
}
