package me.jsinco.jobsaddons.util;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import me.jsinco.jobsaddons.JobsAddons;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class AntiPayRegions {

    private static final JobsAddons plugin = JobsAddons.getPlugin(JobsAddons.class);

    public static boolean shouldPay(Player player) {
        com.sk89q.worldedit.util.Location wgLocation = BukkitAdapter.adapt(player.getLocation());

        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionQuery query = container.createQuery();
        ApplicableRegionSet set = query.getApplicableRegions(wgLocation);


        for (ProtectedRegion region : set) {
            if (plugin.getConfig().getStringList("block-pay").contains(region.getId())) {
                return false;
            }
        }
        return true;
    }
}
