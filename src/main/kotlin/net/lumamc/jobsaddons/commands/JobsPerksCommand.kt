package net.lumamc.jobsaddons.commands

import com.gamingmesh.jobs.Jobs
import com.gamingmesh.jobs.container.Job
import com.gamingmesh.jobs.container.JobsPlayer
import dev.jsinco.luma.lumacore.manager.commands.AbstractCommand
import dev.jsinco.luma.lumacore.manager.commands.CommandInfo
import dev.jsinco.luma.lumacore.manager.modules.AutoRegister
import dev.jsinco.luma.lumacore.manager.modules.RegisterType
import dev.jsinco.luma.lumacore.utility.Text
import net.lumamc.jobsaddons.JobsAddons
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

@CommandInfo(
    name = "jobsperks",
    permission = "jobsaddons.command.jobsperks",
    usage = "/<command> <job>",
    aliases = ["jobperks", "jperks", "perks"],
    playerOnly = true
)
@AutoRegister(RegisterType.COMMAND)
class JobsPerksCommand : AbstractCommand() {

    override fun handle(sender: CommandSender, label: String, args: Array<String>): Boolean {
        val player = sender as Player
        if (args.isEmpty()) return false

        val subCommand = args[0]

        if (subCommand == "reload" && sender.hasPermission("jobsaddons.command.reload")) {
            JobsAddons.PERKS.load(true)
            Text.msg(player, "Perks file reloaded.")
            return true
        }

        val jobsPlayer: JobsPlayer = Jobs.getPlayerManager().getJobsPlayer(player)
        val job : Job = Jobs.getJob(subCommand) ?: return true
        if (!jobsPlayer.isInJob(job)) {
            Text.msg(player, "You must be in this job")
            return true
        }

        val level = jobsPlayer.getJobProgression(job).level

        val clusters = JobsAddons.PERKS.clusters(job.name, level)
        JobsAddons.debug("Player ${player.name} is claiming perks for job ${job.name} at level $level")
        JobsAddons.debug("Found ${clusters.size} perk clusters to process")
        JobsAddons.debug(clusters.toString())
        var claimed = 0
        JobsAddons.debug("Starting")
        for (cluster in clusters) {
            JobsAddons.debug("Processing cluster: $cluster")
            claimed += cluster.claimPermissionPerks(player)
            cluster.claimCommandPerks(player) {
                claimed += it
            }
        }

        if (claimed == 0) {
            Text.msg(player, "You have no perks to claim.")
        } else {
            Text.msg(player, "You have claimed your perks for job ${job.name} up to level $level")
        }
        return true
    }

    override fun handleTabComplete(sender: CommandSender, label: String, args: Array<out String>): List<String> {
        return Jobs.getJobs().map { it.name }.toList()
    }
}
