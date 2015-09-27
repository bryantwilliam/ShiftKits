package com.gmail.gogobebe2.shiftkits;

import com.gmail.gogobebe2.shiftkits.kit.KitGroup;
import com.gmail.gogobebe2.shiftkits.kit.Requirement;
import com.gmail.gogobebe2.shiftstats.ShiftStats;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class KitGroupInstances {
    public static void createInstances() {
        ArcherKitGroup.addAll();
    }

    private static class ArcherKitGroup {
        private static KitGroup archer = new KitGroup("Archer");

        private static void addAll() {
            addLevel1();
            addLevel2();
            addLevel3();
        }

        private static void addLevel1() {
            Map<Integer, ItemStack> items = new HashMap<>();
            items.put(0, new ItemStack(Material.BOW, 1));
            items.put(1, new ItemStack(Material.STONE_PICKAXE, 1));
            items.put(2, new ItemStack(Material.ARROW, 8));
            addLevel(items, 50, 1);
        }

        private static void addLevel2() {
            Map<Integer, ItemStack> items = new HashMap<>();
            items.put(0, new ItemStack(Material.BOW, 1));
            items.put(1, new ItemStack(Material.STONE_PICKAXE, 1));
            items.put(2, new ItemStack(Material.ARROW, 16));
            addLevel(items, 200, 2);
        }

        private static void addLevel3() {
            Map<Integer, ItemStack> items = new HashMap<>();
            ItemStack bow = new ItemStack(Material.BOW, 1);
            bow.addEnchantment(Enchantment.ARROW_DAMAGE, 1);
            items.put(0, bow);
            items.put(1, new ItemStack(Material.STONE_PICKAXE, 1));
            items.put(2, new ItemStack(Material.ARROW, 24));
            addLevel(items, 1000, 3);
        }

        private static void addLevel(Map<Integer, ItemStack> items, final int KILLS_WITH_BOW, int level) {
            archer.addKit(level, false, new Requirement("kill " + KILLS_WITH_BOW + " players with a bow") {
                @Override
                protected boolean satisfies(Player player) throws SQLException, ClassNotFoundException {
                    return ShiftStats.getAPI().getKills(player.getUniqueId(), ShiftStats.KillMethod.BOW) >= KILLS_WITH_BOW;
                }
            }, items, Material.AIR, Material.AIR, Material.AIR, Material.AIR);
        }
    }
}
