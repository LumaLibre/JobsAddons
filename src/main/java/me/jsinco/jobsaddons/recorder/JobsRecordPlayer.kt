package me.jsinco.jobsaddons.recorder

import org.bukkit.entity.Player


// To get the average of a group of numbers, you add them all up and then divide by the number of numbers.
class JobsRecordPlayer (val player: Player) {

    val recordingSince: Long = System.currentTimeMillis()

    var totalMoneyEarned: Double = 0.0
    var moneyEarnedOverLast100Ticks: MutableList<Double> = ArrayList(100)

    var totalExpEarned: Double = 0.0
    var expEarnedOverLast100Ticks: MutableList<Double> = ArrayList(100)




}