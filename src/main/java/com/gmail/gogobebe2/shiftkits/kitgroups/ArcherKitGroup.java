package com.gmail.gogobebe2.shiftkits.kitgroups;

import com.gmail.gogobebe2.shiftkits.Kit;
import com.gmail.gogobebe2.shiftkits.requirements.Requirement;
import com.gmail.gogobebe2.shiftstats.ShiftStats;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class ArcherKitGroup implements KitGroup {

    @Override
    public Kit getLevel1() {
        Map<Integer, ItemStack> items = new HashMap<>();
        items.put(0, new ItemStack(Material.BOW, 1));
        items.put(1, new ItemStack(Material.STONE_PICKAXE, 1));
        items.put(2, new ItemStack(Material.ARROW, 8));
        return getLevel(items, 50, 1);
    }

    @Override
    public Kit getLevel2() {
        Map<Integer, ItemStack> items = new HashMap<>();
        items.put(0, new ItemStack(Material.BOW, 1));
        items.put(1, new ItemStack(Material.STONE_PICKAXE, 1));
        items.put(2, new ItemStack(Material.ARROW, 16));
        return getLevel(items, 200, 2);
    }

    @Override
    public Kit getLevel3() {
        Map<Integer, ItemStack> items = new HashMap<>();
        ItemStack bow = new ItemStack(Material.BOW, 1);
        bow.addEnchantment(Enchantment.ARROW_DAMAGE, 1);
        items.put(0, bow);
        items.put(1, new ItemStack(Material.STONE_PICKAXE, 1));
        items.put(2, new ItemStack(Material.ARROW, 24));
        return getLevel(items, 1000, 3);
    }

    @Override
    public String getName() {
        return "Archer";
    }

    private Kit getLevel(Map<Integer, ItemStack> items, final int KILLS_WITH_BOW, int level) {
        Requirement requirement = new Requirement(KILLS_WITH_BOW + " kills with a bow") {
            @Override
            public boolean satisfies(Player player) throws SQLException, ClassNotFoundException {
                return ShiftStats.getAPI().getKills(player.getUniqueId(), ShiftStats.KillMethod.BOW) >= KILLS_WITH_BOW;
            }
        };

        return new Kit(getName(), level, requirement, items, Material.BOW);
    }
}