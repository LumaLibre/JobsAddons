package me.jsinco.jobsaddons.perks

import com.gamingmesh.jobs.container.Job
import com.gamingmesh.jobs.container.JobsPlayer
import me.jsinco.jobsaddons.FileManager
import me.jsinco.jobsaddons.JobsAddons
import me.jsinco.jobsaddons.util.ColorUtils
import org.bukkit.Bukkit
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player

class DeliverPerks(
    private val plugin: JobsAddons,
    private val jobsPlayer: JobsPlayer,
    private val job: Job,
) {
    private val jobPerksFile: YamlConfiguration = FileManager(plugin).getJobPerksFile()
    private val jobNames: List<String> = jobPerksFile.getKeys(false).stream().toList()
    private val player: Player = jobsPlayer.player
    private val jobLevel = jobsPlayer.getJobProgression(job).level

    fun claimJobPerks(level: Int): Boolean {
        if (!jobNames.contains(job.name)) return false
        val commands = jobPerksFile.getStringList("${job.name}.perks.lvl$level")
        val messages = jobPerksFile.getStringList("${job.name}.messages.lvl$level")



        if (commands.isEmpty() || !jobsPlayer.isInJob(job) || jobLevel < level) return false
        else if (!player.isOp && player.hasPermission("jobsaddons.claimed${job.name}.$level")) return false

        claimPerks(commands, messages)
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user ${player.name} permission set jobsaddons.claimed${job.name}.$level true") // Easiest way to add a permission
        return true
    }


    fun claimAllJobPerksUntil(level: Int): Boolean {
        if (!jobNames.contains(job.name)) return false
        var returnValue = false

        for (i in 1..level) {
            val commands = jobPerksFile.getStringList("${job.name}.perks.lvl$i")
            val messages = jobPerksFile.getStringList("${job.name}.messages.lvl$i")

            if (commands.isEmpty() || !jobsPlayer.isInJob(job) || jobLevel < level) continue
            else if (!player.isOp && player.hasPermission("jobsaddons.claimed${job.name}.$i")) continue

            claimPerks(commands, messages)
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user ${player.name} permission set jobsaddons.claimed${job.name}.$level true")
            returnValue = true
        }
        return returnValue
    }

    private fun claimPerks(commands: List<String>, messages: List<String>) {
        for (command in commands) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("%player%", player.name))
        }
        for (message in messages) {
            player.sendMessage(ColorUtils.colorcode(plugin.config.getString("prefix") + message.replace("%player%", player.name)))
        }
    }

}
