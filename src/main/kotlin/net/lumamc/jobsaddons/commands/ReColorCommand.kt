package net.lumamc.jobsaddons.commands

import dev.jsinco.luma.lumacore.manager.commands.AbstractCommand
import dev.jsinco.luma.lumacore.manager.commands.CommandInfo
import dev.jsinco.luma.lumacore.manager.modules.AutoRegister
import dev.jsinco.luma.lumacore.manager.modules.RegisterType
import dev.jsinco.luma.lumacore.utility.Text
import net.lumamc.jobsaddons.util.ClassUtil
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
        private val COLORS = listOf("WHITE", "LIGHT_GRAY", "GRAY", "BLACK", "BROWN", "RED", "ORANGE", "YELLOW", "LIME", "GREEN", "CYAN", "LIGHT_BLUE", "BLUE", "PURPLE", "MAGENTA", "PINK")
        private val DYEABLE_PATTERN = Regex("^(?<base>.+)_((WOOL)|(CARPET)|(CONCRETE)|(CONCRETE_POWDER)|(TERRACOTTA)|(GLAZED_TERRACOTTA)|(STAINED_GLASS)|(STAINED_GLASS_PANE)|(BED)|(SHULKER_BOX)|(CANDLE)|(BANNER))$")
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

        for (item in items) {
            val matchResult = DYEABLE_PATTERN.find(item.type.name) ?: continue
            val base = matchResult.groups["base"]?.value ?: continue
            val newMaterialName = "${color}_${base.substringAfterLast("_")}"
            val newMaterial = ClassUtil.enumValueOfOrNull(Material::class.java, newMaterialName)
            newMaterial?.let {
                @Suppress("DEPRECATION")
                item.type = it
            }
        }
        Text.msg(player, "Recolored ${items.size} items to $name.")
        return true
    }

    override fun handleTabComplete(sender: CommandSender, label: String, args: Array<String>): List<String>? {
        return COLORS.map { it.lowercase() }
    }
}