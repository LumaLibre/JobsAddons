package dev.lumas.jobsaddons

import dev.lumas.lumacore.manager.modules.ModuleManager
import eu.okaeri.configs.ConfigManager
import eu.okaeri.configs.OkaeriConfig
import eu.okaeri.configs.serdes.standard.StandardSerdes
import eu.okaeri.configs.yaml.bukkit.YamlBukkitConfigurer
import dev.lumas.jobsaddons.configuration.PerksFile
import dev.lumas.jobsaddons.hooks.HookRegistry.registerHookClass
import dev.lumas.jobsaddons.hooks.HookRegistry.unregisterAllHooks
import dev.lumas.jobsaddons.hooks.external.VaultHook
import dev.lumas.jobsaddons.hooks.external.WorldGuardHook
import dev.lumas.jobsaddons.storage.DataSource
import dev.lumas.jobsaddons.util.PerksCommandUtil
import org.bukkit.plugin.java.JavaPlugin


class JobsAddons : JavaPlugin() {

    companion object {
        lateinit var INSTANCE: JobsAddons
            private set

        private lateinit var moduleManager: ModuleManager

        lateinit var PERKS: PerksFile
            private set

        fun debug(message: String) {
            if (PERKS.debug) {
                INSTANCE.logger.info(message)
            }
        }
    }

    override fun onLoad() {
        INSTANCE = this
        moduleManager = ModuleManager(this)
        registerHookClass(::WorldGuardHook)
    }

    override fun onEnable() {
        DataSource.INSTANCE.createTables()

        registerHookClass(::VaultHook)

        moduleManager.reflectivelyRegisterModules()
        PERKS = loadOkaeriFile(PerksFile::class.java, "perks.yml")
        PerksCommandUtil.potionEffectRunnable()

    }

    override fun onDisable() {
        moduleManager.unregisterModules()
        DataSource.INSTANCE.close()
        unregisterAllHooks()
    }

    fun <T : OkaeriConfig> loadOkaeriFile(clazz: Class<T>, fileName: String): T {
        return ConfigManager.create(clazz) {
            it.withConfigurer(YamlBukkitConfigurer(), StandardSerdes())
            it.withRemoveOrphans(false)
            it.withBindFile(dataPath.resolve(fileName))
            it.saveDefaults()
            it.load(true)
        }
    }
}