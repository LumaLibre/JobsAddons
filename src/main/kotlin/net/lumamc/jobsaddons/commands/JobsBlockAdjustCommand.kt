package net.lumamc.jobsaddons.commands

import dev.jsinco.luma.lumacore.manager.commands.AbstractCommand
import dev.jsinco.luma.lumacore.manager.commands.CommandInfo
import dev.jsinco.luma.lumacore.manager.modules.AutoRegister
import dev.jsinco.luma.lumacore.manager.modules.RegisterType
import dev.jsinco.luma.lumacore.utility.Text
import net.lumamc.jobsaddons.JobsAddons
import net.lumamc.jobsaddons.configuration.JobsBlockConstant
import net.lumamc.jobsaddons.hooks.HookRegistry
import net.lumamc.jobsaddons.hooks.external.VaultHook
import net.lumamc.jobsaddons.util.ClassUtil
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender

@AutoRegister(RegisterType.COMMAND)
@CommandInfo(
    name = "jobsblockadjust",
    description = "Adjust a player's jobs block limits",
    usage = "/<command> <player> <jobsblocktype> <add|remove> <amount>",
    permission = "jobsaddons.jobsblockadjust"
)
class JobsBlockAdjustCommand : AbstractCommand() {
    override fun handle(sender: CommandSender, label: String, args: Array<out String>): Boolean {
        if (args.size < 4) {
            return false
        }

        val target = Bukkit.getPlayerExact(args[0]) ?: run {
            Text.msg(sender, "Player not found: ${args[0]}")
            return true
        }
        val jobsBlockConstant = ClassUtil.enumValueOfOrNull(JobsBlockConstant::class.java, args[1].uppercase()) ?: run {
            Text.msg(sender, "Invalid Jobs Block type: ${args[3]}")
            return true
        }
        val action = ClassUtil.enumValueOfOrNull(Action::class.java, args[2].uppercase()) ?: run {
            Text.msg(sender, "Invalid action: ${args[1]}")
            return true
        }
        val amount = args[3].toIntOrNull() ?: run {
            Text.msg(sender, "Invalid amount: ${args[2]}")
            return true
        }

        val currentAmount = jobsBlockConstant.getCurrentAmountFor(target)
        val newAmount = when (action) {
            Action.ADD -> currentAmount + amount
            Action.REMOVE -> (currentAmount - amount).coerceAtLeast(0)
        }

        if (newAmount >= jobsBlockConstant.absoluteMax) {
            Text.msg(target, "Your ${jobsBlockConstant.name} limit cannot exceed the absolute maximum of ${jobsBlockConstant.absoluteMax}.")
            Text.msg(sender, "Cannot set ${target.name}'s ${jobsBlockConstant.name} limit to $newAmount as it exceeds the absolute maximum of ${jobsBlockConstant.absoluteMax}")
            return true
        }

        // i don't feel to great about hardcoding this
        val economy = HookRegistry.getHook<VaultHook>(VaultHook::class.java)?.economy ?: run {
            Text.msg(sender, "Economy hook not found, cannot adjust jobs block limits.")
            return true
        }

        val cost = jobsBlockConstant.getIntendedCost(currentAmount)

        if (economy.getBalance(target) < cost) {
            Text.msg(target, "You do not have enough money to adjust your ${jobsBlockConstant.name} limit to $newAmount. You need $${String.format("%,.2f", cost)} but only have $${String.format("%,.2f", economy.getBalance(target))}.")
            return true
        }

        val response = economy.withdrawPlayer(target, cost)
        if (response.transactionSuccess()) {
            jobsBlockConstant.insertNewAmountFor(target, newAmount)
            Text.msg(target, "Your ${jobsBlockConstant.name} limit has been set to $newAmount (was $currentAmount).")
            Text.msg(sender, "Set ${target.name}'s ${jobsBlockConstant.name} limit to $newAmount (was $currentAmount)")
            JobsAddons.debug("Adjusted ${target.name}'s ${jobsBlockConstant.name} limit from $currentAmount to $newAmount by ${sender.name} using action $action with amount $amount")
        } else {
            Text.msg(sender, "Failed to withdraw money from ${target.name}: ${response.errorMessage}")
            Text.msg(target, "Failed to withdraw money for adjusting your ${jobsBlockConstant.name} limit: ${response.errorMessage}")
        }
        return true

    }

    override fun handleTabComplete(sender: CommandSender, label: String, args: Array<out String>): List<String?>? {
        return when (args.size) {
            1 -> null
            2 -> JobsBlockConstant.entries.map { it.name.lowercase() }
            3 -> Action.entries.map { it.name.lowercase()  }
            4 -> listOf("<amount>")
            else -> emptyList()
        }
    }

    enum class Action {
        ADD,
        REMOVE
    }
}