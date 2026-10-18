package dev.lumas.jobsaddons.events

import com.gmail.nossr50.events.skills.woodcutting.TreeFellerDestroyTreeEvent
import dev.lumas.lumacore.manager.modules.AutoRegister
import dev.lumas.lumacore.manager.modules.RegisterType
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.Sound
import org.bukkit.Tag
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.inventory.ItemStack
import syncDelayed

@AutoRegister(RegisterType.LISTENER)
class McMMOTreeFellerListener : Listener {

    companion object {
        private const val PERMISSION = "jobsaddons.replanttreefeller"
        private val SUBSTRATE_OVERWORLD = (Tag.DIRT.values + listOf(
            Material.MOSS_BLOCK, Material.MYCELIUM, Material.PODZOL, Material.GRASS_BLOCK,
            Material.MUDDY_MANGROVE_ROOTS, Material.MUD
        )).toSet()
        private val SUPPORT_WARPED_FUNGUS = (Tag.NYLIUM.values + SUBSTRATE_OVERWORLD + listOf(
            Material.MYCELIUM, Material.SOUL_SOIL, Material.FARMLAND
        )).toSet()
        private val LOG_TO_SAPLING = compileAlternatives(
            Tag.OAK_LOGS to SaplingReplacementData(Material.OAK_SAPLING, SUBSTRATE_OVERWORLD),
            Tag.SPRUCE_LOGS to SaplingReplacementData(Material.SPRUCE_SAPLING, SUBSTRATE_OVERWORLD),
            Tag.BIRCH_LOGS to SaplingReplacementData(Material.BIRCH_SAPLING, SUBSTRATE_OVERWORLD),
            Tag.JUNGLE_LOGS to SaplingReplacementData(Material.JUNGLE_SAPLING, SUBSTRATE_OVERWORLD),
            Tag.ACACIA_LOGS to SaplingReplacementData(Material.ACACIA_SAPLING, SUBSTRATE_OVERWORLD),
            Tag.DARK_OAK_LOGS to SaplingReplacementData(Material.DARK_OAK_SAPLING, SUBSTRATE_OVERWORLD),
            Tag.MANGROVE_LOGS to SaplingReplacementData(Material.MANGROVE_PROPAGULE, SUBSTRATE_OVERWORLD),
            Tag.CHERRY_LOGS to SaplingReplacementData(Material.CHERRY_SAPLING, SUBSTRATE_OVERWORLD),
            Tag.PALE_OAK_LOGS to SaplingReplacementData(Material.PALE_OAK_SAPLING, SUBSTRATE_OVERWORLD),
            Tag.CRIMSON_STEMS to SaplingReplacementData(Material.CRIMSON_FUNGUS, SUPPORT_WARPED_FUNGUS),
            Tag.WARPED_STEMS to SaplingReplacementData(Material.WARPED_FUNGUS, SUPPORT_WARPED_FUNGUS)
        )
        private const val REPLANT_DELAY_TICKS = 40L

        private fun compileAlternatives(
            vararg alternatives: Pair<Tag<Material>, SaplingReplacementData>
        ): Map<Material, SaplingReplacementData> {
            val output = mutableMapOf<Material, SaplingReplacementData>()
            for (alternative in alternatives) {
                alternative.first.values
                    .forEach { output[it] = alternative.second }
            }
            return output
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    fun onMcMMODestroyTree(event: TreeFellerDestroyTreeEvent) {
        val player = event.player
        if (!player.hasPermission(PERMISSION)) {
            return
        }

        var factor = 0L
        for (block in event.blocks) {
            val below = block.getRelative(0, -1, 0)
            val below2 = block.getRelative(0, -2, 0)
            val replacementData = LOG_TO_SAPLING[block.type] ?: continue
            if (!LOG_TO_SAPLING.containsKey(below.type)
                || !replacementData.under.contains(below2.type)
            ) {
                continue
            }

            val saplingMaterial = replacementData.sapling
            if (!player.inventory.contains(saplingMaterial)) {
                continue
            }
            factor += 5

            below.location.syncDelayed(REPLANT_DELAY_TICKS + factor) {
                if (!player.inventory.contains(saplingMaterial) || !below.type.isAir) {
                    return@syncDelayed
                }

                val saplingItem = ItemStack(saplingMaterial, 1)
                player.inventory.removeItemAnySlot(saplingItem)

                below.blockData = saplingMaterial.createBlockData()
                val loc = below.location.toCenterLocation()
                below.world.playSound(loc, Sound.ITEM_BOTTLE_FILL, 0.8f, 1.0f)
                below.world.spawnParticle(Particle.DUST_PLUME, loc, 10, 0.4, 0.3, 0.4, 0.05)
            }
        }
    }

    data class SaplingReplacementData(val sapling: Material, val under: Set<Material>)
}