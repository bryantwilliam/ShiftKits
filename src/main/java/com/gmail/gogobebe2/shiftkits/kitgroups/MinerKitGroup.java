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

public class MinerKitGroup implements KitGroup {
    @Override
    public Kit getLevel1() {
        ItemStack pickaxe = new ItemStack(Material.STONE_PICKAXE, 1);
        pickaxe.addEnchantment(Enchantment.DURABILITY, 1);
        return getLevel(pickaxe, 500, 1, "Efficiency I stone");
    }

    @Override
    public Kit getLevel2() {
        ItemStack pickaxe = new ItemStack(Material.STONE_PICKAXE, 1);
        pickaxe.addEnchantment(Enchantment.DURABILITY, 2);
        return getLevel(pickaxe, 2000, 2, "Efficiency II stone");
    }

    @Override
    public Kit getLevel3() {
        return getLevel(new ItemStack(Material.IRON_PICKAXE, 1), 5000, 3, "iron");
    }

    @Override
    public String getName() {
        return "Miner";
    }

    @Override
    public List<String> getLore() {
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Spawn with a better pick for faster mining!");
        lore.add(ChatColor.GRAY + "Upgrade to level 2 and 3 for better picks.");
        return lore;
    }

    private Kit getLevel(ItemStack pickaxe, final int ORES_MINED, int level, String description) {
        Map<Integer, ItemStack> items = new HashMap<>();
        items.put(0, new ItemStack(Material.WOOD_SWORD, 1));
        items.put(1, pickaxe);

        Requirement requirement = new Requirement(ORES_MINED + " ores mined (not including the AlphaCore)") {
            @Override
            public boolean satisfies(Player player) throws SQLException, ClassNotFoundException {
                return ShiftStats.getAPI().getOresMined(player.getUniqueId()) >= ORES_MINED;
            }
        };

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GREEN + "Start with an " + description + " pick and a wood sword.");
        lore.add(ChatColor.GREEN + "Unlock through mining " + ORES_MINED + " ores.");

        return new Kit(getName(), (short) level, requirement, items, Material.STONE_PICKAXE, lore);
    }
}
