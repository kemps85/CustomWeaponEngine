/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.milkbowl.vault.economy.Economy
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.NamespacedKey
 *  org.bukkit.OfflinePlayer
 *  org.bukkit.Sound
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.inventory.InventoryClickEvent
 *  org.bukkit.event.inventory.InventoryCloseEvent
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.persistence.PersistentDataContainer
 *  org.bukkit.persistence.PersistentDataType
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.java.JavaPlugin
 */
package org.example.enchant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.example.stats.ItemBuilder;
import org.example.stats.ItemStatsGUI;

public class ReforgeSystem
implements Listener,
CommandExecutor {
    private final JavaPlugin plugin;
    private final Economy econ;
    private static final String MENU_TITLE = "\u00a76\u2726 Reforge Menu";
    private static final String NORMAL_TITLE = "\u00a78\u2692 Normal Reforge";
    private static final String EXCLUSIVE_TITLE = "\u00a75\u2726 Exclusive Reforge";
    private static final int NORMAL_COST = 2000;
    private static final int EXCLUSIVE_COST = 20000;
    private static final Random random = new Random();
    private static final Map<ItemCategory, Map<String, Map<ReforgeTier, ReforgeStat>>> REFORGE_MAP = new HashMap<ItemCategory, Map<String, Map<ReforgeTier, ReforgeStat>>>();
    private static final Map<ItemCategory, Map<String, Map<ReforgeTier, ReforgeStat>>> EXCLUSIVE_MAP = new HashMap<ItemCategory, Map<String, Map<ReforgeTier, ReforgeStat>>>();
    private static final String KEY_ABILITY_CLICK = "stat_ability_click";

    public ReforgeSystem(JavaPlugin plugin, Economy econ) {
        this.plugin = plugin;
        this.econ = econ;
    }

    public static ReforgeStat getReforgeStat(String prefix, ItemCategory category, ReforgeTier tier) {
        Map<String, Map<ReforgeTier, ReforgeStat>> pool = REFORGE_MAP.get(category);
        if (pool == null || !pool.containsKey(prefix)) {
            pool = EXCLUSIVE_MAP.get(category);
        }
        if (pool == null || !pool.containsKey(prefix)) {
            return null;
        }
        return pool.get(prefix).get(tier);
    }

    public static ReforgeStat getExclusiveReforgeStat(String prefix, ItemCategory category, ReforgeTier tier) {
        Map<String, Map<ReforgeTier, ReforgeStat>> pool = EXCLUSIVE_MAP.get(category);
        if (pool == null || !pool.containsKey(prefix)) {
            return null;
        }
        return pool.get(prefix).get(tier);
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }
        Player player = (Player)sender;
        if (command.getName().equalsIgnoreCase("delref")) {
            if (!player.hasPermission("cwe.admin")) {
                player.sendMessage("\u00a7cB\u1ea1n kh\u00f4ng c\u00f3 quy\u1ec1n d\u00f9ng l\u1ec7nh n\u00e0y!");
                return true;
            }
            this.handleRemoveReforge(player);
            return true;
        }
        this.openIndexMenu(player);
        return true;
    }

    private void handleRemoveReforge(Player player) {
        NamespacedKey reforgeKey;
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item == null || item.getType().isAir() || !item.hasItemMeta()) {
            player.sendMessage("\u00a7cB\u1ea1n ph\u1ea3i c\u1ea7m v\u1eadt ph\u1ea9m c\u00f3 Reforge!");
            return;
        }
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        if (!pdc.has(reforgeKey = new NamespacedKey((Plugin)this.plugin, "cwe_reforge"), PersistentDataType.STRING)) {
            player.sendMessage("\u00a7cV\u1eadt ph\u1ea9m n\u00e0y ch\u01b0a \u0111\u01b0\u1ee3c Reforge!");
            return;
        }
        ItemCategory cat = this.getCategory(item, pdc);
        this.applyReforge(item, meta, pdc, "", ReforgeTier.NONE, cat, false);
        player.sendMessage("\u00a7a\u0110\u00e3 xo\u00e1 Reforge kh\u1ecfi v\u1eadt ph\u1ea9m th\u00e0nh c\u00f4ng!");
    }

    private void openIndexMenu(Player player) {
        Inventory gui = Bukkit.createInventory(null, (int)27, (String)MENU_TITLE);
        ItemStack glass = this.createItem(Material.GRAY_STAINED_GLASS_PANE, "\u00a78 ", new String[0]);
        for (int i = 0; i < 27; ++i) {
            gui.setItem(i, glass);
        }
        ItemStack normalBtn = this.createItem(Material.ANVIL, "\u00a7a\u00a7l\u2692 NORMAL REFORGE", "\u00a77Chi ph\u00ed: \u00a7e2,000 Coins", "\u00a77Roll ng\u1eabu nhi\u00ean ti\u1ec1n t\u1ed1 thu\u1ed9c t\u00ednh.", "\u00a77\u00c1p d\u1ee5ng cho: \u00a7fT\u1ea5t c\u1ea3 trang b\u1ecb.", "", "\u00a7eClick \u0111\u1ec3 m\u1edf!");
        gui.setItem(11, normalBtn);
        ItemStack exclusiveBtn = this.createItem(Material.NETHERITE_UPGRADE_SMITHING_TEMPLATE, "\u00a7d\u00a7l\u2726 EXCLUSIVE REFORGE", "\u00a77Chi ph\u00ed: \u00a7d20,000 Coins", "\u00a77M\u1edf kho\u00e1 ti\u1ec1n t\u1ed1 \u0111\u1eb7c quy\u1ec1n c\u1ef1c m\u1ea1nh.", "\u00a77\u00c1p d\u1ee5ng cho: \u00a7fGi\u00e1p, V\u0169 kh\u00ed c\u1eadn chi\u1ebfn & T\u1ea7m xa", "\u00a77(Ph\u1ea9m ch\u1ea5t RARE tr\u1edf l\u00ean).", "", "\u00a7dClick \u0111\u1ec3 m\u1edf!");
        gui.setItem(15, exclusiveBtn);
        gui.setItem(22, this.createItem(Material.BARRIER, "\u00a7c\u0110\u00f3ng Giao Di\u1ec7n", new String[0]));
        player.openInventory(gui);
    }

    private void openNormalReforgeGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, (int)54, (String)NORMAL_TITLE);
        this.fillNormalReforgeGUI(gui);
        player.openInventory(gui);
    }

    private void fillNormalReforgeGUI(Inventory gui) {
        ItemStack redGlass = this.createItem(Material.RED_STAINED_GLASS_PANE, "\u00a78 ", new String[0]);
        ItemStack grayGlass = this.createItem(Material.GRAY_STAINED_GLASS_PANE, "\u00a78 ", new String[0]);
        for (int i = 0; i < 54; ++i) {
            int col = i % 9;
            if (col == 0 || col == 8) {
                gui.setItem(i, redGlass);
                continue;
            }
            gui.setItem(i, grayGlass);
        }
        gui.setItem(13, null);
        ItemStack activateBtn = this.createItem(Material.ANVIL, "\u00a7a\u00a7lK\u00cdCH HO\u1ea0T NORMAL REFORGE", "\u00a77Chi ph\u00ed: \u00a7e2,000 Coins", "\u00a77Roll ng\u1eabu nhi\u00ean ti\u1ec1n t\u1ed1 thu\u1ed9c t\u00ednh.");
        gui.setItem(31, activateBtn);
        gui.setItem(48, this.createItem(Material.ARROW, "\u00a77\u00ab Quay l\u1ea1i Menu", new String[0]));
        gui.setItem(49, this.createItem(Material.BARRIER, "\u00a7c\u0110\u00f3ng Giao Di\u1ec7n", new String[0]));
    }

    private void openExclusiveReforgeGUI(Player player) {
        int[] bottomRed;
        int i;
        Inventory gui = Bukkit.createInventory(null, (int)54, (String)EXCLUSIVE_TITLE);
        ItemStack grayGlass = this.createItem(Material.GRAY_STAINED_GLASS_PANE, "\u00a77 ", new String[0]);
        ItemStack redGlass = this.createItem(Material.RED_STAINED_GLASS_PANE, "\u00a77 ", new String[0]);
        ItemStack lightGrayGlass = this.createItem(Material.LIGHT_GRAY_STAINED_GLASS_PANE, "\u00a77 ", new String[0]);
        for (i = 0; i <= 8; ++i) {
            gui.setItem(i, grayGlass);
        }
        gui.setItem(9, grayGlass);
        gui.setItem(10, grayGlass);
        gui.setItem(11, redGlass);
        gui.setItem(12, redGlass);
        gui.setItem(13, this.createItem(Material.BARRIER, "\u00a7cCh\u01b0a \u0111\u1ee7 \u0111i\u1ec1u ki\u1ec7n", "\u00a77H\u00e3y \u0111\u1eb7t Trang b\u1ecb v\u00e0o \u00f4 30", "\u00a77v\u00e0 M\u1ea3nh v\u1ee1 v\u00e0o \u00f4 34."));
        gui.setItem(14, redGlass);
        gui.setItem(15, redGlass);
        gui.setItem(16, grayGlass);
        gui.setItem(17, grayGlass);
        gui.setItem(18, grayGlass);
        gui.setItem(19, grayGlass);
        gui.setItem(20, redGlass);
        gui.setItem(21, redGlass);
        gui.setItem(22, this.createItem(Material.ANVIL, "\u00a7d\u00a7lK\u00cdCH HO\u1ea0T EXCLUSIVE REFORGE", "\u00a77Chi ph\u00ed: \u00a7d20,000 Coins", "\u00a77Click \u0111\u1ec3 xoay Gacha!"));
        gui.setItem(23, redGlass);
        gui.setItem(24, redGlass);
        gui.setItem(25, grayGlass);
        gui.setItem(26, grayGlass);
        gui.setItem(27, grayGlass);
        gui.setItem(28, grayGlass);
        gui.setItem(29, null);
        gui.setItem(30, grayGlass);
        gui.setItem(31, grayGlass);
        gui.setItem(32, grayGlass);
        gui.setItem(33, null);
        gui.setItem(34, grayGlass);
        gui.setItem(35, grayGlass);
        for (i = 36; i <= 44; ++i) {
            gui.setItem(i, grayGlass);
        }
        for (int i2 : bottomRed = new int[]{45, 46, 47, 48, 50, 51, 53}) {
            gui.setItem(i2, redGlass);
        }
        gui.setItem(49, this.createItem(Material.BARRIER, "\u00a7c\u0110\u00f3ng Giao Di\u1ec7n", new String[0]));
        gui.setItem(52, this.createItem(Material.IRON_INGOT, "\u00a7eDanh s\u00e1ch Ti\u1ec1n T\u1ed1", new String[0]));
        player.openInventory(gui);
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        String title = event.getView().getTitle();
        Player player = (Player)event.getWhoClicked();
        if (title.equals(MENU_TITLE)) {
            event.setCancelled(true);
            int slot = event.getRawSlot();
            if (slot == 11) {
                player.closeInventory();
                Bukkit.getScheduler().runTask((Plugin)this.plugin, () -> this.openNormalReforgeGUI(player));
            } else if (slot == 15) {
                player.closeInventory();
                Bukkit.getScheduler().runTask((Plugin)this.plugin, () -> this.openExclusiveReforgeGUI(player));
            } else if (slot == 22) {
                player.closeInventory();
            }
            return;
        }
        if (title.equals(NORMAL_TITLE)) {
            this.handleReforgeGUIClick(event, player, false);
            return;
        }
        if (title.equals(EXCLUSIVE_TITLE)) {
            this.handleReforgeGUIClick(event, player, true);
            Bukkit.getScheduler().runTask((Plugin)this.plugin, () -> this.updateExclusiveStatus(event.getView().getTopInventory()));
        }
    }

    private void updateExclusiveStatus(Inventory gui) {
        ItemStack item = gui.getItem(29);
        ItemStack frag = gui.getItem(33);
        boolean hasItem = item != null && !item.getType().isAir();
        boolean hasFrag = frag != null && !frag.getType().isAir();
        if (hasItem && hasFrag) {
            ItemMeta meta = item.getItemMeta();
            PersistentDataContainer pdc = meta != null ? meta.getPersistentDataContainer() : null;
            ItemCategory cat = this.getCategory(item, pdc);
            ReforgeTier tier = this.resolveTier(item, pdc);
            
            boolean validCat = (cat == ItemCategory.ARMOR || cat == ItemCategory.MELEE || cat == ItemCategory.RANGED);
            boolean validTier = (tier != ReforgeTier.NONE && tier != ReforgeTier.COMMON && tier != ReforgeTier.UNCOMMON);
            
            boolean validFrag = frag.hasItemMeta() && frag.getItemMeta().getPersistentDataContainer().has(new NamespacedKey((Plugin)this.plugin, "cwe_reforge_fragment"), PersistentDataType.INTEGER);
            
            if (!validCat) {
                gui.setItem(13, this.createItem(Material.BARRIER, "\u00a7cTrang b\u1ecb kh\u00f4ng ph\u00f9 h\u1ee3p", "\u00a77Exclusive Reforge ch\u1ec9 \u00e1p d\u1ee5ng cho Gi\u00e1p,", "\u00a77V\u0169 kh\u00ed c\u1eadn chi\u1ebfn v\u00e0 V\u0169 kh\u00ed t\u1ea7m xa."));
            } else if (!validTier) {
                gui.setItem(13, this.createItem(Material.BARRIER, "\u00a7cPh\u1ea9m ch\u1ea5t kh\u00f4ng h\u1ee3p l\u1ec7", "\u00a77Ch\u1ec9 v\u1eadt ph\u1ea9m RARE, EPIC, LEGENDARY, MYTHIC", "\u00a77m\u1edbi c\u00f3 th\u1ec3 tr\u00f9ng \u0111\u00fac \u0111\u1ed9c quy\u1ec1n!"));
            } else if (!validFrag) {
                gui.setItem(13, this.createItem(Material.BARRIER, "\u00a7cNguy\u00ean li\u1ec7u kh\u00f4ng h\u1ee3p l\u1ec7", "\u00a77H\u00e3y \u0111\u1eb7t M\u1ea3nh v\u1ee1 Reforge h\u1ee3p l\u1ec7 v\u00e0o \u00f4 34."));
            } else {
                gui.setItem(13, this.createItem(Material.GREEN_STAINED_GLASS_PANE, "\u00a7aS\u1eb5n s\u00e0ng Reforge!", "\u00a77Nh\u1ea5n v\u00e0o \u0110e b\u00ean d\u01b0\u1edbi \u0111\u1ec3 ti\u1ebfn h\u00e0nh."));
            }
        } else {
            gui.setItem(13, this.createItem(Material.BARRIER, "\u00a7cCh\u01b0a \u0111\u1ee7 \u0111i\u1ec1u ki\u1ec7n", "\u00a77H\u00e3y \u0111\u1eb7t Trang b\u1ecb v\u00e0o \u00f4 30", "\u00a77v\u00e0 M\u1ea3nh v\u1ee1 v\u00e0o \u00f4 34."));
        }
    }

    private void handleReforgeGUIClick(InventoryClickEvent event, Player player, boolean exclusive) {
        Inventory gui = event.getView().getTopInventory();
        int slot = event.getRawSlot();
        if (event.getClickedInventory() != gui) {
            if (event.isShiftClick()) {
                event.setCancelled(true);
            }
            return;
        }
        if (exclusive) {
            if (slot != 29 && slot != 33) {
                event.setCancelled(true);
            }
            if (slot == 22) {
                this.handleExclusiveReforge(player, gui.getItem(29), gui.getItem(33));
            } else if (slot == 49) {
                player.closeInventory();
            }
        } else {
            if (slot != 13) {
                event.setCancelled(true);
            }
            if (slot == 31) {
                this.handleNormalReforge(player, gui.getItem(13));
            } else if (slot == 48) {
                player.closeInventory();
                Bukkit.getScheduler().runTask((Plugin)this.plugin, () -> this.openIndexMenu(player));
            } else if (slot == 49) {
                player.closeInventory();
            }
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        String title = event.getView().getTitle();
        if (!title.equals(NORMAL_TITLE) && !title.equals(EXCLUSIVE_TITLE)) {
            return;
        }
        Player player = (Player)event.getPlayer();
        Inventory gui = event.getInventory();
        if (title.equals(NORMAL_TITLE)) {
            this.giveItemBack(player, gui, 13);
        } else {
            this.giveItemBack(player, gui, 29);
            this.giveItemBack(player, gui, 33);
        }
    }

    private void giveItemBack(Player player, Inventory gui, int slot) {
        ItemStack item = gui.getItem(slot);
        if (item != null && !item.getType().isAir()) {
            gui.setItem(slot, null);
            java.util.HashMap<Integer, org.bukkit.inventory.ItemStack> leftOvers = player.getInventory().addItem(new ItemStack[]{item});
            if (!leftOvers.isEmpty()) {
                for (ItemStack overflow : leftOvers.values()) {
                    player.getWorld().dropItem(player.getLocation(), overflow);
                }
            }
        }
    }

    private void handleNormalReforge(Player player, ItemStack item) {
        if (item == null || item.getType().isAir() || !item.hasItemMeta()) {
            player.sendMessage("\u00a7cB\u1ea1n ch\u01b0a \u0111\u1eb7t v\u1eadt ph\u1ea9m h\u1ee3p l\u1ec7 v\u00e0o \u00f4!");
            return;
        }
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        ReforgeTier tier = this.resolveTier(item, pdc);
        if (tier == ReforgeTier.NONE || tier == ReforgeTier.MYTHIC) {
            player.sendMessage("\u00a7cCh\u1ec9 v\u1eadt ph\u1ea9m COMMON, UNCOMMON, RARE, EPIC, LEGENDARY m\u1edbi c\u00f3 th\u1ec3 tr\u00f9ng \u0111\u00fac!");
            return;
        }
        if (this.econ == null || this.econ.getBalance((OfflinePlayer)player) < 2000.0) {
            player.sendMessage(String.format("\u00a7cB\u1ea1n kh\u00f4ng c\u00f3 \u0111\u1ee7 %,d Coins!", 2000));
            return;
        }
        this.econ.withdrawPlayer((OfflinePlayer)player, 2000.0);
        player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_USE, 1.0f, 1.0f);
        ItemCategory cat = this.getCategory(item, pdc);
        String prefix = this.rollNormalPrefix(cat);
        this.applyReforge(item, meta, pdc, prefix, tier, cat, false);
    }

    private void handleExclusiveReforge(Player player, ItemStack item, ItemStack fragment) {
        if (item == null || item.getType().isAir() || !item.hasItemMeta()) {
            player.sendMessage("\u00a7cB\u1ea1n ch\u01b0a \u0111\u1eb7t v\u1eadt ph\u1ea9m h\u1ee3p l\u1ec7 v\u00e0o \u00f4 trang b\u1ecb!");
            return;
        }
        if (fragment == null || fragment.getType().isAir() || !fragment.hasItemMeta()) {
            player.sendMessage("\u00a7cB\u1ea1n ch\u01b0a \u0111\u1eb7t M\u1ea3nh v\u1ee1 Reforge v\u00e0o \u00f4 nguy\u00ean li\u1ec7u!");
            return;
        }
        PersistentDataContainer fPdc = fragment.getItemMeta().getPersistentDataContainer();
        if (!fPdc.has(new NamespacedKey((Plugin)this.plugin, "cwe_reforge_fragment"), PersistentDataType.INTEGER)) {
            player.sendMessage("\u00a7c\u0110\u00f3 kh\u00f4ng ph\u1ea3i l\u00e0 M\u1ea3nh v\u1ee1 Reforge h\u1ee3p l\u1ec7!");
            return;
        }
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        ItemCategory cat = this.getCategory(item, pdc);
        if (cat != ItemCategory.ARMOR && cat != ItemCategory.MELEE && cat != ItemCategory.RANGED) {
            player.sendMessage("\u00a7dExclusive Reforge ch\u1ec9 \u00e1p d\u1ee5ng cho \u00a7lGi\u00e1p\u00a7d, \u00a7lV\u0169 kh\u00ed cận chiến\u00a7d và \u00a7lV\u0169 kh\u00ed tầm xa\u00a7d!");
            return;
        }
        ReforgeTier tier = this.resolveTier(item, pdc);
        if (tier == ReforgeTier.NONE || tier == ReforgeTier.COMMON || tier == ReforgeTier.UNCOMMON) {
            player.sendMessage("\u00a7cCh\u1ec9 v\u1eadt ph\u1ea9m RARE, EPIC, LEGENDARY, MYTHIC m\u1edbi c\u00f3 th\u1ec3 tr\u00f9ng \u0111\u00fac!");
            return;
        }
        if (this.econ == null || this.econ.getBalance((OfflinePlayer)player) < 20000.0) {
            player.sendMessage(String.format("\u00a7cB\u1ea1n kh\u00f4ng c\u00f3 \u0111\u1ee7 %,d Coins!", 20000));
            return;
        }
        this.econ.withdrawPlayer((OfflinePlayer)player, 20000.0);
        player.playSound(player.getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1.0f, 0.8f);
        fragment.setAmount(fragment.getAmount() - 1);
        String fragmentTierStr = fPdc.has(new NamespacedKey((Plugin)this.plugin, "cwe_fragment_tier"), PersistentDataType.STRING) ? (String)fPdc.get(new NamespacedKey((Plugin)this.plugin, "cwe_fragment_tier"), PersistentDataType.STRING) : "RARE";
        String prefix = this.rollTieredExclusivePrefix(item, cat, fragmentTierStr);
        if ("MYTHIC".equals(fragmentTierStr)) {
            tier = ReforgeTier.MYTHIC;
            pdc.set(new NamespacedKey((Plugin)this.plugin, "cwe_tier"), PersistentDataType.STRING, "MYTHIC");
            pdc.set(new NamespacedKey((Plugin)this.plugin, "stat_rarity"), PersistentDataType.STRING, "MYTHIC");
        }
        this.applyReforge(item, meta, pdc, prefix, tier, cat, true);
        player.sendMessage("\u00a7a\u00a7l[\u2726] Exclusive Reforge th\u00e0nh c\u00f4ng! Trang b\u1ecb c\u1ee7a b\u1ea1n \u0111\u00e3 nh\u1eadn ti\u1ec1n t\u1ed1: \u00a7d\u00a7l" + prefix);
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.2f);
        Bukkit.getScheduler().runTask((Plugin)this.plugin, () -> player.closeInventory());
    }

    private String rollTieredExclusivePrefix(ItemStack item, ItemCategory cat, String tier) {
        Random r = new Random();
        boolean isSpear = item != null && item.getType().name().contains("SPEAR");
        if ("MYTHIC".equalsIgnoreCase(tier)) {
            if (isSpear) {
                return r.nextBoolean() ? "Dragoon" : "Valkyrie";
            }
            if (cat == ItemCategory.MELEE || cat == ItemCategory.RANGED) {
                return r.nextBoolean() ? "Cosmic" : "Galactic";
            }
            return new String[]{"Celestial", "Infernal", "Divine"}[r.nextInt(3)];
        }
        if ("LEGENDARY".equalsIgnoreCase(tier)) {
            if (cat == ItemCategory.MELEE) {
                return r.nextBoolean() ? "Demonic" : "Shadow";
            }
            if (cat == ItemCategory.RANGED) {
                return r.nextBoolean() ? "Headstrong" : "Eagle";
            }
            return new String[]{"Aegis", "Warlord", "Enlightened"}[r.nextInt(3)];
        }
        if ("EPIC".equalsIgnoreCase(tier)) {
            if (cat == ItemCategory.MELEE) {
                return r.nextBoolean() ? "Withered" : "Slayer";
            }
            if (cat == ItemCategory.RANGED) {
                return r.nextBoolean() ? "Precise" : "Sniper";
            }
            return new String[]{"Giant", "Ancient", "Necrotic", "Swift"}[r.nextInt(4)];
        }
        if ("RARE".equalsIgnoreCase(tier)) {
            if (cat == ItemCategory.MELEE) {
                return r.nextBoolean() ? "Fabled" : "Vicious";
            }
            if (cat == ItemCategory.RANGED) {
                return r.nextBoolean() ? "Spiritual" : "Deadeye";
            }
            return new String[]{"Reinforced", "Savage", "Arcane"}[r.nextInt(3)];
        }
        return "";
    }

    private ReforgeTier resolveTier(ItemStack item, PersistentDataContainer pdc) {
        NamespacedKey rarityKey = new NamespacedKey((Plugin)this.plugin, "stat_rarity");
        if (pdc.has(rarityKey, PersistentDataType.STRING)) {
            String rarityStr = (String)pdc.get(rarityKey, PersistentDataType.STRING);
            try {
                ReforgeTier t = ReforgeTier.valueOf(rarityStr);
                if (t != ReforgeTier.NONE) {
                    return t;
                }
                return ReforgeTier.NONE;
            }
            catch (Exception e) {
                return ReforgeTier.NONE;
            }
        }
        String name = item.getType().name();
        boolean isEquip = name.contains("SWORD") || name.contains("AXE") || name.contains("BOW") || name.contains("HELMET") || name.contains("CHESTPLATE") || name.contains("LEGGINGS") || name.contains("BOOTS") || name.contains("PICKAXE") || name.contains("HOE") || name.contains("SHOVEL") || name.contains("SPEAR") || name.contains("MACE") || name.contains("TRIDENT") || item.getType() == Material.STICK || item.getType() == Material.BLAZE_ROD;
        return isEquip ? ReforgeTier.COMMON : ReforgeTier.NONE;
    }

    private ItemCategory getCategory(ItemStack item, PersistentDataContainer pdc) {
        String name = item.getType().name();
        if (name.contains("BOW") || name.contains("CROSSBOW")) {
            return ItemCategory.RANGED;
        }
        if (name.contains("HELMET") || name.contains("CHESTPLATE") || name.contains("LEGGINGS") || name.contains("BOOTS")) {
            return ItemCategory.ARMOR;
        }
        if (name.contains("PICKAXE") || name.contains("HOE") || name.contains("SPADE") || name.contains("SHOVEL")) {
            return ItemCategory.TOOLS;
        }
        return ItemCategory.MELEE;
    }

    private String rollNormalPrefix(ItemCategory cat) {
        Map<String, Map<ReforgeTier, ReforgeStat>> pool = REFORGE_MAP.get(cat);
        if (pool == null || pool.isEmpty()) {
            return "";
        }
        ArrayList<String> keys = new ArrayList<String>(pool.keySet());
        return (String)keys.get(random.nextInt(keys.size()));
    }

    private String rollExclusivePrefix(ItemCategory cat) {
        Map<String, Map<ReforgeTier, ReforgeStat>> pool = EXCLUSIVE_MAP.get(cat);
        if (pool == null || pool.isEmpty()) {
            return "";
        }
        ArrayList<String> keys = new ArrayList<String>(pool.keySet());
        return (String)keys.get(random.nextInt(keys.size()));
    }

    private void setD(PersistentDataContainer pdc, String key, double value) {
        if (value != 0.0) {
            pdc.set(new NamespacedKey((Plugin)this.plugin, key), PersistentDataType.DOUBLE, value);
        } else {
            pdc.remove(new NamespacedKey((Plugin)this.plugin, key));
        }
    }

    private void applyReforge(ItemStack item, ItemMeta meta, PersistentDataContainer pdc, String prefix, ReforgeTier tier, ItemCategory cat, boolean exclusive) {
        ReforgeStat bonus;
        NamespacedKey ogNameKey = new NamespacedKey((Plugin)this.plugin, "cwe_original_name");
        NamespacedKey reforgeKey = new NamespacedKey((Plugin)this.plugin, "cwe_reforge");
        NamespacedKey exclusiveKey = new NamespacedKey((Plugin)this.plugin, "cwe_reforge_exclusive");
        if (!pdc.has(ogNameKey, PersistentDataType.STRING)) {
            String originalName = meta.hasDisplayName() ? meta.getDisplayName() : String.valueOf(ChatColor.WHITE) + this.formatVanillaName(item.getType().name());
            pdc.set(ogNameKey, PersistentDataType.STRING, originalName);
        }
        if (prefix == null || prefix.isEmpty()) {
            pdc.remove(reforgeKey);
            pdc.remove(exclusiveKey);
            this.setD(pdc, "cwe_rf_strength", 0.0);
            this.setD(pdc, "cwe_rf_crit_chance", 0.0);
            this.setD(pdc, "cwe_rf_crit_damage", 0.0);
            this.setD(pdc, "cwe_rf_health", 0.0);
            this.setD(pdc, "cwe_rf_defense", 0.0);
            this.setD(pdc, "cwe_rf_intelligence", 0.0);
            this.setD(pdc, "cwe_rf_speed", 0.0);
            this.setD(pdc, "cwe_rf_attack_speed", 0.0);
            this.setD(pdc, "cwe_rf_level_mult", 0.0);
            item.setItemMeta(meta);
            ItemBuilder.updateItem(item);
            return;
        }
        String ogName = (String)pdc.get(ogNameKey, PersistentDataType.STRING);
        String[] colorSplit = this.splitColorPrefix(ogName);
        String rawName = colorSplit[1];
        String finalPrefix = prefix;
        if (rawName.startsWith(prefix + " ")) {
            finalPrefix = prefix.equals("Wise") || prefix.equals("Strong") || prefix.equals("Heavy") ? "Very " + prefix : (prefix.equals("Superior") ? "Highly Superior" : (prefix.equals("Perfect") ? "Absolutely Perfect" : "Very " + prefix));
        }
        pdc.set(reforgeKey, PersistentDataType.STRING, finalPrefix);
        if (exclusive) {
            pdc.set(exclusiveKey, PersistentDataType.INTEGER, 1);
        } else {
            pdc.remove(exclusiveKey);
        }
        ReforgeStat reforgeStat = bonus = exclusive ? ReforgeSystem.getExclusiveReforgeStat(prefix, cat, tier) : ReforgeSystem.getReforgeStat(prefix, cat, tier);
        if (bonus != null) {
            this.setD(pdc, "cwe_rf_strength", bonus.strength);
            this.setD(pdc, "cwe_rf_crit_chance", bonus.critChance);
            this.setD(pdc, "cwe_rf_crit_damage", bonus.critDamage);
            this.setD(pdc, "cwe_rf_health", bonus.health);
            this.setD(pdc, "cwe_rf_defense", bonus.defense);
            this.setD(pdc, "cwe_rf_intelligence", bonus.intelligence);
            this.setD(pdc, "cwe_rf_speed", bonus.speed);
            this.setD(pdc, "cwe_rf_attack_speed", bonus.attackSpeed);
            this.setD(pdc, "cwe_rf_level_mult", bonus.levelMultiplier);
        } else {
            this.setD(pdc, "cwe_rf_strength", 0.0);
            this.setD(pdc, "cwe_rf_crit_chance", 0.0);
            this.setD(pdc, "cwe_rf_crit_damage", 0.0);
            this.setD(pdc, "cwe_rf_health", 0.0);
            this.setD(pdc, "cwe_rf_defense", 0.0);
            this.setD(pdc, "cwe_rf_intelligence", 0.0);
            this.setD(pdc, "cwe_rf_speed", 0.0);
            this.setD(pdc, "cwe_rf_attack_speed", 0.0);
            this.setD(pdc, "cwe_rf_level_mult", 0.0);
        }
        item.setItemMeta(meta);
        ItemBuilder.updateItem(item);
    }

    private void addStat(List<String> lore, String label, String colorCode, double totalValue, double bonusValue, String prefix, boolean isPercent) {
        if (totalValue > 0.0) {
            String pct = isPercent ? "%" : "";
            Object line = String.format("\u00a77%s: %s+%.0f%s", label, colorCode, totalValue, pct);
            if (bonusValue > 0.0 && prefix != null && !prefix.isEmpty()) {
                line = (String)line + String.format(" \u00a78(%s +%.0f%s)", prefix, bonusValue, pct);
            }
            lore.add((String)line);
        }
    }

    private String getArmorOrVanillaTypeName(ItemStack item) {
        String custom = ItemStatsGUI.getArmorTypeName(item);
        return custom != null && !custom.isEmpty() ? custom : this.resolveVanillaTypeName(item);
    }

    private String resolveVanillaTypeName(ItemStack item) {
        String name = item.getType().name();
        if (name.contains("SWORD")) {
            return "SWORD";
        }
        if (name.contains("AXE")) {
            return "AXE";
        }
        if (name.contains("BOW")) {
            return "BOW";
        }
        if (name.contains("CROSSBOW")) {
            return "CROSSBOW";
        }
        if (name.contains("PICKAXE")) {
            return "PICKAXE";
        }
        if (name.contains("HOE")) {
            return "HOE";
        }
        if (name.contains("SHOVEL") || name.contains("SPADE")) {
            return "SHOVEL";
        }
        if (name.contains("HELMET")) {
            return "HELMET";
        }
        if (name.contains("CHESTPLATE")) {
            return "CHESTPLATE";
        }
        if (name.contains("LEGGINGS")) {
            return "LEGGINGS";
        }
        if (name.contains("BOOTS")) {
            return "BOOTS";
        }
        return "ITEM";
    }

    private String formatVanillaName(String typeName) {
        StringBuilder sb = new StringBuilder();
        for (String part : typeName.split("_")) {
            if (part.isEmpty()) continue;
            sb.append(Character.toUpperCase(part.charAt(0))).append(part.substring(1).toLowerCase()).append(" ");
        }
        return sb.toString().trim();
    }

    private String[] splitColorPrefix(String name) {
        int i;
        StringBuilder color = new StringBuilder();
        for (i = 0; i < name.length() && name.charAt(i) == '\u00a7' && i + 1 < name.length(); i += 2) {
            color.append(name.charAt(i)).append(name.charAt(i + 1));
        }
        return new String[]{color.toString(), name.substring(i)};
    }

    private boolean isEnchantLoreLine(String line) {
        String noColor = ChatColor.stripColor((String)line).toLowerCase();
        return noColor.contains("protection") || noColor.contains("counter strike") || noColor.contains("feather falling") || noColor.contains("growth") || noColor.contains("big brain") || noColor.contains("smarty pants") || noColor.contains("sugar rush") || noColor.contains("rejuvenate") || noColor.contains("thorns") || noColor.contains("sharpness") || noColor.contains("thunder strike") || noColor.contains("vampirism") || noColor.contains("telepathy");
    }

    private double getD(PersistentDataContainer pdc, String key) {
        NamespacedKey nk = new NamespacedKey((Plugin)this.plugin, key);
        return pdc.has(nk, PersistentDataType.DOUBLE) ? (Double)pdc.get(nk, PersistentDataType.DOUBLE) : 0.0;
    }

    private String getString(PersistentDataContainer pdc, String key, String def) {
        NamespacedKey nk = new NamespacedKey((Plugin)this.plugin, key);
        return pdc.has(nk, PersistentDataType.STRING) ? (String)pdc.get(nk, PersistentDataType.STRING) : def;
    }

    private String getWeaponIdFromDisplayName(String displayName) {
        if (displayName == null) {
            return null;
        }
        String clean = ChatColor.stripColor((String)displayName);
        if (clean.equalsIgnoreCase("Heavenly Sword")) {
            return "heavenly_sword";
        }
        if (clean.equalsIgnoreCase("Shadow Assassin Blade")) {
            return "shadow_assassin_blade";
        }
        if (clean.equalsIgnoreCase("Berserk Axe")) {
            return "berserk_axe";
        }
        if (clean.equalsIgnoreCase("Astral Shepherd Wand")) {
            return "astral_shepherd_wand";
        }
        if (clean.equalsIgnoreCase("Cosmic Void Sword")) {
            return "cosmic_void_sword";
        }
        return null;
    }

    private ItemStack createItem(Material mat, String name, String ... lore) {
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            if (lore.length > 0) {
                meta.setLore(Arrays.asList(lore));
            }
            item.setItemMeta(meta);
        }
        return item;
    }

    static {
        LinkedHashMap melee = new LinkedHashMap();
        HashMap<ReforgeTier, ReforgeStat> spicy = new HashMap<ReforgeTier, ReforgeStat>();
        spicy.put(ReforgeTier.COMMON, new ReforgeStat("Spicy").str(8.0).cc(2.0).cd(10.0));
        spicy.put(ReforgeTier.UNCOMMON, new ReforgeStat("Spicy").str(11.0).cc(3.0).cd(15.0));
        spicy.put(ReforgeTier.RARE, new ReforgeStat("Spicy").str(15.0).cc(4.0).cd(20.0));
        spicy.put(ReforgeTier.EPIC, new ReforgeStat("Spicy").str(20.0).cc(7.0).cd(25.0));
        spicy.put(ReforgeTier.LEGENDARY, new ReforgeStat("Spicy").str(25.0).cc(10.0).cd(35.0));
        melee.put("Spicy", spicy);
        HashMap<ReforgeTier, ReforgeStat> heroic = new HashMap<ReforgeTier, ReforgeStat>();
        heroic.put(ReforgeTier.COMMON, new ReforgeStat("Heroic").str(8.0).spd(1.0).intel(20.0));
        heroic.put(ReforgeTier.UNCOMMON, new ReforgeStat("Heroic").str(11.0).spd(1.0).intel(30.0));
        heroic.put(ReforgeTier.RARE, new ReforgeStat("Heroic").str(15.0).spd(1.0).intel(40.0));
        heroic.put(ReforgeTier.EPIC, new ReforgeStat("Heroic").str(20.0).spd(2.0).intel(50.0));
        heroic.put(ReforgeTier.LEGENDARY, new ReforgeStat("Heroic").str(25.0).spd(3.0).intel(65.0));
        melee.put("Heroic", heroic);
        HashMap<ReforgeTier, ReforgeStat> legendary = new HashMap<ReforgeTier, ReforgeStat>();
        legendary.put(ReforgeTier.COMMON, new ReforgeStat("Legendary").atkSpd(2.0).str(6.0).cc(2.0).cd(8.0).intel(8.0));
        legendary.put(ReforgeTier.UNCOMMON, new ReforgeStat("Legendary").atkSpd(2.0).str(9.0).cc(4.0).cd(11.0).intel(11.0));
        legendary.put(ReforgeTier.RARE, new ReforgeStat("Legendary").atkSpd(3.0).str(12.0).cc(5.0).cd(15.0).intel(15.0));
        legendary.put(ReforgeTier.EPIC, new ReforgeStat("Legendary").atkSpd(5.0).str(15.0).cc(7.0).cd(20.0).intel(20.0));
        legendary.put(ReforgeTier.LEGENDARY, new ReforgeStat("Legendary").atkSpd(7.0).str(20.0).cc(9.0).cd(28.0).intel(25.0));
        melee.put("Legendary", legendary);
        HashMap<ReforgeTier, ReforgeStat> sharp = new HashMap<ReforgeTier, ReforgeStat>();
        sharp.put(ReforgeTier.COMMON, new ReforgeStat("Sharp").cc(6.0).cd(10.0));
        sharp.put(ReforgeTier.UNCOMMON, new ReforgeStat("Sharp").cc(9.0).cd(15.0));
        sharp.put(ReforgeTier.RARE, new ReforgeStat("Sharp").cc(12.0).cd(20.0));
        sharp.put(ReforgeTier.EPIC, new ReforgeStat("Sharp").cc(14.0).cd(25.0));
        sharp.put(ReforgeTier.LEGENDARY, new ReforgeStat("Sharp").cc(17.0).cd(30.0));
        melee.put("Sharp", sharp);
        REFORGE_MAP.put(ItemCategory.MELEE, melee);
        LinkedHashMap ranged = new LinkedHashMap();
        HashMap<ReforgeTier, ReforgeStat> rapid = new HashMap<ReforgeTier, ReforgeStat>();
        rapid.put(ReforgeTier.COMMON, new ReforgeStat("Rapid").str(5.0).cd(12.0));
        rapid.put(ReforgeTier.UNCOMMON, new ReforgeStat("Rapid").str(8.0).cd(19.0));
        rapid.put(ReforgeTier.RARE, new ReforgeStat("Rapid").str(10.0).cd(25.0));
        rapid.put(ReforgeTier.EPIC, new ReforgeStat("Rapid").str(15.0).cd(35.0));
        rapid.put(ReforgeTier.LEGENDARY, new ReforgeStat("Rapid").str(22.0).cd(50.0));
        ranged.put("Rapid", rapid);
        HashMap<ReforgeTier, ReforgeStat> unreal = new HashMap<ReforgeTier, ReforgeStat>();
        unreal.put(ReforgeTier.COMMON, new ReforgeStat("Unreal").atkSpd(2.0).str(4.0).cc(4.0).cd(8.0));
        unreal.put(ReforgeTier.UNCOMMON, new ReforgeStat("Unreal").atkSpd(2.0).str(6.0).cc(6.0).cd(13.0));
        unreal.put(ReforgeTier.RARE, new ReforgeStat("Unreal").atkSpd(3.0).str(8.0).cc(8.0).cd(17.0));
        unreal.put(ReforgeTier.EPIC, new ReforgeStat("Unreal").atkSpd(5.0).str(11.0).cc(10.0).cd(22.0));
        unreal.put(ReforgeTier.LEGENDARY, new ReforgeStat("Unreal").atkSpd(7.0).str(15.0).cc(13.0).cd(29.0));
        ranged.put("Unreal", unreal);
        HashMap<ReforgeTier, ReforgeStat> awkward = new HashMap<ReforgeTier, ReforgeStat>();
        awkward.put(ReforgeTier.COMMON, new ReforgeStat("Awkward").cc(10.0).cd(5.0).intel(5.0));
        awkward.put(ReforgeTier.UNCOMMON, new ReforgeStat("Awkward").cc(12.0).cd(8.0).intel(8.0));
        awkward.put(ReforgeTier.RARE, new ReforgeStat("Awkward").cc(15.0).cd(10.0).intel(10.0));
        awkward.put(ReforgeTier.EPIC, new ReforgeStat("Awkward").cc(20.0).cd(15.0).intel(15.0));
        awkward.put(ReforgeTier.LEGENDARY, new ReforgeStat("Awkward").cc(25.0).cd(20.0).intel(20.0));
        ranged.put("Awkward", awkward);
        HashMap<ReforgeTier, ReforgeStat> rich = new HashMap<ReforgeTier, ReforgeStat>();
        rich.put(ReforgeTier.COMMON, new ReforgeStat("Rich").intel(10.0));
        rich.put(ReforgeTier.UNCOMMON, new ReforgeStat("Rich").intel(15.0));
        rich.put(ReforgeTier.RARE, new ReforgeStat("Rich").intel(20.0));
        rich.put(ReforgeTier.EPIC, new ReforgeStat("Rich").intel(30.0));
        rich.put(ReforgeTier.LEGENDARY, new ReforgeStat("Rich").intel(40.0));
        ranged.put("Rich", rich);
        HashMap<ReforgeTier, ReforgeStat> fine = new HashMap<ReforgeTier, ReforgeStat>();
        fine.put(ReforgeTier.COMMON, new ReforgeStat("Fine").str(3.0).cc(3.0).cd(3.0));
        fine.put(ReforgeTier.UNCOMMON, new ReforgeStat("Fine").str(5.0).cc(4.0).cd(5.0));
        fine.put(ReforgeTier.RARE, new ReforgeStat("Fine").str(7.0).cc(5.0).cd(8.0));
        fine.put(ReforgeTier.EPIC, new ReforgeStat("Fine").str(10.0).cc(8.0).cd(12.0));
        fine.put(ReforgeTier.LEGENDARY, new ReforgeStat("Fine").str(15.0).cc(10.0).cd(18.0));
        ranged.put("Fine", fine);
        HashMap<ReforgeTier, ReforgeStat> neat = new HashMap<ReforgeTier, ReforgeStat>();
        neat.put(ReforgeTier.COMMON, new ReforgeStat("Neat").cd(10.0));
        neat.put(ReforgeTier.UNCOMMON, new ReforgeStat("Neat").cd(14.0));
        neat.put(ReforgeTier.RARE, new ReforgeStat("Neat").cd(18.0));
        neat.put(ReforgeTier.EPIC, new ReforgeStat("Neat").cd(24.0));
        neat.put(ReforgeTier.LEGENDARY, new ReforgeStat("Neat").cd(30.0));
        ranged.put("Neat", neat);
        HashMap<ReforgeTier, ReforgeStat> hasty = new HashMap<ReforgeTier, ReforgeStat>();
        hasty.put(ReforgeTier.COMMON, new ReforgeStat("Hasty").str(3.0).cc(15.0));
        hasty.put(ReforgeTier.UNCOMMON, new ReforgeStat("Hasty").str(5.0).cc(20.0));
        hasty.put(ReforgeTier.RARE, new ReforgeStat("Hasty").str(7.0).cc(25.0));
        hasty.put(ReforgeTier.EPIC, new ReforgeStat("Hasty").str(10.0).cc(35.0));
        hasty.put(ReforgeTier.LEGENDARY, new ReforgeStat("Hasty").str(15.0).cc(50.0));
        ranged.put("Hasty", hasty);
        HashMap<ReforgeTier, ReforgeStat> grand = new HashMap<ReforgeTier, ReforgeStat>();
        grand.put(ReforgeTier.COMMON, new ReforgeStat("Grand").str(15.0));
        grand.put(ReforgeTier.UNCOMMON, new ReforgeStat("Grand").str(20.0));
        grand.put(ReforgeTier.RARE, new ReforgeStat("Grand").str(25.0));
        grand.put(ReforgeTier.EPIC, new ReforgeStat("Grand").str(40.0));
        grand.put(ReforgeTier.LEGENDARY, new ReforgeStat("Grand").str(50.0));
        ranged.put("Grand", grand);
        HashMap<ReforgeTier, ReforgeStat> deadly = new HashMap<ReforgeTier, ReforgeStat>();
        deadly.put(ReforgeTier.COMMON, new ReforgeStat("Deadly").cc(5.0).cd(10.0));
        deadly.put(ReforgeTier.UNCOMMON, new ReforgeStat("Deadly").cc(7.0).cd(15.0));
        deadly.put(ReforgeTier.RARE, new ReforgeStat("Deadly").cc(10.0).cd(20.0));
        deadly.put(ReforgeTier.EPIC, new ReforgeStat("Deadly").cc(14.0).cd(28.0));
        deadly.put(ReforgeTier.LEGENDARY, new ReforgeStat("Deadly").cc(18.0).cd(35.0));
        ranged.put("Deadly", deadly);
        REFORGE_MAP.put(ItemCategory.RANGED, ranged);
        LinkedHashMap armor = new LinkedHashMap();
        HashMap<ReforgeTier, ReforgeStat> fierce = new HashMap<ReforgeTier, ReforgeStat>();
        fierce.put(ReforgeTier.COMMON, new ReforgeStat("Fierce").str(3.0).cc(2.0).cd(5.0));
        fierce.put(ReforgeTier.UNCOMMON, new ReforgeStat("Fierce").str(4.0).cc(3.0).cd(8.0));
        fierce.put(ReforgeTier.RARE, new ReforgeStat("Fierce").str(6.0).cc(4.0).cd(10.0));
        fierce.put(ReforgeTier.EPIC, new ReforgeStat("Fierce").str(8.0).cc(7.0).cd(14.0));
        fierce.put(ReforgeTier.LEGENDARY, new ReforgeStat("Fierce").str(10.0).cc(10.0).cd(18.0));
        armor.put("Fierce", fierce);
        HashMap<ReforgeTier, ReforgeStat> titanic = new HashMap<ReforgeTier, ReforgeStat>();
        titanic.put(ReforgeTier.COMMON, new ReforgeStat("Titanic").hp(8.0).def(8.0));
        titanic.put(ReforgeTier.UNCOMMON, new ReforgeStat("Titanic").hp(11.0).def(11.0));
        titanic.put(ReforgeTier.RARE, new ReforgeStat("Titanic").hp(15.0).def(15.0));
        titanic.put(ReforgeTier.EPIC, new ReforgeStat("Titanic").hp(25.0).def(25.0));
        titanic.put(ReforgeTier.LEGENDARY, new ReforgeStat("Titanic").hp(40.0).def(40.0));
        armor.put("Titanic", titanic);
        HashMap<ReforgeTier, ReforgeStat> wise = new HashMap<ReforgeTier, ReforgeStat>();
        wise.put(ReforgeTier.COMMON, new ReforgeStat("Wise").hp(4.0).intel(25.0));
        wise.put(ReforgeTier.UNCOMMON, new ReforgeStat("Wise").hp(6.0).intel(38.0));
        wise.put(ReforgeTier.RARE, new ReforgeStat("Wise").hp(8.0).intel(50.0));
        wise.put(ReforgeTier.EPIC, new ReforgeStat("Wise").hp(12.0).intel(75.0));
        wise.put(ReforgeTier.LEGENDARY, new ReforgeStat("Wise").hp(15.0).intel(100.0));
        armor.put("Wise", wise);
        HashMap<ReforgeTier, ReforgeStat> pure = new HashMap<ReforgeTier, ReforgeStat>();
        pure.put(ReforgeTier.COMMON, new ReforgeStat("Pure").cd(4.0));
        pure.put(ReforgeTier.UNCOMMON, new ReforgeStat("Pure").cd(6.0));
        pure.put(ReforgeTier.RARE, new ReforgeStat("Pure").cd(8.0));
        pure.put(ReforgeTier.EPIC, new ReforgeStat("Pure").cd(12.0));
        pure.put(ReforgeTier.LEGENDARY, new ReforgeStat("Pure").cd(18.0));
        armor.put("Pure", pure);
        HashMap<ReforgeTier, ReforgeStat> perfect = new HashMap<ReforgeTier, ReforgeStat>();
        perfect.put(ReforgeTier.COMMON, new ReforgeStat("Perfect").str(2.0).def(4.0));
        perfect.put(ReforgeTier.UNCOMMON, new ReforgeStat("Perfect").str(4.0).def(6.0));
        perfect.put(ReforgeTier.RARE, new ReforgeStat("Perfect").str(5.0).def(8.0));
        perfect.put(ReforgeTier.EPIC, new ReforgeStat("Perfect").str(5.0).def(12.0));
        perfect.put(ReforgeTier.LEGENDARY, new ReforgeStat("Perfect").str(5.0).def(18.0));
        armor.put("Perfect", perfect);
        HashMap<ReforgeTier, ReforgeStat> clean = new HashMap<ReforgeTier, ReforgeStat>();
        clean.put(ReforgeTier.COMMON, new ReforgeStat("Clean").hp(5.0).spd(1.0));
        clean.put(ReforgeTier.UNCOMMON, new ReforgeStat("Clean").hp(8.0).spd(1.0));
        clean.put(ReforgeTier.RARE, new ReforgeStat("Clean").hp(10.0).spd(1.0));
        clean.put(ReforgeTier.EPIC, new ReforgeStat("Clean").hp(18.0).spd(2.0));
        clean.put(ReforgeTier.LEGENDARY, new ReforgeStat("Clean").hp(30.0).spd(3.0));
        armor.put("Clean", clean);
        HashMap<ReforgeTier, ReforgeStat> light = new HashMap<ReforgeTier, ReforgeStat>();
        light.put(ReforgeTier.COMMON, new ReforgeStat("Light").spd(1.0).atkSpd(1.0));
        light.put(ReforgeTier.UNCOMMON, new ReforgeStat("Light").spd(2.0).atkSpd(2.0));
        light.put(ReforgeTier.RARE, new ReforgeStat("Light").spd(2.0).atkSpd(2.0));
        light.put(ReforgeTier.EPIC, new ReforgeStat("Light").spd(3.0).atkSpd(3.0));
        light.put(ReforgeTier.LEGENDARY, new ReforgeStat("Light").spd(5.0).atkSpd(5.0));
        armor.put("Light", light);
        REFORGE_MAP.put(ItemCategory.ARMOR, armor);
        LinkedHashMap tools = new LinkedHashMap();
        HashMap<ReforgeTier, ReforgeStat> refined = new HashMap<ReforgeTier, ReforgeStat>();
        refined.put(ReforgeTier.COMMON, new ReforgeStat("Refined").def(2.0).intel(2.0));
        refined.put(ReforgeTier.UNCOMMON, new ReforgeStat("Refined").def(4.0).intel(4.0));
        refined.put(ReforgeTier.RARE, new ReforgeStat("Refined").def(5.0).intel(5.0));
        refined.put(ReforgeTier.EPIC, new ReforgeStat("Refined").def(8.0).intel(10.0));
        refined.put(ReforgeTier.LEGENDARY, new ReforgeStat("Refined").def(12.0).intel(15.0));
        tools.put("Refined", refined);
        REFORGE_MAP.put(ItemCategory.TOOLS, tools);
        LinkedHashMap exArmor = new LinkedHashMap();
        HashMap<ReforgeTier, ReforgeStat> ancient = new HashMap<ReforgeTier, ReforgeStat>();
        ancient.put(ReforgeTier.COMMON, new ReforgeStat("Ancient").str(4.0).cd(3.0).def(5.0));
        ancient.put(ReforgeTier.UNCOMMON, new ReforgeStat("Ancient").str(6.0).cd(4.0).def(8.0));
        ancient.put(ReforgeTier.RARE, new ReforgeStat("Ancient").str(8.0).cd(6.0).def(10.0));
        ancient.put(ReforgeTier.EPIC, new ReforgeStat("Ancient").str(12.0).cd(9.0).def(15.0));
        ancient.put(ReforgeTier.LEGENDARY, new ReforgeStat("Ancient").str(18.0).cd(12.0).def(25.0));
        ancient.put(ReforgeTier.MYTHIC, new ReforgeStat("Ancient").str(25.0).cd(18.0).def(35.0));
        exArmor.put("Ancient", ancient);
        HashMap<ReforgeTier, ReforgeStat> necrotic = new HashMap<ReforgeTier, ReforgeStat>();
        necrotic.put(ReforgeTier.COMMON, new ReforgeStat("Necrotic").intel(55.0));
        necrotic.put(ReforgeTier.UNCOMMON, new ReforgeStat("Necrotic").intel(82.0));
        necrotic.put(ReforgeTier.RARE, new ReforgeStat("Necrotic").intel(110.0));
        necrotic.put(ReforgeTier.EPIC, new ReforgeStat("Necrotic").intel(150.0));
        necrotic.put(ReforgeTier.LEGENDARY, new ReforgeStat("Necrotic").intel(200.0));
        necrotic.put(ReforgeTier.MYTHIC, new ReforgeStat("Necrotic").intel(250.0));
        exArmor.put("Necrotic", necrotic);
        HashMap<ReforgeTier, ReforgeStat> giant = new HashMap<ReforgeTier, ReforgeStat>();
        giant.put(ReforgeTier.COMMON, new ReforgeStat("Giant").hp(25.0));
        giant.put(ReforgeTier.UNCOMMON, new ReforgeStat("Giant").hp(38.0));
        giant.put(ReforgeTier.RARE, new ReforgeStat("Giant").hp(50.0));
        giant.put(ReforgeTier.EPIC, new ReforgeStat("Giant").hp(90.0));
        giant.put(ReforgeTier.LEGENDARY, new ReforgeStat("Giant").hp(140.0));
        giant.put(ReforgeTier.MYTHIC, new ReforgeStat("Giant").hp(200.0));
        exArmor.put("Giant", giant);
        EXCLUSIVE_MAP.put(ItemCategory.ARMOR, exArmor);
        LinkedHashMap exRanged = new LinkedHashMap();
        HashMap<ReforgeTier, ReforgeStat> precise = new HashMap<ReforgeTier, ReforgeStat>();
        precise.put(ReforgeTier.COMMON, new ReforgeStat("Precise").str(5.0).cc(4.0).cd(10.0));
        precise.put(ReforgeTier.UNCOMMON, new ReforgeStat("Precise").str(8.0).cc(6.0).cd(15.0));
        precise.put(ReforgeTier.RARE, new ReforgeStat("Precise").str(12.0).cc(8.0).cd(20.0));
        precise.put(ReforgeTier.EPIC, new ReforgeStat("Precise").str(18.0).cc(10.0).cd(28.0));
        precise.put(ReforgeTier.LEGENDARY, new ReforgeStat("Precise").str(25.0).cc(12.0).cd(35.0));
        precise.put(ReforgeTier.MYTHIC, new ReforgeStat("Precise").str(35.0).cc(15.0).cd(45.0));
        exRanged.put("Precise", precise);
        HashMap<ReforgeTier, ReforgeStat> spiritual = new HashMap<ReforgeTier, ReforgeStat>();
        spiritual.put(ReforgeTier.COMMON, new ReforgeStat("Spiritual").str(8.0).cc(4.0).cd(12.0));
        spiritual.put(ReforgeTier.UNCOMMON, new ReforgeStat("Spiritual").str(12.0).cc(6.0).cd(18.0));
        spiritual.put(ReforgeTier.RARE, new ReforgeStat("Spiritual").str(18.0).cc(8.0).cd(24.0));
        spiritual.put(ReforgeTier.EPIC, new ReforgeStat("Spiritual").str(25.0).cc(10.0).cd(32.0));
        spiritual.put(ReforgeTier.LEGENDARY, new ReforgeStat("Spiritual").str(32.0).cc(12.0).cd(40.0));
        spiritual.put(ReforgeTier.MYTHIC, new ReforgeStat("Spiritual").str(40.0).cc(15.0).cd(50.0));
        exRanged.put("Spiritual", spiritual);
        HashMap<ReforgeTier, ReforgeStat> headstrong = new HashMap<ReforgeTier, ReforgeStat>();
        headstrong.put(ReforgeTier.COMMON, new ReforgeStat("Headstrong").str(15.0).cd(15.0));
        headstrong.put(ReforgeTier.UNCOMMON, new ReforgeStat("Headstrong").str(20.0).cd(20.0));
        headstrong.put(ReforgeTier.RARE, new ReforgeStat("Headstrong").str(25.0).cd(25.0));
        headstrong.put(ReforgeTier.EPIC, new ReforgeStat("Headstrong").str(35.0).cd(35.0));
        headstrong.put(ReforgeTier.LEGENDARY, new ReforgeStat("Headstrong").str(45.0).cd(45.0));
        headstrong.put(ReforgeTier.MYTHIC, new ReforgeStat("Headstrong").str(55.0).cd(55.0));
        exRanged.put("Headstrong", headstrong);
        EXCLUSIVE_MAP.put(ItemCategory.RANGED, exRanged);
        LinkedHashMap exMelee = new LinkedHashMap();
        HashMap<ReforgeTier, ReforgeStat> fabled = new HashMap<ReforgeTier, ReforgeStat>();
        fabled.put(ReforgeTier.COMMON, new ReforgeStat("Fabled").str(15.0).cd(8.0));
        fabled.put(ReforgeTier.UNCOMMON, new ReforgeStat("Fabled").str(22.0).cd(11.0));
        fabled.put(ReforgeTier.RARE, new ReforgeStat("Fabled").str(30.0).cd(15.0));
        fabled.put(ReforgeTier.EPIC, new ReforgeStat("Fabled").str(40.0).cd(20.0));
        fabled.put(ReforgeTier.LEGENDARY, new ReforgeStat("Fabled").str(50.0).cd(25.0));
        fabled.put(ReforgeTier.MYTHIC, new ReforgeStat("Fabled").str(65.0).cd(35.0));
        exMelee.put("Fabled", fabled);
        HashMap<ReforgeTier, ReforgeStat> withered = new HashMap<ReforgeTier, ReforgeStat>();
        withered.put(ReforgeTier.COMMON, new ReforgeStat("Withered").str(8.0).lvlMult(0.4));
        withered.put(ReforgeTier.UNCOMMON, new ReforgeStat("Withered").str(11.0).lvlMult(0.6));
        withered.put(ReforgeTier.RARE, new ReforgeStat("Withered").str(15.0).lvlMult(0.8));
        withered.put(ReforgeTier.EPIC, new ReforgeStat("Withered").str(20.0).lvlMult(1.0));
        withered.put(ReforgeTier.LEGENDARY, new ReforgeStat("Withered").str(25.0).lvlMult(1.2));
        withered.put(ReforgeTier.MYTHIC, new ReforgeStat("Withered").str(35.0).lvlMult(1.5));
        EXCLUSIVE_MAP.put(ItemCategory.MELEE, exMelee);
        HashMap<ReforgeTier, ReforgeStat> reinforced = new HashMap<ReforgeTier, ReforgeStat>();
        reinforced.put(ReforgeTier.COMMON, new ReforgeStat("Reinforced").hp(15.0).def(10.0));
        reinforced.put(ReforgeTier.UNCOMMON, new ReforgeStat("Reinforced").hp(22.0).def(15.0));
        reinforced.put(ReforgeTier.RARE, new ReforgeStat("Reinforced").hp(30.0).def(20.0));
        reinforced.put(ReforgeTier.EPIC, new ReforgeStat("Reinforced").hp(50.0).def(30.0));
        reinforced.put(ReforgeTier.LEGENDARY, new ReforgeStat("Reinforced").hp(70.0).def(45.0));
        reinforced.put(ReforgeTier.MYTHIC, new ReforgeStat("Reinforced").hp(90.0).def(60.0));
        exArmor.put("Reinforced", reinforced);
        HashMap<ReforgeTier, ReforgeStat> magnetic = new HashMap<ReforgeTier, ReforgeStat>();
        magnetic.put(ReforgeTier.COMMON, new ReforgeStat("Magnetic").cd(10.0));
        magnetic.put(ReforgeTier.UNCOMMON, new ReforgeStat("Magnetic").cd(15.0));
        magnetic.put(ReforgeTier.RARE, new ReforgeStat("Magnetic").cd(20.0));
        magnetic.put(ReforgeTier.EPIC, new ReforgeStat("Magnetic").cd(30.0));
        magnetic.put(ReforgeTier.LEGENDARY, new ReforgeStat("Magnetic").cd(45.0));
        magnetic.put(ReforgeTier.MYTHIC, new ReforgeStat("Magnetic").cd(60.0));
        HashMap<ReforgeTier, ReforgeStat> demonic = new HashMap<ReforgeTier, ReforgeStat>();
        demonic.put(ReforgeTier.COMMON, new ReforgeStat("Demonic").str(20.0).cd(15.0));
        demonic.put(ReforgeTier.UNCOMMON, new ReforgeStat("Demonic").str(30.0).cd(22.0));
        demonic.put(ReforgeTier.RARE, new ReforgeStat("Demonic").str(40.0).cd(30.0));
        demonic.put(ReforgeTier.EPIC, new ReforgeStat("Demonic").str(60.0).cd(45.0));
        demonic.put(ReforgeTier.LEGENDARY, new ReforgeStat("Demonic").str(90.0).cd(60.0));
        demonic.put(ReforgeTier.MYTHIC, new ReforgeStat("Demonic").str(120.0).cd(80.0));
        exMelee.put("Demonic", demonic);
        HashMap<ReforgeTier, ReforgeStat> aegis = new HashMap<ReforgeTier, ReforgeStat>();
        aegis.put(ReforgeTier.COMMON, new ReforgeStat("Aegis").hp(50.0).def(40.0));
        aegis.put(ReforgeTier.UNCOMMON, new ReforgeStat("Aegis").hp(75.0).def(60.0));
        aegis.put(ReforgeTier.RARE, new ReforgeStat("Aegis").hp(100.0).def(80.0));
        aegis.put(ReforgeTier.EPIC, new ReforgeStat("Aegis").hp(150.0).def(120.0));
        aegis.put(ReforgeTier.LEGENDARY, new ReforgeStat("Aegis").hp(220.0).def(180.0));
        aegis.put(ReforgeTier.MYTHIC, new ReforgeStat("Aegis").hp(300.0).def(250.0));
        exArmor.put("Aegis", aegis);
        HashMap<ReforgeTier, ReforgeStat> cosmic = new HashMap<ReforgeTier, ReforgeStat>();
        cosmic.put(ReforgeTier.MYTHIC, new ReforgeStat("Cosmic").str(150.0).cd(100.0).baseMult(1.2));
        exMelee.put("Cosmic", cosmic);
        exRanged.put("Cosmic", cosmic);
        HashMap<ReforgeTier, ReforgeStat> divine = new HashMap<ReforgeTier, ReforgeStat>();
        divine.put(ReforgeTier.MYTHIC, new ReforgeStat("Divine").hp(100.0).intel(600.0).baseMult(1.2));
        exArmor.put("Divine", divine);

        // --- SPEAR EXCLUSIVE MYTHIC REFORGES ---
        HashMap<ReforgeTier, ReforgeStat> dragoon = new HashMap<ReforgeTier, ReforgeStat>();
        dragoon.put(ReforgeTier.MYTHIC, new ReforgeStat("Dragoon").str(90.0).cd(120.0).spd(20.0).baseMult(1.15));
        exMelee.put("Dragoon", dragoon);

        HashMap<ReforgeTier, ReforgeStat> valkyrie = new HashMap<ReforgeTier, ReforgeStat>();
        valkyrie.put(ReforgeTier.MYTHIC, new ReforgeStat("Valkyrie").str(60.0).cc(25.0).atkSpd(50.0).baseMult(1.15));
        exMelee.put("Valkyrie", valkyrie);

        // --- NEW EXCLUSIVE REFORGES (2ND OPTIONS) ---
        // 1. Vicious (Melee - RARE)
        HashMap<ReforgeTier, ReforgeStat> vicious = new HashMap<ReforgeTier, ReforgeStat>();
        vicious.put(ReforgeTier.COMMON, new ReforgeStat("Vicious").str(10.0).cc(1.0));
        vicious.put(ReforgeTier.UNCOMMON, new ReforgeStat("Vicious").str(15.0).cc(2.0));
        vicious.put(ReforgeTier.RARE, new ReforgeStat("Vicious").str(20.0).cc(3.0));
        vicious.put(ReforgeTier.EPIC, new ReforgeStat("Vicious").str(30.0).cc(4.0));
        vicious.put(ReforgeTier.LEGENDARY, new ReforgeStat("Vicious").str(40.0).cc(5.0));
        vicious.put(ReforgeTier.MYTHIC, new ReforgeStat("Vicious").str(50.0).cc(8.0));
        exMelee.put("Vicious", vicious);

        // 2. Slayer (Melee - EPIC)
        HashMap<ReforgeTier, ReforgeStat> slayer = new HashMap<ReforgeTier, ReforgeStat>();
        slayer.put(ReforgeTier.COMMON, new ReforgeStat("Slayer").str(12.0).cd(8.0));
        slayer.put(ReforgeTier.UNCOMMON, new ReforgeStat("Slayer").str(18.0).cd(12.0));
        slayer.put(ReforgeTier.RARE, new ReforgeStat("Slayer").str(25.0).cd(18.0));
        slayer.put(ReforgeTier.EPIC, new ReforgeStat("Slayer").str(35.0).cd(25.0));
        slayer.put(ReforgeTier.LEGENDARY, new ReforgeStat("Slayer").str(45.0).cd(30.0));
        slayer.put(ReforgeTier.MYTHIC, new ReforgeStat("Slayer").str(60.0).cd(40.0));
        exMelee.put("Slayer", slayer);

        // 3. Shadow (Melee - LEGENDARY)
        HashMap<ReforgeTier, ReforgeStat> shadow = new HashMap<ReforgeTier, ReforgeStat>();
        shadow.put(ReforgeTier.COMMON, new ReforgeStat("Shadow").str(20.0).spd(2.0));
        shadow.put(ReforgeTier.UNCOMMON, new ReforgeStat("Shadow").str(30.0).spd(4.0));
        shadow.put(ReforgeTier.RARE, new ReforgeStat("Shadow").str(40.0).spd(6.0));
        shadow.put(ReforgeTier.EPIC, new ReforgeStat("Shadow").str(55.0).spd(8.0));
        shadow.put(ReforgeTier.LEGENDARY, new ReforgeStat("Shadow").str(70.0).spd(10.0));
        shadow.put(ReforgeTier.MYTHIC, new ReforgeStat("Shadow").str(90.0).spd(15.0));
        exMelee.put("Shadow", shadow);

        // 4. Galactic (Melee & Ranged - MYTHIC)
        HashMap<ReforgeTier, ReforgeStat> galactic = new HashMap<ReforgeTier, ReforgeStat>();
        galactic.put(ReforgeTier.MYTHIC, new ReforgeStat("Galactic").str(120.0).cc(20.0).cd(80.0).baseMult(1.15));
        exMelee.put("Galactic", galactic);
        exRanged.put("Galactic", galactic);

        // 5. Deadeye (Ranged - RARE)
        HashMap<ReforgeTier, ReforgeStat> deadeye = new HashMap<ReforgeTier, ReforgeStat>();
        deadeye.put(ReforgeTier.COMMON, new ReforgeStat("Deadeye").str(8.0).cc(2.0));
        deadeye.put(ReforgeTier.UNCOMMON, new ReforgeStat("Deadeye").str(12.0).cc(4.0));
        deadeye.put(ReforgeTier.RARE, new ReforgeStat("Deadeye").str(18.0).cc(5.0));
        deadeye.put(ReforgeTier.EPIC, new ReforgeStat("Deadeye").str(25.0).cc(6.0));
        deadeye.put(ReforgeTier.LEGENDARY, new ReforgeStat("Deadeye").str(35.0).cc(8.0));
        deadeye.put(ReforgeTier.MYTHIC, new ReforgeStat("Deadeye").str(45.0).cc(10.0));
        exRanged.put("Deadeye", deadeye);

        // 6. Sniper (Ranged - EPIC)
        HashMap<ReforgeTier, ReforgeStat> sniper = new HashMap<ReforgeTier, ReforgeStat>();
        sniper.put(ReforgeTier.COMMON, new ReforgeStat("Sniper").str(10.0).cd(8.0));
        sniper.put(ReforgeTier.UNCOMMON, new ReforgeStat("Sniper").str(15.0).cd(12.0));
        sniper.put(ReforgeTier.RARE, new ReforgeStat("Sniper").str(22.0).cd(18.0));
        sniper.put(ReforgeTier.EPIC, new ReforgeStat("Sniper").str(30.0).cd(24.0));
        sniper.put(ReforgeTier.LEGENDARY, new ReforgeStat("Sniper").str(40.0).cd(30.0));
        sniper.put(ReforgeTier.MYTHIC, new ReforgeStat("Sniper").str(50.0).cd(40.0));
        exRanged.put("Sniper", sniper);

        // 7. Eagle (Ranged - LEGENDARY)
        HashMap<ReforgeTier, ReforgeStat> eagle = new HashMap<ReforgeTier, ReforgeStat>();
        eagle.put(ReforgeTier.COMMON, new ReforgeStat("Eagle").str(15.0).cc(2.0).cd(10.0));
        eagle.put(ReforgeTier.UNCOMMON, new ReforgeStat("Eagle").str(22.0).cc(4.0).cd(18.0));
        eagle.put(ReforgeTier.RARE, new ReforgeStat("Eagle").str(30.0).cc(6.0).cd(25.0));
        eagle.put(ReforgeTier.EPIC, new ReforgeStat("Eagle").str(40.0).cc(8.0).cd(32.0));
        eagle.put(ReforgeTier.LEGENDARY, new ReforgeStat("Eagle").str(50.0).cc(10.0).cd(40.0));
        eagle.put(ReforgeTier.MYTHIC, new ReforgeStat("Eagle").str(65.0).cc(12.0).cd(50.0));
        exRanged.put("Eagle", eagle);

        // 8. Sturdy (Armor - RARE)
        HashMap<ReforgeTier, ReforgeStat> sturdy = new HashMap<ReforgeTier, ReforgeStat>();
        sturdy.put(ReforgeTier.COMMON, new ReforgeStat("Sturdy").hp(20.0).def(15.0));
        sturdy.put(ReforgeTier.UNCOMMON, new ReforgeStat("Sturdy").hp(30.0).def(22.0));
        sturdy.put(ReforgeTier.RARE, new ReforgeStat("Sturdy").hp(45.0).def(32.0));
        sturdy.put(ReforgeTier.EPIC, new ReforgeStat("Sturdy").hp(60.0).def(45.0));
        sturdy.put(ReforgeTier.LEGENDARY, new ReforgeStat("Sturdy").hp(80.0).def(60.0));
        sturdy.put(ReforgeTier.MYTHIC, new ReforgeStat("Sturdy").hp(100.0).def(80.0));
        exArmor.put("Sturdy", sturdy);

        // 9. Celestial (Armor - MYTHIC)
        HashMap<ReforgeTier, ReforgeStat> celestial = new HashMap<ReforgeTier, ReforgeStat>();
        celestial.put(ReforgeTier.MYTHIC, new ReforgeStat("Celestial").hp(600.0).def(400.0).baseMult(1.15));
        exArmor.put("Celestial", celestial);

        // --- NEW ARMOR REFORGES ---
        HashMap<ReforgeTier, ReforgeStat> infernal = new HashMap<ReforgeTier, ReforgeStat>();
        infernal.put(ReforgeTier.COMMON, new ReforgeStat("Infernal").str(50.0).cd(20.0).spd(2.0));
        infernal.put(ReforgeTier.UNCOMMON, new ReforgeStat("Infernal").str(75.0).cd(30.0).spd(4.0));
        infernal.put(ReforgeTier.RARE, new ReforgeStat("Infernal").str(100.0).cd(40.0).spd(6.0));
        infernal.put(ReforgeTier.EPIC, new ReforgeStat("Infernal").str(150.0).cd(60.0).spd(8.0));
        infernal.put(ReforgeTier.LEGENDARY, new ReforgeStat("Infernal").str(200.0).cd(80.0).spd(10.0));
        infernal.put(ReforgeTier.MYTHIC, new ReforgeStat("Infernal").str(250.0).cd(100.0).spd(15.0).baseMult(1.2));
        exArmor.put("Infernal", infernal);

        HashMap<ReforgeTier, ReforgeStat> warlord = new HashMap<ReforgeTier, ReforgeStat>();
        warlord.put(ReforgeTier.COMMON, new ReforgeStat("Warlord").str(15.0).cd(8.0));
        warlord.put(ReforgeTier.UNCOMMON, new ReforgeStat("Warlord").str(25.0).cd(12.0));
        warlord.put(ReforgeTier.RARE, new ReforgeStat("Warlord").str(40.0).cd(20.0));
        warlord.put(ReforgeTier.EPIC, new ReforgeStat("Warlord").str(60.0).cd(30.0));
        warlord.put(ReforgeTier.LEGENDARY, new ReforgeStat("Warlord").str(80.0).cd(40.0));
        warlord.put(ReforgeTier.MYTHIC, new ReforgeStat("Warlord").str(110.0).cd(55.0));
        exArmor.put("Warlord", warlord);

        HashMap<ReforgeTier, ReforgeStat> enlightened = new HashMap<ReforgeTier, ReforgeStat>();
        enlightened.put(ReforgeTier.COMMON, new ReforgeStat("Enlightened").intel(50.0).hp(10.0));
        enlightened.put(ReforgeTier.UNCOMMON, new ReforgeStat("Enlightened").intel(100.0).hp(20.0));
        enlightened.put(ReforgeTier.RARE, new ReforgeStat("Enlightened").intel(150.0).hp(30.0));
        enlightened.put(ReforgeTier.EPIC, new ReforgeStat("Enlightened").intel(220.0).hp(40.0));
        enlightened.put(ReforgeTier.LEGENDARY, new ReforgeStat("Enlightened").intel(300.0).hp(50.0));
        enlightened.put(ReforgeTier.MYTHIC, new ReforgeStat("Enlightened").intel(400.0).hp(80.0));
        exArmor.put("Enlightened", enlightened);

        HashMap<ReforgeTier, ReforgeStat> savage = new HashMap<ReforgeTier, ReforgeStat>();
        savage.put(ReforgeTier.COMMON, new ReforgeStat("Savage").str(10.0).cd(5.0));
        savage.put(ReforgeTier.UNCOMMON, new ReforgeStat("Savage").str(18.0).cd(10.0));
        savage.put(ReforgeTier.RARE, new ReforgeStat("Savage").str(30.0).cd(15.0));
        savage.put(ReforgeTier.EPIC, new ReforgeStat("Savage").str(45.0).cd(25.0));
        savage.put(ReforgeTier.LEGENDARY, new ReforgeStat("Savage").str(65.0).cd(35.0));
        savage.put(ReforgeTier.MYTHIC, new ReforgeStat("Savage").str(90.0).cd(50.0));
        exArmor.put("Savage", savage);

        HashMap<ReforgeTier, ReforgeStat> arcane = new HashMap<ReforgeTier, ReforgeStat>();
        arcane.put(ReforgeTier.COMMON, new ReforgeStat("Arcane").intel(30.0));
        arcane.put(ReforgeTier.UNCOMMON, new ReforgeStat("Arcane").intel(60.0));
        arcane.put(ReforgeTier.RARE, new ReforgeStat("Arcane").intel(100.0));
        arcane.put(ReforgeTier.EPIC, new ReforgeStat("Arcane").intel(150.0));
        arcane.put(ReforgeTier.LEGENDARY, new ReforgeStat("Arcane").intel(210.0));
        arcane.put(ReforgeTier.MYTHIC, new ReforgeStat("Arcane").intel(280.0));
        exArmor.put("Arcane", arcane);

        HashMap<ReforgeTier, ReforgeStat> swift = new HashMap<ReforgeTier, ReforgeStat>();
        swift.put(ReforgeTier.COMMON, new ReforgeStat("Swift").spd(5.0));
        swift.put(ReforgeTier.UNCOMMON, new ReforgeStat("Swift").spd(10.0));
        swift.put(ReforgeTier.RARE, new ReforgeStat("Swift").spd(15.0));
        swift.put(ReforgeTier.EPIC, new ReforgeStat("Swift").spd(25.0));
        swift.put(ReforgeTier.LEGENDARY, new ReforgeStat("Swift").spd(35.0));
        swift.put(ReforgeTier.MYTHIC, new ReforgeStat("Swift").spd(50.0));
        exArmor.put("Swift", swift);
    }

    public static class ReforgeStat {
        public String name;
        public double strength = 0.0;
        public double critChance = 0.0;
        public double critDamage = 0.0;
        public double speed = 0.0;
        public double intelligence = 0.0;
        public double health = 0.0;
        public double defense = 0.0;
        public double attackSpeed = 0.0;
        public double levelMultiplier = 0.0;
        public double baseStatMultiplier = 1.0;

        public ReforgeStat(String name) {
            this.name = name;
        }

        public ReforgeStat str(double v) {
            this.strength = v;
            return this;
        }

        public ReforgeStat cc(double v) {
            this.critChance = v;
            return this;
        }

        public ReforgeStat cd(double v) {
            this.critDamage = v;
            return this;
        }

        public ReforgeStat spd(double v) {
            this.speed = v;
            return this;
        }

        public ReforgeStat intel(double v) {
            this.intelligence = v;
            return this;
        }

        public ReforgeStat hp(double v) {
            this.health = v;
            return this;
        }

        public ReforgeStat def(double v) {
            this.defense = v;
            return this;
        }

        public ReforgeStat atkSpd(double v) {
            this.attackSpeed = v;
            return this;
        }

        public ReforgeStat lvlMult(double v) {
            this.levelMultiplier = v;
            return this;
        }

        public ReforgeStat baseMult(double v) {
            this.baseStatMultiplier = v;
            return this;
        }
    }

    public static enum ItemCategory {
        MELEE,
        RANGED,
        ARMOR,
        TOOLS;

    }

    public static enum ReforgeTier {
        COMMON,
        UNCOMMON,
        RARE,
        EPIC,
        LEGENDARY,
        MYTHIC,
        NONE;

    }
}

