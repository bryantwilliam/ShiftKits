package com.gmail.gogobebe2.shiftkits.kitgroupinstances;

import com.gmail.gogobebe2.shiftkits.kit.Cost;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class SwordsmanKitGroup extends ThreeLeveledKitGroup {
    protected SwordsmanKitGroup() {
        super("Swordsman");
    }

    @Override
    protected void addLevel1() {
        addLevel(7500, 1);
    }

    @Override
    protected void addLevel2() {
        addLevel(20000, 2);
    }

    @Override
    protected void addLevel3() {
        addLevel(50000, 3);
    }

    private void addLevel(final int XP_REQUIRED, int level) {
        Map<Integer, ItemStack> items = new HashMap<>();

        int swordSharpnessLevel = level - 1;
        ItemStack sword = new ItemStack(Material.STONE_SWORD, 1);
        if (swordSharpnessLevel != 0) sword.addEnchantment(Enchantment.DAMAGE_ALL, 1);
        items.put(0, sword);

        items.put(1, new ItemStack(Material.STONE_PICKAXE));
        getKitGroup().addKit(level, new Cost(XP_REQUIRED), items);
    }
}
