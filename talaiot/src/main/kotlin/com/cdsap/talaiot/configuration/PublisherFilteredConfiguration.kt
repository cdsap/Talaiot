package com.cdsap.talaiot.configuration

interface PublisherFilteredConfiguration : PublisherConfiguration {
    var filter: FilterConfiguration?
}