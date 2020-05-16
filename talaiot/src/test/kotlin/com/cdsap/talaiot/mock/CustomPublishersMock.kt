package com.cdsap.talaiot.mock

import com.cdsap.talaiot.entities.ExecutionReport
import com.cdsap.talaiot.publisher.Publisher

class TestPublisher : Publisher {
    override fun publish(report: ExecutionReport) {}
}

class ConsolePublisher : Publisher {
    override fun publish(report: ExecutionReport) {}
}
