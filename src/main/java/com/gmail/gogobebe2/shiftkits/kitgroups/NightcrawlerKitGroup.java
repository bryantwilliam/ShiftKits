package com.gmail.gogobebe2.shiftkits.kitgroups;

import com.gmail.gogobebe2.shiftkits.Kit;
import com.gmail.gogobebe2.shiftkits.MagicKit;
import com.gmail.gogobebe2.shiftkits.requirements.Cost;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class NightcrawlerKitGroup implements KitGroup {
    private final ItemStack GHAST_TEAR = initGhastTear();

    private ItemStack initGhastTear() {
        ItemStack ghastTear = new ItemStack(Material.GHAST_TEAR, 1);
        return ghastTear;
    }

    @Override
    public Kit getLevel1() {
        Map<Integer, ItemStack> items = new HashMap<>();
        items.put(0, new ItemStack(GHAST_TEAR.clone()));
        return getLevel(1, items, 10000);
    }

    @Override
    public Kit getLevel2() {
        Map<Integer, ItemStack> items = new HashMap<>();
        ItemStack ghastTear = GHAST_TEAR.clone();
        ghastTear.setAmount(2);
        items.put(0, ghastTear);
        return getLevel(2, items, 25000);
    }

    @Override
    public Kit getLevel3() {
        Map<Integer, ItemStack> items = new HashMap<>();
        ItemStack ghastTear = GHAST_TEAR.clone();
        ghastTear.setAmount(2);
        items.put(0, ghastTear);
        items.put(1, new ItemStack(Material.ENDER_PEARL, 2));
        return getLevel(3, items, 60000);
    }

    @Override
    public String getName() {
        return "Nightcrawler";
    }

    private MagicKit getLevel(int level, Map<Integer, ItemStack> items, int cost) {
        int maxSlot = Collections.max(items.keySet());
        items.put(maxSlot + 1, new ItemStack(Material.STONE_PICKAXE, 1));
        items.put(maxSlot + 2, new ItemStack(Material.WOOD_SWORD, 1));

        return new MagicKit(getName(), level, new Cost(cost), items, Material.EYE_OF_ENDER, new Listener() {
            private Map<UUID, Location> lastLandOn = new HashMap<>();

            @EventHandler
            private void onPlayerInteract(PlayerInteractEvent event) {
                ItemStack item = event.getItem();
                if ((event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR
                        || event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK)
                        && item.getType() == Material.GHAST_TEAR) {
                    Player player = event.getPlayer();
                    UUID uuid = player.getUniqueId();
                    if (lastLandOn.containsKey(uuid)) {
                        player.setGameMode(GameMode.CREATIVE);
                        player.teleport(lastLandOn.get(uuid));
                        player.setGameMode(GameMode.SURVIVAL);
                        item.setAmount(item.getAmount() - 1);
                        player.sendMessage(ChatColor.BLUE + "" + ChatColor.BOLD + "You manage to get back on land...");
                    }
                    else {
                        player.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "You have never been on land before.");
                    }
                    event.setCancelled(true);
                }
            }

            @EventHandler
            private void onPlayerMove(PlayerMoveEvent event) {
                Location from = event.getFrom();
                if (from.getWorld().getBlockAt(from.getBlockX(), from.getBlockY() - 1, from.getBlockZ()).getType()
                        != Material.AIR) {
                    lastLandOn.put(event.getPlayer().getUniqueId(), from);
                }
            }
        });
    }
}
