package dev.lumas.jobsaddons.placeholders

import dev.lumas.lumacore.manager.modules.AutoRegister
import dev.lumas.lumacore.manager.modules.RegisterType
import dev.lumas.lumacore.manager.placeholder.PlaceholderInfo
import dev.lumas.jobsaddons.JobsAddons
import dev.lumas.jobsaddons.configuration.JobsBlockConstant
import dev.lumas.jobsaddons.util.ClassUtil
import org.bukkit.OfflinePlayer

@AutoRegister(RegisterType.PLACEHOLDER)
@PlaceholderInfo(identifier = "blockcost", parent = PlaceholderManager::class)
class JobBlockCostPlaceholder : Placeholder {


    override fun onRequest(plugin: JobsAddons, offlinePlayer: OfflinePlayer?, args: List<String>): String {
        if (args.isEmpty()) return "Invalid arguments: missing job block constant"
        val stringJobBlock = args.joinToString("_").uppercase()

        val player = offlinePlayer?.player ?: return "Player not found"
        val jobsBlockConstant = ClassUtil.enumValueOfOrNull(JobsBlockConstant::class.java, stringJobBlock) ?: return "Invalid job block constant: ${args[0]}"
        val currentAmount = jobsBlockConstant.getCurrentAmountFor(player)
        val cost = jobsBlockConstant.getIntendedCost(currentAmount)

        return String.format("%,.2f", cost)
    }
}