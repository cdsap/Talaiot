package com.cdsap.talaiot.publisher.influxdb

import com.cdsap.talaiot.publisher.ReporterConfiguration

class InfluxDbPublisherConfiguration : ReporterConfiguration {
    override var name: String = "influxDb"
    var dbName: String = ""
    var url: String = ""
    var urlMetric: String = ""
}