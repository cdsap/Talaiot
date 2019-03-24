package com.cdsap.talaiot.configuration

class GefxConfiguration : PublisherConfiguration {
    override var name: String = "influxDb"
    var dbName: String = ""
    var url: String = ""
    var urlMetric: String = ""
}