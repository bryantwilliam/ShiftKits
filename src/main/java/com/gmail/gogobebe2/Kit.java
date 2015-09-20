package com.gmail.gogobebe2;

import org.bukkit.inventory.ItemStack;

import java.util.Map;

public abstract class Kit {
    //          slot     item
    private Map<Integer, ItemStack> items;

    protected Kit(Map<Integer, ItemStack> items) {
        this.items = items;
    }

    protected Map<Integer, ItemStack> getItems() {
        return this.items;
    }
}
