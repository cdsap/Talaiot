package com.cdsap.talaiot.provider

import com.cdsap.talaiot.publisher.Publisher

interface PublisherConfigurationProvider {
    fun get(): List<Publisher>
}