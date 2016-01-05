package com.gmail.gogobebe2.shiftkits;

import com.gmail.gogobebe2.shiftkits.kitgroups.KitGroup;
import com.gmail.gogobebe2.shiftkits.kitgroups.KitGroupInstances;
import com.gmail.gogobebe2.shiftkits.requirements.Cost;
import com.gmail.gogobebe2.shiftkits.requirements.Requirement;
import com.gmail.gogobebe2.shiftspawn.GameState;
import com.gmail.gogobebe2.shiftspawn.ShiftSpawn;
import com.gmail.gogobebe2.shiftstats.ShiftStats;
import com.google.common.base.Strings;
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
import org.bukkit.inventory.ItemFlag;
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
                    player.sendMessage(ChatColor.GREEN + "Kit " + getKitName(kit)
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
            int highestLevel = 1;
            String[] kitColumn = ShiftStats.getAPI().getKits(player.getUniqueId());
            if (kitColumn != null) {
                for (String kitID : kitColumn) {
                    if (kitID.contains(kitName)) {
                        int tempLevel = Integer.parseInt(kitID.replace("-" + kitName, ""));
                        if (tempLevel >= highestLevel) highestLevel = tempLevel;
                    }
                }
            }

            Kit kit;

            if (highestLevel == 3) kit = kitGroup.getLevel3();
            else if (highestLevel == 2) kit =  kitGroup.getLevel2();
            else kit =  kitGroup.getLevel1();

            ItemStack button = new ItemStack(kit.getIcon(), 1);
            ItemMeta meta = button.getItemMeta();
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            String displayName = ChatColor.AQUA + kitName;
            meta.setDisplayName(displayName);

            kits.put(displayName, kit);

            meta.setLore(kitGroup.getLore());
            button.setItemMeta(meta);

            kitListMenu.setItem(index, button);
            index++;
        }

        player.updateInventory();
        this.kitsOwned = kits;
    }

    private boolean canBuy(Kit kit) throws SQLException, ClassNotFoundException {
        return kit.getRequirement().satisfies(Bukkit.getPlayer(playerUUID)) && (!kit.needsPermission() || Bukkit.getPlayer(playerUUID).hasPermission(kit.getPermission()));
    }

    private void buy(Kit kit) throws SQLException, ClassNotFoundException {
        Requirement requirement = kit.getRequirement();
        String id = kit.getId();
        Player player = Bukkit.getPlayer(playerUUID);

        player.sendMessage(ChatColor.GREEN + "You just unlocked the " + id + " kit with "
                + requirement.getDescription());
        if (requirement instanceof Cost) ((Cost) requirement).takeXP(player);
        ShiftStats.getAPI().addKit(player.getUniqueId(), id);

        player.sendMessage(ChatColor.GREEN + "You just unlocked the level " + kit.getLevel() + " " + getKitName(kit) + " kit!");
    }

    private boolean hasKit(Kit kit) {
        String[] kitsColumn;
        try {
            kitsColumn = ShiftStats.getAPI().getKits(Bukkit.getPlayer(playerUUID).getUniqueId());
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        if (kitsColumn != null && kitsColumn.length != 0) for (String kitID : kitsColumn) if (kit.getId().equals(kitID)) return true;
        return false;
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

    private static String getKitName(Kit kit) {
        return kit.getId().replace(kit.getLevel() + "-", "");
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

            if (button != null && button.hasItemMeta()) {
                Inventory inventory = event.getInventory();
                String inventoryName = inventory.getName();

                KitSelector kitSelector = kitSelectors.get(player.getUniqueId());

                String buyOrSellKitMenuNameSuffix = ChatColor.BOLD + "" + ChatColor.AQUA + "Submenu - ";
                String selectButtonDisplaynamePrefix = ChatColor.GREEN + "" + ChatColor.ITALIC + "Select ";
                if (inventoryName.equals(kitSelector.kitListMenu.getName())) {

                    String buttonDisplayname = button.getItemMeta().getDisplayName();

                    Kit kit = kitSelector.kitsOwned.get(buttonDisplayname);
                    short level = kit.getLevel();

                    KitGroup kitGroup = KitGroupInstances.getKitGroupInstance(getKitName(kit));
                    assert kitGroup != null;

                    Inventory buyOrSellMenu = Bukkit.createInventory(null, 9, buyOrSellKitMenuNameSuffix + buttonDisplayname);

                    ItemStack buyButton = new ItemStack(Material.GOLD_INGOT, 1);
                    ItemMeta buyButtonMeta = buyButton.getItemMeta();

                    Kit nextKit;

                    Kit level1Kit = kitGroup.getLevel1();
                    Kit level2Kit = kitGroup.getLevel2();
                    Kit level3Kit = kitGroup.getLevel3();

                    if (!kitSelector.hasKit(kit)) {
                        // He does not have the kit...
                        buyButtonMeta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Purchase kit");
                        nextKit = level1Kit;
                    }
                    else if (level == 1 || level == 2) {
                        // He has the kit...
                        buyButtonMeta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Upgrade kit");
                        if (level == 1) nextKit = level2Kit;
                        else nextKit = level3Kit;
                    }
                    else {
                        buyButtonMeta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Kit is already max level");
                        buyButton.setType(Material.BARRIER);
                        player.sendMessage(ChatColor.RED + "Error! This kit is already it's max level.");
                        return;
                    }

                    List<String> buyButtonLore = new ArrayList<>();

                    buyButtonLore.add(ChatColor.YELLOW + "" + ChatColor.BOLD + "Next upgrade:" + ChatColor.GOLD
                            + "Level " + nextKit.getLevel() + " " + kitGroup.getName());
                    buyButtonLore.add(ChatColor.UNDERLINE + Strings.repeat(" ", 10));
                    buyButtonLore.add(ChatColor.BLUE + "" + ChatColor.BOLD + "Level 1:");
                    buyButtonLore.addAll(level1Kit.getLore());
                    buyButtonLore.add(ChatColor.UNDERLINE + Strings.repeat(" ", 10));
                    buyButtonLore.add(ChatColor.BLUE + "" + ChatColor.BOLD + "Level 2:");
                    buyButtonLore.addAll(level2Kit.getLore());
                    buyButtonLore.add(ChatColor.UNDERLINE + Strings.repeat(" ", 10));
                    buyButtonLore.add(ChatColor.BLUE + "" + ChatColor.BOLD + "Level 3:");
                    buyButtonLore.addAll(level3Kit.getLore());

                    buyButtonMeta.setLore(buyButtonLore);
                    buyButton.setItemMeta(buyButtonMeta);

                    ItemStack selectButton = new ItemStack(Material.BEDROCK, 1);
                    ItemMeta selectButtonMeta = selectButton.getItemMeta();
                    selectButtonMeta.setDisplayName(selectButtonDisplaynamePrefix + ChatColor.RESET + buttonDisplayname);
                    selectButton.setItemMeta(selectButtonMeta);

                    buyOrSellMenu.setItem(2, buyButton);
                    buyOrSellMenu.setItem(6, selectButton);
                    player.closeInventory();
                    player.openInventory(buyOrSellMenu);
                    event.setCancelled(true);
                }
                else if (inventoryName.contains(buyOrSellKitMenuNameSuffix)) {
                    String kitDisplayName = inventoryName.replace(buyOrSellKitMenuNameSuffix, "");
                    Kit kit = kitSelector.kitsOwned.get(kitDisplayName);

                    if (button.getItemMeta().getDisplayName().contains(selectButtonDisplaynamePrefix)) {
                        if (kitSelector.hasKit(kit)) {
                            pendingKits.put(player.getUniqueId(), kit);
                            player.sendMessage(ChatColor.GREEN + "You have selected " + getKitName(kit) + " level " + kit.getLevel());
                        }
                        else player.sendMessage(ChatColor.RED + "Error, you don't own this kit!");
                    }
                    else {
                        if (kit.getLevel() != 3) {
                            try {
                                if (kitSelector.canBuy(kit)) kitSelector.buy(kit);
                                else player.sendMessage(ChatColor.RED + "Error! You need to have "
                                        + kit.getRequirement().getDescription() + " in order to unlock this kit!");

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
                    event.setCancelled(true);
                }
            }

        }
    }
}

