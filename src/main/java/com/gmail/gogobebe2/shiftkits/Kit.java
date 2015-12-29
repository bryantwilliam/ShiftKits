package com.gmail.gogobebe2.shiftkits;

import com.gmail.gogobebe2.shiftkits.requirements.Requirement;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

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

    protected void apply(Player player) {
        PlayerInventory inventory = player.getInventory();
        inventory.clear();
        inventory.setHelmet(new ItemStack(helmet, 1));
        inventory.setChestplate(new ItemStack(chestplate, 1));
        inventory.setLeggings(new ItemStack(leggings, 1));
        inventory.setBoots(new ItemStack(boots, 1));
        for (int slot : contents.keySet()) inventory.setItem(slot, contents.get(slot));
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
