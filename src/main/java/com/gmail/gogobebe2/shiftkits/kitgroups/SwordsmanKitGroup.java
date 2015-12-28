package com.gmail.gogobebe2.shiftkits.kitgroups;

import com.gmail.gogobebe2.shiftkits.requirements.Cost;
import com.gmail.gogobebe2.shiftkits.Kit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class SwordsmanKitGroup implements KitGroup {

    @Override
    public Kit getLevel1() {
        return getLevel(7500, 1);
    }

    @Override
    public Kit getLevel2() {
        return getLevel(20000, 2);
    }

    @Override
    public Kit getLevel3() {
        return getLevel(50000, 3);
    }

    @Override
    public String getName() {
        return "Swordsman";
    }

    private Kit getLevel(final int XP_REQUIRED, int level) {
        Map<Integer, ItemStack> items = new HashMap<>();

        int swordSharpnessLevel = level - 1;
        ItemStack sword = new ItemStack(Material.STONE_SWORD, 1);
        if (swordSharpnessLevel != 0) sword.addEnchantment(Enchantment.DAMAGE_ALL, 1);
        items.put(0, sword);

        items.put(1, new ItemStack(Material.STONE_PICKAXE));
        return new Kit(getName(), (short) level, new Cost(XP_REQUIRED), items, Material.STONE_PICKAXE);
    }
}
