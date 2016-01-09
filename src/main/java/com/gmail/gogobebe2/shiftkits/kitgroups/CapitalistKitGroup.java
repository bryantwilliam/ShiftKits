package com.gmail.gogobebe2.shiftkits.kitgroups;

import com.gmail.gogobebe2.shiftkits.Kit;
import com.gmail.gogobebe2.shiftkits.requirements.Cost;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CapitalistKitGroup implements KitGroup {
    @Override
    public Kit getLevel1() {
        return getLevel(1, 5, 10000);
    }

    @Override
    public Kit getLevel2() {
        return getLevel(2, 10, 25000);
    }

    @Override
    public Kit getLevel3() {
        return getLevel(3, 15, 60000);
    }

    @Override
    public String getName() {
        return "Capitalist";
    }

    @Override
    public List<String> getLore() {
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Spawn with gold ingots already in your inventory!");
        lore.add(ChatColor.GRAY + "Upgrade to level 2 and 3 for more gold...and some iron!");
        return lore;
    }

    private Kit getLevel(int level, int gold, int xp) {
        Map<Integer, ItemStack> items = new HashMap<>();
        items.put(0, new ItemStack(Material.WOOD_PICKAXE, 1));
        items.put(1, new ItemStack(Material.WOOD_SWORD, 1));
        items.put(2, new ItemStack(Material.GOLD_INGOT, gold));
        if (level == 3) items.put(3, new ItemStack(Material.IRON_INGOT, 2));

        Cost cost = new Cost(xp);

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GREEN + "Start with a stone pick, wood sword," + (level == 3 ? "" : " and") + " " + gold
                + " gold ingots" + (level == 3 ? ", and 2 iron ingots." : "."));
        lore.add(ChatColor.GREEN + "Unlock with " + cost.getDescription() + "!");

        return new Kit(getName(), (short) level, cost, items, Material.GOLD_INGOT, lore);
    }
}
