package net.lumamc.jobsaddons.configuration

import eu.okaeri.configs.OkaeriConfig
import net.lumamc.jobsaddons.model.JobPerkCluster
import net.lumamc.jobsaddons.util.JobConstant

class PerksFile : OkaeriConfig() {

    fun cluster(jobName: String, level: Int): JobPerkCluster? {
        return perks.firstOrNull { it.jobName == jobName && it.level == level }
    }

    fun clusters(jobName: String, until: Int): List<JobPerkCluster> {
        return perks.filter { it.jobName == jobName && it.level <= until }
    }

    fun builder(): JobPerkCluster.Builder {
        return JobPerkCluster.Builder.create()
    }

    var debug = true

    var autoSellFishRarities = listOf(
        "Common",
        "Uncommon",
        "Rare",
        "Junk",
        "Quirky",
        "Whimsical"
    )

    // this is tedious as fuck
    var perks: List<JobPerkCluster> = buildList {

        // Alchemist

        add(builder()
            .job(JobConstant.ALCHEMIST)
            .level(20)
            .permissionPerk(
                "eternaltags.tag.alchemist",
                "You have unlocked the <b><#7b66fb>Elixirian</#7b66fb></b> tag perk!"
            )
            .build())

        add(builder()
            .job(JobConstant.ALCHEMIST)
            .level(35)
            .permissionPerk(
                "jobsaddons.absorption",
                "You have unlocked the <b><#F7FFC9>/absorption</#F7FFC9></b> command perk!"
            )
            .build())

        add(builder()
            .job(JobConstant.ALCHEMIST)
            .level(45)
            .permissionPerk(
                "jobsaddons.bottle",
                "You have unlocked the <b><#F7FFC9>/bottle</#F7FFC9></b> command perk!"
            )
            .permissionPerk(
                "jobsaddons.blaze",
                "You have unlocked the <b><#F7FFC9>/blaze</#F7FFC9></b> command perk!"
            )
            .commandPerk(
                "mcmmo:addlevels {player} Alchemy 500",
                "You got 500 mcMMO levels of Alchemy!"
            )
            .build())

        add(builder()
            .job(JobConstant.ALCHEMIST)
            .level(70)
            .permissionPerk(
                "eternaltags.tag.alchemist2",
                "You have unlocked the <b><#dd64fb>B<#e16af3>r<#e670eb>e<#ea76e3>w<#ee7cdb>s<#f282d2>m<#f788ca>i<#fb8ec2>t<#ff94ba>h<#ff94ba></b><white> tag perk!"
            )
            .build())

        add(builder()
        .job(JobConstant.ALCHEMIST)
            .level(85)
            .commandPerk(
                "malts:malts max stock add {player} 70000",
                "You have increased your max warehouse stock by 70,000!"
            )
            .build())

        // Blacksmith

        add(builder()
            .job(JobConstant.BLACKSMITH)
            .level(20)
            .permissionPerk(
                "eternaltags.tag.blacksmith",
                "You have unlocked the <b><#5c77a9>Artisan</#5c77a9></b> tag perk!"
            )
            .build())

        add(builder()
            .job(JobConstant.BLACKSMITH)
            .level(35)
            .permissionPerk(
                "jobsaddons.fireresistance",
                "You have unlocked the <b><#F7FFC9>/fireresistance</<#F7FFC9></b> command perk!"
            )
            .build())

        add(builder()
            .job(JobConstant.BLACKSMITH)
            .level(45)
            .permissionPerk(
                "eternaltags.tag.blacksmith2",
                "You have unlocked the <b><#6d9dca>W<#7aa4cd>e<#88abd0>l<#95b2d3>l<#a3b9d6>e<#b0c0d9>r</b><white> tag perk!"
            )
            .permissionPerk(
                "jobsaddons.triad",
                "You have unlocked the <b><#F7FFC9>/triad</#F7FFC9></b> command perk!"
            )
            .build())

        add(builder()
            .job(JobConstant.BLACKSMITH)
            .level(70)
            .permissionPerk(
                "eternaltags.tag.blacksmith3",
                "You have unlocked the <b><#f6e9fb>I<#e8d9eb>r<#dacadb>o<#ccbaca>n<#beaaba>s<#af9aaa>m<#a18b9a>i<#937b89>t<#856b79>h</b><white> tag perk!"
            )
            .build())

        add(builder()
        .job(JobConstant.BLACKSMITH)
            .level(85)
            .permissionPerk(
                "malts.warehouse.mode.click_to_deposit",
                "You have unlocked the <b><#F7FFC9>Click to Deposit</#F7FFC9></b> warehouse mode perk!"
            )
            .build())

        // Builder

        add(builder()
                .job(JobConstant.BUILDER)
                .level(20)
                .permissionPerk(
                    "eternaltags.tag.builder",
                    "You have unlocked the <b><#9cfe88>Carpenter</#9cfe88></b> tag perk!"
                )
                .build())

        add(builder()
            .job(JobConstant.BUILDER)
            .level(35)
            .permissionPerk(
                "jobsaddons.top",
                "You have unlocked the <b><#F7FFC9>/top</#F7FFC9></b> command perk!"
            )
            .permissionPerk(
                "jobsaddons.bottom",
                "You have unlocked the <b><#F7FFC9>/bottom</#F7FFC9></b> command perk!"
            )
            .permissionPerk(
                "malts.warehouse.mode.auto_replenish",
                "You have unlocked the <b><#F7FFC9>Auto Replenish</#F7FFC9></b> warehouse mode perk!"
            )
            .build())

        add(builder()
            .job(JobConstant.BUILDER)
            .level(45)
            .permissionPerk(
                "jobsaddons.concrete",
                "You have unlocked the <b><#F7FFC9>/concrete</#F7FFC9></b> command perk!"
            )
            .permissionPerk(
                "jobsaddons.powder",
                "You have unlocked the <b><#F7FFC9>/powder</#F7FFC9></b> command perk!"
            )
            .build())

        add(builder()
            .job(JobConstant.BUILDER)
            .level(55)
            .permissionPerk(
                "jobsaddons.stripcolor",
                "You have unlocked the <b><#F7FFC9>/stripcolor</#F7FFC9></b> command perk!"
            )
            .build())

        add(builder()
            .job(JobConstant.BUILDER)
            .level(70)
            .permissionPerk(
                "eternaltags.tag.builder2",
                "You have unlocked the <b><#42ff6b>A<#4dff74>r<#57ff7d>c<#62ff86>h<#6cff8f>i<#77ff98>t<#81ffa1>e<#8cffaa>c<#96ffb3>t</b><white> tag perk!"
            )
            .build())

        add(builder()
            .job(JobConstant.BUILDER)
            .level(85)
            .permissionPerk(
                "jobsaddons.recolor",
                "You have unlocked the <b><#F7FFC9>/recolor</#F7FFC9></b> command perk!"
            )
            .build())

        // Cook

        add(builder()
            .job(JobConstant.COOK)
            .level(20)
            .permissionPerk(
                "eternaltags.tag.cook",
                "You have unlocked the <b><#ff8e60>Baker</#ff8e60></b> tag perk!"
            )
            .build())

        add(builder()
            .job(JobConstant.COOK)
            .level(35)
            .permissionPerk(
                "essentials.feed",
                "You have unlocked the <b><#F7FFC9>/feed</#F7FFC9></b> command perk!"
            )
            .build())

        add(builder()
            .job(JobConstant.COOK)
            .level(45)
            .permissionPerk(
                "essentials.heal",
                "You have unlocked the <b><#F7FFC9>/heal</#F7FFC9></b> command perk!"
            )
            .permissionPerk(
                "eternaltags.tag.cook2",
                "You have unlocked the <b><#ffc56d>Chef</#ffc56d></b> tag perk!"
            )
            .build())

        add(builder()
            .job(JobConstant.COOK)
            .level(70)
            .permissionPerk(
                "eternaltags.tag.cook3",
                "You have unlocked the <b><#ffa653>C&#ffa15a>a&#ff9c62>t&#ff9769>e&#ff9170>r&#ff8c78>e&#ff877f>r</#ffa653></b> tag perk!"
            )
            .build())

        add(builder()
            .job(JobConstant.COOK)
            .level(85)
            .permissionPerk(
                "malts.warehouse.mode.click_to_deposit",
                "You have unlocked the <b><#F7FFC9>Click to Deposit</#F7FFC9></b> warehouse mode perk!"
            )
            .build())

        // Digger

        add(builder()
            .job(JobConstant.DIGGER)
            .level(20)
            .permissionPerk(
                "eternaltags.tag.digger",
                "You have unlocked the <b><#ffe589>Graveler</#ffe589></b> tag perk!"
            )
            .build())

        add(builder()
            .job(JobConstant.DIGGER)
            .level(35)
            .permissionPerk(
                "jobsaddons.grass",
                "You have unlocked the <b><#F7FFC9>/grass</#F7FFC9></b> command perk!"
            )
            .permissionPerk(
                "jobsaddons.dirt",
                "You have unlocked the <b><#F7FFC9>/dirt</#F7FFC9></b> command perk!"
            )
            .build())

        add(builder()
            .job(JobConstant.DIGGER)
            .level(45)
            .permissionPerk(
                "jobsaddons.concrete",
                "You have unlocked the <b><#F7FFC9>/concrete</#F7FFC9></b> command perk!"
            )
            .permissionPerk(
                "jobsaddons.powder",
                "You have unlocked the <b><#F7FFC9>/powder</#F7FFC9></b> command perk!"
            )
            .build())

        add(builder()
            .job(JobConstant.DIGGER)
            .level(70)
            .permissionPerk(
                "eternaltags.tag.digger2",
                "You have unlocked the <b><#ffca8c>Excavator</#ffca8c></b> tag perk!"
            )
            .build())

        add(builder()
            .job(JobConstant.DIGGER)
            .level(85)
            .permissionPerk(
                "malts.warehouse.mode.auto_store",
                "You have unlocked the <b><#F7FFC9>Auto Store</#F7FFC9></b> warehouse mode perk!"
            )
            .build())

        // Farmer

        add(builder()
            .job(JobConstant.FARMER)
            .level(20)
            .permissionPerk(
                "eternaltags.tag.farmer",
                "You have unlocked the <b><#9fff63>Gardener</#9fff63></b> tag perk!"
            )
            .build())

        add(builder()
            .job(JobConstant.FARMER)
            .level(35)
            .permissionPerk(
                "safarinet.craft.singleuse",
                "You have unlocked the <b><#F7FFC9>craft mob balls</#F7FFC9></b> perk!"
            )
            .permissionPerk(
                "malts.warehouse.mode.auto_replenish",
                "You have unlocked the <b><#F7FFC9>Auto Replenish</#F7FFC9></b> warehouse mode perk!"
            )
            .build())

        add(builder()
            .job(JobConstant.FARMER)
            .level(45)
            .commandPerk(
                "mcmmo:addlevels {player} Herbalism 500",
                "You got 500 mcMMO levels of Herbalism!"
            )
            .build())

        add(builder()
            .job(JobConstant.FARMER)
            .level(70)
            .permissionPerk(
                "eternaltags.tag.farmer2",
                "You have unlocked the <b><#9aff98>H<#a1ff8a>a<#a9ff7d>r<#b0ff6f>v<#b8ff62>e<#bfff54>s<#c6ff46>t<#ceff39>e<#d5ff2b>r</b><white> tag perk!"
            )
            .build())

        add(builder()
            .job(JobConstant.FARMER)
            .level(85)
            .commandPerk(
                "safarinet:safarinet give {player} reusable 12",
                "You have received 12 reusable mob balls!"
            )
            .build())

        // Fisherman

        add(builder()
            .job(JobConstant.FISHERMAN)
            .level(20)
            .permissionPerk(
                "eternaltags.tag.fisherman",
                "You have unlocked the <b><#7acaff>Fisher</#7acaff></b> tag perk!"
            )
            .build())

        add(builder()
            .job(JobConstant.FISHERMAN)
            .level(35)
            .permissionPerk(
                "jobsaddons.luck",
                "You have unlocked the <b><#F7FFC9>/luck</#F7FFC9></b> command perk!"
            )
            .build())

        add(builder()
            .job(JobConstant.FISHERMAN)
            .level(45)
            .permissionPerk(
                "eternaltags.tag.fisherman2",
                "You have unlocked the <b><#7a97ff>C<#83a4ff>a<#8db0ff>s<#96bdff>t<#a0c9ff>a<#a9d6ff>w<#b3e2ff>a<#bcefff>y</b><white> tag perk!"
            )
            .permissionPerk(
                "jobsaddons.dolphinsgrace",
                "You have unlocked the <b><#F7FFC9>/dolphinsgrace</#F7FFC9></b> command perk!"
            )
            .build())

        add(builder()
            .job(JobConstant.FISHERMAN)
            .level(70)
            .permissionPerk(
                "eternaltags.tag.fisherman3",
                "You have unlocked the <b><#44c7ff>R<#4fd2eb>e<#5bddd6>l<#66e9c2>l<#72f4ad>e<#7dff99>r</b><white> tag perk!"
            )
            .build())

        add(builder()
            .job(JobConstant.FISHERMAN)
            .level(85)
            .permissionPerk(
                "jobsaddons.autosellfish",
                "You have unlocked the <b><#F7FFC9>/autosellfish</#F7FFC9></b> command perk!"
            )
            .build())

        // Hunter

        add(builder()
            .job(JobConstant.HUNTER)
            .level(20)
            .permissionPerk(
                "eternaltags.tag.hunter",
                "You have unlocked the <b><#f5605e>Sniper</#f5605e></b> tag perk!"
            )
            .build())

        add(builder()
            .job(JobConstant.HUNTER)
            .level(35)
            .permissionPerk(
                "jobsaddons.strength",
                "You have unlocked the <b><#F7FFC9>/strength</#F7FFC9></b> command perk!"
            )
            .build())

        add(builder()
            .job(JobConstant.HUNTER)
            .level(45)
            .permissionPerk(
                "eternaltags.tag.hunter2",
                "You have unlocked the <b><#ff9356>A<#ff854c>r<#ff7743>c<#ff6939>h<#ff5b30>e<#ff4d26>r</b><white> tag perk!"
            )
            .permissionPerk(
                "jobsaddons.speed",
                "You have unlocked the <b><#F7FFC9>/speed</#F7FFC9></b> command perk!"
            )
            .build())

        add(builder()
            .job(JobConstant.HUNTER)
            .level(70)
            .permissionPerk(
                "eternaltags.tag.hunter3",
                "You have unlocked the <b><#ff3e14>S<#ee391f>l<#dd332a>a<#cb2e35>y<#ba2840>e<#a9234b>r</b><white> tag perk!"
            )
            .build())

        add(builder()
            .job(JobConstant.HUNTER)
            .level(85)
            .permissionPerk(
                "jobsaddons.itemfilter",
                "You have unlocked the <b><#F7FFC9>/itemfilter</#F7FFC9></b> command perk!"
            )
            .build())

        // Lumberjack

        add(builder()
            .job(JobConstant.LUMBERJACK)
            .level(20)
            .permissionPerk(
                "eternaltags.tag.lumberjack",
                "You have unlocked the <b><#bcf067>Lumberer</#bcf067></b> tag perk!"
            )
            .build())

        add(builder()
            .job(JobConstant.LUMBERJACK)
            .level(30)
            .permissionPerk(
                "mcmmo.ability.woodcutting.treefeller",
                "You have unlocked the Tree Feller ability perk!"
            )
            .build())

        add(builder()
            .job(JobConstant.LUMBERJACK)
            .level(35)
            .permissionPerk(
                "jobsaddons.top",
                "You have unlocked the <b><#F7FFC9>/top</#F7FFC9></b> command perk!"
            )
            .build())

        add(builder()
            .job(JobConstant.LUMBERJACK)
            .level(45)
            .permissionPerk(
                "eternaltags.tag.lumberjack2",
                "You have unlocked the <b><#72f05d>A<#74f066>x<#75f06f>e<#77f078>m<#79f081>a<#7af08a>s<#7cf093>t<#7df09c>e<#7ff0a5>r</b><white> tag perk!"
            )
            .permissionPerk(
                "jobsaddons.haste",
                "You have unlocked the <b><#F7FFC9>/haste</#F7FFC9></b> command perk!"
            )
            .build())

        add(builder()
            .job(JobConstant.LUMBERJACK)
            .level(55)
            .permissionPerk(
                "jobsaddons.strip",
                "You have unlocked the <b><#F7FFC9>/strip</#F7FFC9></b> command perk!"
            )
            .build())

        add(builder()
            .job(JobConstant.LUMBERJACK)
            .level(70)
            .permissionPerk(
                "eternaltags.tag.lumberjack3",
                "You have unlocked the <b><#27ac6c>A<#34b663>r<#41bf5a>b<#4ec951>o<#5cd348>r<#69dd3f>i<#76e636>s<#83f02d>t</b><white> tag perk!"
            )
            .build())

        add(builder()
            .job(JobConstant.LUMBERJACK)
            .level(85)
            .permissionPerk(
                "jobsaddons.replanttreefeller",
                "You have unlocked the <b><#F7FFC9>Tree feller auto-replant</#F7FFC9></b> perk!"
            )
            .build())

        // Miner

        add(builder()
            .job(JobConstant.MINER)
            .level(20)
            .permissionPerk(
                "eternaltags.tag.miner",
                "You have unlocked the <b><#c343ff>Dredger</#c343ff></b> tag perk!"
            )
            .build())

        add(builder()
            .job(JobConstant.MINER)
            .level(35)
            .permissionPerk(
                "essentials.top",
                "You have unlocked the <b><#F7FFC9>/top</#F7FFC9></b> command perk!"
            )
            .build())

        add(builder()
            .job(JobConstant.MINER)
            .level(45)
            .permissionPerk(
                "eternaltags.tag.miner2",
                "You have unlocked the <b><#9a35ff>G<#a037ff>e<#a639ff>m<#ac3bff>o<#b23dff>l<#b73fff>o<#bd41ff>g<#c343ff>i<#c945ff>s<#cf47ff>t</b><white> tag perk!"
            )
            .permissionPerk(
                "jobsaddons.nightvision",
                "You have unlocked the <b><#F7FFC9>/nightvision</#F7FFC9></b> command perk!"
            )
            .build())

        add(builder()
            .job(JobConstant.MINER)
            .level(65)
            .permissionPerk(
                "jobsaddons.haste",
                "You have unlocked the <b><#F7FFC9>/haste</#F7FFC9></b> command perk!"
            )
            .build())

        add(builder()
            .job(JobConstant.MINER)
            .level(70)
            .permissionPerk(
                "eternaltags.tag.miner3",
                "You have unlocked the <b><#ce50fb>C<#d05dfb>a<#d16bfc>v<#d378fc>e<#d586fc>r<#d793fc>n<#d8a1fd>e<#daaefd>r</b><white> tag perk!"
            )
            .build())

        add(builder()
            .job(JobConstant.MINER)
            .level(85)
            .permissionPerk(
                "malts.warehouse.mode.auto_store",
                "You have unlocked the <b><#F7FFC9>Auto Store</#F7FFC9></b> warehouse mode perk!"
            )
            .build())
    }


}