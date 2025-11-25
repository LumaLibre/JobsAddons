package net.lumamc.jobsaddons.util

enum class JobConstant {
    ALCHEMIST("Alchemist"),
    BLACKSMITH("Blacksmith"),
    BUILDER("Builder"),
    COOK("Cook"),
    DIGGER("Digger"),
    FARMER("Farmer"),
    FISHERMAN("Fisherman"),
    HUNTER("Hunter"),
    LUMBERJACK("Lumberjack"),
    MINER("Miner");

    val jobName: String

    constructor(jobName: String) {
        this.jobName = jobName
    }


    override fun toString(): String {
        return jobName
    }
}