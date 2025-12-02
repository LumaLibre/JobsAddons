package net.lumamc.jobsaddons.hooks

abstract class ExternalHook {

    var isRegistered: Boolean = false

    abstract val name: String
    abstract val registerWhen: RegisterWhen

    abstract fun canRegister(): Boolean

    abstract fun register()

    open fun unregister() {

    }

    enum class RegisterWhen {
        ON_LOAD,
        ON_ENABLE
    }
}