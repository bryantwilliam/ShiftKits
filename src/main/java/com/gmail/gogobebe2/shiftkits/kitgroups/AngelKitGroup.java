package com.gmail.gogobebe2.shiftkits.kitgroups;

import com.gmail.gogobebe2.shiftkits.Kit;
import com.gmail.gogobebe2.shiftkits.MagicKit;
import com.gmail.gogobebe2.shiftkits.requirements.Cost;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AngelKitGroup implements KitGroup {
    @Override
    public Kit getLevel1() {
        return getLevel(1, 1, 10000);
    }

    @Override
    public Kit getLevel2() {
        return getLevel(2, 2, 25000);
    }

    @Override
    public Kit getLevel3() {
        return getLevel(3, 2, 60000);
    }

    @Override
    public String getName() {
        return "Angel";
    }

    @Override
    public List<String> getLore() {
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.RED + "" + ChatColor.BOLD + "PREMIUM ONLY!");
        lore.add(ChatColor.GRAY + "Spawn with a heavenly beacon.");
        lore.add(ChatColor.GRAY + "Place the beacon anywhere and stand nearby for Regen I!");
        lore.add(ChatColor.GRAY + "Upgrade to level 2 and 3 for more beacons and medicinal flowers!");
        return lore;
    }

    private Kit getLevel(int level, int beacons, int cost) {
        Map<Integer, ItemStack> items = new HashMap<>();
        items.put(0, new ItemStack(Material.WOOD_PICKAXE, 1));
        items.put(1, new ItemStack(Material.WOOD_SWORD, 1));
        items.put(2, new ItemStack(Material.BEACON, beacons));

        List<String> lore = new ArrayList<>(2);

        Cost requirement = new Cost(cost);

        lore.add(ChatColor.GREEN + "Start with a wood pick, ");
        lore.add(ChatColor.GREEN + "wood sword" + (level == 3 ? " and" : ","));
        lore.add(ChatColor.GREEN + "" + beacons + " heavenly beacons" + (level == 3 ? "," : "."));
        lore.add(ChatColor.GREEN + "and 3 medicinal flowers.");
        lore.add(ChatColor.GOLD + "Purchase rank from buy.xpcraft.com");
        lore.add(ChatColor.GOLD + "Then unlock with " + requirement.getDescription() + "!");

        final String MEDICINAL_FLOWER_DISPLAYNAME = ChatColor.AQUA + "" + ChatColor.ITALIC + "Medicinal Flower";

        if (level == 3) {
            ItemStack azureBluet = new ItemStack(Material.RED_ROSE, 3);
            azureBluet.setDurability((short) 3);
            // 3 is Azure Bluet.
            ItemMeta meta = azureBluet.getItemMeta();
            meta.setDisplayName(MEDICINAL_FLOWER_DISPLAYNAME);

            items.put(3, azureBluet);
        }

        return new MagicKit(getName(), (short) level, requirement, items, Material.BEACON, lore, "shiftkits." + getName().toLowerCase(), new Listener() {
            @EventHandler
            private void onBlockPlace(BlockPlaceEvent event) {
                Block block = event.getBlockPlaced();
                if (block.getType() == Material.BEACON) {

                    final int BEACON_Y = 5;
                    block.getWorld().getBlockAt(block.getX(), BEACON_Y, block.getZ()).setType(Material.BEACON);

                    for (int x = block.getX() - 1; x < block.getX() + 1; x++) {
                        for (int z = block.getZ() - 1; z < block.getZ() + 1; z++) {
                            block.getWorld().getBlockAt(x, BEACON_Y - 1, z).setType(Material.EMERALD_BLOCK);
                        }
                    }

                    for (int y = block.getY(); y > BEACON_Y; y--) {
                        Block b = block.getWorld().getBlockAt(block.getX(), y, block.getZ());
                        if (b.getType() != Material.AIR) b.setType(Material.STAINED_GLASS);
                    }
                    ItemStack item = event.getItemInHand();
                    item.setAmount(item.getAmount() - 1);
                    event.getPlayer().sendMessage(ChatColor.GOLD + "You call upon the heavens to heal you...");
                    event.setCancelled(true);
                }
            }

            @EventHandler
            private void onPlayerInteract(PlayerInteractEvent event) {
                if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) {
                    ItemStack item = event.getItem();
                    if (item != null && item.getType() == Material.RED_ROSE && item.getDurability() == 3
                            && item.getItemMeta().getDisplayName().equals(MEDICINAL_FLOWER_DISPLAYNAME)) {
                        item.setAmount(item.getAmount() - 1);
                        Player player = event.getPlayer();
                        player.setHealth(20);
                        player.sendMessage(ChatColor.AQUA + "You feel refreshed from the magic of the medicinal flower...");
                        event.setCancelled(true);
                    }
                }
            }
        });
    }
}
