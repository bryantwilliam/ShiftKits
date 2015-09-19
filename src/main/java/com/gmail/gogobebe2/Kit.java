package com.gmail.gogobebe2;

import org.bukkit.entity.Player;

public abstract class Kit {
    protected abstract void giveLevel1Kit(Player player);
    protected abstract void giveLevel2Kit(Player player);
    protected abstract void giveLevel3Kit(Player player);

    protected abstract boolean canBuy();
}
