package com.gmail.gogobebe2.shiftkits.requirements;

import org.bukkit.entity.Player;

public abstract class Requirement {
    // A user-friendly description of the requirement in English. Will be used like "You need " + description + "...".
    private String description;

    public Requirement(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public abstract boolean has(Player player);
}
