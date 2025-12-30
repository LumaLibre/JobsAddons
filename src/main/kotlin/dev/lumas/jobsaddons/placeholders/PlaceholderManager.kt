package dev.lumas.jobsaddons.placeholders

import dev.lumas.lumacore.manager.modules.AutoRegister
import dev.lumas.lumacore.manager.modules.RegisterType
import dev.lumas.lumacore.manager.placeholder.AbstractPlaceholderManager
import dev.lumas.lumacore.manager.placeholder.PlaceholderInfo
import dev.lumas.jobsaddons.JobsAddons

@AutoRegister(RegisterType.PLACEHOLDER)
@PlaceholderInfo(identifier = "jobsaddons", author = "Jsinco", version = "2.0")
class PlaceholderManager : AbstractPlaceholderManager<JobsAddons, Placeholder>(JobsAddons.INSTANCE)