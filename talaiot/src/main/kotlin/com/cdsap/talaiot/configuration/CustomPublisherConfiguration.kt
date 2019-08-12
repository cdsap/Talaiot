package com.cdsap.talaiot.configuration

import com.cdsap.talaiot.publisher.Publisher

class CustomPublisherConfiguration : PublisherConfiguration {
    override var name = "CustomPublisherConfiguration"
    override var publishBuildMetrics = true
    override var publishTaskMetrics = true
    val publisher: Publisher? = null
}