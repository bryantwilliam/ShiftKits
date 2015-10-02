package com.gmail.gogobebe2.shiftkits.kitgroupinstances;

import com.gmail.gogobebe2.shiftkits.kit.Cost;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class RogueKitGroup extends ThreeLeveledKitGroup {
    protected RogueKitGroup() {
        super("Rogue");
    }

    @Override
    protected void addLevel1() {
        addLevel(2, 1, 10000, 1);
    }

    @Override
    protected void addLevel2() {
        addLevel(5, 1, 25000, 2);
    }

    @Override
    protected void addLevel3() {
        addLevel(7, 2, 60000, 3);
    }

    private void addLevel(int poisonPotionAmount, int poisonPotionLevel, int XP_REQUIRED, int level) {
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

        getKitGroup().addKit(level, new Cost(XP_REQUIRED), items);
    }
}
