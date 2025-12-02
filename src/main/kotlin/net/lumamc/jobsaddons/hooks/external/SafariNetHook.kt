package net.lumamc.jobsaddons.hooks.external

import de.Linus122.SafariNet.Main
import dev.jsinco.luma.lumacore.utility.Logging
import net.lumamc.jobsaddons.JobsAddons
import net.lumamc.jobsaddons.hooks.ExternalHook
import net.lumamc.jobsaddons.hooks.HookRegistry
import net.lumamc.jobsaddons.util.ClassUtil
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.ShapedRecipe

class SafariNetHook : ExternalHook() {

    override val name = "SafariNet"
    override val registerWhen = RegisterWhen.ON_ENABLE

    override fun canRegister(): Boolean {
        return ClassUtil.exists("de.Linus122.SafariNet.Main") && ClassUtil.exists("dev.jsinco.luma.lumaitems.LumaItems")
    }

    lateinit var reusableMobBall: ItemStack

    lateinit var recipe: ShapedRecipe

    val key = NamespacedKey(JobsAddons.INSTANCE, "reusable-mob-ball")

    override fun register() {
        reusableMobBall = Main.factory.emptyEggReusable

        val lumaItemsHook: LumaItemsHook = HookRegistry.getHook(LumaItemsHook::class.java) ?: throw IllegalStateException("LumaItemsHook not registered")

        recipe = ShapedRecipe(key, reusableMobBall)
        recipe.shape(
            "BBB",
            "BAB",
            "BBB"
        )

        recipe.setIngredient('A', lumaItemsHook.astralCore)
        recipe.setIngredient('B', ItemStack.of(Material.HAY_BLOCK))

        if (Bukkit.getRecipe(key) == null) {
            Bukkit.addRecipe(recipe)
            Logging.log("Registered custom SafariNet reusable mob ball recipe.")
        }
    }

    override fun unregister() {
        Bukkit.removeRecipe(key, true)
    }
}