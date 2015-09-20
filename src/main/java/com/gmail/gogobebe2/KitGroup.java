package com.gmail.gogobebe2;

public class KitGroup {
    final private String NAME;
    final private Kit[] KITS;
    final private int MAX_LEVEL;
    final private String PERMISSION_BASE;

    protected KitGroup(String name, Kit[] kits, String permissionBase) {
        this.NAME = name;
        this.KITS = kits;
        this.MAX_LEVEL = KITS.length;
        this.PERMISSION_BASE = permissionBase;
    }

    protected String getName() {
        return this.NAME;
    }

    protected Kit getKit(int lvl) throws NullPointerException {
        lvl--; checkLevel(lvl); return KITS[lvl];
    }

    protected int getLvl(Kit kit) throws NullPointerException {
        for (int lvl = 0; lvl < getMaxLevel(); lvl++) if (KITS[lvl].equals(kit)) return ++lvl;
        throw new NullPointerException("There's no " + kit.toString() + " kit in the " + NAME + " @KitGroup");
    }

    protected int getMaxLevel() {
        return this.MAX_LEVEL;
    }

    protected String getPermission(int lvl) throws NullPointerException {
        checkLevel(lvl); return PERMISSION_BASE + "." + lvl;
    }

    private void checkLevel(int lvl) throws NullPointerException {
        if (lvl >= getMaxLevel() || lvl < 0) throw new NullPointerException("There's no lvl " + lvl + " @Kit in the " + NAME + " @KitGroup.");
    }
}
