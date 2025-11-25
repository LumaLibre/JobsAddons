package net.lumamc.jobsaddons.model

import com.gamingmesh.jobs.Jobs
import com.gamingmesh.jobs.container.JobsPlayer
import dev.jsinco.luma.lumacore.utility.Text
import eu.okaeri.configs.OkaeriConfig
import net.luckperms.api.LuckPerms
import net.luckperms.api.node.Node
import net.lumamc.jobsaddons.JobsAddons
import net.lumamc.jobsaddons.storage.DataSource
import net.lumamc.jobsaddons.util.JobConstant
import org.bukkit.Bukkit
import org.bukkit.entity.Player

class JobPerkCluster() : OkaeriConfig() {

    private object CompanionHolder {
        val luckpermsAPI: LuckPerms? by lazy {
            Bukkit.getServicesManager().getRegistration(LuckPerms::class.java)?.provider
        }
    }

    var jobName: String = ""
    var level: Int = -1
    var permissions: List<PerkContainer> = listOf()
    var commands: List<PerkContainer> = listOf()

    constructor(jobName: String, level: Int, permissionPerks: List<PerkContainer>, commandPerks: List<PerkContainer>) : this() {
        this.jobName = jobName
        this.level = level
        this.permissions = permissionPerks
        this.commands = commandPerks
    }


    fun claimPermissionPerks(player: Player): Int {
        if (!player.checkJobLevel()) {
            return 0
        }

        var claimed = 0
        val api = CompanionHolder.luckpermsAPI ?: return 0
        val luckPermsUser = api.userManager.getUser(player.uniqueId) ?: return 0

        for (container in permissions) {
            val perm = container.perk
            val msg = container.message
            val node = Node.builder(perm).value(true).build()

            if (luckPermsUser.data().toCollection().contains(node)) continue

            luckPermsUser.data().add(node)
            Text.msg(player, msg)

            claimed++
        }

        api.userManager.saveUser(luckPermsUser)
        return claimed
    }

    fun claimCommandPerks(player: Player, consumer:((Int) -> Unit)? = null) {
        fun runConsumer(count: Int) {
            consumer?.invoke(count)
        }

        if (!player.checkJobLevel()) {
            runConsumer(0)
            return
        }

        val dataSource = DataSource.INSTANCE
        dataSource.selectExecutedCommands(player.uniqueId).thenApply { list ->
            var claimed = 0
            val executedCommands = list.toMutableSet()

            Bukkit.getScheduler().runTask(JobsAddons.INSTANCE, Runnable {
                for (container in commands.filter { !executedCommands.contains(it.perk) }) {
                    val cmd = container.perk
                    val msg = container.message
                    if (executedCommands.contains(cmd)) continue

                    executedCommands.add(cmd)
                    claimed++

                    // Backwards compatibility
                    if (!player.hasPermission("jobsaddons.claimed${jobName}.$level")) {
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd.replace("{player}", player.name))
                        Text.msg(player, msg)
                    }
                }

                // Don't update db if nothing was claimed
                if (claimed > 0) {
                    dataSource.updateExecutedCommands(player.uniqueId, executedCommands.toList()).thenRun {
                        runConsumer(claimed)
                    }
                } else {
                    runConsumer(0)
                }
            })
        }
    }

    fun getJobLevel(player: Player): Int {
        val jobsPlayer: JobsPlayer = Jobs.getPlayerManager().getJobsPlayer(player)
        val job = Jobs.getJob(jobName) ?: return 0
        return jobsPlayer.getJobProgression(job).level
    }

    fun Player.checkJobLevel(): Boolean {
        return getJobLevel(this) >= level
    }


    class PerkContainer() : OkaeriConfig() {
        var perk: String = ""
        var message: String = ""

        constructor(perk: String, message: String) : this() {
            this.perk = perk
            this.message = message
        }
    }

    class Builder {

        companion object {
            @JvmStatic
            fun create() = Builder()
        }

        private var job: JobConstant = JobConstant.ALCHEMIST
        private var level: Int = -1
        private var permissionPerks: MutableList<PerkContainer> = mutableListOf()
        private var commandPerks: MutableList<PerkContainer> = mutableListOf()

        fun job(job: JobConstant) = apply { this.job = job }
        fun level(level: Int) = apply { this.level = level }
        fun permissionPerk(perk: String, msg: String) = apply { this.permissionPerks.add(PerkContainer(perk, msg)) }
        fun commandPerk(perk: String, msg: String) = apply { this.commandPerks.add(PerkContainer(perk, msg)) }

        fun build(): JobPerkCluster {
            return JobPerkCluster(job.jobName, level, permissionPerks, commandPerks)
        }
    }
}