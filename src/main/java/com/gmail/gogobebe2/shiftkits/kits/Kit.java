package com.gmail.gogobebe2.shiftkits.kits;

import com.gmail.gogobebe2.shiftkits.requirements.Cost;
import com.gmail.gogobebe2.shiftkits.requirements.Requirement;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.Map;

public class Kit {
    private String name;
    private Map<Integer, ItemStack> contents;
    private ItemStack helmet;
    private ItemStack chestplate;
    private ItemStack leggings;
    private ItemStack boots;
    private Requirement requirement;
    private int requiredLevel;
    private KitGroup kitGroup;

    protected Kit(String name, KitGroup kitGroup, Requirement requirement, Map<Integer, ItemStack> contents,
                  ItemStack helmet, ItemStack chestplate, ItemStack leggings, ItemStack boots) {
        this.name = name;
        this.requirement = requirement;
        this.contents = contents;
        this.helmet = helmet;
        this.chestplate = chestplate;
        this.leggings = leggings;
        this.boots = boots;
        this.kitGroup = kitGroup;
        this.requiredLevel = kitGroup.getLevel(this) - 1;
    }

    protected boolean trySelect(Player player) {
        boolean canSelect = false;

        if (has(player)) canSelect = true;
        else if (canUnlock(player)) {
            player.sendMessage(ChatColor.GREEN + "You just unlocked the " + name + " kit with " + requirement.getDescription());
            if (requirement instanceof Cost) ((Cost) requirement).takeXP(player);
            // ShiftStats.getAPI().addKits(player.getUniqueId(), name);
            canSelect = true;
        }

        if (canSelect) {
            select(player);
            player.sendMessage(ChatColor.GREEN + name + " kit selected.");
            return true;
        } else {
            player.sendMessage(ChatColor.RED + "You do not satisfy the requirements to unlock this kit!");
            player.sendMessage(ChatColor.DARK_RED + "You need " + requirement.getDescription() + (requiredLevel == 0 ? "" : " and level " + requiredLevel) + " to unlock this kit.");
            return false;
        }
    }

    protected boolean canUnlock(Player player) {
        return requirement.satisfies(player) && (requiredLevel == 0 || kitGroup.getKit(requiredLevel).has(player));
    }

    protected boolean has(Player player) {
        // return ShiftStats.getAPI().getKits(Player.getUniqueId()).contains(name);
        return true;
    }

    private void select(Player player) {
        PlayerInventory inventory = player.getInventory();
        inventory.clear();
        inventory.setHelmet(helmet);
        inventory.setChestplate(chestplate);
        inventory.setLeggings(leggings);
        inventory.setBoots(boots);
        for (int slot : contents.keySet()) {
            inventory.setItem(slot, contents.get(slot));
        }
    }
}
