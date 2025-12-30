package dev.lumas.jobsaddons.model

import com.gamingmesh.jobs.Jobs
import com.gamingmesh.jobs.container.JobsPlayer
import dev.lumas.lumacore.utility.Text
import eu.okaeri.configs.OkaeriConfig
import net.luckperms.api.LuckPerms
import net.luckperms.api.node.Node
import dev.lumas.jobsaddons.JobsAddons
import dev.lumas.jobsaddons.storage.DataSource
import dev.lumas.jobsaddons.util.JobConstant
import org.bukkit.Bukkit
import org.bukkit.entity.Player

class JobPerkCluster() : OkaeriConfig() {

    private object CompanionHolder {
        val luckpermsAPI: LuckPerms? by lazy {
            Bukkit.getServicesManager().getRegistration(LuckPerms::class.java)?.provider
        }
        val logger = JobsAddons.INSTANCE.logger
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
        if (!checkJobLevel(player)) {
            JobsAddons.debug("Player ${player.name} does not meet the job level requirement for job $jobName level $level")
            return 0
        }

        var claimed = 0
        val api = CompanionHolder.luckpermsAPI ?: run {
            CompanionHolder.logger.warning("LuckPerms API not found, cannot claim permission perks.")
            return 0
        }
        val luckPermsUser = api.userManager.getUser(player.uniqueId) ?: run {
            CompanionHolder.logger.warning("LuckPerms user not found for player ${player.name}, cannot claim permission perks.")
            return 0
        }
        JobsAddons.debug("Claiming permission perks for player ${player.name} for job $jobName level $level:")
        for (container in permissions) {
            val perm = container.perk
            val msg = container.message


            if (player.hasPermission(perm)) {
                JobsAddons.debug("Player ${player.name} already has permission: $perm")
                continue
            }

            val node = Node.builder(perm).value(true).build()
            luckPermsUser.data().add(node)
            Text.msg(player, msg)

            claimed++
        }

        JobsAddons.debug("Total permission perks claimed for player ${player.name}: $claimed")

        api.userManager.saveUser(luckPermsUser)
        return claimed
    }

    fun claimCommandPerks(player: Player, consumer:((Int) -> Unit)? = null) {
        fun runConsumer(count: Int) {
            consumer?.invoke(count)
        }

        if (!checkJobLevel(player)) {
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
                    } else {
                        Text.msg(player, "$msg (Already claimed previously, rewriting.)")
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
        JobsAddons.debug("Fetching job level for player ${player.name} for job $jobName")
        val job = Jobs.getJob(jobName) ?: run {
            CompanionHolder.logger.warning("Job $jobName not found.")
            return 0
        }
        return jobsPlayer.getJobProgression(job).level
    }

    fun checkJobLevel(player: Player): Boolean {
        val playerJobLevel = getJobLevel(player)
        JobsAddons.debug("Player ${player.name} has job level $playerJobLevel for job $jobName, required level is $level")
        return playerJobLevel >= level
    }

    override fun toString(): String {
        return "JobPerkCluster(jobName='$jobName', level=$level, permissions=$permissions, commands=$commands)"
    }


    class PerkContainer() : OkaeriConfig() {
        var perk: String = ""
        var message: String = ""

        constructor(perk: String, message: String) : this() {
            this.perk = perk
            this.message = message
        }

        override fun toString(): String {
            return "PerkContainer(perk='$perk', message='$message')"
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