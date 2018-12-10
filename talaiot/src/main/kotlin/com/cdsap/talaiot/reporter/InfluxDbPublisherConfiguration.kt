package com.cdsap.talaiot.reporter

class InfluxDbPublisherConfiguration : ReporterConfiguration {
    override var name: String = "influxDb"
    var influcxDb: InfuxDbExtension? = null
    // var dbName: String = ""
    // var url: String = ""
    // var urlMetric: String = ""
    fun xx() {

    }

    fun influcxDb(block: InfuxDbExtension.() -> Unit) {
        influcxDb = InfuxDbExtension().also(block)
    }

}

class InfuxDbExtension {
    var dbName: String = ""
    var url: String = ""
    var urlMetric: String = ""
}