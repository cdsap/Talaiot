package com.cdsap.talaiot.publisher.output

import com.cdsap.talaiot.publisher.PublisherConfiguration

class OutputPublisherConfiguration : PublisherConfiguration {
    override var name: String = "output"
    var enabled: Boolean = true
}