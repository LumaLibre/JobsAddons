package me.jsinco.jobsaddons

import me.jsinco.jobsaddons.events.Listeners
import me.jsinco.jobsaddons.perks.JobsPerksCommand
import me.jsinco.jobsaddons.perks.MiscPerkCommands
import me.jsinco.jobsaddons.perks.PotionCommands
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
        getCommand("jobperks")!!.setExecutor(JobsPerksCommand())
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }
}
