package com.gmail.gogobebe2.shiftkits.kitgroups;

import com.gmail.gogobebe2.shiftkits.requirements.Cost;
import com.gmail.gogobebe2.shiftkits.Kit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SwordsmanKitGroup implements KitGroup {

    @Override
    public Kit getLevel1() {
        return getLevel(7500, 1, "Sharp II stone");
    }

    @Override
    public Kit getLevel2() {
        return getLevel(20000, 2, "stone");
    }

    @Override
    public Kit getLevel3() {
        return getLevel(50000, 3, "wood");
    }

    @Override
    public String getName() {
        return "Swordsman";
    }

    @Override
    public List<String> getLore() {
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Default kit. Slice enemies with your trusty sword!");
        lore.add(ChatColor.GRAY + "Upgrade to level 2 and 3 for better swords.");
        return lore;
    }

    private Kit getLevel(final int XP_REQUIRED, int level, String description) {
        Map<Integer, ItemStack> items = new HashMap<>();

        int swordSharpnessLevel = level - 1;
        ItemStack sword = new ItemStack(Material.STONE_SWORD, 1);
        if (swordSharpnessLevel != 0) sword.addEnchantment(Enchantment.DAMAGE_ALL, 1);
        items.put(0, sword);

        Cost cost = new Cost(XP_REQUIRED);

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GREEN + "Start with a stone pick and a " + description + " sword.");
        lore.add(ChatColor.GREEN + "Upgrade with " + cost.getDescription() + "!");

        items.put(1, new ItemStack(Material.STONE_PICKAXE));
        return new Kit(getName(), (short) level, cost, items, Material.STONE_SWORD, lore);
    }
}
