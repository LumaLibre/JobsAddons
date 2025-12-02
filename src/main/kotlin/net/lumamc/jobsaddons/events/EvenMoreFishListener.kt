package net.lumamc.jobsaddons.events

import com.oheers.fish.FishUtils
import com.oheers.fish.fishing.items.Fish
import com.oheers.fish.selling.SellHelper
import dev.jsinco.luma.lumacore.manager.modules.AutoRegister
import dev.jsinco.luma.lumacore.manager.modules.RegisterType
import net.lumamc.jobsaddons.JobsAddons
import net.lumamc.jobsaddons.util.ClassUtil
import org.bukkit.NamespacedKey
import org.bukkit.entity.Item
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerFishEvent
import org.bukkit.persistence.PersistentDataType

@AutoRegister(RegisterType.LISTENER)
class EvenMoreFishListener : Listener {

    companion object {
        val emfExists: Boolean by lazy {
            return@lazy ClassUtil.exists("com.oheers.fish.FishUtils")
        }
        val autoSellKey = NamespacedKey(JobsAddons.INSTANCE, "emf-autosell")

        fun Player.hasAutoSellPersistentData(): Boolean {
            return this.persistentDataContainer.has(autoSellKey)
        }

        fun Player.setAutoSellPersistentData() {
            this.persistentDataContainer.set(autoSellKey, PersistentDataType.BOOLEAN, true)
        }

        fun Player.removeAutoSellPersistentData() {
            this.persistentDataContainer.remove(autoSellKey)
        }
    }


    // Only works if !EvenMoreFish#giveStraightToInventory
    @EventHandler(priority = EventPriority.HIGHEST)
    fun onFish(event: PlayerFishEvent) {
        val player = event.player
        if (event.state != PlayerFishEvent.State.CAUGHT_FISH || !emfExists || !player.hasAutoSellPersistentData()) return

        val caughtItem = event.caught as? Item ?: return
        val stack = caughtItem.itemStack

        val fish: Fish = FishUtils.getFish(stack) ?: return

        if (!JobsAddons.PERKS.autoSellFishRarities.contains(fish.rarity.id)) {
            return
        }

        caughtItem.remove()
        val sellHelper = SellHelper(arrayOf(stack), player)
        sellHelper.sell()
    }


}