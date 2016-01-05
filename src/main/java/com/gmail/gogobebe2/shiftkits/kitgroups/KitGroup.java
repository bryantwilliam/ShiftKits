package com.gmail.gogobebe2.shiftkits.kitgroups;

import com.gmail.gogobebe2.shiftkits.Kit;

import java.util.List;

public interface KitGroup {
    Kit getLevel1();
    Kit getLevel2();
    Kit getLevel3();
    String getName();
    List<String> getLore();
}