package com.talaiot.buildplugins

open class BaseConfiguration {
    /**
     *  Deployment for snapshots/maven local requires inform about the group coordinates of the
     *  new artifact. If it's not informed uses the default group of the type of plugins [TalaiotPlugin]
     */
    var group: String? = null

    /**
     * Specific version for the plugin
     * If it's not informed will use version from [Constants]
     */
    var version: String? = null
}
