package com.cdsap.talaiot.configuration

/**
 * Configuration for the OutputPublisher
 */
class OutputPublisherConfiguration : PublisherConfiguration {
    override var name: String = "output"
    /**
     * Represents the displayed orientation in the results
     */
    var order = Order.ASC
    /**
     * Number of tasks we want to display in the results
     */
    var numberOfTasks = -1
}

/**
 * Order enum to represent the order in a list of tasks
 */
enum class Order {
    ASC, DESC
}