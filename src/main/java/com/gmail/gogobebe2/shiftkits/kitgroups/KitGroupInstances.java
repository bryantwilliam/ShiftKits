package com.gmail.gogobebe2.shiftkits.kitgroups;

import java.util.ArrayList;
import java.util.List;

public final class KitGroupInstances {
    private static List<KitGroup> kitGroups = null;

    public static List<KitGroup> getInstances() {
        if (kitGroups == null) {
            kitGroups = new ArrayList<>();
            kitGroups.add(new SwordsmanKitGroup());
            kitGroups.add(new ArcherKitGroup());
            kitGroups.add(new MinerKitGroup());
            kitGroups.add(new RogueKitGroup());
            kitGroups.add(new NightcrawlerKitGroup());
            kitGroups.add(new CapitalistKitGroup());
            // Commented out kits are commented because they have bugs in them and are being temporally removed from the kit selector.
/*            kitGroups.add(new AngelKitGroup());*/
            kitGroups.add(new BerserkerKitGroup());
/*            kitGroups.add(new DemomanKitGroup());*/
        }
        return kitGroups;
    }

    public static KitGroup getKitGroupInstance(String name) {
        for (KitGroup kitGroup : getInstances()) if (kitGroup.getName().equals(name)) return kitGroup;
        return null;
    }
}
