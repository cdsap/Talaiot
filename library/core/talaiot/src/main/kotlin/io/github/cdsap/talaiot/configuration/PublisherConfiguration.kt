package io.github.cdsap.talaiot.configuration

interface PublisherConfiguration {
    var name: String
    var publishBuildMetrics: Boolean
    var publishTaskMetrics: Boolean
}
