package com.gmail.gogobebe2.shiftkits;

import com.gmail.gogobebe2.shiftkits.requirements.Cost;
import com.gmail.gogobebe2.shiftkits.requirements.Requirement;
import com.gmail.gogobebe2.shiftstats.ShiftStats;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Kit {
    private String name;
    private int level;
    private Map<Integer, ItemStack> contents;
    private Material helmet;
    private Material chestplate;
    private Material leggings;
    private Material boots;
    private Requirement requirement;
    private ItemStack icon;

    public Kit(String name, int level, Requirement requirement, Map<Integer, ItemStack> contents, Material icon) {
        this(name, level, requirement, contents, null, null, null, null, icon);
    }

    public Kit(String name, int level, Requirement requirement, Map<Integer, ItemStack> contents,
               Material helmet, Material chestplate, Material leggings, Material boots, Material icon) {
        this.level = level;
        this.requirement = requirement;
        this.contents = contents;
        this.helmet = helmet;
        this.chestplate = chestplate;
        this.leggings = leggings;
        this.boots = boots;
        this.icon = new ItemStack(icon, 1);
        ItemMeta meta = this.icon.getItemMeta();
        meta.setDisplayName(ChatColor.AQUA + "" + ChatColor.BOLD + name + ChatColor.BLUE + " - Level " + level);
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.RED + "Requirement:");
        lore.add(ChatColor.DARK_RED + requirement.getDescription());

        // This needs to be below the icon creation:
        this.name = level + "-" + name;
    }

    public boolean tryClick(Player player) {
        boolean canSelect;
        try {
            if (has(player)) canSelect = true;
            canSelect = tryBuy(player);

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            player.sendMessage(ChatColor.RED + "Error! Can't retrieve kits from SQL database!");
            return false;
        }

        if (canSelect) {
            use(player);
            player.sendMessage(ChatColor.GREEN + name + " kit selected.");
            return true;
        } else {
            player.sendMessage(ChatColor.RED + "You do not satisfy the requirements to unlock this kit!");
            player.sendMessage(ChatColor.DARK_RED + "You need to have " + requirement.getDescription()
                    + (level != 3 ? ""
                    : " and level " + (level - 1))
                    + " to unlock this kit.");
            return false;
        }
    }

    private boolean tryBuy(Player player) throws SQLException, ClassNotFoundException {
        if (requirement.satisfies(player)) {
            player.sendMessage(ChatColor.GREEN + "You just unlocked the " + name + " kit with "
                    + requirement.getDescription());
            if (requirement instanceof Cost) ((Cost) requirement).takeXP(player);
            ShiftStats.getAPI().addKit(player.getUniqueId(), name);
            return true;
        }
        return false;
    }

    private void use(Player player) {
        PlayerInventory inventory = player.getInventory();
        inventory.clear();
        inventory.setHelmet(new ItemStack(helmet, 1));
        inventory.setChestplate(new ItemStack(chestplate, 1));
        inventory.setLeggings(new ItemStack(leggings, 1));
        inventory.setBoots(new ItemStack(boots, 1));
        for (int slot : contents.keySet()) inventory.setItem(slot, contents.get(slot));
    }

    private boolean has(Player player) {
        try {
            for (String kitName : ShiftStats.getAPI().getKits(player.getUniqueId())) {
                if (kitName.equalsIgnoreCase(name)) {
                    return true;
                }
            }
            return false;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            player.sendMessage(ChatColor.RED + "Error! Can't connect to SQL database to retrieve kits!");
            return false;
        }
    }
}
