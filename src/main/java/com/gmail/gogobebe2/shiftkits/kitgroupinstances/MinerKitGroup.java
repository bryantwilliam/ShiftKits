package com.gmail.gogobebe2.shiftkits.kitgroupinstances;

import com.gmail.gogobebe2.shiftkits.kit.Requirement;
import com.gmail.gogobebe2.shiftstats.ShiftStats;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class MinerKitGroup extends ThreeLeveledKitGroup {
    protected MinerKitGroup() {
        super("Miner");
    }

    @Override
    protected void addLevel1() {
        ItemStack pickaxe = new ItemStack(Material.STONE_PICKAXE, 1);
        pickaxe.addEnchantment(Enchantment.DURABILITY, 1);
        addLevel(pickaxe, 500, 1);
    }

    @Override
    protected void addLevel2() {
        ItemStack pickaxe = new ItemStack(Material.STONE_PICKAXE, 1);
        pickaxe.addEnchantment(Enchantment.DURABILITY, 2);
        addLevel(pickaxe, 2000, 2);
    }

    @Override
    protected void addLevel3() {
        addLevel(new ItemStack(Material.IRON_PICKAXE, 1), 5000, 3);
    }

    private void addLevel(ItemStack pickaxe, final int ORES_MINED, int level) {
        Map<Integer, ItemStack> items = new HashMap<>();
        items.put(0, new ItemStack(Material.WOOD_SWORD));
        items.put(1, pickaxe);
        getKitGroup().addKit(level, level != 1, new Requirement("mine " + ORES_MINED + " ores - Not including the AlphaCore") {
            @Override
            protected boolean satisfies(Player player) throws SQLException, ClassNotFoundException {
                return ShiftStats.getAPI().getOresMined(player.getUniqueId()) >= ORES_MINED;
            }
        }, items);
    }
}
