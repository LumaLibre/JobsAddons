package me.jsinco.jobsaddons

import me.jsinco.jobsaddons.events.Listeners
import me.jsinco.jobsaddons.commands.JobsPerksCommand
import me.jsinco.jobsaddons.perks.MiscPerkCommands
import me.jsinco.jobsaddons.perks.PotionCommands
import me.jsinco.jobsaddons.util.ColorUtils
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.plugin.java.JavaPlugin

class JobsAddons : JavaPlugin() {
    override fun onEnable() {
        if (!dataFolder.exists()) dataFolder.mkdir()
        val fileManager = FileManager(this)
        fileManager.setupConfig()
        fileManager.setupJobPerksFile()
        server.pluginManager.registerEvents(Listeners(this), this)
        MiscPerkCommands(this) // Register commands
        PotionCommands(this) // Register commands
        getCommand("jobperks")!!.setExecutor(JobsPerksCommand(this))
        getCommand("jobsaddonsreload")!!.setExecutor(this)
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        reloadConfig()
        sender.sendMessage(ColorUtils.colorcode("${config.getString("prefix")} Reloaded config"))
        return true
    }
}
