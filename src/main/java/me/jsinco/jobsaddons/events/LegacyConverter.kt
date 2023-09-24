package me.jsinco.jobsaddons.events

import me.jsinco.jobsaddons.JobsAddons
import org.bukkit.Bukkit
import org.bukkit.entity.Player

class LegacyConverter(private val plugin: JobsAddons) {
    // For converting SolUtilities permissions to JobsAddons permissions
    private var permissionsModified: Int = 0
    private val commandPermissions: List<String> = listOf(
        "absorption", "fireresistance", "nightvision", "strength", "haste",
        "luck", "speed", "regeneration", "jumpboost", "dolphinsgrace",
        "triad", "bottle", "blaze", "concrete", "powder", "stripcolor",
        "grass", "dirt", "placehere", "strip"
    )


    fun convertPlayerPermissions(player: Player) {
        // probs just gonna be a big if statement

        if (player.hasPermission("solutilities.claimedFarmerCMD")) {
            luckPerms(true, player, "jobsaddons.claimedFarmer.45")
            luckPerms(false, player, "solutilities.claimedFarmerCMD")
        }
        if (player.hasPermission("solutilities.claimedAlchemistCMD")) {
            luckPerms(true, player, "jobsaddons.claimedAlchemist.45")
            luckPerms(false, player, "solutilities.claimedAlchemistCMD")
        }

        for (permission in commandPermissions) {
            if (player.hasPermission("solutilities.$permission")) {
                luckPerms(true, player, "jobsaddons.$permission")
                luckPerms(false, player, "solutilities.$permission")
            }
        }
    }

    fun getPermissionsModified(): Int {
        return permissionsModified
    }

    private fun luckPerms(add: Boolean, player: Player, permission: String) {
        val switch = if (add) "set" else "unset"
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user ${player.name} permission $switch $permission")
        permissionsModified++
    }
}
