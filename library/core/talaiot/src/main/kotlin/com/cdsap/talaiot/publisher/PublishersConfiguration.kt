package com.cdsap.talaiot.publisher

import org.gradle.api.Project

open class PublishersConfiguration(
    val project: Project
) {
    var customPublishers: MutableSet<Publisher> = mutableSetOf()

    /**
     * Adds the given custom publishers into the publisher list.
     *
     * @param publishers takes N [Publisher]s to be added to the publishers list.
     */
    fun customPublishers(vararg publishers: Publisher) {
        customPublishers.addAll(publishers)
    }
}