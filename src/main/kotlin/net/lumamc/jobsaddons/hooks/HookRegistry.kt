package net.lumamc.jobsaddons.hooks

import dev.jsinco.luma.lumacore.utility.Logging
import java.util.function.Supplier

// todo: probably remove this
object HookRegistry {

    private val HOOKS: MutableList<ExternalHook> = mutableListOf()


    fun registerHookClass(supplier: Supplier<ExternalHook>) {
        try {
            val hook = supplier.get()
            if (!hook.canRegister()) {
                Logging.log("Skipping hook registration for external plugin: ${hook.name}")
                return
            }
            hook.register()
            HOOKS.add(hook)
            Logging.log("Registered hook for external plugin: ${hook.name}")
        } catch (ignored: ClassNotFoundException) {
            ignored.printStackTrace()
        } catch (ignored: NoClassDefFoundError) {
            ignored.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    fun unregisterAllHooks() {
        for (hook in HOOKS) {
            try {
                hook.unregister()
                hook.isRegistered = false
                Logging.log("Unregistered hook for external plugin: ${hook.name}")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        HOOKS.clear()
    }

    fun <T : ExternalHook> getHook(clazz: Class<out ExternalHook>): T? {
        return HOOKS.firstOrNull { it::class.java == clazz } as? T
    }

    fun hasHook(clazz: Class<out ExternalHook>): Boolean {
        return HOOKS.any { it::class.java == clazz }
    }
}