package dev.lumas.jobsaddons.configuration

import net.luckperms.api.LuckPerms
import net.luckperms.api.node.Node
import dev.lumas.jobsaddons.JobsAddons
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.permissions.PermissionAttachmentInfo
import java.util.regex.Pattern
import kotlin.math.pow

enum class JobsBlockConstant(val permissionPrefix: String, val defaultMax: Int, val absoluteMax: Int, val defaultCost: Double) {

    // i guess these should be configurable

    FURNACE("jobs.maxfurnaces", 20, 30, 15_000_000.0),
    BLAST_FURNACE("jobs.maxblastfurnaces", 10, 20, 22_000_000.0),
    SMOKER("jobs.maxsmokers", 10, 20, 22_000_000.0),
    BREWING_STAND("jobs.maxbrewingstands", 20, 30, 15_000_000.0);


    companion object {
        val luckpermsAPI: LuckPerms? by lazy {
            Bukkit.getServicesManager().getRegistration(LuckPerms::class.java)?.provider
        }
        val logger = JobsAddons.INSTANCE.logger

        private fun getMaxByPermission(permissionPrefix: String, player: Player): Int {
            val maxPermPattern = Pattern.compile("$permissionPrefix\\.(\\d+)")
            return player.effectivePermissions.stream()
                .map<Int> { permission: PermissionAttachmentInfo ->
                    val matcher = maxPermPattern.matcher(permission.permission)
                    if (matcher.matches()) {
                        val amt: Int = Integer.parseInt(matcher.group(1))
                        return@map amt
                    }
                    -1
                }
                .max(Comparator { obj: Int, anotherInteger: Int -> obj.compareTo(anotherInteger) })
                .orElse(-1)
        }
    }

    fun getCurrentAmountFor(player: Player): Int {
        val maxByPermission = getMaxByPermission(permissionPrefix, player)
        return if (maxByPermission != -1) {
            maxByPermission
        } else {
            defaultMax
        }
    }

    fun insertNewAmountFor(player: Player, amount: Int) {
        val api = luckpermsAPI ?: run {
            logger.warning("LuckPerms API not found, cannot insert new amount for player ${player.name}.")
            return
        }
        val luckPermsUser = api.userManager.getUser(player.uniqueId) ?: run {
            logger.warning("LuckPerms user not found for player ${player.name}, cannot insert new amount.")
            return
        }

        // Remove old permissions
        val maxPermPattern = Pattern.compile("$permissionPrefix\\.(\\d+)")
        val toRemove = mutableListOf<String>()
        for (permission in player.effectivePermissions) {
            val matcher = maxPermPattern.matcher(permission.permission)
            if (matcher.matches()) {
                toRemove.add(permission.permission)
            }
        }
        for (perm in toRemove) {
            val node = Node.builder(perm).value(true).build()
            luckPermsUser.data().remove(node)
        }

        // Add new permission
        val newPerm = "$permissionPrefix.$amount"
        val newNode = Node.builder(newPerm).value(true).build()
        luckPermsUser.data().add(newNode)
        api.userManager.saveUser(luckPermsUser)
        JobsAddons.debug("Set player ${player.name}'s $permissionPrefix limit to $amount")
    }


    fun getIntendedCost(currentAmount: Int): Double {
        // base amount = defaultCost
        // multiply cost by the number of extra blocks over defaultMax

        if (currentAmount <= defaultMax) {
            return defaultCost
        }

        val extraBlocks = currentAmount - defaultMax


        // TODO: changeme to exponential scaling
        // multiply it by 2 for every extra block
        // 20 = 15,000,000
        // 21 = 30,000,000
        // 22 = 60,000,000
        // 23 = 120,000,000
        // etc
        return defaultCost * 2.0.pow(extraBlocks.toDouble())
    }
}