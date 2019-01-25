package com.cdsap.talaiot.publisher

import com.cdsap.talaiot.TalaiotExtension
import com.cdsap.talaiot.logger.LogTrackerImpl
import com.cdsap.talaiot.request.SimpleRequest
import org.gradle.api.Project

class PublishersProvider(val talaiotExtension: TalaiotExtension) {
    fun get(): List<Publisher> {
        val publishers = mutableListOf<Publisher>()

        println( talaiotExtension)
        val logger = LogTrackerImpl(talaiotExtension.logger)
        println( talaiotExtension.publishers)
        println("dldlldldldldldldld")
        println("dldlldldldldldldld")
        println("dldlldldldldldldld")
        talaiotExtension.publishers?.apply {
            println(outputPublisher)
            outputPublisher?.apply {
                publishers.add(OutputPublisher(logger))
            }

            influxDbPublisher?.apply {
                publishers.add(InfluxDbPublisher(this, logger, SimpleRequest(logger)))
            }

            customPublisher?.apply {
                publishers.add(this)
            }
        }
        println("${publishers.size} dldldldldl")
        return publishers
    }
}