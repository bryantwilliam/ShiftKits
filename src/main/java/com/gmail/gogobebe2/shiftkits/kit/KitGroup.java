package com.gmail.gogobebe2.shiftkits.kit;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class KitGroup {
    private Map<Integer, Kit> kits = new HashMap<>();
    private String name;

    public KitGroup(String name) {
        this.name = name;
    }

    public void addKit(int level, Requirement requirement, Map<Integer, ItemStack> contents,
                       Material helmet, Material chestplate, Material leggings, Material boots) throws IllegalArgumentException {
        if (level <= 0) throw new IllegalArgumentException("Can't have a level that is 0 or below!");
        kits.put(level, new Kit(name + level, this, level != 1, requirement, contents, helmet, chestplate, leggings, boots));
    }

    public void addKit(int level, Requirement requirement, Map<Integer, ItemStack> contents) throws IllegalArgumentException {
        addKit(level, requirement, contents, Material.AIR, Material.AIR, Material.AIR, Material.AIR);
    }
    protected int getLevel(Kit kit) throws NullPointerException {
        for (int level : kits.keySet()) if (kits.get(level).equals(kit)) return level;
        throw new NullPointerException("Can't find kit " + kit + "!");
    }

    protected Kit getKit(int level) throws NullPointerException {
        if (kits.containsKey(level)) return kits.get(level);
        throw new NullPointerException("Can't find a kit that is level " + level + "!");
    }
}
