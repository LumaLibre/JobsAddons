package dev.lumas.jobsaddons.events

import com.gamingmesh.jobs.api.JobsExpGainEvent
import com.gamingmesh.jobs.api.JobsLevelUpEvent
import com.gamingmesh.jobs.api.JobsPrePaymentEvent
import com.sk89q.worldedit.bukkit.BukkitAdapter
import com.sk89q.worldguard.WorldGuard
import dev.lumas.lumacore.manager.modules.AutoRegister
import dev.lumas.lumacore.manager.modules.RegisterType
import dev.lumas.lumacore.utility.Logging
import dev.lumas.jobsaddons.JobsAddons
import dev.lumas.jobsaddons.hooks.HookRegistry
import dev.lumas.jobsaddons.hooks.external.WorldGuardHook
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

@AutoRegister(RegisterType.LISTENER)
class JobsListeners : Listener {

    @EventHandler
    fun onJobsLevelUp(event: JobsLevelUpEvent) {
        val player = event.player.player
        val cluster = JobsAddons.PERKS.cluster(event.job.name, event.level)
        Logging.log("Jobs Level Up: ${event.player.name} - ${event.job.name} Level ${event.level}")
        if (cluster != null) {
            cluster.claimPermissionPerks(player)
            cluster.claimCommandPerks(player)
            Logging.log("Claimed perks for ${event.player.name} in job ${event.job.name} level ${event.level}")
        }
    }

    @EventHandler
    fun onJobsPrePayout(event: JobsPrePaymentEvent) {
        val player = event.player.player
        if (player == null || !player.shouldPay()) {
            event.isCancelled = true
        }
    }


    @EventHandler
    fun onJobsExpPayout(event: JobsExpGainEvent) {
        val player = event.player.player
        if (player == null || !player.shouldPay()) {
            event.isCancelled = true
        }
    }

    fun Player.shouldPay(): Boolean {
        if (!HookRegistry.hasHook(WorldGuardHook::class.java)) {
            return true
        }

        val wgHook: WorldGuardHook = HookRegistry.getHook(WorldGuardHook::class.java) ?: return true

        if (!wgHook.isRegistered) {
            return true
        }

        val wgLocation = BukkitAdapter.adapt(this.location)
        val container = WorldGuard.getInstance().platform.regionContainer
        val query = container.createQuery()
        val set = query.getApplicableRegions(wgLocation)
        for (region in set) {
            val flag = region.getFlag(wgHook.blockPayFlag) ?: continue
            if (flag) {
                return false
            }
        }
        return true
    }
}