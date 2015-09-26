package com.gmail.gogobebe2.shiftkits.kits;

import org.bukkit.entity.Player;

public class Cost extends Requirement {
    private int price;

    protected Cost(String description, int price) {
        super(description);
        this.price = price;
    }

    @Override
    protected boolean satisfies(Player player) {
        // TODO: hook into xp plugin to find xp.
        return false;
    }

    protected void takeXP(Player player) {
        // TODO: hook into xp plugin to find xp.
    }
}
