package com.gmail.gogobebe2.shiftkits;

import com.gmail.gogobebe2.shiftkits.requirements.Requirement;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class MagicKit extends Kit implements Listener {

    public MagicKit(String name, int level, Requirement requirement, Map<Integer, ItemStack> contents, Material helmet, Material chestplate, Material leggings, Material boots, Material icon, Listener listener) {
        super(name, level, requirement, contents, helmet, chestplate, leggings, boots, icon);
        Bukkit.getPluginManager().registerEvents(listener, ShiftKits.instance);
    }

    public MagicKit(String name, int level, Requirement requirement, Map<Integer, ItemStack> contents, Material icon, Listener listener) {
        this(name, level, requirement, contents, null, null, null, null, icon, listener);
    }
}
