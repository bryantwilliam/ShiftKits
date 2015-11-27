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

public class MinerKitGroup implements KitGroup {
    @Override
    public Kit getLevel1() {
        ItemStack pickaxe = new ItemStack(Material.STONE_PICKAXE, 1);
        pickaxe.addEnchantment(Enchantment.DURABILITY, 1);
        return getLevel(pickaxe, 500, 1);
    }

    @Override
    public Kit getLevel2() {
        ItemStack pickaxe = new ItemStack(Material.STONE_PICKAXE, 1);
        pickaxe.addEnchantment(Enchantment.DURABILITY, 2);
        return getLevel(pickaxe, 2000, 2);
    }

    @Override
    public Kit getLevel3() {
        return getLevel(new ItemStack(Material.IRON_PICKAXE, 1), 5000, 3);
    }

    @Override
    public String getName() {
        return "Miner";
    }

    private Kit getLevel(ItemStack pickaxe, final int ORES_MINED, int level) {
        Map<Integer, ItemStack> items = new HashMap<>();
        items.put(0, new ItemStack(Material.WOOD_SWORD, 1));
        items.put(1, pickaxe);

        Requirement requirement = new Requirement(ORES_MINED + " ores mined - Not including the AlphaCore") {
            @Override
            public boolean satisfies(Player player) throws SQLException, ClassNotFoundException {
                return ShiftStats.getAPI().getOresMined(player.getUniqueId()) >= ORES_MINED;
            }
        };

        return new Kit(getName(), level, requirement, items, Material.DIAMOND_PICKAXE);
    }
}
