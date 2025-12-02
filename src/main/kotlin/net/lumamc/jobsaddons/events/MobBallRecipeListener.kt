package net.lumamc.jobsaddons.events

import dev.jsinco.luma.lumacore.manager.modules.AutoRegister
import dev.jsinco.luma.lumacore.manager.modules.RegisterType
import net.lumamc.jobsaddons.JobsAddons
import net.lumamc.jobsaddons.hooks.HookRegistry
import net.lumamc.jobsaddons.hooks.external.SafariNetHook
import org.bukkit.NamespacedKey
import org.bukkit.entity.HumanEntity
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.CraftItemEvent
import org.bukkit.event.inventory.PrepareItemCraftEvent
import org.bukkit.inventory.ShapedRecipe
import org.bukkit.persistence.PersistentDataType

@AutoRegister(RegisterType.LISTENER)
class MobBallRecipeListener : Listener {

    companion object {
        private const val CRAFT_HARD_LIMIT = 64 // Lifetime hard limit on how many reusable mob balls can be crafted by a player
        private const val CRAFT_PERMISSION = "jobsaddons.craft.reusable"
        private val CRAFT_HARD_LIMIT_KEY = NamespacedKey(JobsAddons.INSTANCE, "crafted-reusable-mob-ball")
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun onPrepareItemCraft(event: PrepareItemCraftEvent) {
        val safariNetHook: SafariNetHook = HookRegistry.getHook(SafariNetHook::class.java) ?: return
        val shapedRecipe = event.recipe as? ShapedRecipe ?: return

        if (shapedRecipe.key == safariNetHook.recipe.key && event.viewers.any { !it.hasPermission(CRAFT_PERMISSION) }) {
            event.inventory.result = null
            return
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun onItemCraft(event: CraftItemEvent) {
        val safariNetHook: SafariNetHook = HookRegistry.getHook(SafariNetHook::class.java) ?: return
        val player = event.whoClicked


        val shapedRecipe = event.recipe as? ShapedRecipe ?: return
        if (shapedRecipe.key != safariNetHook.recipe.key) return


        if (event.click != ClickType.LEFT || !player.hasPermission(CRAFT_PERMISSION)) {
            event.isCancelled = true
            return
        }


        val amountToCraft = event.inventory.result?.amount ?: run {
            event.isCancelled = true
            return
        }

        val currentCrafted = player.currentCraftedCount()

        if (currentCrafted + amountToCraft > CRAFT_HARD_LIMIT) {
            event.isCancelled = true
        } else {
            player.incrementCraftedCount(amountToCraft)
        }
    }

    fun HumanEntity.currentCraftedCount(): Int {
        val current = this.persistentDataContainer.get(CRAFT_HARD_LIMIT_KEY, PersistentDataType.INTEGER)
        return current ?: 0
    }

    fun HumanEntity.incrementCraftedCount(amount: Int) {
        val current = this.currentCraftedCount()
        this.persistentDataContainer.set(CRAFT_HARD_LIMIT_KEY, PersistentDataType.INTEGER, current + amount)
    }
}