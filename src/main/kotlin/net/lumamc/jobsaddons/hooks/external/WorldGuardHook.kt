package net.lumamc.jobsaddons.hooks.external

import com.sk89q.worldguard.WorldGuard
import com.sk89q.worldguard.protection.flags.BooleanFlag
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException
import dev.jsinco.luma.lumacore.utility.Logging
import net.lumamc.jobsaddons.hooks.ExternalHook
import net.lumamc.jobsaddons.util.ClassUtil

class WorldGuardHook : ExternalHook() {

    override val name = "WorldGuard"
    override val registerWhen = RegisterWhen.ON_LOAD

    override fun canRegister() = ClassUtil.exists("com.sk89q.worldguard.WorldGuard")

    var blockPayFlag: BooleanFlag? = null
        private set

    override fun register() {
        val registry = WorldGuard.getInstance().flagRegistry
        try {
            blockPayFlag = BooleanFlag("jobsaddons-block-pay")
            registry.register(blockPayFlag)
        } catch (e: FlagConflictException) {
            Logging.errorLog("Another plugin already took the jobsaddons-block-pay flag!", e)
        } catch (e: IllegalStateException) {
            Logging.errorLog("Failed to register jobsaddons-block-pay flag!")
        }
    }
}