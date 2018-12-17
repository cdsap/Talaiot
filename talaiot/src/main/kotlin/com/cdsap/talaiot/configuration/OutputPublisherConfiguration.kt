package com.cdsap.talaiot.configuration

import com.cdsap.talaiot.configuration.PublisherConfiguration

class OutputPublisherConfiguration : PublisherConfiguration {
    override var name: String = "output"
    var enabled: Boolean = true
}