package com.gmail.gogobebe2.shiftkits;

import com.gmail.gogobebe2.shiftkits.kitgroups.KitGroup;

public class KitButton {
    private KitGroup kitGroup;
    private int level;
    private Kit kit = initKit();

    /*public boolean tryClick(Player player) {
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
            apply(player);
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
    }*/

    private Kit initKit() {
        if (level == 1) return kitGroup.getLevel1();
        else if (level == 2) return kitGroup.getLevel2();
        else return kitGroup.getLevel3();
    }
}
