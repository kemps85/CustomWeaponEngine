/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.aurelium.auraskills.api.AuraSkillsApi
 *  dev.aurelium.auraskills.api.stat.Stat
 *  dev.aurelium.auraskills.api.stat.StatModifier
 *  dev.aurelium.auraskills.api.stat.Stats
 *  dev.aurelium.auraskills.api.user.SkillsUser
 *  dev.aurelium.auraskills.api.util.AuraSkillsModifier$Operation
 *  org.bukkit.Bukkit
 *  org.bukkit.Color
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.NamespacedKey
 *  org.bukkit.Particle
 *  org.bukkit.Particle$DustOptions
 *  org.bukkit.Sound
 *  org.bukkit.attribute.Attribute
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.block.Action
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.event.inventory.InventoryClickEvent
 *  org.bukkit.event.inventory.InventoryCloseEvent
 *  org.bukkit.event.player.PlayerInteractEvent
 *  org.bukkit.inventory.EquipmentSlot
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.persistence.PersistentDataContainer
 *  org.bukkit.persistence.PersistentDataType
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.java.JavaPlugin
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 *  org.bukkit.scheduler.BukkitRunnable
 *  org.bukkit.util.Vector
 */
package org.example.skillbook;

import dev.aurelium.auraskills.api.AuraSkillsApi;
import dev.aurelium.auraskills.api.stat.Stat;
import dev.aurelium.auraskills.api.stat.StatModifier;
import dev.aurelium.auraskills.api.stat.Stats;
import dev.aurelium.auraskills.api.user.SkillsUser;
import dev.aurelium.auraskills.api.util.AuraSkillsModifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.example.core.CustomWeaponEngine;
import org.example.stats.ItemBuilder;
import org.example.stats.ItemStatsGUI;
import org.example.weapon.legendary.ManaHelper;

public class SkillBookSystem
implements Listener,
CommandExecutor {
    private final JavaPlugin plugin;
    private static final String INDEX_TITLE = "\u00a76\u2726 Skill Book Menu";
    private static final String SET_BONUS_TITLE = "\u00a7b\u2726 Apply Set Bonus Book";
    private static final String ABILITY_TITLE = "\u00a7c\u2726 Apply Item Ability Book";
    private final Map<UUID, Map<String, Long>> abilityCooldowns = new HashMap<UUID, Map<String, Long>>();
    private final Map<UUID, Long> setBonusCooldowns = new HashMap<UUID, Long>();
    public static final Map<String, SkillBook> BOOKS = new LinkedHashMap<String, SkillBook>();

    public SkillBookSystem(JavaPlugin plugin) {
        this.plugin = plugin;
        new BukkitRunnable(){

            public void run() {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    SkillBookSystem.this.triggerPeriodicSetBonuses(p);
                }
            }
        }.runTaskTimer((Plugin)plugin, 100L, 100L);

        org.bukkit.configuration.file.FileConfiguration lib = ((org.example.core.CustomWeaponEngine) plugin).getLibraryConfig();
        for (java.util.Map.Entry<String, SkillBook> entry : BOOKS.entrySet()) {
            String libKey = "items.skillbook_" + entry.getKey();
            lib.set(libKey, createSkillBookItem(entry.getValue()));
        }
        ((org.example.core.CustomWeaponEngine) plugin).saveLibraryConfig();
    }

    public static ItemStack createSkillBookItem(SkillBook book) {
        ItemStack item = new ItemStack(Material.ENCHANTED_BOOK);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(book.rarity.color + "\u00a7l" + book.name + " Skill Book");
            ArrayList<String> lore = new ArrayList<>();
            lore.add("\u00a77Lo\u1ea1i s\u00e1ch: " + (book.type == SkillType.SET_BONUS ? "\u00a7bSet Gi\u00e1p (Full Set Bonus)" : "\u00a7cV\u0169 kh\u00ed (Item Ability)"));
            if (book.type == SkillType.ABILITY) {
                lore.add("\u00a77V\u0169 kh\u00ed \u00e1p d\u1ee5ng: \u00a7f" + book.targetType);
                lore.add("\u00a77C\u00e1ch k\u00edch ho\u1ea1t: \u00a7e" + book.clickType);
                if (book.manaCost > 0.0) {
                    lore.add("\u00a77T\u1ed1n Mana: \u00a73" + (int)book.manaCost);
                }
                if (book.cooldown > 0) {
                    lore.add("\u00a77Th\u1eddi gian h\u1ed3i: \u00a7a" + book.cooldown + "s");
                }
            }
            lore.add("\u00a7f");
            lore.add("\u00a76\u2726 Hi\u1ec7u qu\u1ea3 k\u1ef9 n\u0103ng:");
            lore.add("  \u00a7f" + book.desc1);
            if (!book.desc2.isEmpty()) {
                lore.add("  \u00a7f" + book.desc2);
            }
            lore.add("\u00a7f");
            lore.add(book.rarity.color + "\u00a7l" + book.rarity.name() + " SKILL BOOK");
            meta.setLore(lore);
            PersistentDataContainer pdc = meta.getPersistentDataContainer();
            pdc.set(new NamespacedKey((org.bukkit.plugin.Plugin)CustomWeaponEngine.getInstance(), "cwe_skill_book"), PersistentDataType.INTEGER, 1);
            pdc.set(new NamespacedKey((org.bukkit.plugin.Plugin)CustomWeaponEngine.getInstance(), "cwe_book_type"), PersistentDataType.STRING, book.type.name());
            pdc.set(new NamespacedKey((org.bukkit.plugin.Plugin)CustomWeaponEngine.getInstance(), "cwe_skill_id"), PersistentDataType.STRING, book.id);
            pdc.set(new NamespacedKey((org.bukkit.plugin.Plugin)CustomWeaponEngine.getInstance(), "cwe_id"), PersistentDataType.STRING, "skillbook_" + book.id);
            item.setItemMeta(meta);
        }
        return item;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }
        Player player = (Player)sender;
        if (args.length > 0 && args[0].equalsIgnoreCase("get")) {
            if (!player.hasPermission("cwe.admin")) {
                player.sendMessage("\u00a7cB\u1ea1n kh\u00f4ng c\u00f3 quy\u1ec1n d\u00f9ng l\u1ec7nh n\u00e0y!");
                return true;
            }
            if (args.length < 2) {
                player.sendMessage("\u00a7cS\u1eed d\u1ee5ng: /skillbook get <id_s\u00e1ch>");
                return true;
            }
            String id = args[1].toLowerCase();
            if (!BOOKS.containsKey(id)) {
                player.sendMessage("\u00a7cKh\u00f4ng t\u00ecm th\u1ea5y s\u00e1ch k\u0129 n\u0103ng n\u00e0y!");
                return true;
            }
            player.getInventory().addItem(new ItemStack[]{SkillBookSystem.createSkillBookItem(BOOKS.get(id))});
            player.sendMessage("\u00a7a\u0110\u00e3 nh\u1eadn s\u00e1ch k\u0129 n\u0103ng th\u00e0nh c\u00f4ng!");
            return true;
        }
        if (args.length > 0 && args[0].equalsIgnoreCase("list")) {
            if (!player.hasPermission("cwe.admin")) {
                player.sendMessage("\u00a7cB\u1ea1n kh\u00f4ng c\u00f3 quy\u1ec1n!");
                return true;
            }
            player.sendMessage("\u00a7e=== DANH S\u00c1CH S\u00c1CH K\u0128 N\u0102NG ===");
            for (String key : BOOKS.keySet()) {
                SkillBook b = BOOKS.get(key);
                player.sendMessage("\u00a7a- " + key + " \u00a77(" + b.rarity.name() + " - " + b.type.name() + ")");
            }
            return true;
        }
        this.openIndexMenu(player);
        return true;
    }

    private void openIndexMenu(Player player) {
        Inventory gui = Bukkit.createInventory(null, (int)27, (String)INDEX_TITLE);
        ItemStack glass = this.createDecorativeGlass(Material.GRAY_STAINED_GLASS_PANE, "\u00a78 ", new String[0]);
        for (int i = 0; i < 27; ++i) {
            gui.setItem(i, glass);
        }
        ItemStack setBonusBtn = this.createDecorativeGlass(Material.GOLDEN_CHESTPLATE, "\u00a7b\u00a7l\ud83d\udee1\ufe0f \u00c9P S\u00c1CH SET GI\u00c1P", "\u00a77\u00c1p d\u1ee5ng Full Set Bonus cho 4 m\u00f3n Gi\u00e1p.", "\u00a77Chi ph\u00ed: \u00a7eMi\u1ec5n ph\u00ed nguy\u00ean li\u1ec7u.", "", "\u00a7eClick \u0111\u1ec3 m\u1edf!");
        gui.setItem(11, setBonusBtn);
        ItemStack abilityBtn = this.createDecorativeGlass(Material.DIAMOND_SWORD, "\u00a7c\u00a7l\ud83d\udde1\ufe0f \u00c9P S\u00c1CH K\u1ef8 N\u0102NG V\u0168 KH\u00cd", "\u00a77\u00c1p d\u1ee5ng Active Ability cho v\u0169 kh\u00ed ph\u00f9 h\u1ee3p.", "\u00a77Chi ph\u00ed: \u00a7eMi\u1ec5n ph\u00ed nguy\u00ean li\u1ec7u.", "", "\u00a7eClick \u0111\u1ec3 m\u1edf!");
        gui.setItem(15, abilityBtn);
        gui.setItem(22, this.createDecorativeGlass(Material.BARRIER, "\u00a7c\u0110\u00f3ng Giao Di\u1ec7n", new String[0]));
        player.openInventory(gui);
    }

    private void openSetBonusGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, (int)54, (String)SET_BONUS_TITLE);
        ItemStack grayGlass = this.createDecorativeGlass(Material.GRAY_STAINED_GLASS_PANE, "\u00a77 ", new String[0]);
        ItemStack blueGlass = this.createDecorativeGlass(Material.BLUE_STAINED_GLASS_PANE, "\u00a77 ", new String[0]);
        for (int i = 0; i < 54; ++i) {
            int col = i % 9;
            if (col == 0 || col == 8 || i < 9 || i >= 45) {
                gui.setItem(i, blueGlass);
                continue;
            }
            gui.setItem(i, grayGlass);
        }
        gui.setItem(13, this.createDecorativeGlass(Material.BARRIER, "\u00a7cCh\u01b0a \u0111\u1ee7 \u0111i\u1ec1u ki\u1ec7n", "\u00a77H\u00e3y \u0111\u1eb7t 4 m\u00f3n Gi\u00e1p v\u00e0o \u00f4 20, 21, 22, 23", "\u00a77v\u00e0 s\u00e1ch Set Bonus v\u00e0o \u00f4 25."));
        gui.setItem(19, null);
        gui.setItem(20, null);
        gui.setItem(21, null);
        gui.setItem(22, null);
        gui.setItem(24, null);
        gui.setItem(23, this.createDecorativeGlass(Material.GOLD_BLOCK, "\u00a76\u00a7lK\u00cdCH HO\u1ea0T DUNG H\u1ee2P", "\u00a77Gh\u00e9p Full Set Bonus v\u00e0o b\u1ed9 gi\u00e1p c\u1ee7a b\u1ea1n."));
        gui.setItem(49, this.createDecorativeGlass(Material.BARRIER, "\u00a7c\u0110\u00f3ng Giao Di\u1ec7n", new String[0]));
        player.openInventory(gui);
    }

    private void openAbilityGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, (int)54, (String)ABILITY_TITLE);
        ItemStack grayGlass = this.createDecorativeGlass(Material.GRAY_STAINED_GLASS_PANE, "\u00a77 ", new String[0]);
        ItemStack redGlass = this.createDecorativeGlass(Material.RED_STAINED_GLASS_PANE, "\u00a77 ", new String[0]);
        for (int i = 0; i < 54; ++i) {
            int col = i % 9;
            if (col == 0 || col == 8 || i < 9 || i >= 45) {
                gui.setItem(i, redGlass);
                continue;
            }
            gui.setItem(i, grayGlass);
        }
        gui.setItem(13, this.createDecorativeGlass(Material.BARRIER, "\u00a7cCh\u01b0a \u0111\u1ee7 \u0111i\u1ec1u ki\u1ec7n", "\u00a77H\u00e3y \u0111\u1eb7t V\u0169 kh\u00ed v\u00e0o \u00f4 21", "\u00a77v\u00e0 s\u00e1ch K\u1ef9 n\u0103ng v\u00e0o \u00f4 25."));
        gui.setItem(20, null);
        gui.setItem(24, null);
        gui.setItem(22, this.createDecorativeGlass(Material.DIAMOND_BLOCK, "\u00a7b\u00a7lK\u00cdCH HO\u1ea0T DUNG H\u1ee2P", "\u00a77Gh\u00e9p Active Ability v\u00e0o v\u0169 kh\u00ed c\u1ee7a b\u1ea1n."));
        gui.setItem(49, this.createDecorativeGlass(Material.BARRIER, "\u00a7c\u0110\u00f3ng Giao Di\u1ec7n", new String[0]));
        player.openInventory(gui);
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        String title = event.getView().getTitle();
        Player player = (Player)event.getWhoClicked();
        if (title.equals(INDEX_TITLE)) {
            event.setCancelled(true);
            int slot = event.getRawSlot();
            if (slot == 11) {
                player.closeInventory();
                Bukkit.getScheduler().runTask((Plugin)this.plugin, () -> this.openSetBonusGUI(player));
            } else if (slot == 15) {
                player.closeInventory();
                Bukkit.getScheduler().runTask((Plugin)this.plugin, () -> this.openAbilityGUI(player));
            } else if (slot == 22) {
                player.closeInventory();
            }
            return;
        }
        if (title.equals(SET_BONUS_TITLE)) {
            this.handleSetBonusGUIClick(event, player);
            Bukkit.getScheduler().runTask((Plugin)this.plugin, () -> this.updateSetBonusStatus(event.getView().getTopInventory()));
            return;
        }
        if (title.equals(ABILITY_TITLE)) {
            this.handleAbilityGUIClick(event, player);
            Bukkit.getScheduler().runTask((Plugin)this.plugin, () -> this.updateAbilityStatus(event.getView().getTopInventory()));
        }
    }

    private void updateSetBonusStatus(Inventory gui) {
        boolean hasBook;
        ItemStack helmet = gui.getItem(19);
        ItemStack chest = gui.getItem(20);
        ItemStack legs = gui.getItem(21);
        ItemStack boots = gui.getItem(22);
        ItemStack book = gui.getItem(24);
        boolean hasArmor = helmet != null && chest != null && legs != null && boots != null && !helmet.getType().isAir() && !chest.getType().isAir() && !legs.getType().isAir() && !boots.getType().isAir();
        boolean bl = hasBook = book != null && !book.getType().isAir() && this.isSpecificSkillBook(book, SkillType.SET_BONUS);
        if (hasArmor && hasBook) {
            gui.setItem(13, this.createDecorativeGlass(Material.GREEN_STAINED_GLASS_PANE, "\u00a7aS\u1eb5n s\u00e0ng Dung h\u1ee3p!", "\u00a77Nh\u1ea5n v\u00e0o Kh\u1ed1i V\u00e0ng b\u00ean c\u1ea1nh \u0111\u1ec3 ti\u1ebfn h\u00e0nh."));
        } else {
            gui.setItem(13, this.createDecorativeGlass(Material.BARRIER, "\u00a7cCh\u01b0a \u0111\u1ee7 \u0111i\u1ec1u ki\u1ec7n", "\u00a77H\u00e3y \u0111\u1eb7t 4 m\u00f3n Gi\u00e1p v\u00e0o \u00f4 20, 21, 22, 23", "\u00a77v\u00e0 s\u00e1ch Set Bonus v\u00e0o \u00f4 25."));
        }
    }

    private void updateAbilityStatus(Inventory gui) {
        boolean hasBook;
        ItemStack weapon = gui.getItem(20);
        ItemStack book = gui.getItem(24);
        boolean hasWeapon = weapon != null && !weapon.getType().isAir() && ItemBuilder.detectIsWeapon(weapon, weapon.getItemMeta().getPersistentDataContainer());
        boolean bl = hasBook = book != null && !book.getType().isAir() && this.isSpecificSkillBook(book, SkillType.ABILITY);
        if (hasWeapon && hasBook) {
            gui.setItem(13, this.createDecorativeGlass(Material.GREEN_STAINED_GLASS_PANE, "\u00a7aS\u1eb5n s\u00e0ng Dung h\u1ee3p!", "\u00a77Nh\u1ea5n v\u00e0o Kh\u1ed1i Kim C\u01b0\u01a1ng b\u00ean c\u1ea1nh \u0111\u1ec3 ti\u1ebfn h\u00e0nh."));
        } else {
            gui.setItem(13, this.createDecorativeGlass(Material.BARRIER, "\u00a7cCh\u01b0a \u0111\u1ee7 \u0111i\u1ec1u ki\u1ec7n", "\u00a77H\u00e3y \u0111\u1eb7t V\u0169 kh\u00ed v\u00e0o \u00f4 21", "\u00a77v\u00e0 s\u00e1ch K\u1ef9 n\u0103ng v\u00e0o \u00f4 25."));
        }
    }

    private boolean isSpecificSkillBook(ItemStack item, SkillType type) {
        if (item == null || !item.hasItemMeta()) {
            return false;
        }
        PersistentDataContainer pdc = item.getItemMeta().getPersistentDataContainer();
        if (!pdc.has(new NamespacedKey((Plugin)CustomWeaponEngine.getInstance(), "cwe_skill_book"), PersistentDataType.INTEGER)) {
            return false;
        }
        String typeStr = (String)pdc.get(new NamespacedKey((Plugin)CustomWeaponEngine.getInstance(), "cwe_book_type"), PersistentDataType.STRING);
        return type.name().equals(typeStr);
    }

    private void handleSetBonusGUIClick(InventoryClickEvent event, Player player) {
        Inventory gui = event.getView().getTopInventory();
        int slot = event.getRawSlot();
        if (event.getClickedInventory() != gui) {
            if (event.isShiftClick()) {
                event.setCancelled(true);
            }
            return;
        }
        if (slot != 19 && slot != 20 && slot != 21 && slot != 22 && slot != 24) {
            event.setCancelled(true);
        }
        if (slot == 23) {
            this.handleCombineSetBonus(player, gui);
        } else if (slot == 49) {
            player.closeInventory();
        }
    }

    private void handleAbilityGUIClick(InventoryClickEvent event, Player player) {
        Inventory gui = event.getView().getTopInventory();
        int slot = event.getRawSlot();
        if (event.getClickedInventory() != gui) {
            if (event.isShiftClick()) {
                event.setCancelled(true);
            }
            return;
        }
        if (slot != 20 && slot != 24) {
            event.setCancelled(true);
        }
        if (slot == 22) {
            this.handleCombineAbility(player, gui);
        } else if (slot == 49) {
            player.closeInventory();
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        String title = event.getView().getTitle();
        if (!title.equals(SET_BONUS_TITLE) && !title.equals(ABILITY_TITLE)) {
            return;
        }
        Player player = (Player)event.getPlayer();
        Inventory gui = event.getInventory();
        if (title.equals(SET_BONUS_TITLE)) {
            this.giveItemBack(player, gui, 19);
            this.giveItemBack(player, gui, 20);
            this.giveItemBack(player, gui, 21);
            this.giveItemBack(player, gui, 22);
            this.giveItemBack(player, gui, 24);
        } else {
            this.giveItemBack(player, gui, 20);
            this.giveItemBack(player, gui, 24);
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

    private void handleCombineSetBonus(Player player, Inventory gui) {
        ItemStack helmet = gui.getItem(19);
        ItemStack chest = gui.getItem(20);
        ItemStack legs = gui.getItem(21);
        ItemStack boots = gui.getItem(22);
        ItemStack book = gui.getItem(24);
        if (helmet == null || chest == null || legs == null || boots == null || book == null || helmet.getType().isAir() || chest.getType().isAir() || legs.getType().isAir() || boots.getType().isAir() || book.getType().isAir()) {
            player.sendMessage("\u00a7cB\u1ea1n ch\u01b0a \u0111\u1eb7t \u0111\u1ea7y \u0111\u1ee7 gi\u00e1p v\u00e0 s\u00e1ch k\u0129 n\u0103ng v\u00e0o \u00f4!");
            return;
        }
        if (!(helmet.getType().name().contains("HELMET") && (chest.getType().name().contains("CHESTPLATE") || chest.getType().name().contains("ELYTRA")) && legs.getType().name().contains("LEGGINGS") && boots.getType().name().contains("BOOTS"))) {
            player.sendMessage("\u00a7cH\u00e3y \u0111\u1ea3m b\u1ea3o \u0111\u1eb7t \u0111\u00fang ph\u1ea7n gi\u00e1p: N\u00f3n \u1edf \u00f4 20, \u00c1o \u1edf \u00f4 21, Qu\u1ea7n \u1edf \u00f4 22, v\u00e0 Gi\u00e0y \u1edf \u00f4 23!");
            return;
        }
        if (!this.isSpecificSkillBook(book, SkillType.SET_BONUS)) {
            player.sendMessage("\u00a7c\u0110\u00f3 kh\u00f4ng ph\u1ea3i l\u00e0 s\u00e1ch k\u1ef9 n\u0103ng Set Gi\u00e1p h\u1ee3p l\u1ec7!");
            return;
        }
        PersistentDataContainer bookPdc = book.getItemMeta().getPersistentDataContainer();
        String skillId = (String)bookPdc.get(new NamespacedKey((Plugin)CustomWeaponEngine.getInstance(), "cwe_skill_id"), PersistentDataType.STRING);
        SkillBook skill = BOOKS.get(skillId);
        if (skill == null) {
            player.sendMessage("\u00a7cK\u1ef9 n\u0103ng kh\u00f4ng t\u1ed3n t\u1ea1i!");
            return;
        }
        this.applySetBonusData(helmet, skill);
        this.applySetBonusData(chest, skill);
        this.applySetBonusData(legs, skill);
        this.applySetBonusData(boots, skill);
        book.setAmount(book.getAmount() - 1);
        player.playSound(player.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1.0f, 1.0f);
        player.spawnParticle(Particle.HAPPY_VILLAGER, player.getLocation().add(0.0, 1.0, 0.0), 30, 0.5, 0.5, 0.5, 0.05);
        player.sendMessage("\u00a7a\u00a7l[\ud83d\udee1\ufe0f] Dung h\u1ee3p Set Bonus th\u00e0nh c\u00f4ng! B\u1ed9 gi\u00e1p c\u1ee7a b\u1ea1n \u0111\u00e3 nh\u1eadn: " + skill.rarity.color + "\u00a7l" + skill.name);
        Bukkit.getScheduler().runTask((Plugin)this.plugin, () -> player.closeInventory());
    }

    private void applySetBonusData(ItemStack item, SkillBook skill) {
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        pdc.set(new NamespacedKey((Plugin)CustomWeaponEngine.getInstance(), "stat_setbonus_title"), PersistentDataType.STRING, skill.name);
        pdc.set(new NamespacedKey((Plugin)CustomWeaponEngine.getInstance(), "stat_setbonus_desc1"), PersistentDataType.STRING, skill.desc1);
        pdc.set(new NamespacedKey((Plugin)CustomWeaponEngine.getInstance(), "stat_setbonus_desc2"), PersistentDataType.STRING, skill.desc2);
        pdc.set(new NamespacedKey((Plugin)CustomWeaponEngine.getInstance(), "cwe_skill_id"), PersistentDataType.STRING, skill.id);
        item.setItemMeta(meta);
        ItemBuilder.updateItem(item);
    }

    private void handleCombineAbility(Player player, Inventory gui) {
        ItemStack weapon = gui.getItem(20);
        ItemStack book = gui.getItem(24);
        if (weapon == null || book == null || weapon.getType().isAir() || book.getType().isAir()) {
            player.sendMessage("\u00a7cB\u1ea1n ch\u01b0a \u0111\u1eb7t \u0111\u1ea7y \u0111\u1ee7 v\u0169 kh\u00ed v\u00e0 s\u00e1ch k\u0129 n\u0103ng v\u00e0o \u00f4!");
            return;
        }
        if (!this.isSpecificSkillBook(book, SkillType.ABILITY)) {
            player.sendMessage("\u00a7c\u0110\u00f3 kh\u00f4ng ph\u1ea3i l\u00e0 s\u00e1ch k\u1ef9 n\u0103ng V\u0169 Kh\u00ed h\u1ee3p l\u1ec7!");
            return;
        }
        PersistentDataContainer bookPdc = book.getItemMeta().getPersistentDataContainer();
        String skillId = (String)bookPdc.get(new NamespacedKey((Plugin)CustomWeaponEngine.getInstance(), "cwe_skill_id"), PersistentDataType.STRING);
        SkillBook skill = BOOKS.get(skillId);
        if (skill == null) {
            player.sendMessage("\u00a7cK\u1ef9 n\u0103ng kh\u00f4ng t\u1ed3n t\u1ea1i!");
            return;
        }
        String weaponType = ItemBuilder.getWeaponTypeName(weapon);
        boolean matches = false;
        if (skill.targetType.equals("MELEE") && (weaponType.equals("SWORD") || weaponType.equals("AXE") || weaponType.equals("WEAPON"))) {
            matches = true;
        } else if (skill.targetType.equals(weaponType)) {
            matches = true;
        }
        if (!matches) {
            player.sendMessage("\u00a7cKh\u1edbp l\u1ed7i! S\u00e1ch k\u1ef9 n\u0103ng n\u00e0y ch\u1ec9 c\u00f3 th\u1ec3 \u00e9p v\u00e0o d\u00f2ng v\u0169 kh\u00ed: \u00a7e\u00a7l" + skill.targetType);
            return;
        }
        ItemMeta meta = weapon.getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        pdc.set(new NamespacedKey((Plugin)CustomWeaponEngine.getInstance(), "stat_setbonus_title"), PersistentDataType.STRING, skill.name);
        pdc.set(new NamespacedKey((Plugin)CustomWeaponEngine.getInstance(), "stat_setbonus_desc1"), PersistentDataType.STRING, skill.desc1);
        pdc.set(new NamespacedKey((Plugin)CustomWeaponEngine.getInstance(), "stat_setbonus_desc2"), PersistentDataType.STRING, skill.desc2);
        pdc.set(new NamespacedKey((Plugin)CustomWeaponEngine.getInstance(), "stat_ability_click"), PersistentDataType.STRING, skill.clickType);
        pdc.set(new NamespacedKey((Plugin)CustomWeaponEngine.getInstance(), "cwe_skill_id"), PersistentDataType.STRING, skill.id);
        weapon.setItemMeta(meta);
        ItemBuilder.updateItem(weapon);
        book.setAmount(book.getAmount() - 1);
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.2f);
        player.spawnParticle(Particle.ENCHANTED_HIT, player.getLocation().add(0.0, 1.0, 0.0), 30, 0.5, 0.5, 0.5, 0.1);
        player.sendMessage("\u00a7a\u00a7l[\ud83d\udde1\ufe0f] Dung h\u1ee3p Active Ability th\u00e0nh c\u00f4ng! V\u0169 kh\u00ed \u0111\u00e3 nh\u1eadn: " + skill.rarity.color + "\u00a7l" + skill.name);
        Bukkit.getScheduler().runTask((Plugin)this.plugin, () -> player.closeInventory());
    }

    private String getArmorSkillId(ItemStack item) {
        if (item == null || !item.hasItemMeta()) {
            return null;
        }
        return (String)item.getItemMeta().getPersistentDataContainer().get(new NamespacedKey((Plugin)CustomWeaponEngine.getInstance(), "cwe_skill_id"), PersistentDataType.STRING);
    }

    private boolean isWearingSkillSet(Player p, String skillId) {
        String h = this.getArmorSkillId(p.getInventory().getHelmet());
        String c = this.getArmorSkillId(p.getInventory().getChestplate());
        String l = this.getArmorSkillId(p.getInventory().getLeggings());
        String b = this.getArmorSkillId(p.getInventory().getBoots());
        return skillId.equals(h) && skillId.equals(c) && skillId.equals(l) && skillId.equals(b);
    }

    private void triggerPeriodicSetBonuses(Player p) {
        SkillsUser u = AuraSkillsApi.get().getUser(p.getUniqueId());
        if (u == null) return;

        if (this.isWearingSkillSet(p, "dragons_protection")) {
            double maxHp = p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
            double healAmount = maxHp * 0.02;
            p.setHealth(Math.min(maxHp, p.getHealth() + healAmount));
            p.spawnParticle(Particle.HEART, p.getLocation().add(0.0, 1.0, 0.0), 2, 0.3, 0.3, 0.3, 0.0);
        }

        // Magical Synergy: +50 Intelligence
        if (this.isWearingSkillSet(p, "magical_synergy")) {
            u.addStatModifier(new StatModifier("cwe_magical_synergy", Stats.WISDOM, 50.0, dev.aurelium.auraskills.api.util.AuraSkillsModifier.Operation.ADD));
        } else {
            u.removeStatModifier("cwe_magical_synergy");
        }

        // Cosmic Overlord: +15% all stats
        if (this.isWearingSkillSet(p, "cosmic_overlord")) {
            double str = u.getStatLevel(Stats.STRENGTH) * 0.15;
            double crit = u.getStatLevel(Stats.CRIT_CHANCE) * 0.15;
            double cd = u.getStatLevel(Stats.CRIT_DAMAGE) * 0.15;
            double def = u.getStatLevel(Stats.TOUGHNESS) * 0.15;
            double hp = u.getStatLevel(Stats.HEALTH) * 0.15;
            double intel = u.getStatLevel(Stats.WISDOM) * 0.15;

            u.addStatModifier(new StatModifier("cwe_cosmic_str", Stats.STRENGTH, str, dev.aurelium.auraskills.api.util.AuraSkillsModifier.Operation.ADD));
            u.addStatModifier(new StatModifier("cwe_cosmic_crit", Stats.CRIT_CHANCE, crit, dev.aurelium.auraskills.api.util.AuraSkillsModifier.Operation.ADD));
            u.addStatModifier(new StatModifier("cwe_cosmic_cd", Stats.CRIT_DAMAGE, cd, dev.aurelium.auraskills.api.util.AuraSkillsModifier.Operation.ADD));
            u.addStatModifier(new StatModifier("cwe_cosmic_def", Stats.TOUGHNESS, def, dev.aurelium.auraskills.api.util.AuraSkillsModifier.Operation.ADD));
            u.addStatModifier(new StatModifier("cwe_cosmic_hp", Stats.HEALTH, hp, dev.aurelium.auraskills.api.util.AuraSkillsModifier.Operation.ADD));
            u.addStatModifier(new StatModifier("cwe_cosmic_int", Stats.WISDOM, intel, dev.aurelium.auraskills.api.util.AuraSkillsModifier.Operation.ADD));
        } else {
            u.removeStatModifier("cwe_cosmic_str");
            u.removeStatModifier("cwe_cosmic_crit");
            u.removeStatModifier("cwe_cosmic_cd");
            u.removeStatModifier("cwe_cosmic_def");
            u.removeStatModifier("cwe_cosmic_hp");
            u.removeStatModifier("cwe_cosmic_int");
        }
    }

    @EventHandler(priority=EventPriority.HIGH, ignoreCancelled=true)
    public void onPlayerDamageTake(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        Player p = (Player)event.getEntity();
        if (this.isWearingSkillSet(p, "dragons_protection")) {
            event.setDamage(event.getDamage() * 0.9);
        }
        if (this.isWearingSkillSet(p, "adrenaline_rush")) {
            long cooldownEnd;
            long now;
            double maxHp = p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
            double ratio = p.getHealth() / maxHp;
            if (ratio < 0.4 && (now = System.currentTimeMillis()) >= (cooldownEnd = this.setBonusCooldowns.getOrDefault(p.getUniqueId(), 0L).longValue())) {
                this.setBonusCooldowns.put(p.getUniqueId(), now + 45000L);
                p.sendMessage("\u00a7c\u00a7l[\u2726] KH\u00cd TH\u1ebe N\u1ed4I D\u1eacY! Nh\u1eadn +100 T\u1ed1c \u0111\u1ed9 v\u00e0 +30% S\u00e1t th\u01b0\u01a1ng trong 8 gi\u00e2y!");
                p.playSound(p.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1.0f, 1.2f);
                p.spawnParticle(Particle.FLAME, p.getLocation(), 20, 0.5, 1.0, 0.5, 0.1);
                p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 160, 2));
            }
        }
        if (this.isWearingSkillSet(p, "cosmic_overlord") && Math.random() < 0.05) {
            event.setCancelled(true);
            p.playSound(p.getLocation(), Sound.ITEM_SHIELD_BLOCK, 1.0f, 1.5f);
            p.spawnParticle(Particle.CRIT, p.getLocation().add(0.0, 1.0, 0.0), 10, 0.3, 0.3, 0.3, 0.2);
            p.sendMessage("\u00a7d\u00a7l[\u2726] Ph\u1ea3n \u0111\u00f2n t\u1ed1i th\u01b0\u1ee3ng! B\u1ea1n ch\u1eb7n to\u00e0n b\u1ed9 s\u00e1t th\u01b0\u01a1ng v\u00e0 ph\u1ea3n l\u1ea1i m\u1ee5c ti\u00eau!");
            if (event.getDamager() instanceof LivingEntity) {
                LivingEntity attacker = (LivingEntity)event.getDamager();
                attacker.damage(event.getDamage());
            }
        }
    }

    @EventHandler(priority=EventPriority.MONITOR, ignoreCancelled=true)
    public void onPlayerMeleeHit(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) {
            return;
        }
        Player p = (Player)event.getDamager();
        if (this.isWearingSkillSet(p, "vampiric_touch")) {
            double heal = event.getFinalDamage() * 0.05;
            double maxHp = p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
            p.setHealth(Math.min(maxHp, p.getHealth() + heal));
            p.spawnParticle(Particle.DUST, p.getLocation().add(0.0, 1.0, 0.0), 5, 0.2, 0.2, 0.2, new Particle.DustOptions(Color.RED, 1.0f));
        }
        // Adrenaline Rush +30% Damage buff
        if (this.isWearingSkillSet(p, "adrenaline_rush")) {
            long cooldownEnd = this.setBonusCooldowns.getOrDefault(p.getUniqueId(), 0L).longValue();
            long now = System.currentTimeMillis();
            // Nếu buff 8s (8000ms) đang hoạt động (cooldown 45s => cooldownEnd là now + 45000, 8s là cooldownEnd - 45000 + 8000)
            if (now < cooldownEnd - 45000L + 8000L) {
                event.setDamage(event.getDamage() * 1.3);
            }
        }
        this.handleLeftClickAbility(p, event);
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {
        long end;
        if (event.getHand() != EquipmentSlot.HAND) {
            return;
        }
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        ItemStack item = event.getItem();
        if (item == null || !item.hasItemMeta()) {
            return;
        }
        PersistentDataContainer pdc = item.getItemMeta().getPersistentDataContainer();
        if (!pdc.has(new NamespacedKey((Plugin)CustomWeaponEngine.getInstance(), "cwe_skill_id"), PersistentDataType.STRING)) {
            return;
        }
        String skillId = (String)pdc.get(new NamespacedKey((Plugin)CustomWeaponEngine.getInstance(), "cwe_skill_id"), PersistentDataType.STRING);
        SkillBook skill = BOOKS.get(skillId);
        if (skill == null || skill.type != SkillType.ABILITY || !skill.clickType.equals("RIGHT CLICK")) {
            return;
        }
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        java.util.Map<String, Long> personalCDs = this.abilityCooldowns.computeIfAbsent(uuid, k -> new java.util.HashMap<>());
        long now = System.currentTimeMillis();
        if (now < (end = personalCDs.getOrDefault(skillId, 0L).longValue())) {
            long remaining = (end - now + 999L) / 1000L;
            player.sendMessage("\u00a7cK\u1ef9 n\u0103ng \u00a7e" + skill.name + "\u00a7c \u0111ang h\u1ed3i chi\u00eau! C\u00f2n \u00a7e" + remaining + "s\u00a7c.");
            return;
        }
        if (!ManaHelper.consumeMana(player, skill.manaCost, (CustomWeaponEngine)this.plugin, skill.name)) {
            return;
        }
        personalCDs.put(skillId, now + (long)skill.cooldown * 1000L);
        this.triggerMagicalSynergyManaReturn(player);
        this.executeRightClickAbility(player, skillId);
    }

    private void handleLeftClickAbility(Player player, EntityDamageByEntityEvent event) {
        long end;
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item == null || !item.hasItemMeta()) {
            return;
        }
        PersistentDataContainer pdc = item.getItemMeta().getPersistentDataContainer();
        if (!pdc.has(new NamespacedKey((Plugin)CustomWeaponEngine.getInstance(), "cwe_skill_id"), PersistentDataType.STRING)) {
            return;
        }
        String skillId = (String)pdc.get(new NamespacedKey((Plugin)CustomWeaponEngine.getInstance(), "cwe_skill_id"), PersistentDataType.STRING);
        SkillBook skill = BOOKS.get(skillId);
        if (skill == null || skill.type != SkillType.ABILITY || !skill.clickType.equals("LEFT CLICK")) {
            return;
        }
        UUID uuid = player.getUniqueId();
        java.util.Map<String, Long> personalCDs = this.abilityCooldowns.computeIfAbsent(uuid, k -> new java.util.HashMap<>());
        long now = System.currentTimeMillis();
        if (now < (end = personalCDs.getOrDefault(skillId, 0L).longValue())) {
            return;
        }
        if (!ManaHelper.consumeMana(player, skill.manaCost, (CustomWeaponEngine)this.plugin, skill.name)) {
            return;
        }
        personalCDs.put(skillId, now + (long)skill.cooldown * 1000L);
        this.triggerMagicalSynergyManaReturn(player);
        this.executeLeftClickAbility(player, event, skillId);
    }

    private void triggerMagicalSynergyManaReturn(Player p) {
        SkillsUser u;
        if (this.isWearingSkillSet(p, "magical_synergy") && (u = AuraSkillsApi.get().getUser(p.getUniqueId())) != null) {
            double newMana = Math.min(u.getMaxMana(), u.getMana() + 15.0);
            u.setMana(newMana);
            p.spawnParticle(Particle.ENCHANTED_HIT, p.getLocation().add(0.0, 1.5, 0.0), 5, 0.2, 0.2, 0.2, 0.0);
        }
    }

    private void executeRightClickAbility(final Player player, String skillId) {
        Location loc = player.getLocation();
        block15 : switch (skillId) {
            case "tornado_spin": {
                player.playSound(loc, Sound.ENTITY_ENDER_DRAGON_FLAP, 1.0f, 1.2f);
                player.spawnParticle(Particle.SWEEP_ATTACK, loc.add(0.0, 0.5, 0.0), 10, 1.5, 0.2, 1.5, 0.1);
                for (Entity e : player.getNearbyEntities(4.0, 2.0, 4.0)) {
                    if (!(e instanceof LivingEntity) || e instanceof org.bukkit.entity.ArmorStand || e.hasMetadata("NPC")) continue;
                    LivingEntity le = (LivingEntity)e;
                    if (le.equals(player)) continue;
                    this.dealSkillDamage(le, player, 150.0);
                    Vector vec = le.getLocation().toVector().subtract(player.getLocation().toVector()).normalize().multiply(1.5).setY(0.4);
                    le.setVelocity(vec);
                }
                break;
            }
            case "shadow_teleport": {
                LivingEntity target = this.getClosestTarget(player, 10.0);
                if (target == null) {
                    player.sendMessage("\u00a7cKh\u00f4ng t\u00ecm th\u1ea5y m\u1ee5c ti\u00eau n\u00e0o g\u1ea7n \u0111\u00f3!");
                    Map<String, Long> cds = this.abilityCooldowns.get(player.getUniqueId());
                    if (cds != null) {
                        cds.remove("shadow_teleport");
                    }
                    return;
                }
                Location behind = target.getLocation().clone().add(target.getLocation().getDirection().multiply(-1.5));
                behind.setDirection(target.getLocation().getDirection());
                player.spawnParticle(Particle.PORTAL, player.getLocation(), 20, 0.3, 1.0, 0.3, 0.1);
                player.teleport(behind);
                player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
                this.dealSkillDamage(target, player, 200.0);
                target.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 60, 0));
                break;
            }
            case "bloodlust": {
                player.playSound(loc, Sound.ENTITY_WOLF_GROWL, 1.0f, 1.2f);
                player.spawnParticle(Particle.DUST, loc.add(0.0, 1.0, 0.0), 20, 0.3, 0.5, 0.3, new Particle.DustOptions(Color.RED, 1.5f));
                player.addPotionEffect(new PotionEffect(PotionEffectType.HASTE, 120, 2));
                this.pumpingBloodlustTask(player);
                break;
            }
            case "blade_of_judgement": {
                LivingEntity targetEntity = this.getClosestTarget(player, 15.0);
                org.bukkit.block.Block b = player.getTargetBlockExact(15);
                Location beamLoc = targetEntity != null ? targetEntity.getLocation() : (b != null ? b.getLocation() : player.getLocation());
                player.playSound(beamLoc, Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1.0f, 1.2f);
                beamLoc.getWorld().spawnParticle(Particle.FLASH, beamLoc, 10, 0.5, 1.0, 0.5, 0.1);
                for (Entity e : beamLoc.getWorld().getNearbyEntities(beamLoc, 5.0, 5.0, 5.0)) {
                    if (!(e instanceof LivingEntity) || e instanceof org.bukkit.entity.ArmorStand || e.hasMetadata("NPC")) continue;
                    LivingEntity le = (LivingEntity)e;
                    if (le.equals(player)) continue;
                    le.setNoDamageTicks(0);
                    this.dealSkillDamage(le, player, 1000.0);
                    le.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 40, 4));
                }
                break;
            }
            case "ground_slam": {
                player.playSound(loc, Sound.ENTITY_GENERIC_EXPLODE, 1.0f, 0.8f);
                player.spawnParticle(Particle.EXPLOSION, loc, 15, 1.5, 0.2, 1.5, 0.1);
                for (Entity e : player.getNearbyEntities(3.0, 2.0, 3.0)) {
                    if (!(e instanceof LivingEntity) || e instanceof org.bukkit.entity.ArmorStand || e.hasMetadata("NPC")) continue;
                    LivingEntity le = (LivingEntity)e;
                    if (le.equals(player)) continue;
                    this.dealSkillDamage(le, player, 100.0);
                    le.setVelocity(new Vector(0.0, 0.8, 0.0));
                }
                break;
            }
            case "heavy_shield": {
                player.playSound(loc, Sound.ITEM_SHIELD_BLOCK, 1.0f, 0.8f);
                player.spawnParticle(Particle.CRIT, loc.add(0.0, 1.0, 0.0), 20, 0.5, 0.5, 0.5, 0.05);
                final SkillsUser u = AuraSkillsApi.get().getUser(player.getUniqueId());
                if (u == null) break;
                u.addStatModifier(new StatModifier("heavy_shield_buff", (Stat)Stats.TOUGHNESS, 200.0, AuraSkillsModifier.Operation.ADD));
                new BukkitRunnable(){

                    public void run() {
                        u.removeStatModifier("heavy_shield_buff");
                    }
                }.runTaskLater((Plugin)this.plugin, 100L);
                break;
            }
            case "gravity_pull": {
                player.playSound(loc, Sound.ENTITY_ENDERMAN_SCREAM, 1.0f, 0.8f);
                player.spawnParticle(Particle.PORTAL, loc, 50, 2.0, 1.0, 2.0, 0.1);
                for (Entity e : player.getNearbyEntities(8.0, 4.0, 8.0)) {
                    if (!(e instanceof LivingEntity) || e instanceof org.bukkit.entity.ArmorStand || e.hasMetadata("NPC")) continue;
                    LivingEntity le = (LivingEntity)e;
                    if (le.equals(player)) continue;
                    Vector pull = player.getLocation().toVector().subtract(le.getLocation().toVector()).normalize().multiply(1.2).setY(0.2);
                    le.setVelocity(pull);
                    le.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 60, 1));
                }
                break;
            }
            case "meteor_strike": {
                player.setVelocity(new Vector(0.0, 1.5, 0.0));
                player.playSound(loc, Sound.ENTITY_BAT_TAKEOFF, 1.0f, 0.8f);
                new BukkitRunnable(){
                    int ticks = 0;

                    public void run() {
                        ++this.ticks;
                        if (player.isOnGround() || this.ticks > 40) {
                            Location landing = player.getLocation();
                            landing.getWorld().playSound(landing, Sound.ENTITY_GENERIC_EXPLODE, 1.5f, 0.8f);
                            landing.getWorld().spawnParticle(Particle.EXPLOSION_EMITTER, landing, 2, 2.0, 0.2, 2.0, 0.1);
                            for (Entity e : player.getNearbyEntities(6.0, 3.0, 6.0)) {
                                if (!(e instanceof LivingEntity) || e instanceof org.bukkit.entity.ArmorStand || e.hasMetadata("NPC")) continue;
                                LivingEntity le = (LivingEntity)e;
                                if (le.equals(player)) continue;
                                SkillBookSystem.this.dealSkillDamage(le, player, 800.0);
                                le.setFireTicks(100);
                            }
                            this.cancel();
                        }
                    }
                }.runTaskTimer((Plugin)this.plugin, 5L, 2L);
                break;
            }
            case "water_spout": {
                player.playSound(loc, Sound.ITEM_BUCKET_EMPTY, 1.0f, 1.2f);
                final Vector dir = loc.getDirection().normalize();
                new BukkitRunnable(){
                    int steps = 0;
                    Location current = player.getEyeLocation();

                    public void run() {
                        ++this.steps;
                        this.current.add(dir);
                        this.current.getWorld().spawnParticle(Particle.SPLASH, this.current, 10, 0.1, 0.1, 0.1, 0.0);
                        for (Entity e : this.current.getWorld().getNearbyEntities(this.current, 1.5, 1.5, 1.5)) {
                            if (!(e instanceof LivingEntity) || e instanceof org.bukkit.entity.ArmorStand || e.hasMetadata("NPC")) continue;
                            LivingEntity le = (LivingEntity)e;
                            if (le.equals(player)) continue;
                            SkillBookSystem.this.dealSkillDamage(le, player, 120.0);
                            le.setVelocity(dir.clone().multiply(1.2).setY(0.3));
                        }
                        if (this.steps >= 8 || !this.current.getBlock().getType().isAir()) {
                            this.cancel();
                        }
                    }
                }.runTaskTimer((Plugin)this.plugin, 0L, 1L);
                break;
            }
            case "ocean_healing": {
                player.playSound(loc, Sound.BLOCK_WATER_AMBIENT, 1.0f, 1.0f);
                player.spawnParticle(Particle.SPLASH, loc, 50, 5.0, 0.2, 5.0, 0.05);
                double maxHp = player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
                player.setHealth(Math.min(maxHp, player.getHealth() + maxHp * 0.15));
                for (Entity e : player.getNearbyEntities(5.0, 2.0, 5.0)) {
                    if (!(e instanceof Player)) continue;
                    Player ally = (Player)e;
                    double allyMax = ally.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
                    ally.setHealth(Math.min(allyMax, ally.getHealth() + allyMax * 0.15));
                    ally.sendMessage("\u00a7a[\ud83d\udd31] B\u1ea1n \u0111\u01b0\u1ee3c Ocean Healing ch\u1eefa tr\u1ecb!");
                }
                break;
            }
            case "lightning_chain": {
                LivingEntity chainTarget = this.getClosestTarget(player, 12.0);
                if (chainTarget == null) {
                    player.sendMessage("\u00a7cKh\u00f4ng t\u00ecm th\u1ea5y m\u1ee5c ti\u00eau n\u00e0o g\u1ea7n \u0111\u00f3!");
                    Map<String, Long> cds = this.abilityCooldowns.get(player.getUniqueId());
                    if (cds != null) {
                        cds.remove("lightning_chain");
                    }
                    return;
                }
                this.triggerLightningChain(player, chainTarget, 4);
                break;
            }
            case "tsunami_wave": {
                player.playSound(loc, Sound.BLOCK_WATER_AMBIENT, 1.5f, 0.5f);
                final Vector waveDir = loc.getDirection().setY(0).normalize();
                new BukkitRunnable(){
                    int waveSteps = 0;
                    Location waveCenter = player.getLocation().clone();

                    public void run() {
                        ++this.waveSteps;
                        this.waveCenter.add(waveDir);
                        this.waveCenter.getWorld().spawnParticle(Particle.SPLASH, this.waveCenter, 30, 2.0, 0.5, 2.0, 0.05);
                        for (Entity e : this.waveCenter.getWorld().getNearbyEntities(this.waveCenter, 3.0, 2.0, 3.0)) {
                            if (!(e instanceof LivingEntity) || e instanceof org.bukkit.entity.ArmorStand || e.hasMetadata("NPC")) continue;
                            LivingEntity le = (LivingEntity)e;
                            if (le.equals(player)) continue;
                            SkillBookSystem.this.dealSkillDamage(le, player, 300.0);
                            le.setVelocity(waveDir.clone().multiply(1.5).setY(0.4));
                        }
                        if (this.waveSteps >= 12) {
                            this.cancel();
                        }
                    }
                }.runTaskTimer((Plugin)this.plugin, 0L, 2L);
                break;
            }
            case "poseidons_wrath": {
                player.playSound(loc, Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 2.0f, 0.8f);
                player.sendMessage("\u00a7b\u00a7l[\ud83d\udd31] C\u01a0N TH\u1ecaNH N\u1ed8 C\u1ee6A POSEIDON \u0110ANG GI\u00c1NG XU\u1ed0NG!");
                java.util.List<org.bukkit.entity.Entity> list = player.getNearbyEntities(15.0, 5.0, 15.0);
                int lightningCount = 0;
                for (Entity e : list) {
                    if (!(e instanceof LivingEntity) || e instanceof org.bukkit.entity.ArmorStand || e.hasMetadata("NPC")) continue;
                    LivingEntity le = (LivingEntity)e;
                    if (le.equals(player)) continue;
                    le.getWorld().strikeLightningEffect(le.getLocation());
                    this.dealSkillDamage(le, player, 800.0);
                    if (++lightningCount < 8) continue;
                    break block15;
                }
                break;
            }
        }
    }

    private void executeLeftClickAbility(Player player, EntityDamageByEntityEvent event, String skillId) {
        if (event.getEntity() instanceof LivingEntity) {
            LivingEntity le = (LivingEntity)event.getEntity();
            if (skillId.equals("sharp_edge")) {
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_ATTACK_CRIT, 1.0f, 1.5f);
                player.spawnParticle(Particle.CRIT, le.getLocation().add(0.0, 1.0, 0.0), 10, 0.2, 0.2, 0.2, 0.1);
                event.setDamage(event.getDamage() + 100.0);
            } else if (skillId.equals("crushing_blow")) {
                player.playSound(player.getLocation(), Sound.ENTITY_IRON_GOLEM_DAMAGE, 1.0f, 0.8f);
                le.getWorld().spawnParticle(Particle.EXPLOSION, le.getLocation(), 10, 1.0, 0.2, 1.0, 0.1);
                for (Entity e : le.getNearbyEntities(3.0, 2.0, 3.0)) {
                    if (!(e instanceof LivingEntity) || e instanceof org.bukkit.entity.ArmorStand || e.hasMetadata("NPC")) continue;
                    if (e.equals(player)) continue;
                    this.dealSkillDamage((LivingEntity)e, player, 150.0);
                }
                final SkillsUser targetSkills = AuraSkillsApi.get().getUser(le.getUniqueId());
                if (targetSkills != null) {
                    targetSkills.addStatModifier(new StatModifier("crushed_debuff", (Stat)Stats.TOUGHNESS, -100.0, AuraSkillsModifier.Operation.ADD));
                    new BukkitRunnable(){

                        public void run() {
                            targetSkills.removeStatModifier("crushed_debuff");
                        }
                    }.runTaskLater((Plugin)this.plugin, 120L);
                }
            }
        }
    }

    private void pumpingBloodlustTask(final Player p) {
        new BukkitRunnable(){
            int time = 0;

            public void run() {
                ++this.time;
                if (!p.isOnline() || this.time > 6) {
                    this.cancel();
                    return;
                }
                p.spawnParticle(Particle.DUST, p.getLocation().add(0.0, 0.5, 0.0), 5, 0.2, 0.2, 0.2, new Particle.DustOptions(Color.RED, 0.5f));
            }
        }.runTaskTimer((Plugin)this.plugin, 0L, 20L);
    }

    private void triggerLightningChain(final Player player, final LivingEntity current, final int maxBounces) {
        current.getWorld().strikeLightningEffect(current.getLocation());
        this.dealSkillDamage(current, player, 250.0);
        if (maxBounces <= 1) {
            return;
        }
        new BukkitRunnable(){

            public void run() {
                LivingEntity next = null;
                double closest = 6.0;
                for (Entity e : current.getNearbyEntities(6.0, 3.0, 6.0)) {
                    double dist;
                    if (!(e instanceof LivingEntity) || e instanceof org.bukkit.entity.ArmorStand || e.hasMetadata("NPC") || e == current || e == player || !((dist = e.getLocation().distance(current.getLocation())) < closest)) continue;
                    closest = dist;
                    next = (LivingEntity)e;
                }
                if (next != null) {
                    SkillBookSystem.this.triggerLightningChain(player, next, maxBounces - 1);
                }
            }
        }.runTaskLater((Plugin)this.plugin, 3L);
    }

    private void dealSkillDamage(LivingEntity target, Player attacker, double baseDamage) {
        if (target instanceof Player) {
            double pvpDamage = baseDamage * 0.05;
            double currentHp = target.getHealth();
            if (currentHp <= pvpDamage) {
                // Đảm bảo event Death ghi nhận đúng người giết
                target.damage(currentHp * 10.0, attacker); 
            } else {
                target.setHealth(currentHp - pvpDamage);
                target.damage(0.01, attacker); // Cho có hiệu ứng giật
                org.example.system.DamageIndicatorManager.spawnHologram(target.getLocation().add(0, target.getHeight() / 2, 0), pvpDamage, target.hasMetadata("cwe_is_crit"));
            }
        } else {
            target.damage(baseDamage, attacker);
        }
    }

    private LivingEntity getClosestTarget(Player player, double maxDist) {
        LivingEntity target = null;
        
        try {
            org.bukkit.util.RayTraceResult rayResult = player.getWorld().rayTraceEntities(
                    player.getEyeLocation(), 
                    player.getLocation().getDirection(), 
                    maxDist, 
                    1.0, 
                    e -> e instanceof LivingEntity && e != player && !(e instanceof org.bukkit.entity.ArmorStand) && !e.hasMetadata("NPC")
            );
            if (rayResult != null && rayResult.getHitEntity() != null) {
                return (LivingEntity) rayResult.getHitEntity();
            }
        } catch (Exception ignored) {}

        double closest = maxDist;
        for (Entity e : player.getNearbyEntities(maxDist, maxDist, maxDist)) {
            double dist;
            if (!(e instanceof LivingEntity) || e instanceof Player || e instanceof org.bukkit.entity.ArmorStand || e.hasMetadata("NPC")) continue;
            if ((dist = e.getLocation().distance(player.getLocation())) < closest) {
                closest = dist;
                target = (LivingEntity)e;
            }
        }
        return target;
    }

    private ItemStack createDecorativeGlass(Material mat, String name, String ... lore) {
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
        BOOKS.put("dragons_protection", new SkillBook("dragons_protection", "Dragon's Protection", SkillType.SET_BONUS, ItemStatsGUI.Rarity.RARE, "ARMOR", 0.0, 0, "", "Gi\u1ea3m 10% s\u00e1t th\u01b0\u01a1ng nh\u1eadn v\u00e0o t\u1eeb qu\u00e1i v\u1eadt.", "H\u1ed3i ph\u1ee5c 2% HP t\u1ed1i \u0111a m\u1ed7i 5 gi\u00e2y."));
        BOOKS.put("magical_synergy", new SkillBook("magical_synergy", "Magical Synergy", SkillType.SET_BONUS, ItemStatsGUI.Rarity.RARE, "ARMOR", 0.0, 0, "", "T\u0103ng +50 Intelligence.", "H\u1ed3i l\u1ea1i 15 Mana m\u1ed7i khi k\u00edch ho\u1ea1t k\u1ef9 n\u0103ng v\u0169 kh\u00ed."));
        BOOKS.put("vampiric_touch", new SkillBook("vampiric_touch", "Vampiric Touch", SkillType.SET_BONUS, ItemStatsGUI.Rarity.EPIC, "ARMOR", 0.0, 0, "", "Khi t\u1ea5n c\u00f4ng c\u1eadn chi\u1ebfn, h\u00fat l\u1ea1i HP b\u1eb1ng 5%", "l\u01b0\u1ee3ng s\u00e1t th\u01b0\u01a1ng g\u00e2y ra cho m\u1ee5c ti\u00eau."));
        BOOKS.put("adrenaline_rush", new SkillBook("adrenaline_rush", "Adrenaline Rush", SkillType.SET_BONUS, ItemStatsGUI.Rarity.EPIC, "ARMOR", 0.0, 45, "", "Khi HP d\u01b0\u1edbi 40%, t\u0103ng +100 Speed v\u00e0 +30% s\u00e1t th\u01b0\u01a1ng", "trong 8 gi\u00e2y (Th\u1eddi gian h\u1ed3i: 45 gi\u00e2y)."));
        BOOKS.put("cosmic_overlord", new SkillBook("cosmic_overlord", "Cosmic Overlord", SkillType.SET_BONUS, ItemStatsGUI.Rarity.LEGENDARY, "ARMOR", 0.0, 0, "", "T\u0103ng +15% t\u1ed5ng t\u1ea5t c\u1ea3 m\u1ecdi ch\u1ec9 s\u1ed1.", "C\u00f3 5% c\u01a1 h\u1ed9i ph\u1ea3n l\u1ea1i 100% s\u00e1t th\u01b0\u01a1ng chu\u1ea9n nh\u1eadn v\u00e0o."));
        BOOKS.put("tornado_spin", new SkillBook("tornado_spin", "Tornado Spin", SkillType.ABILITY, ItemStatsGUI.Rarity.RARE, "MELEE", 60.0, 10, "RIGHT CLICK", "Xoay v\u0169 kh\u00ed t\u1ea1o c\u01a1n l\u1ed1c \u0111\u1ea9y l\u00f9i qu\u00e1i v\u1eadt (4 \u00f4)", "v\u00e0 g\u00e2y 150 s\u00e1t th\u01b0\u01a1ng di\u1ec7n r\u1ed9ng."));
        BOOKS.put("sharp_edge", new SkillBook("sharp_edge", "Sharp Edge", SkillType.ABILITY, ItemStatsGUI.Rarity.RARE, "MELEE", 40.0, 15, "LEFT CLICK", "\u0110\u00f2n \u0111\u00e1nh ti\u1ebfp theo b\u1ecf qua 30% gi\u00e1p m\u1ee5c ti\u00eau", "v\u00e0 g\u00e2y th\u00eam 100 s\u00e1t th\u01b0\u01a1ng trong 5 gi\u00e2y."));
        BOOKS.put("shadow_teleport", new SkillBook("shadow_teleport", "Shadow Teleport", SkillType.ABILITY, ItemStatsGUI.Rarity.EPIC, "MELEE", 70.0, 12, "RIGHT CLICK", "D\u1ecbch chuy\u1ec3n ra sau l\u01b0ng m\u1ee5c ti\u00eau g\u1ea7n nh\u1ea5t (10 \u00f4)", "g\u00e2y 200 s\u00e1t th\u01b0\u01a1ng k\u00e8m M\u00f9 l\u00f2a (Blindness) 3s."));
        BOOKS.put("bloodlust", new SkillBook("bloodlust", "Bloodlust", SkillType.ABILITY, ItemStatsGUI.Rarity.EPIC, "MELEE", 100.0, 30, "RIGHT CLICK", "K\u00edch ho\u1ea1t Cu\u1ed3ng n\u1ed9: t\u0103ng +30% Atk Speed, +20% S\u00e1t th\u01b0\u01a1ng", "v\u00e0 m\u1ed7i \u0111\u00f2n \u0111\u00e1nh h\u1ed3i 10 HP trong 6 gi\u00e2y."));
        BOOKS.put("blade_of_judgement", new SkillBook("blade_of_judgement", "Blade of Judgement", SkillType.ABILITY, ItemStatsGUI.Rarity.LEGENDARY, "MELEE", 150.0, 25, "RIGHT CLICK", "Tri\u1ec7u h\u1ed3i thanh g\u01b0\u01a1m kh\u1ed5ng l\u1ed3 \u0111\u00e1nh xu\u1ed1ng m\u1ee5c ti\u00eau,", "g\u00e2y 1,000 s\u00e1t th\u01b0\u01a1ng (5 \u00f4) v\u00e0 l\u00e0m ch\u1eadm V trong 2s."));
        BOOKS.put("ground_slam", new SkillBook("ground_slam", "Ground Slam", SkillType.ABILITY, ItemStatsGUI.Rarity.RARE, "MACE", 50.0, 8, "RIGHT CLICK", "\u0110\u1eadp m\u1ea1nh ch\u00f9y xu\u1ed1ng \u0111\u1ea5t h\u1ea5t tung qu\u00e1i v\u1eadt xung quanh", "trong ph\u1ea1m vi 3 \u00f4 l\u00ean tr\u1eddi v\u00e0 g\u00e2y 100 s\u00e1t th\u01b0\u01a1ng."));
        BOOKS.put("heavy_shield", new SkillBook("heavy_shield", "Heavy Shield", SkillType.ABILITY, ItemStatsGUI.Rarity.RARE, "MACE", 60.0, 20, "RIGHT CLICK", "K\u00edch ho\u1ea1t khi\u00ean h\u1ed9 th\u1ec3: nh\u1eadn th\u00eam +200 ph\u00f2ng th\u1ee7", "Defense trong v\u00f2ng 5 gi\u00e2y."));
        BOOKS.put("gravity_pull", new SkillBook("gravity_pull", "Gravity Pull", SkillType.ABILITY, ItemStatsGUI.Rarity.EPIC, "MACE", 80.0, 15, "RIGHT CLICK", "T\u1ea1o h\u1ed1 \u0111en tr\u1ecdng l\u1ef1c h\u00fat m\u1ecdi qu\u00e1i v\u1eadt trong 8 \u00f4", "v\u1ec1 t\u00e2m v\u00e0 l\u00e0m ch\u1eadm ch\u00fang 50% trong 3 gi\u00e2y."));
        BOOKS.put("crushing_blow", new SkillBook("crushing_blow", "Crushing Blow", SkillType.ABILITY, ItemStatsGUI.Rarity.EPIC, "MACE", 70.0, 12, "LEFT CLICK", "\u0110\u00f2n \u0111\u00e1nh ph\u00e1 h\u1ee7y 50% ph\u00f2ng th\u1ee7 m\u1ee5c ti\u00eau trong 6 gi\u00e2y", "v\u00e0 g\u00e2y th\u00eam 150 s\u00e1t th\u01b0\u01a1ng di\u1ec7n r\u1ed9ng xung quanh."));
        BOOKS.put("meteor_strike", new SkillBook("meteor_strike", "Meteor Strike", SkillType.ABILITY, ItemStatsGUI.Rarity.LEGENDARY, "MACE", 180.0, 30, "RIGHT CLICK", "Lao v\u00fat l\u00ean tr\u1eddi r\u1ed3i gi\u00e1ng xu\u1ed1ng nh\u01b0 thi\u00ean th\u1ea1ch,", "g\u00e2y 800 s\u00e1t th\u01b0\u01a1ng di\u1ec7n r\u1ed9ng (6 \u00f4) k\u00e8m Thi\u00eau \u0111\u1ed1t 5s."));
        BOOKS.put("water_spout", new SkillBook("water_spout", "Water Spout", SkillType.ABILITY, ItemStatsGUI.Rarity.RARE, "TRIDENT", 50.0, 10, "RIGHT CLICK", "B\u1eafn ra d\u00f2ng n\u01b0\u1edbc xi\u1ebft \u0111\u1ea9y l\u00f9i qu\u00e1i v\u1eadt tr\u00ean \u0111\u01b0\u1eddng th\u1eb3ng", "d\u00e0i 8 \u00f4 v\u00e0 g\u00e2y 120 s\u00e1t th\u01b0\u01a1ng."));
        BOOKS.put("ocean_healing", new SkillBook("ocean_healing", "Ocean Healing", SkillType.ABILITY, ItemStatsGUI.Rarity.RARE, "TRIDENT", 80.0, 20, "RIGHT CLICK", "T\u1ea1o v\u00f2ng n\u01b0\u1edbc ch\u1eefa tr\u1ecb h\u1ed3i ph\u1ee5c 15% HP t\u1ed1i \u0111a", "cho b\u1ea3n th\u00e2n v\u00e0 \u0111\u1ed3ng minh xung quanh trong 5 \u00f4."));
        BOOKS.put("lightning_chain", new SkillBook("lightning_chain", "Lightning Chain", SkillType.ABILITY, ItemStatsGUI.Rarity.EPIC, "TRIDENT", 100.0, 14, "RIGHT CLICK", "G\u1ecdi tia s\u00e9t gi\u00e1ng xu\u1ed1ng m\u1ee5c ti\u00eau v\u00e0 lan sang 4 qu\u00e1i v\u1eadt", "xung quanh, g\u00e2y 250 s\u00e1t th\u01b0\u01a1ng m\u1ed7i tia s\u00e9t."));
        BOOKS.put("tsunami_wave", new SkillBook("tsunami_wave", "Tsunami Wave", SkillType.ABILITY, ItemStatsGUI.Rarity.EPIC, "TRIDENT", 120.0, 18, "RIGHT CLICK", "Tri\u1ec7u h\u1ed3i m\u1ed9t \u0111\u1ee3t s\u00f3ng th\u1ea7n cu\u1ed1n tr\u00f4i m\u1ecdi qu\u00e1i v\u1eadt", "tr\u00ean \u0111\u01b0\u1eddng \u0111i v\u00e0 g\u00e2y 300 s\u00e1t th\u01b0\u01a1ng di\u1ec7n r\u1ed9ng."));
        BOOKS.put("poseidons_wrath", new SkillBook("poseidons_wrath", "Poseidon's Wrath", SkillType.ABILITY, ItemStatsGUI.Rarity.LEGENDARY, "TRIDENT", 200.0, 40, "RIGHT CLICK", "Tri\u1ec7u h\u1ed3i b\u00e3o t\u1ed1, s\u1ea5m s\u00e9t li\u00ean ho\u00e0n gi\u00e1ng xu\u1ed1ng 8 m\u1ee5c", "ti\u00eau xung quanh ph\u1ea1m vi 15 \u00f4, g\u00e2y 800 s\u00e1t th\u01b0\u01a1ng m\u1ed7i tia."));
    }

    public static class SkillBook {
        public String id;
        public String name;
        public SkillType type;
        public ItemStatsGUI.Rarity rarity;
        public String targetType;
        public double manaCost;
        public int cooldown;
        public String clickType;
        public String desc1;
        public String desc2;

        public SkillBook(String id, String name, SkillType type, ItemStatsGUI.Rarity rarity, String targetType, double manaCost, int cooldown, String clickType, String desc1, String desc2) {
            this.id = id;
            this.name = name;
            this.type = type;
            this.rarity = rarity;
            this.targetType = targetType;
            this.manaCost = manaCost;
            this.cooldown = cooldown;
            this.clickType = clickType;
            this.desc1 = desc1;
            this.desc2 = desc2;
        }
    }

    public static enum SkillType {
        SET_BONUS,
        ABILITY;

    }
}

