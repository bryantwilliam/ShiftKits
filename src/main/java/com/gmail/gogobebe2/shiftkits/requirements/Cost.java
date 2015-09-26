package com.gmail.gogobebe2.shiftkits.requirements;

import org.bukkit.entity.Player;

public class Cost extends Requirement {
    private int price;

    public Cost(String description, int price) {
        super(description);
        this.price = price;
    }

    @Override
    public boolean satisfies(Player player) {
        // TODO: hook into xp plugin to find xp.
        return false;
    }

    public void takeXP(Player player) {
        // TODO: hook into xp plugin to find xp.
    }
}
