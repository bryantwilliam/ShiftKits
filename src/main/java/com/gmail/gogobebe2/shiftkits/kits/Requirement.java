package com.gmail.gogobebe2.shiftkits.kits;

import org.bukkit.entity.Player;

public abstract class Requirement {
    // A user-friendly description of the requirement in English. Will be used like "You need " + description + "...".
    private String description;

    protected Requirement(String description) {
        this.description = description;
    }

    protected String getDescription() {
        return description;
    }

    protected abstract boolean satisfies(Player player);
}
