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

public class RogueKitGroup implements KitGroup {

    // http://minecraft-ids.grahamedgecombe.com/potion-calculator
    public final static short TIER1_SPLASH_POISON_METADATA = 16388;
    public final static short TIER2_SPLASH_POISON_METADATA = 16420;

    @Override
    public Kit getLevel1() {
        return getLevel(2, 1, 10000, 1);
    }

    @Override
    public Kit getLevel2() {
        return getLevel(4, 1, 25000, 2);
    }

    @Override
    public Kit getLevel3() {
        return getLevel(7, 2, 60000, 3);
    }

    @Override
    public String getName() {
        return "Rogue";
    }

    @Override
    public List<String> getLore() {
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Spawn with splash poison potions!");
        lore.add(ChatColor.GRAY + "Upgrade to level 2 and 3 for more, stronger potions.");
        return lore;
    }

    private Kit getLevel(int poisonPotionAmount, int poisonPotionLevel, int XP_REQUIRED, int level) {
        Map<Integer, ItemStack> items = new HashMap<>();

        items.put(0, new ItemStack(Material.WOOD_SWORD, 1));
        items.put(1, new ItemStack(Material.STONE_PICKAXE, 1));

        ItemStack potion = new ItemStack(Material.POTION, poisonPotionAmount);

        if (poisonPotionLevel == 1) potion.setDurability(TIER1_SPLASH_POISON_METADATA);
        else potion.setDurability(TIER2_SPLASH_POISON_METADATA);

        items.put(2, potion);

        Cost cost = new Cost(XP_REQUIRED);

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GREEN + "Start with a stone pick and " + poisonPotionAmount + " Splash 0:33 Poison potions.");
        lore.add(ChatColor.GREEN + "Unlock with " + cost.getDescription() + "!");

        return new Kit(getName(), (short) level, cost, items, Material.POTION, lore);
    }
}
