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
        private const val UNCOMPILED_LOG_PATTERN = "(LOG|WOOD|STEM|HYPHAE)"
        private val LOG_REPLACE_PATTERN = Regex("_$UNCOMPILED_LOG_PATTERN")
        private val LOG_MATCH_ANY_PATTERN = Regex(".*$UNCOMPILED_LOG_PATTERN")
        private val SOIL_MATCH_ANY_PATTERN = Regex(".*(MOSS|DIRT|MYCELIUM|PODZOL|GRASS_BLOCK|MUD|MUDDY_MANGROVE_ROOTS)")
        private const val REPLANT_DELAY_TICKS = 40L
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    fun onMcMMODestroyTree(event: TreeFellerDestroyTreeEvent) {
        val player = event.player
        if (!player.hasPermission(PERMISSION)) {
            return
        }

        Executors.async {
            var factor = 0L
            for (block in event.blocks) {
                val below = block.getRelative(0, -1, 0)
                if (!LOG_MATCH_ANY_PATTERN.matches(block.type.name) || !SOIL_MATCH_ANY_PATTERN.matches(below.type.name)) {
                    continue
                }

                val saplingMaterial = saplingForWoodType(block.type) ?: continue
                if (!player.inventory.contains(saplingMaterial)) {
                    continue
                }
                factor += 5

                block.location.syncDelayed(REPLANT_DELAY_TICKS + factor) {
                    if (!player.inventory.contains(saplingMaterial)) {
                        return@syncDelayed
                    }

                    val saplingItem = ItemStack(saplingMaterial, 1)
                    player.inventory.removeItemAnySlot(saplingItem)

                    block.blockData = saplingMaterial.createBlockData()
                    val loc = block.location.toCenterLocation()
                    block.world.playSound(loc, Sound.ITEM_BOTTLE_FILL, 0.8f, 1.0f)
                    block.world.spawnParticle(Particle.DUST_PLUME, loc, 10, 0.4, 0.3, 0.4, 0.05)
                }
            }
        }
    }

    private fun saplingForWoodType(woodType: Material): Material? { // good enough
        val wood = woodType.name.replace(LOG_REPLACE_PATTERN, "")
        return Tag.SAPLINGS.values
            .firstOrNull { it.name.contains(wood) }
    }
}