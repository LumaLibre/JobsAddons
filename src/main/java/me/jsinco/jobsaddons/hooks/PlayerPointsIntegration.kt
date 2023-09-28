package me.jsinco.jobsaddons.hooks

import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.chat.TextComponent
import org.black_ixx.playerpoints.PlayerPoints
import org.black_ixx.playerpoints.PlayerPointsAPI
import org.bukkit.ChatColor
import org.bukkit.entity.Player

object PlayerPointsIntegration {


    private val ppAPI: PlayerPointsAPI = PlayerPoints.getInstance().api

    fun givePoints(player: Player, amount: Int) {
        ppAPI.give(player.uniqueId, amount)
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent("${ChatColor.GREEN}You got:${ChatColor.GOLD} $amount Solcoin(s)"))
    }

    fun takePoints(player: Player, amount: Int) {
        ppAPI.take(player.uniqueId, amount)
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent("${ChatColor.GREEN}You lost:${ChatColor.GOLD} $amount Solcoin(s)"))
    }
}