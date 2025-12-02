package net.lumamc.jobsaddons.hooks.external

import dev.jsinco.luma.lumaitems.relics.RelicCrafting
import net.lumamc.jobsaddons.hooks.ExternalHook
import net.lumamc.jobsaddons.util.ClassUtil
import org.bukkit.inventory.ItemStack

class LumaItemsHook : ExternalHook() {

    override val name = "LumaItems"
    override val registerWhen = RegisterWhen.ON_ENABLE

    override fun canRegister() = ClassUtil.exists("dev.jsinco.luma.lumaitems.LumaItems")


    lateinit var astralCore: ItemStack

    override fun register() {
        astralCore = RelicCrafting.astralCore
    }
}