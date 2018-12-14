package com.cdsap.talaiot.publisher.influxdb

import com.cdsap.talaiot.publisher.PublisherConfiguration

class InfluxDbPublisherConfiguration : PublisherConfiguration {
    override var name: String = "influxDb"
    var dbName: String = ""
    var url: String = ""
    var urlMetric: String = ""
}