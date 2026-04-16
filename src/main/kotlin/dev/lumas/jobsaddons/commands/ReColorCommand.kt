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

// fixme
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
        private val COLORS = listOf(
            "WHITE", "LIGHT_GRAY", "GRAY", "BLACK", "BROWN", "RED", "ORANGE", "YELLOW",
            "LIME", "GREEN", "CYAN", "LIGHT_BLUE", "BLUE", "PURPLE", "MAGENTA", "PINK"
        )
        private val COLOR_PREFIX = Regex(
            "^(WHITE|LIGHT_GRAY|GRAY|BLACK|BROWN|RED|ORANGE|YELLOW|LIME|GREEN|CYAN|LIGHT_BLUE|BLUE|PURPLE|MAGENTA|PINK)_(.+)$"
        )
        private val VALID_SUFFIXES = setOf(
            "WOOL",
            "CARPET",
            "CONCRETE",
            "CONCRETE_POWDER",
            "TERRACOTTA",
            "GLAZED_TERRACOTTA",
            "STAINED_GLASS",
            "STAINED_GLASS_PANE",
            "SHULKER_BOX",
            "BED",
            "CANDLE",
            "BANNER",
            //"BUNDLE",
            "HARNESS",
            "DYE"
        )
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

        var changed = 0

        for (item in player.inventory.contents.filterNotNull()) {
            val match = COLOR_PREFIX.matchEntire(item.type.name) ?: continue
            val suffix = match.groupValues[2]
            if (suffix !in VALID_SUFFIXES) continue

            val newMaterial = ClassUtil.enumValueOfOrNull(Material::class.java, "${color}_$suffix") ?: continue
            if (item.type != newMaterial) {
                @Suppress("DEPRECATION")
                item.type = newMaterial
                changed += item.amount
            }
        }

        Text.msg(player, "Recolored $changed item(s) to ${color.lowercase()}.")
        return true
    }

    override fun handleTabComplete(sender: CommandSender, label: String, args: Array<String>): List<String> {
        val prefix = args.getOrNull(0)?.lowercase() ?: ""
        return COLORS.map { it.lowercase() }.filter { it.startsWith(prefix) }
    }
}