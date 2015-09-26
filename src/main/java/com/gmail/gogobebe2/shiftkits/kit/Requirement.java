package com.gmail.gogobebe2.shiftkits.kit;

import org.bukkit.entity.Player;

import java.sql.SQLException;

public abstract class Requirement {
    // A user-friendly description of the requirement in English. Will be used like "You need " + description + "...".
    private String description;

    protected Requirement(String description) {
        this.description = description;
    }

    protected String getDescription() {
        return description;
    }

    protected abstract boolean satisfies(Player player) throws SQLException, ClassNotFoundException;
}
