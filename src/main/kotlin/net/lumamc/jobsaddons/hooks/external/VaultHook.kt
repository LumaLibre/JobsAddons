package net.lumamc.jobsaddons.hooks.external

import net.lumamc.jobsaddons.hooks.ExternalHook
import net.lumamc.jobsaddons.util.ClassUtil
import net.milkbowl.vault.economy.Economy
import org.bukkit.Bukkit

class VaultHook : ExternalHook() {

    override val name = "Vault"

    override fun canRegister() = ClassUtil.exists("net.milkbowl.vault.economy.Economy")

    var economy: Economy? = null
        private set

    override fun register() {
        this.economy = Bukkit.getServicesManager().getRegistration(Economy::class.java)?.provider
    }
}