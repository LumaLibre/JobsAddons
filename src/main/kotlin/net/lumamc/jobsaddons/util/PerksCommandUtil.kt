package net.lumamc.jobsaddons.util

import dev.jsinco.luma.lumacore.utility.Text
import net.lumamc.jobsaddons.JobsAddons
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import java.util.List
import java.util.Locale

object PerksCommandUtil {

    // Copied from the original plugin

    fun convertItemFromInventory(player: Player, from: Material, to: Material, craftAmount: Int, craftReturnAmount: Int) {
        if (player.inventory.itemInOffHand.type == from || (player.inventory.helmet != null && player.inventory.helmet?.type == from)) {
            Text.msg(player, "You cannot convert your offhand or helmet into this.")
            return
        }
        val inv = player.inventory
        var amount = 0
        for (item in inv.contents) {
            if (item != null && item.type == from) {
                amount += item.amount
            }
        }
        amount /= craftAmount
        val replace = from.toString().lowercase(Locale.getDefault()).replace("_", " ")
        if (amount < craftAmount) {
            Text.msg(player, "You do not have any $replace to convert.")
            return
        }
        inv.removeItem(ItemStack(from, amount * craftAmount))
        var dropped = false
        val map: Map<Int, ItemStack> = inv.addItem(ItemStack(to, amount * craftReturnAmount))
        for (item in map.values) {
            player.world.dropItemNaturally(player.location, item)
            dropped = true
        }
        if (dropped) {
            Text.msg(player, "Please note, some items were dropped on the ground due to a full inv. They are on the ground, but may not render.")
        } else {
            Text.msg(player, "You have converted " + amount + " " + replace + " into " + amount * craftReturnAmount + " " + to.toString().lowercase().replace("_", " "))
        }
    }

    fun replaceInventoryItemFromString(player: Player, stringType: String, replaceStringType: String) {
        if (player.inventory.itemInOffHand.type.toString().contains(stringType) || (player.inventory.helmet != null && player.inventory.helmet!!.type.toString().contains(stringType))) {
            Text.msg(player, "You cannot convert your offhand or helmet into$stringType.")
            return
        }
        val inv = player.inventory
        for (item in inv.contents) {
            if (item != null && item.type.toString().contains(stringType)) {
                if (stringType == "CONCRETE" && item.type.toString().contains("CONCRETE_POWDER")) continue
                if (item.hasItemMeta()) continue  // Don't convert custom items
                val newMaterial = item.type.toString().replace(stringType, replaceStringType).trim()
                val amount = item.amount
                inv.removeItem(ItemStack(item.type, amount))
                inv.addItem(ItemStack(Material.valueOf(newMaterial), amount))
            }
        }
        Text.msg(player, "You have converted all " + stringType.lowercase() + " in your inventory to " + replaceStringType.lowercase() + ".")
    }

    fun stripColor(player: Player) {
        val blacklist = arrayListOf("MOSS_CARPET", "PALE_MOSS_CARPET")
        val whiteItems = arrayListOf("_WOOL", "_CONCRETE", "_CONCRETE_POWDER", "_GLAZED_TERRACOTTA", "_GLAZED_TERRACOTTA", "_BED", "_BANNER", "_CARPET")
        val blankItems = mapOf(Pair("_STAINED_GLASS_PANE", "GLASS_PANE"), Pair("_STAINED_GLASS", "GLASS"), Pair("_SHULKER_BOX", "SHULKER_BOX"), Pair("_CANDLE", "CANDLE"))

        val item = player.inventory.itemInMainHand
        if (!blacklist.contains(item.type.toString())) {
            for (whiteItem in whiteItems) {
                if (!item.type.toString().endsWith(whiteItem)) continue
                item.setType(Material.valueOf("WHITE$whiteItem"))
            }
            for (blankItem in blankItems) {
                if (!item.type.toString().endsWith(blankItem.key)) continue
                item.setType(Material.valueOf(blankItem.value))
            }
        }
        Text.msg(player, "Stripped color from the item in your hand.")
    }

    fun placeHere(player: Player) {
        if (player.inventory.itemInMainHand.type == Material.AIR || noPlace(player)) {
            Text.msg(player, "You cannot place that block here!")
            return
        }
        val block = player.location.block
        if (!block.type.isAir) {
            Text.msg(player, "There is already a block here!")
            return
        }
        try {
            block.type = player.inventory.itemInMainHand.type
        } catch (_: IllegalArgumentException) {
            Text.msg(player, "You cannot place that block here!")
            return
        }
        player.inventory.itemInMainHand.amount -= 1
        val m = block.type.toString()
        Text.msg(player, "Set block at location to ${m.lowercase().replace("_", " ")}")
    }

    fun strip(player: Player) {
        for (item in player.inventory.contents) {
            if (item == null || item.type.toString().contains("STRIPPED_")) continue
            var logType: String? = null
            if (item.type.toString().endsWith("_LOG")) logType = item.type.toString() else if (item.type.toString()
                    .endsWith("_WOOD")
            ) logType = item.type.toString() else if (item.type.toString().endsWith("_STEM")) logType =
                item.type.toString() else if (item.type.toString().endsWith("_HYPHAE")) logType = item.type.toString()
            if (logType != null) {
                item.setType(Material.valueOf("STRIPPED_$logType"))
            }
        }
        Text.msg(player, "Stripped all logs and wood in your inventory.")
    }


    fun potionTag(player: Player, tag: String, potionEffectType: PotionEffectType) {
        if (player.scoreboardTags.contains(tag)) {
            player.removeScoreboardTag(tag)
            player.removePotionEffect(potionEffectType)
        } else {
            player.addScoreboardTag(tag)
            player.addPotionEffect(PotionEffect(potionEffectType, 600, 0, true, false, true))
        }
        player.playSound(player.location, Sound.ENTITY_GLOW_SQUID_AMBIENT, 1f, 1f)
    }

    fun potEffect(player: Player, potionEffectType: PotionEffectType) {
        player.addPotionEffect(PotionEffect(potionEffectType, 600, 0, true, false, true))
    }

    fun potionEffectRunnable() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(JobsAddons.INSTANCE, {
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
                    potEffect(player, PotionEffectType.STRENGTH)
                }
                if (player.scoreboardTags.contains("pot.haste")) {
                    potEffect(player, PotionEffectType.HASTE)
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
                    potEffect(player, PotionEffectType.JUMP_BOOST)
                }
                if (player.scoreboardTags.contains("pot.resistance")) {
                    potEffect(player, PotionEffectType.RESISTANCE)
                }
                if (player.scoreboardTags.contains("pot.dolphinsgrace")) {
                    potEffect(player, PotionEffectType.DOLPHINS_GRACE)
                }
            }
        }, 0L, 200L)
    }



    private fun noPlace(player: Player): Boolean {
        val event = BlockPlaceEvent(
            player.location.block,
            player.location.block.state,
            player.location.block,
            player.inventory.itemInMainHand,
            player,
            true,
            EquipmentSlot.HAND
        )
        Bukkit.getPluginManager().callEvent(event)
        return event.isCancelled
    }
}