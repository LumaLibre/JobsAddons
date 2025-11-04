package me.jsinco.jobsaddons.events

import com.gamingmesh.jobs.api.JobsExpGainEvent
import com.gamingmesh.jobs.api.JobsLevelUpEvent
import com.gamingmesh.jobs.api.JobsPrePaymentEvent
import me.jsinco.jobsaddons.JobsAddons
import me.jsinco.jobsaddons.hooks.PlayerPointsIntegration
import me.jsinco.jobsaddons.perks.DeliverPerks
import me.jsinco.jobsaddons.util.AntiPayRegions
import me.jsinco.jobsaddons.util.ColorUtils
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import java.util.Random

class Listeners(private val plugin: JobsAddons) : Listener {
    @EventHandler
    fun onJobsLevelUp(event: JobsLevelUpEvent) {
        val dP = DeliverPerks(plugin, event.player, event.job)
        dP.claimJobPerks(event.level)
    }

    @EventHandler
    fun onJobsPrePayout(event: JobsPrePaymentEvent) {
        val player = event.player.player
        if (player == null || !AntiPayRegions.shouldPay(player, plugin)) {
            event.isCancelled = true
            return
        }
    }


    @EventHandler
    fun onJobsExpPayout(event: JobsExpGainEvent) {
        val player = event.player.player
        if (player == null || !AntiPayRegions.shouldPay(player, plugin)) {
            event.isCancelled = true
            return
        }
        val name = event.job.name
        try {
            if (Random().nextInt(plugin.config.getInt("PlayerPoints-integration.$name.bound")) <= plugin.config.getInt("PlayerPoints-integration.$name.weight")) {
                PlayerPointsIntegration.givePoints(player, plugin.config.getInt("PlayerPoints-integration.$name.amount-to-give"))
            }
        } catch (e: Exception) {
            plugin.logger.warning("Failed to give player points to ${player.name} for job $name")
        }
    }

}
