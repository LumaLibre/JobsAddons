package me.jsinco.jobsaddons.perks

import me.jsinco.jobsaddons.JobsAddons
import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import java.util.List

class PotionCommands(private val plugin: JobsAddons) : CommandExecutor {
    init {
        plugin.getCommand("absorption")!!.setExecutor(this)
        plugin.getCommand("fireresistance")!!.setExecutor(this)
        plugin.getCommand("nightvision")!!.setExecutor(this)
        plugin.getCommand("strength")!!.setExecutor(this)
        plugin.getCommand("haste")!!.setExecutor(this)
        plugin.getCommand("luck")!!.setExecutor(this)
        plugin.getCommand("speed")!!.setExecutor(this)
        plugin.getCommand("regeneration")!!.setExecutor(this)
        plugin.getCommand("jumpboost")!!.setExecutor(this)
        plugin.getCommand("dolphinsgrace")!!.setExecutor(this)
        plugin.getCommand("triad")!!.setExecutor(this)
        potionCMDRunnables()
    }

    override fun onCommand(commandSender: CommandSender, command: Command, s: String, strings: Array<String>): Boolean {
        if (commandSender !is Player) return true
        when (command.name) {
            "absorption" -> potionTag(commandSender, "pot.absorption", PotionEffectType.ABSORPTION)
            "fireresistance" -> potionTag(commandSender, "pot.fireresistance", PotionEffectType.FIRE_RESISTANCE)
            "nightvision" -> potionTag(commandSender, "pot.nightvision", PotionEffectType.NIGHT_VISION)
            "strength" -> potionTag(commandSender, "pot.strength", PotionEffectType.INCREASE_DAMAGE)
            "haste" -> potionTag(commandSender, "pot.haste", PotionEffectType.FAST_DIGGING)
            "luck" -> potionTag(commandSender, "pot.luck", PotionEffectType.LUCK)
            "speed" -> potionTag(commandSender, "pot.speed", PotionEffectType.SPEED)
            "regeneration" -> potionTag(commandSender, "pot.regeneration", PotionEffectType.REGENERATION)
            "jumpboost" -> potionTag(commandSender, "pot.jumpboost", PotionEffectType.JUMP)
            "dolphinsgrace" -> potionTag(commandSender, "pot.dolphinsgrace", PotionEffectType.DOLPHINS_GRACE)
            "triad" -> {
                potionTag(commandSender, "pot.resistance", PotionEffectType.DAMAGE_RESISTANCE)
                potionTag(commandSender, "pot.absorption", PotionEffectType.ABSORPTION)
                potionTag(commandSender, "pot.regeneration", PotionEffectType.REGENERATION)
            }
        }
        return true
    }

    private fun potionTag(player: Player, tag: String, potionEffectType: PotionEffectType) {
        if (player.scoreboardTags.contains(tag)) {
            player.removeScoreboardTag(tag)
            player.removePotionEffect(potionEffectType)
        } else {
            player.addScoreboardTag(tag)
            player.addPotionEffect(PotionEffect(potionEffectType, 600, 0, false, false))
        }
        player.playSound(player.location, Sound.ENTITY_GLOW_SQUID_AMBIENT, 1f, 1f)
    }

    private fun potEffect(player: Player, potionEffectType: PotionEffectType) {
        player.addPotionEffect(PotionEffect(potionEffectType, 600, 0, false, false))
    }
    // FIXME: CLEANUP t
    fun potionCMDRunnables() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, {
            val players = List.copyOf(Bukkit.getOnlinePlayers())
            for (player in players) {
                if (player.scoreboardTags.contains("pot.absorption")) {
                    potEffect(player, PotionEffectType.ABSORPTION)
                }
                if (player.scoreboardTags.contains("pot.fireresistance")) {
                    potEffect(player, PotionEffectType.FIRE_RESISTANCE)
                }
                if (player.scoreboardTags.contains("pot.nightvision")) {
                    potEffect(player, PotionEffectType.NIGHT_VISION)
                }
                if (player.scoreboardTags.contains("pot.strength")) {
                    potEffect(player, PotionEffectType.INCREASE_DAMAGE)
                }
                if (player.scoreboardTags.contains("pot.haste")) {
                    potEffect(player, PotionEffectType.FAST_DIGGING)
                }
                if (player.scoreboardTags.contains("pot.luck")) {
                    potEffect(player, PotionEffectType.LUCK)
                }
                if (player.scoreboardTags.contains("pot.speed")) {
                    potEffect(player, PotionEffectType.SPEED)
                }
                if (player.scoreboardTags.contains("pot.regeneration")) {
                    potEffect(player, PotionEffectType.REGENERATION)
                }
                if (player.scoreboardTags.contains("pot.jumpboost")) {
                    potEffect(player, PotionEffectType.JUMP)
                }
                if (player.scoreboardTags.contains("pot.resistance")) {
                    potEffect(player, PotionEffectType.DAMAGE_RESISTANCE)
                }
                if (player.scoreboardTags.contains("pot.dolphinsgrace")) {
                    potEffect(player, PotionEffectType.DOLPHINS_GRACE)
                }
            }
        }, 0L, 100L)
    }
}