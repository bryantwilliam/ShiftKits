package com.gmail.gogobebe2.shiftkits;

import com.gmail.gogobebe2.shiftkits.requirements.Requirement;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.permissions.Permission;

import java.util.List;
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
    private List<String> lore;
    private Permission permission;

    public Kit(String id, short level, Requirement requirement, Map<Integer, ItemStack> contents, Material icon, List<String> lore) {
        this(id, level, requirement, contents, null, null, null, null, icon, lore, null);
    }

    public Kit(String id, short level, Requirement requirement, Map<Integer, ItemStack> contents, Material icon, List<String> lore, String permissionNode) {
        this(id, level, requirement, contents, null, null, null, null, icon, lore, permissionNode);
    }

    public Kit(String name, short level, Requirement requirement, Map<Integer, ItemStack> contents,
               Material helmet, Material chestplate, Material leggings, Material boots, Material icon, List<String> lore) {
        this(name, level, requirement, contents, helmet, chestplate, leggings, boots, icon, lore, null);
    }

    public Kit(String name, short level, Requirement requirement, Map<Integer, ItemStack> contents,
               Material helmet, Material chestplate, Material leggings, Material boots, Material icon, List<String> lore, String permissionNode) {
        this.id = level + "-" + name.toLowerCase();
        this.level = level;
        this.requirement = requirement;
        this.contents = contents;
        this.helmet = helmet;
        this.chestplate = chestplate;
        this.leggings = leggings;
        this.boots = boots;
        this.icon = icon;
        this.lore = lore;
        if (permissionNode != null) this.permission = new Permission(permissionNode);
    }

    protected void apply(Player player) {
        PlayerInventory inventory = player.getInventory();
        inventory.clear();

        if (helmet != null) inventory.setHelmet(new ItemStack(helmet, 1));
        if (chestplate != null) inventory.setChestplate(new ItemStack(chestplate, 1));
        if (leggings != null) inventory.setLeggings(new ItemStack(leggings, 1));
        if (boots != null) inventory.setBoots(new ItemStack(boots, 1));

        for (int slot : contents.keySet()) inventory.setItem(slot, contents.get(slot));
    }

    protected List<String> getLore() {
        return this.lore;
    }

    protected Material getIcon() {
        return this.icon;
    }

    protected Requirement getRequirement() {
        return this.requirement;
    }

    protected boolean needsPermission() {
        return this.permission != null;
    }

    protected Permission getPermission() {
        return this.permission;
    }

    protected String getId() {
        return this.id;
    }

    protected short getLevel() {
        return this.level;
    }
}
