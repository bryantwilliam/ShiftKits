package com.gmail.gogobebe2.shiftkits.kit;

import com.gmail.gogobebe2.shiftstats.ShiftStats;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.sql.SQLException;
import java.util.Map;

public class Kit {
    private String name;
    private Map<Integer, ItemStack> contents;
    private ItemStack helmet;
    private ItemStack chestplate;
    private ItemStack leggings;
    private ItemStack boots;
    private Requirement requirement;
    private boolean levelRequirement;
    private KitGroup kitGroup;

    protected Kit(String name, KitGroup kitGroup, boolean levelRequirement, Requirement requirement, Map<Integer, ItemStack> contents,
                  ItemStack helmet, ItemStack chestplate, ItemStack leggings, ItemStack boots) {
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

    // TODO: remove warning suppression once ShiftStats has been sorted out.
    @SuppressWarnings("ConstantConditions")
    public boolean trySelect(Player player) {
        boolean canSelect = false;

        if (has(player)) canSelect = true;
        else if (satisfiesRequirements(player)) {
            player.sendMessage(ChatColor.GREEN + "You just unlocked the " + name + " kit with "
                    + requirement.getDescription());
            if (requirement instanceof Cost) ((Cost) requirement).takeXP(player);
            try {
                ShiftStats.getAPI().addKit(player.getUniqueId(), name);
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
                player.sendMessage(ChatColor.RED + "Error! Can't connect to SQL database to retrieve kits!");
                return false;
            }
            canSelect = true;
        }

        if (canSelect) {
            select(player);
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

    private void select(Player player) {
        PlayerInventory inventory = player.getInventory();
        inventory.clear();
        inventory.setHelmet(helmet);
        inventory.setChestplate(chestplate);
        inventory.setLeggings(leggings);
        inventory.setBoots(boots);
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

    private boolean satisfiesRequirements(Player player) {
        return requirement.satisfies(player) && satisfiesLevelRequirement(player);
    }

    private boolean satisfiesLevelRequirement(Player player) {
        return !levelRequirement || kitGroup.getKit(kitGroup.getLevel(this) - 1).has(player);
    }
}
