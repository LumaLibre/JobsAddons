package net.lumamc.jobsaddons.hooks.external

import com.sk89q.worldguard.WorldGuard
import com.sk89q.worldguard.protection.flags.BooleanFlag
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException
import dev.jsinco.luma.lumacore.utility.Logging
import net.lumamc.jobsaddons.hooks.ExternalHook
import net.lumamc.jobsaddons.util.ClassUtil

class WorldGuardHook : ExternalHook() {

    companion object {
        const val BLOCK_PAY_FLAG_NAME = "jobsaddons-block-pay"
    }

    override val name = "WorldGuard"

    override fun canRegister() = ClassUtil.exists("com.sk89q.worldguard.WorldGuard")

    var blockPayFlag: BooleanFlag? = null
        private set

    override fun register() {
        val registry = WorldGuard.getInstance().flagRegistry
        try {
            blockPayFlag = BooleanFlag(BLOCK_PAY_FLAG_NAME)
            registry.register(blockPayFlag)
        } catch (e: FlagConflictException) {
            Logging.warningLog("Another plugin already took the jobsaddons-block-pay flag!")

            blockPayFlag = registry.get(BLOCK_PAY_FLAG_NAME) as? BooleanFlag
            if (blockPayFlag == null) {
                Logging.errorLog("Failed to retrieve existing jobsaddons-block-pay flag after conflict!", e)
                return
            }
        } catch (e: IllegalStateException) {
            Logging.warningLog("Failed to register jobsaddons-block-pay flag!")

            blockPayFlag = registry.get(BLOCK_PAY_FLAG_NAME) as? BooleanFlag
            if (blockPayFlag == null) {
                Logging.errorLog("Failed to retrieve existing jobsaddons-block-pay flag after conflict!", e)
                return
            }
        }
    }
}