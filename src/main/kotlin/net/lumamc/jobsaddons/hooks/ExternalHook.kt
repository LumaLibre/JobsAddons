package net.lumamc.jobsaddons.hooks

abstract class ExternalHook {

    var isRegistered: Boolean = false

    abstract val name: String

    abstract fun canRegister(): Boolean

    abstract fun register()

    open fun unregister() {

    }
}