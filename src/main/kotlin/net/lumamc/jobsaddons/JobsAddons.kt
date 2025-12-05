package net.lumamc.jobsaddons

import dev.jsinco.luma.lumacore.manager.modules.ModuleManager
import eu.okaeri.configs.ConfigManager
import eu.okaeri.configs.OkaeriConfig
import eu.okaeri.configs.serdes.standard.StandardSerdes
import eu.okaeri.configs.yaml.bukkit.YamlBukkitConfigurer
import net.lumamc.jobsaddons.configuration.PerksFile
import net.lumamc.jobsaddons.hooks.HookRegistry.registerHookClass
import net.lumamc.jobsaddons.hooks.HookRegistry.unregisterAllHooks
import net.lumamc.jobsaddons.hooks.external.WorldGuardHook
import net.lumamc.jobsaddons.storage.DataSource
import net.lumamc.jobsaddons.util.PerksCommandUtil
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