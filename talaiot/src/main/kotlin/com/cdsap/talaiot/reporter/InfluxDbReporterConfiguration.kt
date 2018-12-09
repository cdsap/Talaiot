import com.cdsap.talaiot.reporter.ReporterConfiguration

class InfluxDbReporterConfiguration : ReporterConfiguration {
    override var name: String = "influxDb"
    var influxDb: InfuxDbExtension? = null
    var dbName: String = ""
    var url: String = ""
    var urlMetric: String = ""

}

class InfuxDbExtension {
    var dbName: String = ""
    var url: String = ""
    var urlMetric: String = ""
}