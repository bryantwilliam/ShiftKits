package com.gmail.gogobebe2.shiftkits.kits;

import java.util.HashMap;
import java.util.Map;

public class KitGroup {
    private Map<Integer, Kit> kits = new HashMap<>();

    protected void addKit(int level, Kit kit) throws IllegalArgumentException {
        if (level <= 0) throw new IllegalArgumentException("Can't have a level that is 0 or below!");
        kits.put(level, kit);
    }

    protected int getLevel(Kit kit) throws NullPointerException {
        for (int level : kits.keySet()) if (kits.get(level).equals(kit)) return level;
        throw new NullPointerException("Can't find kit " + kit + "!");
    }

    protected Kit getKit(int level) throws NullPointerException {
        if (kits.containsKey(level)) return kits.get(level);
        throw new NullPointerException("Can't find a kit that is level " + level + "!");
    }
}
