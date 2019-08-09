package com.cdsap.talaiot.configuration

class HybridPublisherConfiguration : PublisherConfiguration {

    /**
     * name of the publisher
     */
    override var name: String = "hybrid"

    var taskPublisher: PublisherConfiguration? = null

    var buildPublisher: PublisherConfiguration? = null

}
