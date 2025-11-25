package net.lumamc.jobsaddons

import com.sk89q.worldguard.WorldGuard
import com.sk89q.worldguard.protection.flags.BooleanFlag
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException
import dev.jsinco.luma.lumacore.manager.modules.ModuleManager
import dev.jsinco.luma.lumacore.utility.Logging
import eu.okaeri.configs.ConfigManager
import eu.okaeri.configs.OkaeriConfig
import eu.okaeri.configs.serdes.standard.StandardSerdes
import eu.okaeri.configs.yaml.bukkit.YamlBukkitConfigurer
import net.lumamc.jobsaddons.configuration.PerksFile
import net.lumamc.jobsaddons.storage.DataSource
import net.lumamc.jobsaddons.util.PerksCommandUtil
import org.bukkit.plugin.java.JavaPlugin


class JobsAddons : JavaPlugin() {

    companion object {
        lateinit var INSTANCE: JobsAddons
            private set

        var JOBSADDONS_BLOCK_PAY_FLAG: BooleanFlag? = null
            private set

        private lateinit var moduleManager: ModuleManager

        lateinit var PERKS: PerksFile
            private set
    }

    override fun onLoad() {
        INSTANCE = this
        moduleManager = ModuleManager(this)

        val registry = WorldGuard.getInstance().flagRegistry
        try {
            val flag = BooleanFlag("jobsaddons-block-pay")
            registry.register(flag)
            JOBSADDONS_BLOCK_PAY_FLAG = flag
        } catch (e: FlagConflictException) {
            Logging.errorLog("Another plugin already took the jobsaddons-block-pay flag!", e)
        }
    }

    override fun onEnable() {
        DataSource.INSTANCE.createTables()
        moduleManager.reflectivelyRegisterModules()
        PERKS = loadOkaeriFile(PerksFile::class.java, "perks.yml")
        PerksCommandUtil.potionEffectRunnable()
    }

    override fun onDisable() {
        DataSource.INSTANCE.close()
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