package me.jsinco.jobsaddons.perks;

import me.jsinco.jobsaddons.util.ColorUtils;
import me.jsinco.jobsaddons.JobsAddons;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.Map;

public class MiscPerkCommands implements CommandExecutor {


    private final JobsAddons plugin;

    public MiscPerkCommands(JobsAddons plugin) {
        this.plugin = plugin;
        plugin.getCommand("bottle").setExecutor(this);
        plugin.getCommand("blaze").setExecutor(this);
        plugin.getCommand("concrete").setExecutor(this);
        plugin.getCommand("powder").setExecutor(this);
        plugin.getCommand("stripcolor").setExecutor(this);
        plugin.getCommand("grass").setExecutor(this);
        plugin.getCommand("dirt").setExecutor(this);
        plugin.getCommand("placehere").setExecutor(this);
        plugin.getCommand("strip").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player player)) return true;
        switch (command.getName().toLowerCase()) {
            case "bottle" -> convertItemFromInventory(player, Material.GLASS, Material.GLASS_BOTTLE, 3, 3);
            case "blaze" -> convertItemFromInventory(player, Material.BLAZE_ROD, Material.BLAZE_POWDER, 1, 2);
            case "grass" -> convertItemFromInventory(player, Material.DIRT, Material.GRASS_BLOCK, 1, 1);
            case "dirt" -> convertItemFromInventory(player, Material.GRASS_BLOCK, Material.DIRT, 1, 1);
            case "concrete" -> replaceInventoryItemFromString(player, "CONCRETE_POWDER", "CONCRETE");
            case "powder" -> replaceInventoryItemFromString(player, "CONCRETE", "CONCRETE_POWDER");
            // Methods I have not edited yet
            case "stripcolor" -> player.sendMessage(stripcolor(player));
            case "placehere" -> player.sendMessage(placehere(player));
            case "strip" -> player.sendMessage(strip(player));
        }
        return true;
    }

    private String stripcolor(Player player) {
        ItemStack item = player.getInventory().getItemInMainHand();
        String m = item.getType().toString();

        if (m.endsWith("_WOOL")) item.setType(Material.WHITE_WOOL);
        else if (m.endsWith("_CONCRETE")) item.setType(Material.WHITE_CONCRETE);
        else if (m.endsWith("_CONCRETE_POWDER")) item.setType(Material.WHITE_CONCRETE_POWDER);
        else if (m.endsWith("_GLAZED_TERRACOTTA")) item.setType(Material.WHITE_GLAZED_TERRACOTTA);
        else if (m.endsWith("_TERRACOTTA")) item.setType(Material.TERRACOTTA);
        else if (m.endsWith("_STAINED_GLASS_PANE")) item.setType(Material.GLASS_PANE);
        else if (m.endsWith("_STAINED_GLASS") || m.equals("TINTED_GLASS")) item.setType(Material.GLASS);
        else if (m.endsWith("_SHULKER_BOX")) item.setType(Material.SHULKER_BOX);
        else if (m.endsWith("_BED")) item.setType(Material.WHITE_BED);
        else if (m.endsWith("_CANDLE")) item.setType(Material.CANDLE);
        else if (m.endsWith("_BANNER")) item.setType(Material.WHITE_BANNER);
        else if (m.endsWith("_CARPET") && !m.equals("MOSS_CARPET")) item.setType(Material.WHITE_CARPET);

        return ColorUtils.colorcode(plugin.getConfig().getString("prefix") + "Stripped color from item in hand");
    }

    private String placehere(Player player) {
        try {
            if (player.getInventory().getItemInMainHand().getType().equals(Material.AIR) || noPlace(player)) {
                throw new IllegalArgumentException();
            }
            Block block = player.getLocation().getBlock();
            if (!block.getType().isAir()) {
                return ColorUtils.colorcode(plugin.getConfig().getString("prefix") + "There is already a block here!");
            }
            block.setType(player.getInventory().getItemInMainHand().getType());
            String m = block.getType().toString();
            player.getInventory().getItemInMainHand().setAmount(player.getInventory().getItemInMainHand().getAmount() - 1);
            return ColorUtils.colorcode(plugin.getConfig().getString("prefix") + "Set block at location to " + m.toLowerCase().replace("_", " "));
        } catch (IllegalArgumentException e) {
            return ColorUtils.colorcode(plugin.getConfig().getString("prefix") + "You cannot place that block here!");
        }
    }

    private String strip(Player player) {
        for (ItemStack item : player.getInventory().getContents()) {
            if (item == null || item.getType().toString().contains("STRIPPED_")) continue;
            String logType = null;
            if (item.getType().toString().endsWith("_LOG")) logType = item.getType().toString();
            else if (item.getType().toString().endsWith("_WOOD")) logType = item.getType().toString();
            else if (item.getType().toString().endsWith("_STEM")) logType = item.getType().toString();
            else if (item.getType().toString().endsWith("_HYPHAE")) logType = item.getType().toString();

            if (logType != null) {
                item.setType(Material.valueOf("STRIPPED_" + logType));
            }
        }
        return ColorUtils.colorcode(plugin.getConfig().getString("prefix") + "Stripped all logs and wood in your inventory");
    }

    public boolean noPlace(Player player) {
        BlockPlaceEvent event = new BlockPlaceEvent(player.getLocation().getBlock(), player.getLocation().getBlock().getState(), player.getLocation().getBlock(), player.getInventory().getItemInMainHand(), player, true, EquipmentSlot.HAND);
        Bukkit.getPluginManager().callEvent(event);
        return event.isCancelled();
    }


    // FIXME
    public void convertItemFromInventory(Player player, Material from, Material to, int craftAmount, int craftReturnAmount) {
        if (player.getInventory().getItemInOffHand().getType().equals(from)) {
            player.sendMessage(ColorUtils.colorcode(plugin.getConfig().getString("prefix") + "You cannot convert your offhand or helmet into this"));
            return;
        } else if (player.getInventory().getHelmet() != null && player.getInventory().getHelmet().getType().equals(from)) {
            player.sendMessage(ColorUtils.colorcode(plugin.getConfig().getString("prefix") + "You cannot convert your offhand or helmet into this"));
            return;
        }

        PlayerInventory inv = player.getInventory();


        int amount = 0;
        for (ItemStack item : inv.getContents()) {
            if (item != null && item.getType() == from) {
                amount += item.getAmount();
            }
        }
        amount /= craftAmount;

        String replace = from.toString().toLowerCase().replace("_", " ");
        if (amount < craftAmount) {
            player.sendMessage(ColorUtils.colorcode(plugin.getConfig().getString("prefix") + "You do not have any " + replace + "'s to convert"));
            return;
        }

        inv.removeItem(new ItemStack(from, amount * craftAmount));


        boolean dropped = false;
        final Map<Integer, ItemStack> map = inv.addItem(new ItemStack(to, amount * craftReturnAmount));
        for (final ItemStack item : map.values()) {
            player.getWorld().dropItemNaturally(player.getLocation(), item);
            dropped = true;
        }
        if (dropped) {
            player.sendMessage(ColorUtils.colorcode(plugin.getConfig().getString("prefix") + "Please note, some items were dropped on the ground due to a full inv. They are on the ground, but may not render"));
        } else {
            player.sendMessage(ColorUtils.colorcode(plugin.getConfig().getString("prefix") + "You have converted " + amount + " " + replace + " into " + (amount * craftReturnAmount) + " " + to.toString().toLowerCase().replace("_", " ")));
        }
    }


    public void replaceInventoryItemFromString(Player player, String stringType, String replaceStringType) {
        if (player.getInventory().getItemInOffHand().getType().toString().contains(stringType)) {
            player.sendMessage(ColorUtils.colorcode(plugin.getConfig().getString("prefix") + "You cannot convert your offhand or helmet into" + stringType));
            return;
        } else if (player.getInventory().getHelmet() != null && player.getInventory().getHelmet().getType().toString().contains(stringType)) {
            player.sendMessage(ColorUtils.colorcode(plugin.getConfig().getString("prefix") + "You cannot convert your offhand or helmet into" + stringType));
            return;
        }

        PlayerInventory inv = player.getInventory();
        for (ItemStack item : inv.getContents()) {
            if (item != null && item.getType().toString().contains(stringType)) {
                if (item.hasItemMeta()) continue; // Don't convert custom items

                String newMaterial = item.getType().toString().replace(stringType, replaceStringType).strip();
                int amount = item.getAmount();
                inv.removeItem(new ItemStack(item.getType(), amount));
                inv.addItem(new ItemStack(Material.valueOf(newMaterial), amount));
            }
        }
        player.sendMessage(ColorUtils.colorcode(plugin.getConfig().getString("prefix") + "You have converted all " + stringType + " in your inventory to " + replaceStringType));
    }
}
