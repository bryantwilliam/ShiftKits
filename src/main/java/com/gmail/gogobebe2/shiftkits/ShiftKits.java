package com.gmail.gogobebe2.shiftkits;

import com.gmail.gogobebe2.shiftkits.kitgroups.KitGroupInstances;
import org.bukkit.plugin.java.JavaPlugin;

public class ShiftKits extends JavaPlugin {
    protected static ShiftKits instance;

    @Override
    public void onEnable() {
        instance = this;
        getLogger().info("Starting up " + this.getName() + ". If you need me to update this plugin, email at gogobebe2@gmail.com");
        KitGroupInstances.getInstances();
    }

    @Override
    public void onDisable() {
        getLogger().info("Disabling " + this.getName() + ". If you need me to update this plugin, email at gogobebe2@gmail.com");
    }
}
