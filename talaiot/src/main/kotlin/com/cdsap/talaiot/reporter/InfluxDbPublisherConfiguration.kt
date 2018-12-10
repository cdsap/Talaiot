package com.cdsap.talaiot.reporter

class InfluxDbPublisherConfiguration : ReporterConfiguration {
    override var name: String = "influxDb"
    var dbName: String = ""
    var url: String = ""
    var urlMetric: String = ""

    var influxDb: InfuxDbExtension? = null

    fun influxDb(block: InfuxDbExtension.() -> Unit) {
        influxDb = InfuxDbExtension().also(block)
    }
}

class InfuxDbExtension {
    var dbName: String = ""
    var url: String = ""
    var urlMetric: String = ""
}