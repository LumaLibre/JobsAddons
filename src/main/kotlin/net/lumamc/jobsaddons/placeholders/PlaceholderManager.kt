package net.lumamc.jobsaddons.placeholders

import dev.jsinco.luma.lumacore.manager.modules.AutoRegister
import dev.jsinco.luma.lumacore.manager.modules.RegisterType
import dev.jsinco.luma.lumacore.manager.placeholder.AbstractPlaceholderManager
import dev.jsinco.luma.lumacore.manager.placeholder.PlaceholderInfo
import net.lumamc.jobsaddons.JobsAddons

@AutoRegister(RegisterType.PLACEHOLDER)
@PlaceholderInfo(identifier = "jobsaddons", author = "Jsinco", version = "2.0")
class PlaceholderManager : AbstractPlaceholderManager<JobsAddons, Placeholder>(JobsAddons.INSTANCE)