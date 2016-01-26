package com.gmail.gogobebe2.shiftkits.kitgroups;

import com.gmail.gogobebe2.shiftkits.Kit;
import com.gmail.gogobebe2.shiftkits.MagicKit;
import com.gmail.gogobebe2.shiftkits.requirements.Cost;
import org.bukkit.ChatColor;
import org.bukkit.EntityEffect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BerserkerKitGroup implements KitGroup {
    public static final short RED_DYE_METADATA = 1;

    @Override
    public Kit getLevel1() {
        return getLevel(2, 10, 15000, 1);
    }

    @Override
    public Kit getLevel2() {
        return getLevel(3, 20, 40000, 2);
    }

    @Override
    public Kit getLevel3() {
        return getLevel(4, 30, 100000, 3);
    }

    @Override
    public String getName() {
        return "Berserker";
    }

    @Override
    public List<String> getLore() {
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.RED + "" + ChatColor.BOLD + "PREMIUM ONLY!");
        lore.add(ChatColor.GRAY + "Spawn with rose reds.");
        lore.add(ChatColor.GRAY + "Left click them to activate bloodlust!");
        lore.add(ChatColor.GRAY + "You'll gain Strength II at the cost of some health.");
        lore.add(ChatColor.GRAY + "Level 2 & 3 upgrades give more rose reds and more potent bloodlust!");
        return lore;
    }

    private Kit getLevel(int roseRedAmount, final int strengthDuration, int xp, int level) {
        Map<Integer, ItemStack> items = new HashMap<>();

        items.put(0, new ItemStack(Material.STONE_PICKAXE, 1));
        items.put(1, new ItemStack(Material.WOOD_SWORD));

        ItemStack redDye = new ItemStack(Material.INK_SACK, roseRedAmount);
        redDye.setDurability(RED_DYE_METADATA);
        ItemMeta meta = redDye.getItemMeta();
        final String BLOODLUST_DISPLAYNAME = ChatColor.DARK_RED + "" + ChatColor.BOLD + "Activate Bloodlust";
        meta.setDisplayName(BLOODLUST_DISPLAYNAME);
        final List<String> itemLore = new ArrayList<>();
        itemLore.add(ChatColor.RED + "Right click deals 3 hearts damage to you;");
        itemLore.add(ChatColor.RED + "Gives strength II for " + strengthDuration + " seconds");
        meta.setLore(itemLore);
        redDye.setItemMeta(meta);

        items.put(2, redDye);

        Cost cost = new Cost(xp);

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GREEN + "Start with a wood pick,");
        lore.add(ChatColor.GREEN + "wood sword, and " + roseRedAmount + " rose reds.");
        lore.add(ChatColor.GREEN + "Deals " + (level + 1) + " hearts for ");
        lore.add(ChatColor.GREEN + "" + strengthDuration + " seconds of Strength II.");
        lore.add(ChatColor.GOLD + "Purchase rank from buy.xpcraft.com");
        lore.add(ChatColor.GOLD + "Then unlock with " + cost.getDescription() + "!");

        return new MagicKit(getName(), (short) level, cost, items, Material.INK_SACK, lore, "shiftkits."
                + getName().toLowerCase(), new Listener() {
            @EventHandler
            private void onPlayerInteract(PlayerInteractEvent event) {
                ItemStack item = event.getItem();
                if (item != null) {
                    ItemMeta meta = item.getItemMeta();
                    if ((event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR)
                            && item.getType() == Material.INK_SACK
                            && item.getDurability() == RED_DYE_METADATA
                            && meta.getDisplayName().equals(BLOODLUST_DISPLAYNAME)
                            && meta.getLore().equals(itemLore)) {
                        Player player = event.getPlayer();
                        player.setHealth(player.getHealth() - 6);
                        player.playEffect(EntityEffect.HURT);
                        player.playSound(player.getLocation(), Sound.HURT_FLESH, 1, 1);
                        player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, strengthDuration * 20, 1));
                        player.sendMessage(ChatColor.RED + "Your deadly inhumane lust takes over!");
                        item.setAmount(item.getAmount() - 1);
                        event.setCancelled(true);
                    }
                }
            }
        });
    }
}
