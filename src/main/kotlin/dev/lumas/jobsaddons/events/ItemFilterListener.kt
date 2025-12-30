package dev.lumas.jobsaddons.events

import dev.lumas.lumacore.manager.modules.AutoRegister
import dev.lumas.lumacore.manager.modules.RegisterType
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerAttemptPickupItemEvent
import java.util.UUID

@AutoRegister(RegisterType.LISTENER)
class ItemFilterListener : Listener {

    companion object {
        val FILTERED: MutableMap<UUID, HashSet<Material>> = mutableMapOf()
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    fun onPlayerAttemptPickupItem(event: PlayerAttemptPickupItemEvent) {
        val player = event.player
        val filteredMaterials = FILTERED[player.uniqueId]?.ifEmpty { return } ?: return
        val item = event.item

        if (item.itemStack.type in filteredMaterials) {
            event.isCancelled = true
        }
    }
}