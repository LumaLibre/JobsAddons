package me.jsinco.jobsaddons;

import me.jsinco.jobsaddons.events.Listeners;
import me.jsinco.jobsaddons.perks.MiscPerkCommands;
import me.jsinco.jobsaddons.perks.JobsPerksCommand;
import me.jsinco.jobsaddons.perks.PotionCommands;
import org.bukkit.plugin.java.JavaPlugin;

public final class JobsAddons extends JavaPlugin {

    @Override
    public void onEnable() {
        if (!getDataFolder().exists()) getDataFolder().mkdir();
        FileManager fileManager = new FileManager(this);
        fileManager.setupConfig();
        fileManager.setupJobPerksFile();

        getServer().getPluginManager().registerEvents(new Listeners(this), this);

        new MiscPerkCommands(this); // Register commands
        new PotionCommands(this); // Register commands
        getCommand("jobperks").setExecutor(new JobsPerksCommand());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
