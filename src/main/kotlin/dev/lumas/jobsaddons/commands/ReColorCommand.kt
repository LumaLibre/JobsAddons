package dev.lumas.jobsaddons.commands

import dev.lumas.lumacore.manager.commands.AbstractCommand
import dev.lumas.lumacore.manager.commands.CommandInfo
import dev.lumas.lumacore.manager.modules.AutoRegister
import dev.lumas.lumacore.manager.modules.RegisterType
import dev.lumas.lumacore.utility.Text
import dev.lumas.jobsaddons.util.ClassUtil
import org.bukkit.Material
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

@AutoRegister(RegisterType.COMMAND)
@CommandInfo(
    name = "recolor",
    description = "Recolor all dyeable items in your inventory to the specified color",
    usage = "/<command> <color>",
    permission = "jobsaddons.recolor",
    playerOnly = true
)
class ReColorCommand : AbstractCommand() {

    companion object {
        private val COLORS = listOf("WHITE", "LIGHT_GRAY", "GRAY", "BLACK", "BROWN", "RED", "ORANGE", "YELLOW", "LIME", "GREEN", "CYAN", "LIGHT_BLUE", "BLUE", "PURPLE", "MAGENTA", "PINK")

        // Longest suffixes first so CONCRETE_POWDER isn't swallowed by CONCRETE, etc.
        private val TYPES = listOf("CONCRETE_POWDER", "CONCRETE", "GLAZED_TERRACOTTA", "TERRACOTTA", "STAINED_GLASS_PANE", "STAINED_GLASS", "WOOL", "CARPET", "BED", "SHULKER_BOX", "CANDLE", "BANNER")

        // Anchoring on the known colors keeps things like MOSS_CARPET or TUBE_CORAL out of the match.
        private val DYEABLE_PATTERN = Regex("^(?:${COLORS.joinToString("|")})_(?<type>${TYPES.joinToString("|")})$")
    }

    override fun handle(sender: CommandSender, label: String, args: Array<String>): Boolean {
        val player = sender as Player
        if (args.isEmpty()) {
            return false
        }

        val name = args[0]
        val color = name.uppercase()
        if (color !in COLORS) {
            Text.msg(player, "Invalid color: $name")
            return true
        }

        val items = player.inventory.contents
            .filterNotNull()
            .filter { itemStack ->
                !itemStack.hasItemMeta() && DYEABLE_PATTERN.matches(itemStack.type.name)
            }

        var recolored = 0
        for (item in items) {
            val matchResult = DYEABLE_PATTERN.find(item.type.name) ?: continue
            val type = matchResult.groups["type"]?.value ?: continue
            val newMaterial = ClassUtil.enumValueOfOrNull(Material::class.java, "${color}_$type") ?: continue
            if (newMaterial == item.type) continue

            @Suppress("DEPRECATION")
            item.type = newMaterial
            recolored++
        }
        Text.msg(player, "Recolored $recolored items to $name.")
        return true
    }

    override fun handleTabComplete(sender: CommandSender, label: String, args: Array<String>): List<String>? {
        return COLORS.map { it.lowercase() }
    }
}