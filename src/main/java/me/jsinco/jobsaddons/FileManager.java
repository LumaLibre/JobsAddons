package me.jsinco.jobsaddons;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;

public class FileManager {

    private final JobsAddons plugin;

    public FileManager(JobsAddons plugin) {
        this.plugin = plugin;
    }

    public void setupJobPerksFile() {
        File jobPerksFile = new File(plugin.getDataFolder(), "JobsPerks.yml");
        try {
            if (!jobPerksFile.exists()) {
                jobPerksFile.createNewFile();
                InputStream inputStream = plugin.getResource("JobsPerks.yml");
                OutputStream outputStream = Files.newOutputStream(jobPerksFile.toPath());
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                inputStream.close();
                outputStream.flush();
                outputStream.close();
            }
        } catch (IOException ignored) {
        }
    }

    public void setupConfig() {
        plugin.getConfig().options().copyDefaults(true);
        plugin.saveDefaultConfig();
    }


    public YamlConfiguration getJobPerksFile(){
        File jobPerksFile = new File(plugin.getDataFolder(), "JobsPerks.yml");
        return YamlConfiguration.loadConfiguration(jobPerksFile);
    }

}
