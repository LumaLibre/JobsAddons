package me.jsinco.jobsaddons.perks

import me.jsinco.jobsaddons.JobsAddons
import me.jsinco.jobsaddons.util.ColorUtils.colorcode
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import java.util.*

class MiscPerkCommands(private val plugin: JobsAddons) : CommandExecutor {
    init {
        plugin.getCommand("bottle")!!.setExecutor(this)
        plugin.getCommand("blaze")!!.setExecutor(this)
        plugin.getCommand("concrete")!!.setExecutor(this)
        plugin.getCommand("powder")!!.setExecutor(this)
        plugin.getCommand("stripcolor")!!.setExecutor(this)
        plugin.getCommand("grass")!!.setExecutor(this)
        plugin.getCommand("dirt")!!.setExecutor(this)
        plugin.getCommand("placehere")!!.setExecutor(this)
        plugin.getCommand("strip")!!.setExecutor(this)
    }

    override fun onCommand(commandSender: CommandSender, command: Command, s: String, strings: Array<String>): Boolean {
        if (commandSender !is Player) return true
        when (command.name.lowercase()) {
            "bottle" -> convertItemFromInventory(commandSender, Material.GLASS, Material.GLASS_BOTTLE, 3, 3)
            "blaze" -> convertItemFromInventory(commandSender, Material.BLAZE_ROD, Material.BLAZE_POWDER, 1, 2)
            "grass" -> convertItemFromInventory(commandSender, Material.DIRT, Material.GRASS_BLOCK, 1, 1)
            "dirt" -> convertItemFromInventory(commandSender, Material.GRASS_BLOCK, Material.DIRT, 1, 1)
            "concrete" -> replaceInventoryItemFromString(commandSender, "CONCRETE_POWDER", "CONCRETE")
            "powder" -> replaceInventoryItemFromString(commandSender, "CONCRETE", "CONCRETE_POWDER")
            "stripcolor" -> stripColor(commandSender)
            "placehere" -> placeHere(commandSender)
            // Have not been edited
            "strip" -> commandSender.sendMessage(strip(commandSender))
        }
        return true
    }

    private fun stripColor(player: Player) {
        val blacklist = arrayListOf("MOSS_CARPET")
        val whiteItems = arrayListOf("_WOOL", "_CONCRETE", "_CONCRETE_POWDER", "_GLAZED_TERRACOTTA", "_GLAZED_TERRACOTTA", "_BED", "_BANNER", "_CARPET")
        val blankItems = mapOf(Pair("_STAINED_GLASS_PANE", "GLASS_PANE"), Pair("_STAINED_GLASS", "GLAss"), Pair("_SHULKER_BOX", "SHULKER_BOX"), Pair("_CANDLE", "CANDLE"))

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

        player.sendMessage(colorcode("${plugin.getConfig().getString("prefix")} Stripped color from item in hand"))
    }

    private fun placeHere(player: Player) {
        if (player.inventory.itemInMainHand.type == Material.AIR || noPlace(player)) {
            player.sendMessage(colorcode(plugin.getConfig().getString("prefix") + "You cannot place that block here!"))
            return
        }
        val block = player.location.block
        if (!block.type.isAir) {
            player.sendMessage(colorcode(plugin.getConfig().getString("prefix") + "There is already a block here!"))
            return
        }
        block.type = player.inventory.itemInMainHand.type
        val m = block.type.toString()
        player.inventory.itemInMainHand.amount -= 1

        player.sendMessage(colorcode("${plugin.getConfig().getString("prefix")} Set block at location to ${m.lowercase().replace("_", " ")}"))
    }

    private fun strip(player: Player): String {
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
        return colorcode(plugin.getConfig().getString("prefix") + "Stripped all logs and wood in your inventory")
    }



    // FIXME
    private fun convertItemFromInventory(
        player: Player,
        from: Material,
        to: Material,
        craftAmount: Int,
        craftReturnAmount: Int
    ) {
        if (player.inventory.itemInOffHand.type == from || (player.inventory.helmet != null && player.inventory.helmet!!.type == from)) {
            player.sendMessage(
                colorcode(
                    plugin.getConfig().getString("prefix") + "You cannot convert your offhand or helmet into this"
                )
            )
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
            player.sendMessage(
                colorcode(
                    plugin.getConfig().getString("prefix") + "You do not have any " + replace + "'s to convert"
                )
            )
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
            player.sendMessage(
                colorcode(
                    plugin.getConfig()
                        .getString("prefix") + "Please note, some items were dropped on the ground due to a full inv. They are on the ground, but may not render"
                )
            )
        } else {
            player.sendMessage(colorcode(plugin.getConfig().getString("prefix") + "You have converted " + amount + " " + replace + " into " + amount * craftReturnAmount + " " + to.toString().lowercase().replace("_", " ")))
        }
    }

    private fun replaceInventoryItemFromString(player: Player, stringType: String, replaceStringType: String) {
        if (player.inventory.itemInOffHand.type.toString().contains(stringType) || (player.inventory.helmet != null && player.inventory.helmet!!.type.toString().contains(stringType))) {
            player.sendMessage(colorcode(plugin.getConfig().getString("prefix") + "You cannot convert your offhand or helmet into" + stringType))
            return
        }
        val inv = player.inventory
        for (item in inv.contents) {
            if (item != null && item.type.toString().contains(stringType)) {
                if (item.hasItemMeta()) continue  // Don't convert custom items
                val newMaterial = item.type.toString().replace(stringType, replaceStringType).trim()
                val amount = item.amount
                inv.removeItem(ItemStack(item.type, amount))
                inv.addItem(ItemStack(Material.valueOf(newMaterial), amount))
            }
        }
        player.sendMessage(colorcode(plugin.getConfig().getString("prefix") + "You have converted all " + stringType + " in your inventory to " + replaceStringType))
    }

    companion object {
        fun noPlace(player: Player): Boolean {
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
}
