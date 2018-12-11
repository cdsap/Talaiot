package com.cdsap.talaiot.reporter

class InfluxDbPublisherConfiguration : ReporterConfiguration {
    override var name: String = "influxDb"
    var dbName: String = ""
    var url: String = ""
    var urlMetric: String = ""
}