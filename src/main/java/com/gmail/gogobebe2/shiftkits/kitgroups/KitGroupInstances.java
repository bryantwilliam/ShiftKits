package com.gmail.gogobebe2.shiftkits.kitgroups;

import java.util.ArrayList;
import java.util.List;

public final class KitGroupInstances {

    public static List<KitGroup> getInstances() {
        List<KitGroup> kitGroups = new ArrayList<>();

        kitGroups.add(new ArcherKitGroup());
        kitGroups.add(new MinerKitGroup());
        kitGroups.add(new SwordsmanKitGroup());
        kitGroups.add(new RogueKitGroup());

        return kitGroups;
    }
}
