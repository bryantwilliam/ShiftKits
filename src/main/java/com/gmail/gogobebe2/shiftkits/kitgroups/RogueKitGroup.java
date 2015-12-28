package com.gmail.gogobebe2.shiftkits.kitgroups;

import com.gmail.gogobebe2.shiftkits.Kit;
import com.gmail.gogobebe2.shiftkits.requirements.Cost;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class RogueKitGroup implements KitGroup {

    @Override
    public Kit getLevel1() {
        return getLevel(2, 1, 10000, 1);
    }

    @Override
    public Kit getLevel2() {
        return getLevel(5, 1, 25000, 2);
    }

    @Override
    public Kit getLevel3() {
        return getLevel(7, 2, 60000, 3);
    }

    @Override
    public String getName() {
        return "Rogue";
    }

    private Kit getLevel(int poisonPotionAmount, int poisonPotionLevel, int XP_REQUIRED, int level) {
        Map<Integer, ItemStack> items = new HashMap<>();

        items.put(0, new ItemStack(Material.WOOD_SWORD, 1));
        items.put(1, new ItemStack(Material.STONE_PICKAXE, 1));

        ItemStack potion = new ItemStack(Material.POTION, poisonPotionAmount);

        // http://minecraft-ids.grahamedgecombe.com/potion-calculator
        final short TIER1_SPLASH_POISON_METADATA = 16388;
        final short TIER2_SPLASH_POISON_METADATA = 16420;
        if (poisonPotionLevel == 1) potion.setDurability(TIER1_SPLASH_POISON_METADATA);
        else potion.setDurability(TIER2_SPLASH_POISON_METADATA);

        items.put(2, potion);

        return new Kit(getName(), (short) level, new Cost(XP_REQUIRED), items, Material.SPIDER_EYE);
    }
}
