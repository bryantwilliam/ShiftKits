package com.gmail.gogobebe2.shiftkits.requirements;

import org.bukkit.entity.Player;

import java.sql.SQLException;

public abstract class Requirement {
    // A user-friendly description of the requirements in English. Will be used like "You need to have " + description + "...".
    private String description;

    protected Requirement(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public abstract boolean satisfies(Player player) throws SQLException, ClassNotFoundException;
}
