package com.gmail.gogobebe2.shiftkits.kitgroups;

import com.gmail.gogobebe2.shiftkits.Kit;
import com.gmail.gogobebe2.shiftkits.MagicKit;
import com.gmail.gogobebe2.shiftkits.requirements.Cost;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DemomanKitGroup implements KitGroup {
    @Override
    public Kit getLevel1() {
        return null;
    }

    @Override
    public Kit getLevel2() {
        return null;
    }

    @Override
    public Kit getLevel3() {
        return null;
    }

    @Override
    public String getName() {
        return "Demoman";
    }

    private Kit getLevel(int fireworksAmount, int cost, int level) {
        Map<Integer, ItemStack> items = new HashMap<>();
        items.put(0, new ItemStack(Material.WOOD_PICKAXE, 1));

        final String MIRV_DISPLAYNAME = ChatColor.BLACK + "MIRV";
        final String ROCKET_LAUNCHER_DISPLAYNAME = ChatColor.DARK_BLUE + "" + ChatColor.ITALIC + "Rocket Launcher";

        ItemStack firework = new ItemStack(Material.FIREWORK, fireworksAmount);
        ItemMeta fireworkMeta = firework.getItemMeta();
        fireworkMeta.setDisplayName(ROCKET_LAUNCHER_DISPLAYNAME);
        List<String> fireworkLore = new ArrayList<>();
        fireworkLore.add(ChatColor.GREEN + "Shoots a rocket that does equal damage to tnt");
        fireworkMeta.setLore(fireworkLore);
        firework.setItemMeta(fireworkMeta);
        items.put(1, firework);

        if (level == 3) {
            ItemStack MIRV = new ItemStack(Material.FIREWORK, 1);
            ItemMeta MIRVmeta = MIRV.getItemMeta();
            MIRVmeta.setDisplayName(MIRV_DISPLAYNAME);
            List<String> MIRVlore = new ArrayList<>();
            MIRVlore.add(ChatColor.GOLD + "Similar to Rocket Launcher...");
            MIRVlore.add(ChatColor.GOLD + "except creates a cluster of rockets ");
            MIRVlore.add(ChatColor.GOLD + "in sub-cardinal directions on impact.");
            MIRVmeta.setLore(MIRVlore);
            MIRV.setItemMeta(MIRVmeta);
            items.put(2, MIRV);
        }

        return new MagicKit(getName(), level, new Cost(cost), items, Material.FIREWORK, new Listener() {
            @EventHandler
            private void onPlayerInteract(PlayerInteractEvent event) {
                ItemStack item = event.getItem();
                if ((event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) && item.getType() == Material.FIREWORK) {
                    String itemDisplayName = item.getItemMeta().getDisplayName();
                    RocketGunType rocketGunType = null;
                    if (itemDisplayName.equals(ROCKET_LAUNCHER_DISPLAYNAME))
                        rocketGunType = RocketGunType.ROCKET_LAUNCHER;
                    else if (itemDisplayName.equals(MIRV_DISPLAYNAME)) rocketGunType = RocketGunType.MIRV;

                    if (rocketGunType == RocketGunType.MIRV || rocketGunType == RocketGunType.ROCKET_LAUNCHER) {
                        fire(rocketGunType);
                        event.setCancelled(true);
                    }
                }
            }

        });
    }

    private void fire(RocketGunType rocketGunType) {
        // TODO: do shooting rocket effects and create explosion.
        // TODO: create 1 explosion on impact.
        if (rocketGunType == RocketGunType.MIRV) return; // TODO: do MIRV cluster logic shit.


    }

    private enum RocketGunType {
        MIRV, ROCKET_LAUNCHER
    }
}
