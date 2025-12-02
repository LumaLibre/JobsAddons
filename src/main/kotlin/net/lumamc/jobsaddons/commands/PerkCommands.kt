@file:Suppress("unused")
package net.lumamc.jobsaddons.commands

import dev.jsinco.luma.lumacore.manager.commands.AbstractCommand
import dev.jsinco.luma.lumacore.manager.commands.CommandInfo
import dev.jsinco.luma.lumacore.manager.modules.AutoRegister
import dev.jsinco.luma.lumacore.manager.modules.RegisterType
import dev.jsinco.luma.lumacore.utility.Text
import net.lumamc.jobsaddons.events.EvenMoreFishListener.Companion.hasAutoSellPersistentData
import net.lumamc.jobsaddons.events.EvenMoreFishListener.Companion.removeAutoSellPersistentData
import net.lumamc.jobsaddons.events.EvenMoreFishListener.Companion.setAutoSellPersistentData
import net.lumamc.jobsaddons.util.PerksCommandUtil
import org.bukkit.Material
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffectType


abstract class CommandPerk : AbstractCommand() {

    abstract fun doAction(sender: Player)

    override fun handle(sender: CommandSender, label: String, args: Array<String>): Boolean {
        sender as Player
        doAction(sender)
        return true
    }

    override fun handleTabComplete(sender: CommandSender, label: String, args: Array<String>): List<String>? {
        return null
    }
}

@AutoRegister(RegisterType.COMMAND)
@CommandInfo(
    name = "bottle",
    permission = "jobsaddons.bottle",
    playerOnly = true
)
class BottleCommand : CommandPerk() {
    override fun doAction(sender: Player) {
        PerksCommandUtil.convertItemFromInventory(sender, Material.GLASS, Material.GLASS_BOTTLE, 3, 3)
    }
}

@AutoRegister(RegisterType.COMMAND)
@CommandInfo(
    name = "blaze",
    permission = "jobsaddons.blaze",
    playerOnly = true
)
class BlazeCommand : CommandPerk() {
    override fun doAction(sender: Player) {
        PerksCommandUtil.convertItemFromInventory(sender, Material.BLAZE_ROD, Material.BLAZE_POWDER, 1, 2)
    }
}

@AutoRegister(RegisterType.COMMAND)
@CommandInfo(
    name = "concrete",
    permission = "jobsaddons.concrete",
    playerOnly = true,
    aliases = ["conc"]
)
class ConcreteCommand : CommandPerk() {
    override fun doAction(sender: Player) {
        PerksCommandUtil.replaceInventoryItemFromString(sender, "CONCRETE_POWDER", "CONCRETE")
    }
}

@AutoRegister(RegisterType.COMMAND)
@CommandInfo(
    name = "powder",
    permission = "jobsaddons.powder",
    playerOnly = true,
    aliases = ["pow"]
)
class PowderCommand : CommandPerk() {
    override fun doAction(sender: Player) {
        PerksCommandUtil.replaceInventoryItemFromString(sender, "CONCRETE", "CONCRETE_POWDER")
    }
}

@AutoRegister(RegisterType.COMMAND)
@CommandInfo(
    name = "stripcolor",
    permission = "jobsaddons.stripcolor",
    playerOnly = true,
    aliases = ["undye"]
)
class StripColorCommand : CommandPerk() {
    override fun doAction(sender: Player) {
        PerksCommandUtil.stripColor(sender)
    }
}

@AutoRegister(RegisterType.COMMAND)
@CommandInfo(
    name = "grass",
    permission = "jobsaddons.grass",
    playerOnly = true
)
class GrassCommand : CommandPerk() {
    override fun doAction(sender: Player) {
        PerksCommandUtil.convertItemFromInventory(sender, Material.DIRT, Material.GRASS_BLOCK, 1, 1)
    }
}

@AutoRegister(RegisterType.COMMAND)
@CommandInfo(
    name = "dirt",
    permission = "jobsaddons.dirt",
    playerOnly = true
)
class DirtCommand : CommandPerk() {
    override fun doAction(sender: Player) {
        PerksCommandUtil.convertItemFromInventory(sender, Material.GRASS_BLOCK, Material.DIRT, 1, 1)
    }
}

@AutoRegister(RegisterType.COMMAND)
@CommandInfo(
    name = "placehere",
    permission = "jobsaddons.placehere",
    playerOnly = true,
    aliases = ["place", "here"]
)
class PlaceHereCommand : CommandPerk() {
    override fun doAction(sender: Player) {
        PerksCommandUtil.placeHere(sender)
    }
}

@AutoRegister(RegisterType.COMMAND)
@CommandInfo(
    name = "strip",
    permission = "jobsaddons.strip",
    playerOnly = true
)
class StripCommand : CommandPerk() {
    override fun doAction(sender: Player) {
        PerksCommandUtil.strip(sender)
    }
}

@AutoRegister(RegisterType.COMMAND)
@CommandInfo(
    name = "absorption",
    permission = "jobsaddons.absorption",
    playerOnly = true,
    aliases = ["absor"]
)
class AbsorptionCommand : CommandPerk() {
    override fun doAction(sender: Player) {
        PerksCommandUtil.potionTag(sender, "pot.absorption", PotionEffectType.ABSORPTION)
    }
}

@AutoRegister(RegisterType.COMMAND)
@CommandInfo(
    name = "fireresistance",
    permission = "jobsaddons.fireresistance",
    playerOnly = true,
    aliases = ["fireres"]
)
class FireResistanceCommand : CommandPerk() {
    override fun doAction(sender: Player) {
        PerksCommandUtil.potionTag(sender, "pot.fireresistance", PotionEffectType.FIRE_RESISTANCE)
    }
}

@AutoRegister(RegisterType.COMMAND)
@CommandInfo(
    name = "nightvision",
    permission = "jobsaddons.nightvision",
    playerOnly = true,
    aliases = ["nv"]
)
class NightVisionCommand : CommandPerk() {
    override fun doAction(sender: Player) {
        PerksCommandUtil.potionTag(sender, "pot.nightvision", PotionEffectType.NIGHT_VISION)
    }
}

@AutoRegister(RegisterType.COMMAND)
@CommandInfo(
    name = "strength",
    permission = "jobsaddons.strength",
    playerOnly = true,
    aliases = ["str"]
)
class StrengthCommand : CommandPerk() {
    override fun doAction(sender: Player) {
        PerksCommandUtil.potionTag(sender, "pot.strength", PotionEffectType.STRENGTH)
    }
}

@AutoRegister(RegisterType.COMMAND)
@CommandInfo(
    name = "haste",
    permission = "jobsaddons.haste",
    playerOnly = true
)
class HasteCommand : CommandPerk() {
    override fun doAction(sender: Player) {
        PerksCommandUtil.potionTag(sender, "pot.haste", PotionEffectType.HASTE)
    }
}

@AutoRegister(RegisterType.COMMAND)
@CommandInfo(
    name = "luck",
    permission = "jobsaddons.luck",
    playerOnly = true
)
class LuckCommand : CommandPerk() {
    override fun doAction(sender: Player) {
        PerksCommandUtil.potionTag(sender, "pot.luck", PotionEffectType.LUCK)
    }
}

@AutoRegister(RegisterType.COMMAND)
@CommandInfo(
    name = "speed",
    permission = "jobsaddons.speed",
    playerOnly = true,
    aliases = ["speed", "spd"]
)
class SpeedCommand : CommandPerk() {
    override fun doAction(sender: Player) {
        PerksCommandUtil.potionTag(sender, "pot.speed", PotionEffectType.SPEED)
    }
}

@AutoRegister(RegisterType.COMMAND)
@CommandInfo(
    name = "regeneration",
    permission = "jobsaddons.regeneration",
    playerOnly = true,
    aliases = ["regen"]
)
class RegenerationCommand : CommandPerk() {
    override fun doAction(sender: Player) {
        PerksCommandUtil.potionTag(sender, "pot.regeneration", PotionEffectType.REGENERATION)
    }
}

@AutoRegister(RegisterType.COMMAND)
@CommandInfo(
    name = "jumpboost",
    permission = "jobsaddons.jumpboost",
    playerOnly = true,
    aliases = ["jb"]
)
class JumpBoostCommand : CommandPerk() {
    override fun doAction(sender: Player) {
        PerksCommandUtil.potionTag(sender, "pot.jumpboost", PotionEffectType.JUMP_BOOST)
    }
}

@AutoRegister(RegisterType.COMMAND)
@CommandInfo(
    name = "dolphinsgrace",
    permission = "jobsaddons.dolphinsgrace",
    playerOnly = true
)
class DolphinsGraceCommand : CommandPerk() {
    override fun doAction(sender: Player) {
        PerksCommandUtil.potionTag(sender, "pot.dolphinsgrace", PotionEffectType.DOLPHINS_GRACE)
    }
}

@AutoRegister(RegisterType.COMMAND)
@CommandInfo(
    name = "triad",
    permission = "jobsaddons.triad",
    playerOnly = true
)
class TriadCommand : CommandPerk() {
    override fun doAction(sender: Player) {
        PerksCommandUtil.potionTag(sender, "pot.resistance", PotionEffectType.RESISTANCE)
        PerksCommandUtil.potionTag(sender, "pot.absorption", PotionEffectType.ABSORPTION)
        PerksCommandUtil.potionTag(sender, "pot.regeneration", PotionEffectType.REGENERATION)
    }
}

@AutoRegister(RegisterType.COMMAND)
@CommandInfo(
    name = "fishautosell",
    permission = "jobsaddons.fishautosell",
    playerOnly = true,
    aliases = ["fas"]
)
class FishAutoSellCommand : CommandPerk() {
    override fun doAction(sender: Player) {
        if (sender.hasAutoSellPersistentData()) {
            sender.removeAutoSellPersistentData()
            Text.msg(sender, "Disabled auto sell for fishing.")
        } else {
            sender.setAutoSellPersistentData()
            Text.msg(sender, "Enabled auto sell for fishing.")
        }
    }
}