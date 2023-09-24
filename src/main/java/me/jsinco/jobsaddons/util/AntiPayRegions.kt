package me.jsinco.jobsaddons.util

import com.sk89q.worldedit.bukkit.BukkitAdapter
import com.sk89q.worldguard.WorldGuard
import me.jsinco.jobsaddons.JobsAddons
import org.bukkit.entity.Player

object AntiPayRegions {
    private val plugin = JobsAddons.getPlugin(JobsAddons::class.java)
    fun shouldPay(player: Player): Boolean {
        val wgLocation = BukkitAdapter.adapt(player.location)
        val container = WorldGuard.getInstance().getPlatform().regionContainer
        val query = container.createQuery()
        val set = query.getApplicableRegions(wgLocation)
        for (region in set) {
            if (plugin.getConfig().getStringList("block-pay").contains(region.id)) {
                return false
            }
        }
        return true
    }
}
