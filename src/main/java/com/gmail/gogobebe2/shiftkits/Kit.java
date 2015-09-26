package com.gmail.gogobebe2.shiftkits;

import com.gmail.gogobebe2.shiftkits.requirements.Cost;
import com.gmail.gogobebe2.shiftkits.requirements.Requirement;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public abstract class Kit {
    private String name;
    private Map<Integer, ItemStack> inventory;
    private Requirement requirement;

    protected Kit(String name, Map<Integer, ItemStack> inventory, Requirement requirement) {
        this.inventory = inventory;
        this.name = name;
        this.requirement = requirement;
    }

    protected boolean tryUnlock(Player player) {
        if (canUnlock(player)) {
            player.sendMessage(ChatColor.GREEN + "You just unlocked the " + name + " kit with " + requirement.getDescription());
            if (requirement instanceof Cost) {
                ((Cost) requirement).takeXP(player);
            }
            return true;
        }
        else {
            player.sendMessage(ChatColor.RED + "You do not satisfy the requirements to unlock this kit!");
            player.sendMessage(ChatColor.DARK_RED + "You need " + requirement.getDescription() + " to unlock this kit.");
            return false;
        }
    }

    protected boolean canUnlock(Player player) {
        return requirement.has(player);
    }
}
