package com.gmail.gogobebe2.shiftkits;

import com.gmail.gogobebe2.shiftkits.kitgroups.KitGroup;
import com.gmail.gogobebe2.shiftkits.kitgroups.KitGroupInstances;
import com.gmail.gogobebe2.shiftkits.requirements.Cost;
import com.gmail.gogobebe2.shiftkits.requirements.Requirement;
import com.gmail.gogobebe2.shiftspawn.GameState;
import com.gmail.gogobebe2.shiftspawn.ShiftSpawn;
import com.gmail.gogobebe2.shiftstats.ShiftStats;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitScheduler;

import java.sql.SQLException;
import java.util.*;

public class KitSelector {
    private static Map<UUID, KitSelector> kitSelectors = new HashMap<>();

    private static KitSelectorListener kitSelectorListener = new KitSelectorListener();

    private static final ItemStack selector = initSelector();

    private static Map<UUID, Kit> pendingKits = new HashMap<>();

    private static Set<UUID> pastJoins = new HashSet<>();

    private Inventory kitListMenu = Bukkit.createInventory(null,
            roundUpToNearestMultiple(KitGroupInstances.getInstances().size(), 9),
            ChatColor.BOLD + "" + ChatColor.AQUA + "Kit Selection Menu");

    private UUID playerUUID;

    private Map<String, Kit> kitsOwned;

    private KitSelector(final UUID playerUUID) throws SQLException, ClassNotFoundException {
        this.playerUUID = playerUUID;
        BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
        scheduler.scheduleSyncRepeatingTask(ShiftKits.instance, new Runnable() {
            @Override
            public void run() {
                // To reduce possible lag, check if inventory is open:
                if (!kitListMenu.getViewers().isEmpty()) {
                    try {
                        updateKitListMenu();
                    } catch (SQLException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                if (pendingKits.containsKey(playerUUID)
                        && ShiftSpawn.getInstance().getGame().getGameState() == GameState.STARTED) {
                    Player player = Bukkit.getPlayer(playerUUID);
                    Kit kit = pendingKits.get(playerUUID);
                    kit.apply(player);
                    PlayerInventory inventory = player.getInventory();
                    if (inventory.contains(selector)) {
                        inventory.remove(selector);
                        player.updateInventory();
                    }
                    player.sendMessage(ChatColor.GREEN + "Kit " + kit.getId().replace(kit.getLevel() + "-", "")
                            + " level " + kit.getLevel() + " has been applied to you");
                    pendingKits.remove(playerUUID);
                }
            }
        }, 0L, 2L);
    }

    private void updateKitListMenu() throws SQLException, ClassNotFoundException {
        int index = 0;

        Map<String, Kit> kits = new HashMap<>();
        Player player = Bukkit.getPlayer(playerUUID);

        for (KitGroup kitGroup : KitGroupInstances.getInstances()) {
            String kitName = kitGroup.getName();
            int highestLevel = 0;
            for (String kitID : ShiftStats.getAPI().getKits(player.getUniqueId())) {
                if (kitID.contains(kitName)) {
                    int tempLevel = Integer.parseInt(kitID.replace("-" + kitName, ""));
                    if (tempLevel >= highestLevel) {
                        highestLevel = tempLevel;
                    }
                }
            }

            Kit kit;

            if (highestLevel == 3) kit = kitGroup.getLevel3();
            else if (highestLevel == 2) kit =  kitGroup.getLevel2();
            else kit =  kitGroup.getLevel1();

            ItemStack button = new ItemStack(kit.getIcon(), 1);
            ItemMeta meta = button.getItemMeta();
            String displayName = ChatColor.AQUA + "" + ChatColor.BOLD + kitName + ChatColor.BLUE + " - Level " + highestLevel;
            meta.setDisplayName(displayName);
            kits.put(displayName, kit);
            button.setItemMeta(meta);

            kitListMenu.setItem(index, button);
        }

        player.updateInventory();
        this.kitsOwned = kits;
    }

    private boolean canBuy(Kit kit) throws SQLException, ClassNotFoundException {
        return kit.getRequirement().satisfies(Bukkit.getPlayer(playerUUID));
    }

    private void buy(Kit kit) throws SQLException, ClassNotFoundException {
        Requirement requirement = kit.getRequirement();
        String id = kit.getId();
        Player player = Bukkit.getPlayer(playerUUID);

        player.sendMessage(ChatColor.GREEN + "You just unlocked the " + id + " kit with "
                + requirement.getDescription());
        if (requirement instanceof Cost) ((Cost) requirement).takeXP(player);
        ShiftStats.getAPI().addKit(player.getUniqueId(), id);
    }

    private static ItemStack initSelector() {
        ItemStack selector = new ItemStack(Material.EMERALD, 1);
        ItemMeta meta = selector.getItemMeta();
        meta.setDisplayName(ChatColor.BOLD + "" + ChatColor.AQUA + "Kit Selector");
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.BLUE + "Choose a kit before you can play.");
        meta.setLore(lore);
        selector.setItemMeta(meta);
        return selector;
    }

    private static int roundUpToNearestMultiple(double number, double factor) {
        return (int) (Math.ceil(number / factor) * factor);
    }

    protected static KitSelectorListener getListener() {
        return kitSelectorListener;
    }

    private static class KitSelectorListener implements Listener {
        @EventHandler
        private static void onPlayerJoin(PlayerJoinEvent event) {
            Player player = event.getPlayer();
            UUID playerUUID = player.getUniqueId();
            try {
                kitSelectors.put(playerUUID, new KitSelector(playerUUID));
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }

            if (!pastJoins.contains(playerUUID)) {
                player.getInventory().addItem(selector);
                pastJoins.add(playerUUID);
            }
        }

        @EventHandler
        private static void onInteractEvent(PlayerInteractEvent event) {
            Action action = event.getAction();
            if (action == Action.RIGHT_CLICK_BLOCK || action == Action.RIGHT_CLICK_AIR
                    || action == Action.LEFT_CLICK_BLOCK || action == Action.LEFT_CLICK_AIR) {
                ItemStack item = event.getItem();
                if (item.getType() == selector.getType() && item.getItemMeta().equals(selector.getItemMeta())) {
                    Player player = event.getPlayer();
                    player.sendMessage(ChatColor.GREEN + "Opening kit selection menu...");
                    player.openInventory(kitSelectors.get(player.getUniqueId()).kitListMenu);
                }
            }
        }

        @EventHandler
        private static void onInventoryClick(InventoryClickEvent event) {
            Player player = (Player) event.getWhoClicked();
            ItemStack button = event.getCurrentItem();
            Inventory inventory = event.getInventory();
            String inventoryName = inventory.getName();

            KitSelector kitSelector = kitSelectors.get(player.getUniqueId());

            String buyOrSellKitMenuNameSuffix = ChatColor.BOLD + "" + ChatColor.AQUA + "Buy or Sell Kit Menu - ";
            String selectButtonDisplaynamePrefix = ChatColor.GREEN + "" + ChatColor.ITALIC + "Select ";
            if (inventoryName.equals(kitSelector.kitListMenu.getName())) {

                String kitDisplayName = button.getItemMeta().getDisplayName();

                Kit kit = kitSelector.kitsOwned.get(kitDisplayName);
                short level = kit.getLevel();

                Inventory buyOrSellMenu = Bukkit.createInventory(null, 9, buyOrSellKitMenuNameSuffix + kitDisplayName);

                ItemStack buyButton = new ItemStack(Material.GOLD_INGOT, 1);
                ItemMeta buyButtonMeta = buyButton.getItemMeta();

                KitGroup kitGroup = KitGroupInstances.getKitGroupInstance(kit.getId().replace(level + "-", ""));
                assert kitGroup != null;

                Kit nextKit;

                if (level == 0) {
                    // He does not have the kit...
                    buyButtonMeta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Purchase kit");
                    nextKit = kitGroup.getLevel1();
                }
                else if (level == 1 || level == 2) {
                    // He has the kit...
                    buyButtonMeta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Upgrade kit");
                    if (level == 1) nextKit = kitGroup.getLevel2();
                    else nextKit = kitGroup.getLevel3();
                }
                else {
                    buyButtonMeta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Kit is already max level");
                    buyButton.setType(Material.BARRIER);
                    player.sendMessage(ChatColor.RED + "Error! This kit is already it's max level.");
                    return;
                }

                List<String> buyButtonLore = new ArrayList<>();
                buyButtonLore.add(ChatColor.GOLD + "Level " + nextKit.getLevel() + " " + kitGroup.getName());
                buyButtonLore.add(ChatColor.RED + "Requirement:");
                buyButtonLore.add(ChatColor.DARK_RED + "You need to have " + nextKit.getRequirement().getDescription()
                        + " to unlock this kit");

                buyButtonMeta.setLore(buyButtonLore);
                buyButton.setItemMeta(buyButtonMeta);

                ItemStack selectButton = new ItemStack(Material.FIRE, 1);
                ItemMeta selectButtonMeta = selectButton.getItemMeta();
                selectButtonMeta.setDisplayName(selectButtonDisplaynamePrefix + ChatColor.RESET + kitDisplayName);
                selectButton.setItemMeta(selectButtonMeta);

                buyOrSellMenu.setItem(2, buyButton);
                buyOrSellMenu.setItem(6, selectButton);
                player.closeInventory();
                player.openInventory(buyOrSellMenu);
            }
            else if (inventoryName.contains(buyOrSellKitMenuNameSuffix)) {
                String kitDisplayName = inventoryName.replace(buyOrSellKitMenuNameSuffix, "");
                Kit kit = kitSelector.kitsOwned.get(kitDisplayName);
                KitGroup kitGroup = KitGroupInstances.getKitGroupInstance(kit.getId().replace(kit.getLevel() + "-", ""));
                assert kitGroup != null;

                if (button.getItemMeta().getDisplayName().equals(selectButtonDisplaynamePrefix)) {
                    pendingKits.put(player.getUniqueId(), kit);
                    player.sendMessage(ChatColor.GREEN + "You have selected " + kitGroup.getName() + " level " + kit.getLevel());

                }
                else {
                    if (kit.getLevel() != 3) {
                        try {
                            ShiftStats.getAPI().addKit(player.getUniqueId(), kit.getLevel() + "-" + kitGroup.getName());
                            player.sendMessage(ChatColor.GREEN + "You just unlocked the level " + kit.getLevel() + " " + kitGroup.getName() + " kit!");
                        } catch (SQLException | ClassNotFoundException e) {
                            e.printStackTrace();
                            player.sendMessage(ChatColor.RED + "Error! Can't connect to SQL database!");
                        }
                    }
                    else {
                        player.sendMessage(ChatColor.RED + "Error! You can't unlock this kit because it's already at it's max level!");
                    }
                }
                player.closeInventory();
            }
        }
    }
}

