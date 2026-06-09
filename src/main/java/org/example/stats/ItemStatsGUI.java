package org.example.stats;

import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
import org.bukkit.Bukkit;
import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
import org.bukkit.Material;
import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
import org.bukkit.NamespacedKey;
import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
import org.bukkit.ChatColor;
import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
import org.bukkit.command.Command;
import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
import org.bukkit.command.CommandExecutor;
import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
import org.bukkit.command.CommandSender;
import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
import org.bukkit.conversations.*;
import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
import org.bukkit.entity.Player;
import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
import org.bukkit.event.EventHandler;
import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
import org.bukkit.event.Listener;
import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
import org.bukkit.event.inventory.ClickType;
import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
import org.bukkit.inventory.Inventory;
import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
import org.bukkit.inventory.ItemStack;
import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
import org.bukkit.persistence.PersistentDataContainer;
import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
import org.bukkit.persistence.PersistentDataType;
import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
import org.example.core.CustomWeaponEngine;

import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
import java.util.*;

/**
 * ⚙️ Item Custom Stats Engine — Admin GUI
 * Lệnh: /cwestats | Permission: cwe.admin hoặc OP
 * Mở GUI 54 ô để chỉnh sửa 7 chỉ số RPG, Rarity và Full Set Bonus
 * của vật phẩm đang cầm trên tay chính.
 */
import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
public class ItemStatsGUI implements Listener, CommandExecutor {

    private final JavaPlugin plugin;

    // PDC Keys
    public static final String KEY_STRENGTH      = "stat_strength";
    public static final String KEY_CRIT_CHANCE   = "stat_crit_chance";
    public static final String KEY_CRIT_DAMAGE   = "stat_crit_damage";
    public static final String KEY_HEALTH        = "stat_health";
    public static final String KEY_DEFENSE       = "stat_defense";
    public static final String KEY_INTELLIGENCE  = "stat_intelligence";
    public static final String KEY_SPEED         = "stat_speed";
    public static final String KEY_RARITY        = "stat_rarity";
    public static final String KEY_SETBONUS_TITLE = "stat_setbonus_title";
    public static final String KEY_SETBONUS_DESC1 = "stat_setbonus_desc1";
    public static final String KEY_SETBONUS_DESC2 = "stat_setbonus_desc2";
    public static final String KEY_ABILITY_CLICK = "stat_ability_click";
    public static final String KEY_HAS_STATS     = "cwe_item_stats";

    // Rarity values
    public enum Rarity {
        NONE("§7NONE", "§7"),
        COMMON("§fCOMMON", "§f"),
        UNCOMMON("§aUNCOMMON", "§a"),
        RARE("§9RARE", "§9"),
        EPIC("§5EPIC", "§5"),
        LEGENDARY("§6LEGENDARY", "§6"),
        MYTHIC("§d§lMYTHIC", "§d§l");

        public final String display;
        public final String color;
        Rarity(String display, String color) { this.display = display; this.color = color; }
    }

    // GUI title
    private static final String GUI_TITLE = "§4⚙ §lItem Stats Editor";

    // Temp working data per player UUID: stores values being edited before Save
    private final Map<UUID, double[]> tempStats = new HashMap<>();
    // Index: 0=strength, 1=critChance, 2=critDamage, 3=health, 4=defense, 5=intelligence, 6=speed
    private final Map<UUID, Rarity>  tempRarity = new HashMap<>();
    private final Map<UUID, String>  tempBonusTitle = new HashMap<>();
    private final Map<UUID, String>  tempBonusDesc1 = new HashMap<>();
    private final Map<UUID, String>  tempBonusDesc2 = new HashMap<>();
    private final Map<UUID, String>  tempClickType = new HashMap<>();
    private final Map<UUID, Boolean> tempIsWeaponMode = new HashMap<>();
    private final Map<UUID, Double>  tempDamage = new HashMap<>();
    private final Set<UUID> editingChat = new HashSet<>();

    // GUI slot layout
    // Row 0 (slots 0-8): decorative header
    // Row 1-4 (slots 9-44): 7 stats displayed in pairs
    // Row 5 (slots 45-53): Rarity, Set Bonus, Save, Clear
    private static final int[] STAT_SLOTS = {10, 19, 28, 37, 12, 21, 30}; // strength, critChance, critDamage, health, defense, intel, speed
    private static final String[] STAT_KEYS = {KEY_STRENGTH, KEY_CRIT_CHANCE, KEY_CRIT_DAMAGE, KEY_HEALTH, KEY_DEFENSE, KEY_INTELLIGENCE, KEY_SPEED};
    private static final String[] STAT_NAMES = {"Strength", "Crit Chance", "Crit Damage", "Health", "Defense", "Intelligence", "Speed"};
    private static final String[] STAT_FORMATS = {"§c+%.0f", "§9+%.1f%%", "§5+%.1f%%", "§a+%.0f HP", "§a+%.0f", "§a+%.0f", "§a+%.1f%%"};
    private static final Material[] STAT_ICONS = {
        Material.IRON_SWORD,       // Strength
        Material.ARROW,            // Crit Chance
        Material.SPECTRAL_ARROW,   // Crit Damage
        Material.RED_DYE,          // Health
        Material.SHIELD,           // Defense
        Material.BOOK,             // Intelligence
        Material.SUGAR,            // Speed
    };

    private static final int SLOT_RARITY    = 46;
    private static final int SLOT_BONUS     = 48;
    private static final int SLOT_SAVE      = 50;
    private static final int SLOT_CLICK_TYPE = 51;
    private static final int SLOT_CLEAR     = 52;

    public ItemStatsGUI(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    // ─── COMMAND ──────────────────────────────────────────────────────────────

    private static final String BRANCH_GUI_TITLE = "§8Chọn loại Item Settings";

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§c[CWE] Lệnh này chỉ dành cho người chơi!");
            return true;
        }
        Player player = (Player) sender;
        if (!player.isOp() && !player.hasPermission("cwe.admin")) {
            player.sendMessage("§c[CWE] Bạn không có quyền dùng lệnh /cwestats!");
            return true;
        }
        ItemStack held = player.getInventory().getItemInMainHand();
        if (held == null || held.getType().isAir()) {
            player.sendMessage("§c[CWE] Bạn phải §ecầm một trang bị §ftrên tay chính!");
            return true;
        }
        openBranchGUI(player);
        return true;
    }

    private void openBranchGUI(Player player) {
        Inventory gui = Bukkit.createInventory(null, 27, BRANCH_GUI_TITLE);
        
        ItemStack armor = new ItemStack(Material.DIAMOND_CHESTPLATE);
        ItemMeta aMeta = armor.getItemMeta();
        if (aMeta != null) {
            aMeta.setDisplayName("§bCustom Armor");
            armor.setItemMeta(aMeta);
        }
        gui.setItem(11, armor);
        
        ItemStack weapon = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta wMeta = weapon.getItemMeta();
        if (wMeta != null) {
            wMeta.setDisplayName("§cCustom Weapon");
            weapon.setItemMeta(wMeta);
        }
        gui.setItem(15, weapon);
        
        player.openInventory(gui);
    }

    // ─── OPEN GUI ─────────────────────────────────────────────────────────────

    public void openStatEditor(Player player, ItemStack targetItem, boolean loadFromItem, boolean isWeaponMode) {
        UUID uuid = player.getUniqueId();

        if (loadFromItem || !tempStats.containsKey(uuid)) {
            double[] stats = new double[7];
            double damage = 0.0;
            Rarity rarity = Rarity.NONE;
            String bonusTitle = "", bonusDesc1 = "", bonusDesc2 = "", clickType = "RIGHT CLICK";

            if (targetItem != null && targetItem.hasItemMeta()) {
                PersistentDataContainer pdc = targetItem.getItemMeta().getPersistentDataContainer();
                stats[0] = getDouble(pdc, KEY_STRENGTH);
                stats[1] = getDouble(pdc, KEY_CRIT_CHANCE);
                stats[2] = getDouble(pdc, KEY_CRIT_DAMAGE);
                stats[3] = getDouble(pdc, KEY_HEALTH);
                stats[4] = getDouble(pdc, KEY_DEFENSE);
                stats[5] = getDouble(pdc, KEY_INTELLIGENCE);
                stats[6] = getDouble(pdc, KEY_SPEED);
                
                NamespacedKey dmgKey = new NamespacedKey(plugin, "stat_damage");
                NamespacedKey dmgKeyAlt = new NamespacedKey(plugin, "cwe_damage");
                damage = pdc.has(dmgKey, PersistentDataType.DOUBLE) ? pdc.getOrDefault(dmgKey, PersistentDataType.DOUBLE, 0.0) :
                         (pdc.has(dmgKeyAlt, PersistentDataType.DOUBLE) ? pdc.getOrDefault(dmgKeyAlt, PersistentDataType.DOUBLE, 0.0) : 0.0);
                
                String rarStr = getString(pdc, KEY_RARITY, "NONE");
                try { rarity = Rarity.valueOf(rarStr); } catch (Exception e) { rarity = Rarity.NONE; }
                bonusTitle = getString(pdc, KEY_SETBONUS_TITLE, "");
                bonusDesc1 = getString(pdc, KEY_SETBONUS_DESC1, "");
                bonusDesc2 = getString(pdc, KEY_SETBONUS_DESC2, "");
                clickType = getString(pdc, KEY_ABILITY_CLICK, "RIGHT CLICK");
            }

            tempStats.put(uuid, stats);
            tempDamage.put(uuid, damage);
            tempRarity.put(uuid, rarity);
            tempBonusTitle.put(uuid, bonusTitle);
            tempBonusDesc1.put(uuid, bonusDesc1);
            tempBonusDesc2.put(uuid, bonusDesc2);
            tempClickType.put(uuid, clickType);
            tempIsWeaponMode.put(uuid, isWeaponMode);
        }

        Inventory gui = buildGUI(player);
        player.openInventory(gui);
        if (loadFromItem) {
            player.sendMessage("§a[CWE Stats] §fMở bảng chỉnh sửa chỉ số. §eClick §fvào các nút ±1/±10/±100 để điều chỉnh, sau đó §aLƯU§f.");
        }
    }

    private Inventory buildGUI(Player player) {
        UUID uuid = player.getUniqueId();
        double[] stats = tempStats.getOrDefault(uuid, new double[7]);
        Rarity rarity = tempRarity.getOrDefault(uuid, Rarity.NONE);
        String bonusTitle = tempBonusTitle.getOrDefault(uuid, "");
        String bonusDesc1 = tempBonusDesc1.getOrDefault(uuid, "");
        String bonusDesc2 = tempBonusDesc2.getOrDefault(uuid, "");
        String clickType = tempClickType.getOrDefault(uuid, "RIGHT CLICK");
        boolean isWeapon = tempIsWeaponMode.getOrDefault(uuid, false);
        double damage = tempDamage.getOrDefault(uuid, 0.0);

        Inventory gui = Bukkit.createInventory(null, 54, GUI_TITLE);
        ItemStack bg = makeGlass(Material.GRAY_STAINED_GLASS_PANE, "§8 ");
        ItemStack bgDark = makeGlass(Material.BLACK_STAINED_GLASS_PANE, "§8 ");
        ItemStack bgBlue = makeGlass(Material.BLUE_STAINED_GLASS_PANE, "§8 ");
        for (int i = 0; i < 54; i++) gui.setItem(i, bg);
        for (int i = 0; i < 9; i++) gui.setItem(i, bgBlue);
        for (int i = 45; i < 54; i++) gui.setItem(i, bgDark);

        ItemStack header = new ItemStack(Material.ENCHANTED_BOOK);
        ItemMeta hm = header.getItemMeta();
        if (hm != null) {
            hm.setDisplayName(isWeapon ? "§c⚙ §lWeapon Stats Editor" : "§b⚙ §lArmor Stats Editor");
            hm.setLore(Arrays.asList("§7Chỉnh sửa chỉ số", "§aNhấn SAVE để lưu vào vật phẩm."));
            header.setItemMeta(hm);
        }
        gui.setItem(4, header);

        ItemStack backBtn = new ItemStack(Material.ARROW);
        ItemMeta bm = backBtn.getItemMeta();
        if (bm != null) {
            bm.setDisplayName("§c⬅ Quay lại Chọn Loại Item");
            backBtn.setItemMeta(bm);
        }
        gui.setItem(8, backBtn);

        if (isWeapon) {
            // Weapon mode: Damage(7), Strength(0), CritChance(1), CritDamage(2), Intel(5)
            placeStatRow(gui, 9, 7, damage); // Index 7 will be treated specially for Damage
            placeStatRow(gui, 18, 0, stats[0]);
            placeStatRow(gui, 27, 1, stats[1]);
            placeStatRow(gui, 36, 2, stats[2]);
            placeCompactStat(gui, 45, 5, stats[5]);
        } else {
            // Armor mode: Health(3), Defense(4), Strength(0), CritDamage(2), Speed(6), Intel(5)
            placeStatRow(gui, 9, 3, stats[3]);
            placeStatRow(gui, 18, 4, stats[4]);
            placeStatRow(gui, 27, 0, stats[0]);
            placeStatRow(gui, 36, 2, stats[2]);
            placeCompactStat(gui, 45, 6, stats[6]);
            placeCompactStat(gui, 47, 5, stats[5]);
            placeCompactStat(gui, 49, 1, stats[1]);
        }

        gui.setItem(SLOT_RARITY, buildRarityButton(rarity));
        gui.setItem(SLOT_BONUS, buildBonusButton(bonusTitle, bonusDesc1, bonusDesc2));

        ItemStack saveBtn = new ItemStack(Material.LIME_DYE);
        ItemMeta sm = saveBtn.getItemMeta();
        if (sm != null) {
            sm.setDisplayName("§a§lLƯU CHỈ SỐ VÀO VẬT PHẨM");
            saveBtn.setItemMeta(sm);
        }
        gui.setItem(SLOT_SAVE, saveBtn);
        
        if (isWeapon) {
            ItemStack clickBtn = new ItemStack(Material.LEVER);
            ItemMeta ckm = clickBtn.getItemMeta();
            if (ckm != null) {
                ckm.setDisplayName("§6🖱 Tùy Chọn Click: §e§l" + clickType);
                ckm.setLore(Arrays.asList("§7Chuyển đổi giữa", "§7RIGHT CLICK và LEFT CLICK.", "", "§eClick để đổi!"));
                clickBtn.setItemMeta(ckm);
            }
            gui.setItem(SLOT_CLICK_TYPE, clickBtn);
        }

        ItemStack clearBtn = new ItemStack(Material.RED_DYE);
        ItemMeta cm = clearBtn.getItemMeta();
        if (cm != null) {
            cm.setDisplayName("§c§lXÓA TẤT CẢ CHỈ SỐ");
            clearBtn.setItemMeta(cm);
        }
        gui.setItem(SLOT_CLEAR, clearBtn);

        return gui;
    }

    private void placeStatRow(Inventory gui, int base, int statIndex, double value) {
        ItemStack bg = makeGlass(Material.GRAY_STAINED_GLASS_PANE, "§8 ");
        gui.setItem(base,     makeDeltaButton(statIndex, -100));
        gui.setItem(base + 1, makeDeltaButton(statIndex, -10));
        gui.setItem(base + 2, makeDeltaButton(statIndex, -1));
        gui.setItem(base + 3, buildStatIcon(statIndex, value));
        gui.setItem(base + 4, makeDeltaButton(statIndex, +1));
        gui.setItem(base + 5, makeDeltaButton(statIndex, +10));
        gui.setItem(base + 6, makeDeltaButton(statIndex, +100));
        gui.setItem(base + 7, bg);
        gui.setItem(base + 8, bg);
    }

    private void placeCompactStat(Inventory gui, int slot, int statIndex, double value) {
        gui.setItem(slot, buildStatIcon(statIndex, value));
    }

    private ItemStack buildStatIcon(int si, double value) {
        String name;
        String format;
        Material mat;
        if (si == 7) {
            name = "Damage"; format = "§c+%.0f"; mat = Material.DIAMOND_SWORD;
        } else {
            name = STAT_NAMES[si]; format = STAT_FORMATS[si]; mat = STAT_ICONS[si];
        }
        ItemStack icon = new ItemStack(mat);
        ItemMeta meta = icon.getItemMeta();
        if (meta != null) {
            meta.setDisplayName("§e" + name + ": " + String.format(format, value));
            meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "gui_stat_index"), PersistentDataType.INTEGER, si);
            icon.setItemMeta(meta);
        }
        return icon;
    }

    private ItemStack makeDeltaButton(int statIndex, double delta) {
        Material mat = (delta > 0) ? Material.LIME_STAINED_GLASS_PANE : Material.RED_STAINED_GLASS_PANE;
        ItemStack btn = new ItemStack(mat);
        ItemMeta meta = btn.getItemMeta();
        if (meta != null) {
            meta.setDisplayName((delta > 0 ? "§a+" : "§c") + (int)delta);
            meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "gui_stat_index"), PersistentDataType.INTEGER, statIndex);
            meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "gui_delta"), PersistentDataType.DOUBLE, delta);
            btn.setItemMeta(meta);
        }
        return btn;
    }

    private ItemStack makeGlass(Material mat, String name) {
        ItemStack g = new ItemStack(mat);
        ItemMeta m = g.getItemMeta();
        if (m != null) { m.setDisplayName(name); g.setItemMeta(m); }
        return g;
    }

    private ItemStack buildRarityButton(Rarity rarity) {
        ItemStack icon = new ItemStack(Material.NETHER_STAR);
        ItemMeta meta = icon.getItemMeta();
        if (meta != null) {
            meta.setDisplayName("§d⋆ Phẩm Chất: " + rarity.display);
            icon.setItemMeta(meta);
        }
        return icon;
    }

    private ItemStack buildBonusButton(String title, String desc1, String desc2) {
        ItemStack icon = new ItemStack(Material.GOLD_INGOT);
        ItemMeta meta = icon.getItemMeta();
        if (meta != null) {
            meta.setDisplayName("§6⚡ Full Set Bonus");
            meta.setLore(Arrays.asList("§7Title: " + title, "§7Desc1: " + desc1, "§7Desc2: " + desc2));
            icon.setItemMeta(meta);
        }
        return icon;
    }

    @EventHandler
    public void onGuiClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        Player player = (Player) event.getWhoClicked();
        
        if (event.getView().getTitle().equals(BRANCH_GUI_TITLE)) {
            event.setCancelled(true);
            if (event.getClickedInventory() != event.getView().getTopInventory()) return;
            
            ItemStack held = player.getInventory().getItemInMainHand();
            if (held == null || held.getType().isAir()) return;
            
            if (event.getSlot() == 11) {
                openStatEditor(player, held, true, false);
            } else if (event.getSlot() == 15) {
                openStatEditor(player, held, true, true);
            }
            return;
        }

        if (!event.getView().getTitle().equals(GUI_TITLE)) return;
        event.setCancelled(true);
        UUID uuid = player.getUniqueId();
        if (event.getClickedInventory() != event.getView().getTopInventory()) return;
        int slot = event.getSlot();

        if (slot == SLOT_SAVE) {
            saveStatsToItem(player);
            return;
        }
        if (slot == 8) {
            openBranchGUI(player);
            return;
        }
        if (slot == SLOT_CLEAR) {
            tempStats.put(uuid, new double[7]);
            tempDamage.put(uuid, 0.0);
            tempRarity.put(uuid, Rarity.NONE);
            tempBonusTitle.put(uuid, "");
            tempBonusDesc1.put(uuid, "");
            tempBonusDesc2.put(uuid, "");
            tempClickType.put(uuid, "RIGHT CLICK");
            refreshGUI(event.getView().getTopInventory(), player);
            return;
        }
        if (slot == SLOT_RARITY) {
            Rarity current = tempRarity.getOrDefault(uuid, Rarity.NONE);
            Rarity next = Rarity.values()[(current.ordinal() + 1) % Rarity.values().length];
            tempRarity.put(uuid, next);
            event.getView().getTopInventory().setItem(SLOT_RARITY, buildRarityButton(next));
            return;
        }
        if (slot == SLOT_CLICK_TYPE && tempIsWeaponMode.getOrDefault(uuid, false)) {
            String current = tempClickType.getOrDefault(uuid, "RIGHT CLICK");
            String next = current.equals("RIGHT CLICK") ? "LEFT CLICK" : "RIGHT CLICK";
            tempClickType.put(uuid, next);
            
            ItemStack clickBtn = new ItemStack(Material.LEVER);
            ItemMeta ckm = clickBtn.getItemMeta();
            if (ckm != null) {
                ckm.setDisplayName("§6🖱 Tùy Chọn Click: §e§l" + next);
                ckm.setLore(Arrays.asList("§7Chuyển đổi giữa", "§7RIGHT CLICK và LEFT CLICK.", "", "§eClick để đổi!"));
                clickBtn.setItemMeta(ckm);
            }
            event.getView().getTopInventory().setItem(SLOT_CLICK_TYPE, clickBtn);
            return;
        }
        if (slot == SLOT_BONUS) {
            handleBonusClick(player, event.getClick(), event.getView().getTopInventory());
            return;
        }

        ItemStack clicked = event.getCurrentItem();
        if (clicked == null || !clicked.hasItemMeta()) return;
        PersistentDataContainer pdc = clicked.getItemMeta().getPersistentDataContainer();
        if (!pdc.has(new NamespacedKey(plugin, "gui_stat_index"), PersistentDataType.INTEGER)) return;
        int si = pdc.get(new NamespacedKey(plugin, "gui_stat_index"), PersistentDataType.INTEGER);

        double delta = pdc.has(new NamespacedKey(plugin, "gui_delta"), PersistentDataType.DOUBLE)
            ? pdc.get(new NamespacedKey(plugin, "gui_delta"), PersistentDataType.DOUBLE)
            : (event.isLeftClick() ? 1.0 : -1.0);
        
        if (si == 7) {
            double dmg = tempDamage.getOrDefault(uuid, 0.0);
            tempDamage.put(uuid, Math.max(0, dmg + delta));
        } else {
            double[] stats = tempStats.getOrDefault(uuid, new double[7]);
            stats[si] = Math.max(0, stats[si] + delta);
            tempStats.put(uuid, stats);
        }
        refreshGUI(event.getView().getTopInventory(), player);
    }

    private void handleBonusClick(Player player, ClickType click, Inventory gui) {
        UUID uuid = player.getUniqueId();
        editingChat.add(uuid);
        player.closeInventory();
        String field = (click == ClickType.LEFT) ? "title" : (click == ClickType.RIGHT) ? "desc1" : "desc2";
        
        player.sendMessage("§a[CWE Stats] §fNhập dữ liệu (" + field + "):");
        new ConversationFactory(plugin).withModality(true).withFirstPrompt(new StringPrompt() {
            @Override public String getPromptText(ConversationContext ctx) { return ""; }
            @Override public Prompt acceptInput(ConversationContext ctx, String input) {
                if (!input.equalsIgnoreCase("cancel")) {
                    String coloredInput = org.bukkit.ChatColor.translateAlternateColorCodes('&', input);
                    if (field.equals("title")) tempBonusTitle.put(uuid, coloredInput);
                    else if (field.equals("desc1")) tempBonusDesc1.put(uuid, coloredInput);
                    else tempBonusDesc2.put(uuid, coloredInput);
                }
                Bukkit.getScheduler().runTask(plugin, () -> {
                    ItemStack held = player.getInventory().getItemInMainHand();
                    if (held != null && !held.getType().isAir()) {
                        saveStatsToItemWithoutClosing(player);
                        openStatEditor(player, held, false, tempIsWeaponMode.getOrDefault(uuid, false));
                    }
                });
                return Prompt.END_OF_CONVERSATION;
            }
        }).withEscapeSequence("cancel").withLocalEcho(false).buildConversation(player).begin();
    }

    private void refreshGUI(Inventory gui, Player player) {
        UUID uuid = player.getUniqueId();
        boolean isWeapon = tempIsWeaponMode.getOrDefault(uuid, false);
        double[] stats = tempStats.getOrDefault(uuid, new double[7]);
        double damage = tempDamage.getOrDefault(uuid, 0.0);
        
        if (isWeapon) {
            placeStatRow(gui, 9, 7, damage); 
            placeStatRow(gui, 18, 0, stats[0]);
            placeStatRow(gui, 27, 1, stats[1]);
            placeStatRow(gui, 36, 2, stats[2]);
            placeCompactStat(gui, 45, 5, stats[5]);
        } else {
            placeStatRow(gui, 9, 3, stats[3]);
            placeStatRow(gui, 18, 4, stats[4]);
            placeStatRow(gui, 27, 0, stats[0]);
            placeStatRow(gui, 36, 2, stats[2]);
            placeCompactStat(gui, 45, 6, stats[6]);
            placeCompactStat(gui, 47, 5, stats[5]);
            placeCompactStat(gui, 49, 1, stats[1]);
        }
        gui.setItem(SLOT_RARITY, buildRarityButton(tempRarity.get(uuid)));
        gui.setItem(SLOT_BONUS, buildBonusButton(tempBonusTitle.get(uuid), tempBonusDesc1.get(uuid), tempBonusDesc2.get(uuid)));
    }

    private void saveStatsToItem(Player player) {
        if (!saveStatsToItemWithoutClosing(player)) return;

        player.closeInventory();
        ItemStack held = player.getInventory().getItemInMainHand();
        player.sendMessage("§a[CWE Stats] §f✔ Đã lưu chỉ số vào §e" + held.getType().name() + "§f thành công!");
        player.playSound(player.getLocation(), org.bukkit.Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1f, 1.2f);
        
        cleanTempData(player.getUniqueId());
    }

    private boolean saveStatsToItemWithoutClosing(Player player) {
        UUID uuid = player.getUniqueId();
        ItemStack held = player.getInventory().getItemInMainHand();
        if (held == null || held.getType().isAir()) return false;
        ItemMeta meta = held.getItemMeta();
        if (meta == null) return false;
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        double[] stats = tempStats.getOrDefault(uuid, new double[7]);
        double damage = tempDamage.getOrDefault(uuid, 0.0);
        setDouble(pdc, "stat_damage", damage);
        setDouble(pdc, KEY_STRENGTH, stats[0]);
        setDouble(pdc, KEY_CRIT_CHANCE, stats[1]);
        setDouble(pdc, KEY_CRIT_DAMAGE, stats[2]);
        setDouble(pdc, KEY_HEALTH, stats[3]);
        setDouble(pdc, KEY_DEFENSE, stats[4]);
        setDouble(pdc, KEY_INTELLIGENCE, stats[5]);
        setDouble(pdc, KEY_SPEED, stats[6]);
        
        Rarity rarity = tempRarity.getOrDefault(uuid, Rarity.NONE);
        String bTitle = tempBonusTitle.getOrDefault(uuid, "");
        String bD1 = tempBonusDesc1.getOrDefault(uuid, "");
        String bD2 = tempBonusDesc2.getOrDefault(uuid, "");
        String clickType = tempClickType.getOrDefault(uuid, "RIGHT CLICK");

        setString(pdc, KEY_RARITY, rarity.name());
        setString(pdc, KEY_SETBONUS_TITLE, bTitle);
        setString(pdc, KEY_SETBONUS_DESC1, bD1);
        setString(pdc, KEY_SETBONUS_DESC2, bD2);
        setString(pdc, KEY_ABILITY_CLICK, clickType);
        boolean isWeapon = tempIsWeaponMode.getOrDefault(uuid, false);
        pdc.set(new NamespacedKey(plugin, "cwe_is_weapon"), PersistentDataType.INTEGER, isWeapon ? 1 : 0);
        pdc.set(new NamespacedKey(plugin, KEY_HAS_STATS), PersistentDataType.INTEGER, 1);

        rebuildLore(held, meta, stats, damage, rarity, bTitle, bD1, bD2, clickType, isWeapon);
        held.setItemMeta(meta);
        
        // Rebuild full lore via central ItemBuilder
        ItemBuilder.updateItem(held);
        
        player.getInventory().setItemInMainHand(held);
        player.updateInventory();
        return true;
    }

    private static void addBlankLineIfNeeded(List<String> list) {
        if (!list.isEmpty() && !list.get(list.size() - 1).equals("§f")) {
            list.add("§f");
        }
    }

    public static void rebuildLore(ItemStack item, ItemMeta meta, double[] stats, double damage, Rarity rarity, String bTitle, String bD1, String bD2, String clickType, boolean isWeapon) {
        List<String> existingLore = meta.hasLore() ? meta.getLore() : new ArrayList<>();
        if (existingLore == null) existingLore = new ArrayList<>();

        List<String> enchantLines = new ArrayList<>();
        for (String line : existingLore) {
            if (isEnchantLoreLine(line)) {
                enchantLines.add(line);
            }
        }

        List<String> newLore = new ArrayList<>();

        if (isWeapon) {
            if (damage > 0) newLore.add(String.format("§7Damage: §c+%.0f", damage));
            if (stats[0] > 0) newLore.add(String.format("§7Strength: §c+%.0f", stats[0]));
            if (damage > 0 || stats[0] > 0) newLore.add("§f");
            
            if (stats[1] > 0) newLore.add(String.format("§7Crit Chance: §9+%.0f%%", stats[1]));
            if (stats[2] > 0) newLore.add(String.format("§7Crit Damage: §5+%.0f%%", stats[2]));
            if (stats[5] > 0) newLore.add(String.format("§7Intelligence: §a+%.0f", stats[5]));
            if (stats[1] > 0 || stats[2] > 0 || stats[5] > 0) newLore.add("§f");

            if (!bTitle.isEmpty()) {
                newLore.add("§6Item Ability: " + bTitle + " §e§l[" + clickType + "]");
                if (!bD1.isEmpty()) newLore.add("§7" + bD1);
                if (!bD2.isEmpty()) newLore.add("§7" + bD2);
                
                FileConfiguration config = CustomWeaponEngine.getInstance().getConfig();
                String weaponId = getWeaponIdFromDisplayName(meta.getDisplayName());
                if (weaponId != null) {
                    double baseManaCost = config.getDouble("weapons." + weaponId + ".mana-cost", 0.0);
                    int cooldownSec = config.getInt("weapons." + weaponId + ".cooldown", 0);
                    if (baseManaCost > 0) newLore.add("§9Mana Cost: §3" + (int)baseManaCost);
                    if (cooldownSec > 0) newLore.add("§9Cooldown: §a" + cooldownSec + "s");
                }
                newLore.add("§f");
            }
        } else {
            if (stats[3] > 0) newLore.add(String.format("§7Health: §c+%.0f", stats[3]));
            if (stats[4] > 0) newLore.add(String.format("§7Defense: §a+%.0f", stats[4]));
            if (stats[0] > 0) newLore.add(String.format("§7Strength: §c+%.0f", stats[0]));
            if (stats[3] > 0 || stats[4] > 0 || stats[0] > 0) newLore.add("§f");

            if (stats[1] > 0) newLore.add(String.format("§7Crit Chance: §9+%.0f%%", stats[1]));
            if (stats[2] > 0) newLore.add(String.format("§7Crit Damage: §5+%.0f%%", stats[2]));
            if (stats[6] > 0) newLore.add(String.format("§7Speed: §f+%.0f", stats[6]));
            if (stats[5] > 0) newLore.add(String.format("§7Intelligence: §a+%.0f", stats[5]));
            if (stats[1] > 0 || stats[2] > 0 || stats[6] > 0 || stats[5] > 0) newLore.add("§f");

            if (!bTitle.isEmpty()) {
                newLore.add("§6Full Set Bonus: " + bTitle);
                if (!bD1.isEmpty()) newLore.add("§7" + bD1);
                if (!bD2.isEmpty()) newLore.add("§7" + bD2);
                newLore.add("§f");
            }
        }

        if (!enchantLines.isEmpty()) {
            newLore.addAll(enchantLines);
            newLore.add("§f");
        }

        if (rarity != Rarity.NONE) {
            String rarityStr = rarity.color + "§l" + rarity.name();
            if (isWeapon) {
                newLore.add(rarityStr + " " + getWeaponTypeName(item));
            } else {
                newLore.add(rarityStr + " " + getArmorTypeName(item));
            }
        }

        meta.setLore(newLore);
    }

    public static String getArmorTypeName(ItemStack item) {
        if (item == null) return "HELMET";
        String name = item.getType().name();
        if (name.contains("HELMET")) return "HELMET";
        if (name.contains("CHESTPLATE")) return "CHESTPLATE";
        if (name.contains("LEGGINGS")) return "LEGGINGS";
        if (name.contains("BOOTS")) return "BOOTS";
        return "HELMET";
    }

    public static boolean isWeapon(ItemStack item) {
        if (item == null) return false;
        Material mat = item.getType();
        String name = mat.name();
        if (item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
            if (item.getItemMeta().getDisplayName().contains("Wand")) return true;
        }
        return name.contains("SWORD") || name.contains("AXE") || mat == Material.STICK || mat == Material.BLAZE_ROD;
    }

    public static String getWeaponTypeName(ItemStack item) {
        if (item == null) return "WEAPON";
        Material mat = item.getType();
        String name = mat.name();
        if (item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
            if (item.getItemMeta().getDisplayName().contains("Wand")) return "WAND";
        }
        if (name.contains("SWORD")) return "SWORD";
        if (name.contains("AXE")) return "AXE";
        if (mat == Material.STICK || mat == Material.BLAZE_ROD) return "WAND";
        return "WEAPON";
    }

    private static String getWeaponIdFromDisplayName(String displayName) {
        if (displayName == null) return null;
        FileConfiguration config = CustomWeaponEngine.getInstance().getConfig();
        if (!config.contains("weapons")) return null;
        for (String id : config.getConfigurationSection("weapons").getKeys(false)) {
            String configName = config.getString("weapons." + id + ".display-name");
            if (configName == null) continue;
            String cleanName = ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', configName));
            if (ChatColor.stripColor(displayName).contains(cleanName)) return id;
        }
        return null;
    }

    private static boolean isEnchantLoreLine(String line) {
        if (!line.startsWith("§9")) return false;
        for (CustomEnchant enc : CustomEnchant.values()) {
            if (line.contains(enc.getDisplayName())) return true;
        }
        return false;
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        if (!event.getView().getTitle().equals(GUI_TITLE)) return;
        UUID uuid = event.getPlayer().getUniqueId();
        if (editingChat.contains(uuid)) {
            editingChat.remove(uuid);
        } else {
            cleanTempData(uuid);
        }
    }

    @EventHandler
    public void onQuit(org.bukkit.event.player.PlayerQuitEvent event) {
        cleanTempData(event.getPlayer().getUniqueId());
    }

    private void cleanTempData(UUID uuid) {
        tempStats.remove(uuid);
        tempDamage.remove(uuid);
        tempRarity.remove(uuid);
        tempBonusTitle.remove(uuid);
        tempBonusDesc1.remove(uuid);
        tempBonusDesc2.remove(uuid);
        tempClickType.remove(uuid);
        tempIsWeaponMode.remove(uuid);
        editingChat.remove(uuid);
    }

    // ─── PDC HELPERS ──────────────────────────────────────────────────────────

    private double getDouble(PersistentDataContainer pdc, String key) {
        NamespacedKey k = new NamespacedKey(plugin, key);
        return pdc.has(k, PersistentDataType.DOUBLE)
            ? pdc.getOrDefault(k, PersistentDataType.DOUBLE, 0.0)
            : 0.0;
    }

    private void setDouble(PersistentDataContainer pdc, String key, double value) {
        NamespacedKey k = new NamespacedKey(plugin, key);
        if (value == 0.0) pdc.remove(k);
        else pdc.set(k, PersistentDataType.DOUBLE, value);
    }

    private String getString(PersistentDataContainer pdc, String key, String def) {
        NamespacedKey k = new NamespacedKey(plugin, key);
        return pdc.has(k, PersistentDataType.STRING)
            ? pdc.getOrDefault(k, PersistentDataType.STRING, def)
            : def;
    }

    private void setString(PersistentDataContainer pdc, String key, String value) {
        NamespacedKey k = new NamespacedKey(plugin, key);
        if (value == null || value.isEmpty()) pdc.remove(k);
        else pdc.set(k, PersistentDataType.STRING, value);
    }
}


