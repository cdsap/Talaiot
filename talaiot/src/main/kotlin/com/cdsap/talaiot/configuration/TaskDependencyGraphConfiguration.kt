package com.cdsap.talaiot.configuration

class TaskDependencyGraphConfiguration : PublisherConfiguration {
    override var name: String = "influxDb"
    var dbName: String = ""
    var url: String = ""
    var urlMetric: String = ""
}