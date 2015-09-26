package com.gmail.gogobebe2.shiftkits.kit;

import com.gmail.gogobebe2.shiftstats.ShiftStats;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.sql.SQLException;
import java.util.Map;

public class Kit {
    private String name;
    private Map<Integer, ItemStack> contents;
    private Material helmet;
    private Material chestplate;
    private Material leggings;
    private Material boots;
    private Requirement requirement;
    private boolean levelRequirement;
    private KitGroup kitGroup;

    protected Kit(String name, KitGroup kitGroup, boolean levelRequirement, Requirement requirement, Map<Integer, ItemStack> contents,
               Material helmet, Material chestplate, Material leggings, Material boots) {
        this.name = name;
        this.requirement = requirement;
        this.contents = contents;
        this.helmet = helmet;
        this.chestplate = chestplate;
        this.leggings = leggings;
        this.boots = boots;
        this.kitGroup = kitGroup;
        this.levelRequirement = levelRequirement;
    }

    public boolean trySelect(Player player) {
        boolean canSelect = false;
        try {
            if (has(player)) canSelect = true;
            else if (satisfiesRequirements(player)) {
                player.sendMessage(ChatColor.GREEN + "You just unlocked the " + name + " kit with "
                        + requirement.getDescription());
                if (requirement instanceof Cost) ((Cost) requirement).takeXP(player);
                ShiftStats.getAPI().addKit(player.getUniqueId(), name);
                canSelect = true;
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            player.sendMessage(ChatColor.RED + "Error! Can't connect to SQL database to retrieve kits!");
            return false;
        }

        if (canSelect) {
            use(player);
            player.sendMessage(ChatColor.GREEN + name + " kit selected.");
            return true;
        } else {
            player.sendMessage(ChatColor.RED + "You do not satisfy the requirements to unlock this kit!");
            player.sendMessage(ChatColor.DARK_RED + "You need " + requirement.getDescription()
                    + (!levelRequirement ? ""
                    : " and level " + (kitGroup.getLevel(this) - 1))
                    + " to unlock this kit.");
            return false;
        }
    }

    private void use(Player player) {
        PlayerInventory inventory = player.getInventory();
        inventory.clear();
        inventory.setHelmet(new ItemStack(helmet, 1));
        inventory.setChestplate(new ItemStack(chestplate, 1));
        inventory.setLeggings(new ItemStack(leggings, 1));
        inventory.setBoots(new ItemStack(boots, 1));
        for (int slot : contents.keySet()) inventory.setItem(slot, contents.get(slot));
    }

    private boolean has(Player player) {
        try {
            for (String kitName : ShiftStats.getAPI().getKits(player.getUniqueId())) {
                if (kitName.equalsIgnoreCase(name)) {
                    return true;
                }
            }
            return false;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            player.sendMessage(ChatColor.RED + "Error! Can't connect to SQL database to retrieve kits!");
            return false;
        }
    }

    private boolean satisfiesRequirements(Player player) throws SQLException, ClassNotFoundException {
        return requirement.satisfies(player) && satisfiesLevelRequirement(player);
    }

    private boolean satisfiesLevelRequirement(Player player) {
        return !levelRequirement || kitGroup.getKit(kitGroup.getLevel(this) - 1).has(player);
    }
}
