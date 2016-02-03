package com.gmail.gogobebe2.shiftkits;

import com.gmail.gogobebe2.shiftkits.requirements.Requirement;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;

public class MagicKit extends Kit {
    public MagicKit(String name, short level, Requirement requirement, Map<Integer, ItemStack> contents, Material icon, List<String> lore, Listener listener) {
        this(name, level, requirement, contents, null, null, null, null, icon, lore, null, listener);
    }

    public MagicKit(String id, short level, Requirement requirement, Map<Integer, ItemStack> contents, Material icon, List<String> lore, String permissionNode, Listener listener) {
        this(id, level, requirement, contents, null, null, null, null, icon, lore, permissionNode, listener);
    }

    public MagicKit(String name, short level, Requirement requirement, Map<Integer, ItemStack> contents, Material helmet, Material chestplate, Material leggings, Material boots, Material icon, List<String> lore, String permissionNode, Listener listener) {
        super(name, level, requirement, contents, helmet, chestplate, leggings, boots, icon, lore, permissionNode);
        Bukkit.getPluginManager().registerEvents(listener, ShiftKits.instance);
    }
}
