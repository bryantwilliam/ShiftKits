package com.gmail.gogobebe2.shiftkits;

import com.gmail.gogobebe2.shiftkits.kit.KitGroup;
import com.gmail.gogobebe2.shiftkits.kit.Requirement;
import com.gmail.gogobebe2.shiftstats.ShiftStats;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class KitInstances {
    public static void createInstances() {
        KitGroup archer = new KitGroup("Archer");

        Map<Integer, ItemStack> archerLevel1Items = new HashMap<>();
        archerLevel1Items.put(0, new ItemStack(Material.BOW, 1));
        archerLevel1Items.put(1, new ItemStack(Material.STONE_PICKAXE, 1));
        archerLevel1Items.put(2, new ItemStack(Material.ARROW, 8));
        archer.addKit(1, false, new Requirement("kill 50 players with a bow") {
            @Override
            protected boolean satisfies(Player player) throws SQLException, ClassNotFoundException {
                return ShiftStats.getAPI().getKills(player.getUniqueId(), ShiftStats.KillMethod.BOW) >= 50;
            }
        }, archerLevel1Items, Material.AIR, Material.AIR, Material.AIR, Material.AIR);
    }
}
