package com.gmail.gogobebe2.shiftkits.kitgroups;

import com.gmail.gogobebe2.shiftkits.Kit;
import com.gmail.gogobebe2.shiftkits.requirements.Requirement;
import com.gmail.gogobebe2.shiftstats.ShiftStats;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArcherKitGroup implements KitGroup {

    @Override
    public Kit getLevel1() {
        Map<Integer, ItemStack> items = new HashMap<>();
        items.put(0, new ItemStack(Material.BOW, 1));
        items.put(1, new ItemStack(Material.STONE_PICKAXE, 1));
        items.put(2, new ItemStack(Material.ARROW, 8));
        return getLevel(items, 50, 1, "bow and 8 arrows");
    }

    @Override
    public Kit getLevel2() {
        Map<Integer, ItemStack> items = new HashMap<>();
        items.put(0, new ItemStack(Material.BOW, 1));
        items.put(1, new ItemStack(Material.STONE_PICKAXE, 1));
        items.put(2, new ItemStack(Material.ARROW, 16));
        return getLevel(items, 200, 2, "bow and 16 arrows");
    }

    @Override
    public Kit getLevel3() {
        Map<Integer, ItemStack> items = new HashMap<>();
        ItemStack bow = new ItemStack(Material.BOW, 1);
        bow.addEnchantment(Enchantment.ARROW_DAMAGE, 1);
        items.put(0, bow);
        items.put(1, new ItemStack(Material.STONE_PICKAXE, 1));
        items.put(2, new ItemStack(Material.ARROW, 24));
        return getLevel(items, 1000, 3, "Power I bow and 24 arrows");
    }

    @Override
    public String getName() {
        return "Archer";
    }

    @Override
    public List<String> getLore() {
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Spawn with a bow and arrows to hit enemies from afar!");
        lore.add(ChatColor.GRAY + "Level 2 & 3 upgrades give more arrows and better bows.");
        return lore;
    }

    private Kit getLevel(Map<Integer, ItemStack> items, final int KILLS_WITH_BOW, int level, String description) {
        Requirement requirement = new Requirement(KILLS_WITH_BOW + " kills with a bow") {
            @Override
            public boolean satisfies(Player player) throws SQLException, ClassNotFoundException {
                return ShiftStats.getAPI().getKills(player.getUniqueId(), ShiftStats.KillMethod.BOW) >= KILLS_WITH_BOW;
            }
        };

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GREEN + "Start with a " + description);
        lore.add(ChatColor.GREEN + "Unlock through " + requirement.getDescription());

        return new Kit(getName(), (short) level, requirement, items, Material.BOW, lore);
    }
}