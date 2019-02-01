package com.cdsap.talaiot.configuration


class OutputPublisherConfiguration : PublisherConfiguration {
    override var name: String = "output"
    var order = Order.ASC
    var numberOfTasks = -1
}

enum class Order {
    ASC, DESC
}