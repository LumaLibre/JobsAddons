package net.lumamc.jobsaddons.commands

import dev.jsinco.luma.lumacore.manager.commands.AbstractCommand
import dev.jsinco.luma.lumacore.manager.commands.CommandInfo
import dev.jsinco.luma.lumacore.manager.modules.AutoRegister
import dev.jsinco.luma.lumacore.manager.modules.RegisterType
import dev.jsinco.luma.lumacore.utility.Text
import net.lumamc.jobsaddons.events.ItemFilterListener
import org.bukkit.Material
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

// bad code but whatever
@AutoRegister(RegisterType.COMMAND)
@CommandInfo(
    name = "itemfilter",
    description = "Manage your item pickup filter",
    usage = "/<command> <add|remove|clear> <material>",
    permission = "jobsaddons.itemfilter",
    playerOnly = true
)
class ItemFilterCommand : AbstractCommand() {

    companion object {
        private val MATERIALS = Material.entries
            .filter { it.isItem }
            .map { it.name.lowercase() }
    }

    override fun handle(sender: CommandSender, label: String, args: Array<String>): Boolean {
        if (args.isEmpty()) {
            return false
        }

        sender as Player
        val currentFilter = ItemFilterListener.FILTERED.getOrDefault(sender.uniqueId, HashSet())

        val arg = args[0].lowercase()

        if (arg == "clear") {
            ItemFilterListener.FILTERED.remove(sender.uniqueId)
            Text.msg(sender, "Cleared your item filter.")
            return true
        }

        if (args.size < 2) {
            return false
        }


        val materialName = args[1].uppercase()
        val material = Material.getMaterial(materialName)
        if (material == null || !material.isItem) {
            Text.msg(sender, "Invalid material: $materialName")
            return true
        }


        if (arg == "add") {
            currentFilter.add(material)
            Text.msg(sender, "Added $materialName to your item filter.")
        } else {
            currentFilter.remove(material)
            Text.msg(sender, "Removed $materialName from your item filter.")
        }

        ItemFilterListener.FILTERED[sender.uniqueId] = currentFilter
        return true
    }

    override fun handleTabComplete(sender: CommandSender, label: String, args: Array<String>): List<String>? {
        return when (args.size) {
            1 -> listOf("add", "remove", "clear")
            2 -> {
                val arg = args.getOrElse(0) { "" }.lowercase()
                if (arg == "add") {
                    MATERIALS
                } else if (arg == "remove") {
                    val player = sender as Player
                    val currentFilter = ItemFilterListener.FILTERED.getOrDefault(player.uniqueId, HashSet())
                    currentFilter.map { it.name.lowercase() }
                } else {
                    null
                }
            }

            else -> null
        }
    }
}