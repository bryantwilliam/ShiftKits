package com.gmail.gogobebe2.shiftkits;

import com.gmail.gogobebe2.shiftkits.requirements.Requirement;
import com.gmail.gogobebe2.shiftstats.ShiftStats;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.sql.SQLException;
import java.util.Map;

public class Kit {
    private String id;
    private short level;
    private Map<Integer, ItemStack> contents;
    private Material helmet;
    private Material chestplate;
    private Material leggings;
    private Material boots;
    private Requirement requirement;
    private Material icon;

    public Kit(String id, short level, Requirement requirement, Map<Integer, ItemStack> contents, Material icon) {
        this(id, level, requirement, contents, null, null, null, null, icon);
    }

    public Kit(String name, short level, Requirement requirement, Map<Integer, ItemStack> contents,
               Material helmet, Material chestplate, Material leggings, Material boots, Material icon) {
        this.id = level + "-" + name;
        this.level = level;
        this.requirement = requirement;
        this.contents = contents;
        this.helmet = helmet;
        this.chestplate = chestplate;
        this.leggings = leggings;
        this.boots = boots;
        this.icon = icon;
    }

    private void apply(Player player) {
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
            for (String kitID : ShiftStats.getAPI().getKits(player.getUniqueId()))
                if (kitID.equalsIgnoreCase(id)) return true;
            return false;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            player.sendMessage(ChatColor.RED + "Error! Can't connect to SQL database to retrieve kits!");
            return false;
        }
    }

    protected Material getIcon() {
        return this.icon;
    }

    protected Requirement getRequirement() {
        return this.requirement;
    }

    protected String getId() {
        return this.id;
    }

    protected short getLevel() {
        return this.level;
    }
}
