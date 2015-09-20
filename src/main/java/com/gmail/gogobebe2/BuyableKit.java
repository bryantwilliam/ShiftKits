package com.gmail.gogobebe2;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class BuyableKit extends Kit {
    private int xpCost;

    protected BuyableKit(Map<Integer, ItemStack> items, int xpCost) {
        super(items);
        this.xpCost = xpCost;
    }

    protected void tryBuy(Player player) {
        // TODO: create ShiftStats and hook in.
    }
}
