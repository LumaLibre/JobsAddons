package me.jsinco.jobsaddons.perks;

import me.jsinco.jobsaddons.JobsAddons;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class PotionCommands implements CommandExecutor {

    private JobsAddons plugin;

    public PotionCommands(JobsAddons plugin) {
        this.plugin = plugin;
        plugin.getCommand("absorption").setExecutor(this);
        plugin.getCommand("fireresistance").setExecutor(this);
        plugin.getCommand("nightvision").setExecutor(this);
        plugin.getCommand("strength").setExecutor(this);
        plugin.getCommand("haste").setExecutor(this);
        plugin.getCommand("luck").setExecutor(this);
        plugin.getCommand("speed").setExecutor(this);
        plugin.getCommand("regeneration").setExecutor(this);
        plugin.getCommand("jumpboost").setExecutor(this);
        plugin.getCommand("dolphinsgrace").setExecutor(this);
        plugin.getCommand("triad").setExecutor(this);

        potionCMDRunnables();
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player player)) return true;
        switch (command.getName()) {
            case "absorption" -> potionTag(player, "pot.absorption", PotionEffectType.ABSORPTION);
            case "fireresistance" -> potionTag(player, "pot.fireresistance", PotionEffectType.FIRE_RESISTANCE);
            case "nightvision" -> potionTag(player, "pot.nightvision", PotionEffectType.NIGHT_VISION);
            case "strength" -> potionTag(player, "pot.strength", PotionEffectType.INCREASE_DAMAGE);
            case "haste" -> potionTag(player, "pot.haste", PotionEffectType.FAST_DIGGING);
            case "luck" -> potionTag(player, "pot.luck", PotionEffectType.LUCK);
            case "speed" -> potionTag(player, "pot.speed", PotionEffectType.SPEED);
            case "regeneration" -> potionTag(player, "pot.regeneration", PotionEffectType.REGENERATION);
            case "jumpboost" -> potionTag(player, "pot.jumpboost", PotionEffectType.JUMP);
            case "dolphinsgrace" -> potionTag(player, "pot.dolphinsgrace", PotionEffectType.DOLPHINS_GRACE);
            case "triad" -> {
                potionTag(player, "pot.resistance", PotionEffectType.DAMAGE_RESISTANCE);
                potionTag(player, "pot.absorption", PotionEffectType.ABSORPTION);
                potionTag(player, "pot.regeneration", PotionEffectType.REGENERATION);
            }
        }

        return true;
    }


    private void potionTag(Player player, String tag, PotionEffectType potionEffectType) {
        if (player.getScoreboardTags().contains(tag)) {
            player.removeScoreboardTag(tag);
            player.removePotionEffect(potionEffectType);
        } else {
            player.addScoreboardTag(tag);
            player.addPotionEffect(new PotionEffect(potionEffectType, 340, 0, false, false));
        }
        player.playSound(player.getLocation(), Sound.ENTITY_GLOW_SQUID_AMBIENT, 1, 1);
    }

    private void potEffect(Player player, PotionEffectType potionEffectType) {
        player.addPotionEffect(new PotionEffect(potionEffectType, 340, 0, false, false));
    }

    public void potionCMDRunnables() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            List<Player> players = List.copyOf(Bukkit.getOnlinePlayers());

            for (Player player : players) {
                if (player.getScoreboardTags().contains("pot.absorption")) {
                    potEffect(player, PotionEffectType.ABSORPTION);
                }
                if (player.getScoreboardTags().contains("pot.fireresistance")) {
                    potEffect(player, PotionEffectType.FIRE_RESISTANCE);
                }
                if (player.getScoreboardTags().contains("pot.nightvision")) {
                    potEffect(player, PotionEffectType.NIGHT_VISION);
                }
                if (player.getScoreboardTags().contains("pot.strength")) {
                    potEffect(player, PotionEffectType.INCREASE_DAMAGE);
                }
                if (player.getScoreboardTags().contains("pot.haste")) {
                    potEffect(player, PotionEffectType.FAST_DIGGING);
                }
                if (player.getScoreboardTags().contains("pot.luck")) {
                    potEffect(player, PotionEffectType.LUCK);
                }
                if (player.getScoreboardTags().contains("pot.speed")) {
                    potEffect(player, PotionEffectType.SPEED);
                }
                if (player.getScoreboardTags().contains("pot.regeneration")) {
                    potEffect(player, PotionEffectType.REGENERATION);
                }
                if (player.getScoreboardTags().contains("pot.jumpboost")) {
                    potEffect(player, PotionEffectType.JUMP);
                }
                if (player.getScoreboardTags().contains("pot.resistance")) {
                    potEffect(player, PotionEffectType.DAMAGE_RESISTANCE);
                }
                if (player.getScoreboardTags().contains("pot.dolphinsgrace")) {
                    potEffect(player, PotionEffectType.DOLPHINS_GRACE);
                }
            }

        }, 0L, 100L);
    }
}