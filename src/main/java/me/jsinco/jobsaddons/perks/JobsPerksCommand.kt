package me.jsinco.jobsaddons.perks

import com.gamingmesh.jobs.Jobs
import com.gamingmesh.jobs.container.Job
import com.gamingmesh.jobs.container.JobsPlayer
import me.jsinco.jobsaddons.JobsAddons
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player

class JobsPerksCommand (
    private val plugin: JobsAddons
) : CommandExecutor, TabCompleter {




    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (sender !is Player || args.isEmpty()) return true

        val jobsPlayer: JobsPlayer = Jobs.getPlayerManager().getJobsPlayer(sender)
        val job : Job = Jobs.getJob(args[0].replace("Cook", "Cooker")) // Stupid as shit
        val dP = DeliverPerks(plugin, jobsPlayer, job)
        dP.claimAllJobPerksUntil(jobsPlayer.getJobProgression(job).level)


        return true
    }

    override fun onTabComplete(sender: CommandSender, command: Command, label: String, args: Array<out String>): MutableList<String>? {
        val jobNames: MutableList<String> = arrayListOf()

        Jobs.getJobs().forEach{job: Job? ->
            if (job != null) {
                jobNames.add(job.name.replace("Cooker", "Cook"))
            }
        }

        return jobNames
    }
}
