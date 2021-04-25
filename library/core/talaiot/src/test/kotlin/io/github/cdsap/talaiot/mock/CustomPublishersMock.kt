package io.github.cdsap.talaiot.mock

import io.github.cdsap.talaiot.entities.ExecutionReport
import io.github.cdsap.talaiot.publisher.Publisher

class TestPublisher : Publisher {
    override fun publish(report: ExecutionReport) {}
}

class ConsolePublisher : Publisher {
    override fun publish(report: ExecutionReport) {}
}
