package dev.lumas.jobsaddons.hooks.external

import dev.lumas.jobsaddons.hooks.ExternalHook
import dev.lumas.jobsaddons.util.ClassUtil
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