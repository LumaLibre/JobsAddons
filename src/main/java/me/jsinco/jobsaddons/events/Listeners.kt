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
        val name = event.job.name
        if (Random().nextInt(plugin.config.getInt("PlayerPoints-integration.$name.bound")) <= plugin.config.getInt("PlayerPoints-integration.$name.bound")) {
            PlayerPointsIntegration.givePoints(player, plugin.config.getInt("PlayerPoints-integration.$name.amount-to-give"))
        }
    }


    @EventHandler
    fun onJobsExpPayout(event: JobsExpGainEvent) {
        val player = event.player.player
        if (player == null || !AntiPayRegions.shouldPay(player, plugin)) {
            event.isCancelled = true
        }
    }

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        if (event.player.isOp) return
        val converter = LegacyConverter(plugin)
        converter.convertPlayerPermissions(event.player)
        val modified = converter.getPermissionsModified()
        if (modified == 0) return
        plugin.logger.info("Converted $modified permissions for ${event.player.name}")
        for (player in Bukkit.getOnlinePlayers()) {
            if (player.isOp) {
                player.sendMessage(ColorUtils.colorcode(plugin.config.getString("prefix") + "Converted ${modified/2} legacy permissions for ${event.player.name} (T: $modified)"))
            }
        }
    }
}
