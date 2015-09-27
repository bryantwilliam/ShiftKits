package com.gmail.gogobebe2.shiftkits.kitgroupinstances;

import com.gmail.gogobebe2.shiftkits.kit.KitGroup;

public abstract class ThreeLeveledKitGroup {
    private KitGroup kitGroup;

    protected ThreeLeveledKitGroup(String name) {
        addLevel1();
        addLevel2();
        addLevel3();
        kitGroup = new KitGroup(name);
    }
    protected KitGroup getKitGroup() {
        return kitGroup;
    }
    protected abstract void addLevel1();
    protected abstract void addLevel2();
    protected abstract void addLevel3();
}
