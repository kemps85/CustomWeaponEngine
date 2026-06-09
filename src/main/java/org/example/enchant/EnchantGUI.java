/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Material
 *  org.bukkit.NamespacedKey
 *  org.bukkit.Sound
 *  org.bukkit.block.Block
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.enchantments.Enchantment
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.block.Action
 *  org.bukkit.event.inventory.ClickType
 *  org.bukkit.event.inventory.InventoryClickEvent
 *  org.bukkit.event.inventory.InventoryCloseEvent
 *  org.bukkit.event.inventory.InventoryType
 *  org.bukkit.event.inventory.PrepareGrindstoneEvent
 *  org.bukkit.event.player.PlayerInteractEvent
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.inventory.ItemFlag
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
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareGrindstoneEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.example.enchant.BookshelfScanner;
import org.example.enchant.CustomEnchant;
import org.example.enchant.EnchantManager;
import org.example.enchant.UltimateEnchant;

public class EnchantGUI
implements Listener,
CommandExecutor {
    private final JavaPlugin plugin;
    private final EnchantManager enchantManager;
    private final String guiName = "Enchant Item";
    private final String adminGuiName = "\u00a74\u2699 \u00a7lAdmin Enchant Editor";
    private final Map<UUID, Integer> activeBookshelves = new HashMap<UUID, Integer>();
    private final Set<UUID> adminGuiPlayers = new HashSet<UUID>();

    public EnchantGUI(JavaPlugin plugin, EnchantManager enchantManager) {
        this.plugin = plugin;
        this.enchantManager = enchantManager;
    }

    private boolean isValidEnchantable(ItemStack item) {
        if (item == null || item.getType().isAir()) {
            return false;
        }
        Material mat = item.getType();
        String name = mat.name();
        if (mat == Material.BOOK) {
            return true;
        }
        if (item.hasItemMeta() && item.getItemMeta().hasDisplayName() && item.getItemMeta().getDisplayName().contains("Wand")) {
            return true;
        }
        return name.contains("SWORD") || name.contains("AXE") || name.contains("PICKAXE") || name.contains("SHOVEL") || name.contains("HOE") || name.contains("BOW") || name.contains("CROSSBOW") || name.contains("HELMET") || name.contains("CHESTPLATE") || name.contains("LEGGINGS") || name.contains("BOOTS") || name.contains("FISHING_ROD") || name.contains("SPEAR") || mat == Material.STICK || mat == Material.BLAZE_ROD || mat == Material.TRIDENT || mat == Material.MACE;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("\u00a7cL\u1ec7nh n\u00e0y ch\u1ec9 d\u00e0nh cho ng\u01b0\u1eddi ch\u01a1i!");
            return true;
        }
        Player player = (Player)sender;
        if (!player.isOp() && !player.hasPermission("cwe.admin")) {
            player.sendMessage("\u00a7c[CWE] B\u1ea1n kh\u00f4ng c\u00f3 quy\u1ec1n d\u00f9ng l\u1ec7nh n\u00e0y!");
            return true;
        }
        ItemStack mainHand = player.getInventory().getItemInMainHand();
        if (mainHand == null || mainHand.getType().isAir() || !this.isValidEnchantable(mainHand)) {
            player.sendMessage("\u00a7c[CWE] \u00a7fB\u1ea1n ph\u1ea3i \u0111ang \u00a7ec\u1ea7m m\u1ed9t trang b\u1ecb h\u1ee3p l\u1ec7 \u00a7ftr\u00ean tay ch\u00ednh \u0111\u1ec3 d\u00f9ng l\u1ec7nh n\u00e0y!");
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
            return true;
        }
        this.openAdminEnchantGUI(player);
        return true;
    }

    public void openAdminEnchantGUI(Player player) {
        ArrayList<String> lore;
        ItemMeta meta;
        ItemStack book;
        Inventory gui = Bukkit.createInventory(null, (int)54, (String)"\u00a74\u2699 \u00a7lAdmin Enchant Editor");
        ItemStack bgGlass = new ItemStack(Material.PURPLE_STAINED_GLASS_PANE);
        ItemMeta bgMeta = bgGlass.getItemMeta();
        if (bgMeta != null) {
            bgMeta.setDisplayName("\u00a77 ");
            bgGlass.setItemMeta(bgMeta);
        }
        for (int i = 0; i < 54; ++i) {
            gui.setItem(i, bgGlass);
        }
        int slot = 0;
        for (CustomEnchant customEnchant : CustomEnchant.values()) {
            if (slot >= 54) break;
            book = new ItemStack(Material.ENCHANTED_BOOK);
            meta = book.getItemMeta();
            if (meta != null) {
                meta.setDisplayName("\u00a7b\u2726 \u00a7l" + customEnchant.getDisplayName());
                lore = new ArrayList<>();
                lore.add("\u00a77Nh\u00f3m: \u00a7e" + customEnchant.getItemGroup().name());
                lore.add("\u00a77Max C\u1ea5p: \u00a7c" + customEnchant.getMaxLevel());
                lore.add("");
                lore.add("\u00a7eHi\u1ec7u qu\u1ea3: \u00a7f" + customEnchant.getDescription());
                lore.add("");
                lore.add("\u00a7a\u25b6 Chu\u1ed9t Tr\u00e1i: \u00a7fC\u1ed9ng +1 C\u1ea5p v\u00e0o trang b\u1ecb");
                lore.add("\u00a76\u25b6 Chu\u1ed9t Ph\u1ea3i: \u00a7f\u00c9p th\u1eb3ng C\u1ea5p T\u1ed0I \u0110A (" + customEnchant.getMaxLevel() + ")");
                meta.setLore(lore);
                meta.addEnchant(Enchantment.getByKey((NamespacedKey)NamespacedKey.minecraft((String)"luck_of_the_sea")), 1, true);
                meta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ENCHANTS});
                meta.getPersistentDataContainer().set(new NamespacedKey((Plugin)this.plugin, "admin_enc_id"), PersistentDataType.STRING, customEnchant.name());
                book.setItemMeta(meta);
            }
            gui.setItem(slot, book);
            ++slot;
        }
        for (Enum enum_ : UltimateEnchant.values()) {
            if (slot >= 54) break;
            book = new ItemStack(Material.ENCHANTED_BOOK);
            meta = book.getItemMeta();
            if (meta != null) {
                meta.setDisplayName("\u00a7d\u00a7l" + ((UltimateEnchant)enum_).getDisplayName().toUpperCase());
                lore = new java.util.ArrayList<>();
                lore.add("\u00a77Nh\u00f3m: \u00a7dULTIMATE " + ((UltimateEnchant)enum_).getGroup().name());
                lore.add("\u00a77Max C\u1ea5p: \u00a7c" + ((UltimateEnchant)enum_).getMaxLevel());
                lore.add("");
                lore.add("\u00a7eHi\u1ec7u qu\u1ea3: \u00a7f" + ((UltimateEnchant)enum_).getDescription());
                lore.add("");
                lore.add("\u00a7a\u25b6 Chu\u1ed9t Tr\u00e1i: \u00a7fC\u1ed9ng +1 C\u1ea5p v\u00e0o trang b\u1ecb");
                lore.add("\u00a76\u25b6 Chu\u1ed9t Ph\u1ea3i: \u00a7f\u00c9p th\u1eb3ng C\u1ea5p T\u1ed0I \u0110A (" + ((UltimateEnchant)enum_).getMaxLevel() + ")");
                meta.setLore(lore);
                meta.addEnchant(Enchantment.getByKey((NamespacedKey)NamespacedKey.minecraft((String)"luck_of_the_sea")), 1, true);
                meta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ENCHANTS});
                meta.getPersistentDataContainer().set(new NamespacedKey((Plugin)this.plugin, "admin_ult_enc_id"), PersistentDataType.STRING, enum_.name());
                book.setItemMeta(meta);
            }
            gui.setItem(slot, book);
            ++slot;
        }
        this.adminGuiPlayers.add(player.getUniqueId());
        player.openInventory(gui);
        player.sendMessage("\u00a7a[CWE Admin] \u00a7fM\u1edf giao di\u1ec7n \u00e9p b\u00f9a Admin. Chu\u1ed9t Tr\u00e1i \u00a7a+1 c\u1ea5p\u00a7f, Chu\u1ed9t Ph\u1ea3i \u00a76Max C\u1ea5p\u00a7f.");
    }

    @EventHandler
    public void onOpenTable(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        Block block = event.getClickedBlock();
        if (block == null || block.getType() != Material.ENCHANTING_TABLE) {
            return;
        }
        event.setCancelled(true);
        Player player = event.getPlayer();
        int bookshelves = BookshelfScanner.countBookshelves(block);
        this.activeBookshelves.put(player.getUniqueId(), bookshelves);
        this.openCustomEnchantMenu(player);
    }

    public void openCustomEnchantMenu(Player player) {
        int[] redSlots;
        Inventory gui = Bukkit.createInventory(null, (int)54, (String)"Enchant Item");
        ItemStack grayGlass = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta grayMeta = grayGlass.getItemMeta();
        if (grayMeta != null) {
            grayMeta.setDisplayName("\u00a77 ");
            grayGlass.setItemMeta(grayMeta);
        }
        for (int i = 0; i < 54; ++i) {
            gui.setItem(i, grayGlass);
        }
        ItemStack yellowGlass = new ItemStack(Material.YELLOW_STAINED_GLASS_PANE);
        ItemMeta yellowMeta = yellowGlass.getItemMeta();
        if (yellowMeta != null) {
            yellowMeta.setDisplayName("\u00a77 ");
            yellowGlass.setItemMeta(yellowMeta);
        }
        gui.setItem(0, yellowGlass);
        gui.setItem(8, yellowGlass);
        gui.setItem(45, yellowGlass);
        gui.setItem(53, yellowGlass);
        ItemStack redGlass = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        ItemMeta redMeta = redGlass.getItemMeta();
        if (redMeta != null) {
            redMeta.setDisplayName("\u00a77 ");
            redGlass.setItemMeta(redMeta);
        }
        for (int slot : redSlots = new int[]{2, 3, 4, 5, 6, 11, 12, 14, 15, 22}) {
            gui.setItem(slot, redGlass);
        }
        gui.setItem(13, new ItemStack(Material.AIR));
        this.setDisabledButtons(gui);
        player.openInventory(gui);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!event.getView().getTitle().equals("Enchant Item")) {
            return;
        }
        Player player = (Player)event.getWhoClicked();
        Inventory gui = event.getInventory();
        int slot = event.getSlot();
        if (event.getClick().isShiftClick()) {
            event.setCancelled(true);
            return;
        }
        if (event.getClickedInventory() != event.getView().getTopInventory()) {
            Bukkit.getScheduler().runTask((Plugin)this.plugin, () -> this.refreshEnchantButtons(gui, player));
            return;
        }
        if (slot != 13 && slot != 29 && slot != 31 && slot != 33) {
            event.setCancelled(true);
            return;
        }
        if (slot == 13) {
            Bukkit.getScheduler().runTask((Plugin)this.plugin, () -> this.refreshEnchantButtons(gui, player));
            return;
        }
        if (slot == 29 || slot == 31 || slot == 33) {
            NamespacedKey flagKey;
            event.setCancelled(true);
            ItemStack targetItem = gui.getItem(13);
            if (targetItem == null || targetItem.getType().isAir()) {
                player.sendMessage("\u00a7c\u0110\u00e3 c\u00f3 \u0111\u1ed3 \u0111\u00e9o \u0111\u00e2u m\u00e0 \u0111\u00f2i ph\u00f9 ph\u00e9p cu m!");
                return;
            }
            if (!this.isValidEnchantable(targetItem)) {
                player.sendMessage("\u00a7c[Engine] V\u1eadt ph\u1ea9m kh\u00f4ng h\u1ee3p l\u1ec7! B\u00e0n enchant ch\u1ec9 nh\u1eadn V\u0169 kh\u00ed, C\u00f4ng c\u1ee5, Gi\u00e1p, Wand ho\u1eb7c S\u00e1ch th\u01b0\u1eddng.");
                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
                return;
            }
            PersistentDataContainer container = targetItem.getItemMeta().getPersistentDataContainer();
            if (container.has(flagKey = new NamespacedKey((Plugin)this.plugin, "is_custom_enchanted"), PersistentDataType.INTEGER)) {
                player.sendMessage("\u00a7cM\u00f3n \u0111\u1ed3 n\u00e0y \u0111\u00e3 \u0111\u01b0\u1ee3c khai quang r\u1ed3i, mang ra c\u00e1i \u0110e m\u00e0 g\u1ed9p d\u00f2ng \u0111i!");
                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
                return;
            }
            int bookshelves = this.activeBookshelves.getOrDefault(player.getUniqueId(), 0);
            int reqLevel = 0;
            int reqBookshelves = 0;
            int tier = 0;
            if (slot == 29) {
                tier = 1;
                reqLevel = 5;
                reqBookshelves = 0;
            } else if (slot == 31) {
                tier = 2;
                reqLevel = 15;
                reqBookshelves = 5;
            } else if (slot == 33) {
                tier = 3;
                reqLevel = 35;
                reqBookshelves = 15;
            }
            if (bookshelves < reqBookshelves) {
                player.sendMessage("\u00a7cK\u1ec7 s\u00e1ch kh\u00f4ng \u0111\u1ee7!");
                return;
            }
            if (player.getLevel() < reqLevel) {
                player.sendMessage("\u00a7cKh\u00f4ng \u0111\u1ee7 level!");
                return;
            }
            boolean success = false;
            ArrayList<CustomEnchant> pool = new ArrayList<CustomEnchant>();
            Material mat = targetItem.getType();
            String name = mat.name();
            boolean isAxe = (name.endsWith("_AXE") || name.equals("AXE")) && !name.contains("PICKAXE");
            boolean axeChooseSword = Math.random() < 0.5;
            for (CustomEnchant enc : CustomEnchant.values()) {
                if (enc == CustomEnchant.MENDING) continue;
                if (mat == Material.BOOK) {
                    pool.add(enc);
                    continue;
                }
                if (isAxe) {
                    if (axeChooseSword && enc.getItemGroup() == CustomEnchant.ItemGroup.SWORD) {
                        pool.add(enc);
                        continue;
                    }
                    if (axeChooseSword || enc.getItemGroup() != CustomEnchant.ItemGroup.TOOL) continue;
                    pool.add(enc);
                    continue;
                }
                if (!enc.getItemGroup().canApply(mat) || enc == CustomEnchant.BIG_BRAIN && !name.contains("HELMET") || enc == CustomEnchant.COUNTER_STRIKE && !name.contains("CHESTPLATE") || enc == CustomEnchant.SMARTY_PANTS && !name.contains("LEGGINGS") || enc == CustomEnchant.SUGAR_RUSH && !name.contains("BOOTS") || enc == CustomEnchant.FEATHER_FALLING && !name.contains("BOOTS") || enc == CustomEnchant.REPLENISH && !name.contains("HOE")) continue;
                pool.add(enc);
            }
            if (!pool.isEmpty()) {
                Collections.shuffle(pool);
                int countToApply = Math.min(2 + (int)(Math.random() * 2.0), pool.size());
                for (int i = 0; i < countToApply; ++i) {
                    CustomEnchant randomEnc = (CustomEnchant)(pool.get(i));
                    int lvl = 1;
                    if (tier == 1) {
                        lvl = Math.max(1, randomEnc.getMaxLevel() / 3);
                    } else if (tier == 2) {
                        lvl = Math.max(1, randomEnc.getMaxLevel() * 2 / 3);
                    } else if (tier == 3) {
                        lvl = randomEnc.getMaxLevel();
                    }
                    if (!this.enchantManager.applyEnchantToItem(targetItem, randomEnc, lvl)) continue;
                    success = true;
                }
            }
            if (success) {
                if (targetItem.getType() == Material.BOOK) {
                    targetItem.setType(Material.ENCHANTED_BOOK);
                    this.enchantManager.rebuildEnchantLore(targetItem);
                }
                player.setLevel(player.getLevel() - reqLevel);
                player.sendMessage("\u00a7a\ud83d\udfe9 Ph\u00f9 ph\u00e9p th\u00e0nh c\u00f4ng! \u0110\u00e3 kh\u1ea5u tr\u1eeb \u00a7e" + reqLevel + " Levels\u00a7a.");
                player.playSound(player.getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1.0f, 1.0f);
                this.refreshEnchantButtons(gui, player);
            }
        }
    }

    @EventHandler
    public void onAdminEnchantClick(InventoryClickEvent event) {
        int newLevel;
        PersistentDataContainer pdc;
        boolean isRight;
        CustomEnchant enc;
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }
        Player player = (Player)event.getWhoClicked();
        if (!event.getView().getTitle().equals("\u00a74\u2699 \u00a7lAdmin Enchant Editor")) {
            return;
        }
        event.setCancelled(true);
        if (event.getClickedInventory() != event.getView().getTopInventory()) {
            return;
        }
        ItemStack clicked = event.getCurrentItem();
        if (clicked == null || clicked.getType() != Material.ENCHANTED_BOOK) {
            return;
        }
        if (!clicked.hasItemMeta()) {
            return;
        }
        String ultEncName = (String)clicked.getItemMeta().getPersistentDataContainer().get(new NamespacedKey((Plugin)this.plugin, "admin_ult_enc_id"), PersistentDataType.STRING);
        if (ultEncName != null) {
            this.handleAdminUltimateEnchantClick(player, ultEncName, event.getClick());
            return;
        }
        String encName = (String)clicked.getItemMeta().getPersistentDataContainer().get(new NamespacedKey((Plugin)this.plugin, "admin_enc_id"), PersistentDataType.STRING);
        if (encName == null) {
            return;
        }
        try {
            enc = CustomEnchant.valueOf(encName);
        }
        catch (IllegalArgumentException e) {
            player.sendMessage("\u00a7c[CWE Admin] L\u1ed7i: Kh\u00f4ng t\u00ecm th\u1ea5y b\u00f9a '" + encName + "'!");
            return;
        }
        ItemStack targetItem = player.getInventory().getItemInMainHand();
        if (targetItem == null || targetItem.getType().isAir() || !this.isValidEnchantable(targetItem)) {
            player.sendMessage("\u00a7c[CWE Admin] B\u1ea1n kh\u00f4ng c\u00f2n c\u1ea7m trang b\u1ecb h\u1ee3p l\u1ec7 tr\u00ean tay!");
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
            return;
        }
        Material mat = targetItem.getType();
        if (!enc.getItemGroup().canApply(mat)) {
            player.sendMessage("\u00a7c[CWE Admin] \u00a74\u274c CH\u1eb6N! \u00a7fB\u00f9a \u00a7e" + enc.getDisplayName() + " \u00a7f(nh\u00f3m \u00a7c" + enc.getItemGroup().name() + "\u00a7f) KH\u00d4NG TH\u1ec2 \u00e9p l\u00ean \u00a7e" + mat.name() + "\u00a7f! H\u1ee7y l\u1ec7nh.");
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.2f);
            return;
        }
        String matName = mat.name();
        if (enc == CustomEnchant.BIG_BRAIN && !matName.contains("HELMET")) {
            player.sendMessage("\u00a7c[CWE Admin] Big Brain ch\u1ec9 \u00e9p \u0111\u01b0\u1ee3c v\u00e0o M\u0169!");
            return;
        }
        if (enc == CustomEnchant.COUNTER_STRIKE && !matName.contains("CHESTPLATE")) {
            player.sendMessage("\u00a7c[CWE Admin] Counter-Strike ch\u1ec9 \u00e9p \u0111\u01b0\u1ee3c v\u00e0o \u00c1o Gi\u00e1p!");
            return;
        }
        if (enc == CustomEnchant.SMARTY_PANTS && !matName.contains("LEGGINGS")) {
            player.sendMessage("\u00a7c[CWE Admin] Smarty Pants ch\u1ec9 \u00e9p \u0111\u01b0\u1ee3c v\u00e0o Qu\u1ea7n!");
            return;
        }
        if (enc == CustomEnchant.SUGAR_RUSH && !matName.contains("BOOTS")) {
            player.sendMessage("\u00a7c[CWE Admin] Sugar Rush ch\u1ec9 \u00e9p \u0111\u01b0\u1ee3c v\u00e0o Gi\u00e0y!");
            return;
        }
        if (enc == CustomEnchant.FEATHER_FALLING && !matName.contains("BOOTS")) {
            player.sendMessage("\u00a7c[CWE Admin] Feather Falling ch\u1ec9 \u00e9p \u0111\u01b0\u1ee3c v\u00e0o Gi\u00e0y!");
            return;
        }
        if (enc == CustomEnchant.REPLENISH && !matName.contains("HOE")) {
            player.sendMessage("\u00a7c[CWE Admin] Replenish ch\u1ec9 \u00e9p \u0111\u01b0\u1ee3c v\u00e0o Cu\u1ed1c!");
            return;
        }
        boolean isLeft = event.getClick() == ClickType.LEFT;
        boolean bl = isRight = event.getClick() == ClickType.RIGHT;
        if (!isLeft && !isRight) {
            return;
        }
        NamespacedKey encKey = new NamespacedKey((Plugin)this.plugin, "enchant_" + enc.getId());
        int currentLevel = 0;
        if (targetItem.hasItemMeta() && (pdc = targetItem.getItemMeta().getPersistentDataContainer()).has(encKey, PersistentDataType.INTEGER)) {
            currentLevel = (Integer)pdc.get(encKey, PersistentDataType.INTEGER);
        }
        if (isLeft) {
            newLevel = Math.min(currentLevel + 1, enc.getMaxLevel());
            if (newLevel == currentLevel) {
                player.sendMessage("\u00a7e[CWE Admin] \u00a7fB\u00f9a \u00a7b" + enc.getDisplayName() + " \u00a7f\u0111\u00e3 \u0111\u1ea1t \u00a7cC\u1ea5p T\u1ed0I \u0110A (" + enc.getMaxLevel() + ")\u00a7f r\u1ed3i!");
                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
                return;
            }
        } else {
            newLevel = enc.getMaxLevel();
        }
        if (!this.enchantManager.applyEnchantToItem(targetItem, enc, newLevel)) {
            player.sendMessage("\u00a7c[CWE Admin] \u00a74\u274c TH\u1ea4T B\u1ea0I! \u00a7fB\u00f9a \u00a7e" + enc.getDisplayName() + " \u00a7fxung \u0111\u1ed9t v\u1edbi b\u00f9a ch\u00fa hi\u1ec7n c\u00f3 tr\u00ean v\u1eadt ph\u1ea9m!");
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
            return;
        }
        this.enchantManager.rebuildEnchantLore(targetItem);
        player.getInventory().setItemInMainHand(targetItem);
        player.updateInventory();
        String action = isLeft ? "\u00a7a+1 C\u1ea5p \u2192 \u00a7fC\u1ea5p \u00a7e" + newLevel : "\u00a76Max C\u1ea5p \u2192 \u00a7fC\u1ea5p \u00a7e" + newLevel;
        player.sendMessage("\u00a7a[CWE Admin] \u2714 \u00a7b" + enc.getDisplayName() + " \u00a7f| " + action + " \u00a7f| \u00a77(" + mat.name() + ")");
        player.playSound(player.getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1.0f, 1.5f);
    }

    private void handleAdminUltimateEnchantClick(Player player, String ultEncName, ClickType clickType) {
        int newLevel;
        boolean isRight;
        UltimateEnchant enc;
        try {
            enc = UltimateEnchant.valueOf(ultEncName);
        }
        catch (IllegalArgumentException e) {
            player.sendMessage("\u00a7c[CWE Admin] L\u1ed7i: Kh\u00f4ng t\u00ecm th\u1ea5y b\u00f9a Ultimate '" + ultEncName + "'!");
            return;
        }
        ItemStack targetItem = player.getInventory().getItemInMainHand();
        if (targetItem == null || targetItem.getType().isAir() || !this.isValidEnchantable(targetItem)) {
            player.sendMessage("\u00a7c[CWE Admin] B\u1ea1n kh\u00f4ng c\u00f2n c\u1ea7m trang b\u1ecb h\u1ee3p l\u1ec7 tr\u00ean tay!");
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
            return;
        }
        Material mat = targetItem.getType();
        if (targetItem.getType() != Material.BOOK && targetItem.getType() != Material.ENCHANTED_BOOK && !enc.getGroup().canApply(mat)) {
            player.sendMessage("\u00a7c[CWE Admin] \u00a74\u274c CH\u1eb6N! \u00a7fB\u00f9a \u00a7d" + enc.getDisplayName() + " \u00a7f(nh\u00f3m \u00a7c" + enc.getGroup().name() + "\u00a7f) KH\u00d4NG TH\u1ec2 \u00e9p l\u00ean \u00a7e" + mat.name() + "\u00a7f! H\u1ee7y l\u1ec7nh.");
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.2f);
            return;
        }
        boolean isLeft = clickType == ClickType.LEFT;
        boolean bl = isRight = clickType == ClickType.RIGHT;
        if (!isLeft && !isRight) {
            return;
        }
        int currentLevel = this.enchantManager.getUltimateEnchantLevel(targetItem, enc);
        if (isLeft) {
            newLevel = Math.min(currentLevel + 1, enc.getMaxLevel());
            if (newLevel == currentLevel) {
                player.sendMessage("\u00a7e[CWE Admin] \u00a7fB\u00f9a \u00a7d" + enc.getDisplayName() + " \u00a7f\u0111\u00e3 \u0111\u1ea1t \u00a7cC\u1ea5p T\u1ed0I \u0110A (" + enc.getMaxLevel() + ")\u00a7f r\u1ed3i!");
                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
                return;
            }
        } else {
            newLevel = enc.getMaxLevel();
        }
        this.enchantManager.applyUltimateEnchantToItem(targetItem, enc, newLevel);
        player.getInventory().setItemInMainHand(targetItem);
        player.updateInventory();
        String action = isLeft ? "\u00a7a+1 C\u1ea5p \u2192 \u00a7fC\u1ea5p \u00a7e" + newLevel : "\u00a76Max C\u1ea5p \u2192 \u00a7fC\u1ea5p \u00a7e" + newLevel;
        player.sendMessage("\u00a7a[CWE Admin] \u2714 \u00a7d" + enc.getDisplayName() + " \u00a7f| " + action + " \u00a7f| \u00a77(" + mat.name() + ")");
        player.playSound(player.getLocation(), Sound.BLOCK_ENCHANTMENT_TABLE_USE, 1.0f, 1.5f);
    }

    @EventHandler
    public void onPrepareGrindstone(PrepareGrindstoneEvent event) {
        ItemStack result = event.getResult();
        if (result == null || result.getType().isAir() || !result.hasItemMeta()) {
            return;
        }
        NamespacedKey flagKey = new NamespacedKey((Plugin)this.plugin, "is_custom_enchanted");
        if (result.getItemMeta().getPersistentDataContainer().has(flagKey, PersistentDataType.INTEGER)) {
            ItemStack cleanResult = result.clone();
            ItemMeta meta = cleanResult.getItemMeta();
            meta.getPersistentDataContainer().remove(flagKey);
            for (CustomEnchant enc : CustomEnchant.values()) {
                meta.getPersistentDataContainer().remove(new NamespacedKey((Plugin)this.plugin, "enchant_" + enc.getId()));
                // Xóa cả key charges để không còn dữ liệu thừa
                meta.getPersistentDataContainer().remove(new NamespacedKey((Plugin)this.plugin, "cwe_charges_" + enc.getId()));
                Enchantment vanillaMatch = this.enchantManager.getVanillaEquivalent(enc);
                if (vanillaMatch == null) continue;
                meta.removeEnchant(vanillaMatch);
            }
            try {
                meta.removeEnchant(Enchantment.getByKey((NamespacedKey)NamespacedKey.minecraft((String)"luck_of_the_sea")));
                meta.removeEnchant(Enchantment.getByKey((NamespacedKey)NamespacedKey.minecraft((String)"mending")));
            }
            catch (Throwable throwable) {
                // empty catch block
            }
            java.util.List<String> lore = meta.getLore();
            if (lore != null) {
                ArrayList<String> newLore = new java.util.ArrayList<String>();
                Iterator iterator = lore.iterator();
                while (iterator.hasNext()) {
                    String line;
                    String cleanedLine = line = (String)iterator.next();
                    boolean isEnchantComponentRemoved = false;
                    for (CustomEnchant enc : CustomEnchant.values()) {
                        // Khớp cả phần §b(charge⚡) của bùa có pin (Telekinesis, Life Steal...)
                        String regex = "\u00a79" + Pattern.quote(enc.getDisplayName()) + "(?: [IVX0-9]+)?(?:\\s*\u00a7b\\([^)]+\u26a1\\))?";
                        String afterReplace = cleanedLine.replaceAll(regex, "");
                        if (afterReplace.equals(cleanedLine)) continue;
                        cleanedLine = afterReplace;
                        isEnchantComponentRemoved = true;
                    }
                    if (isEnchantComponentRemoved) {
                        String rawText = (cleanedLine = cleanedLine.replaceAll("\u00a77,\\s*", "").trim()).replaceAll("(?i)\u00a7[0-9a-fk-or]", "").trim();
                        if (rawText.isEmpty()) continue;
                        newLore.add(cleanedLine);
                        continue;
                    }
                    newLore.add(line);
                }
                meta.setLore(newLore);
            }
            cleanResult.setItemMeta(meta);
            event.setResult(cleanResult);
        }
    }

    @EventHandler
    public void onGrindstoneResultClick(InventoryClickEvent event) {
        if (event.getInventory().getType() != InventoryType.GRINDSTONE) {
            return;
        }
        if (event.getRawSlot() != 2) {
            return;
        }
        ItemStack clicked = event.getCurrentItem();
        if (clicked == null || !clicked.hasItemMeta()) {
            return;
        }
        NamespacedKey flagKey = new NamespacedKey((Plugin)this.plugin, "is_custom_enchanted");
        if (clicked.getItemMeta().getPersistentDataContainer().has(flagKey, PersistentDataType.INTEGER)) {
            ItemStack cleanItem = clicked.clone();
            ItemMeta meta = cleanItem.getItemMeta();
            meta.getPersistentDataContainer().remove(flagKey);
            for (CustomEnchant enc : CustomEnchant.values()) {
                meta.getPersistentDataContainer().remove(new NamespacedKey((Plugin)this.plugin, "enchant_" + enc.getId()));
                // Xóa cả key charges để không còn dữ liệu thừa
                meta.getPersistentDataContainer().remove(new NamespacedKey((Plugin)this.plugin, "cwe_charges_" + enc.getId()));
                Enchantment vanillaMatch = this.enchantManager.getVanillaEquivalent(enc);
                if (vanillaMatch == null) continue;
                meta.removeEnchant(vanillaMatch);
            }
            try {
                meta.removeEnchant(Enchantment.getByKey((NamespacedKey)NamespacedKey.minecraft((String)"luck_of_the_sea")));
                meta.removeEnchant(Enchantment.getByKey((NamespacedKey)NamespacedKey.minecraft((String)"mending")));
            }
            catch (Throwable throwable) {
                // empty catch block
            }
            java.util.List<String> lore = meta.getLore();
            if (lore != null) {
                ArrayList<String> newLore = new java.util.ArrayList<String>();
                Iterator iterator = lore.iterator();
                while (iterator.hasNext()) {
                    String line;
                    String cleanedLine = line = (String)iterator.next();
                    boolean isEnchantComponentRemoved = false;
                    for (CustomEnchant enc : CustomEnchant.values()) {
                        // Khớp cả phần §b(charge⚡) của bùa có pin (Telekinesis, Life Steal...)
                        String regex = "\u00a79" + Pattern.quote(enc.getDisplayName()) + "(?: [IVX0-9]+)?(?:\\s*\u00a7b\\([^)]+\u26a1\\))?";
                        String afterReplace = cleanedLine.replaceAll(regex, "");
                        if (afterReplace.equals(cleanedLine)) continue;
                        cleanedLine = afterReplace;
                        isEnchantComponentRemoved = true;
                    }
                    if (isEnchantComponentRemoved) {
                        String rawText = (cleanedLine = cleanedLine.replaceAll("\u00a77,\\s*", "").trim()).replaceAll("(?i)\u00a7[0-9a-fk-or]", "").trim();
                        if (rawText.isEmpty()) continue;
                        newLore.add(cleanedLine);
                        continue;
                    }
                    newLore.add(line);
                }
                meta.setLore(newLore);
            }
            cleanItem.setItemMeta(meta);
            event.setCurrentItem(cleanItem);
        }
    }

    private void refreshEnchantButtons(Inventory gui, Player player) {
        ItemStack targetItem = gui.getItem(13);
        if (targetItem == null || targetItem.getType().isAir()) {
            this.setDisabledButtons(gui);
            return;
        }
        int bookshelves = this.activeBookshelves.getOrDefault(player.getUniqueId(), 0);
        gui.setItem(29, this.createBottleItem("\u00a7aEnchant Tier I", 5, 0, bookshelves, player));
        gui.setItem(31, this.createBottleItem("\u00a7eEnchant Tier II", 15, 5, bookshelves, player));
        gui.setItem(33, this.createBottleItem("\u00a7cEnchant Tier III", 35, 15, bookshelves, player));
        ItemStack barrier = new ItemStack(Material.BARRIER);
        ItemMeta bMeta = barrier.getItemMeta();
        if (bMeta != null) {
            bMeta.setDisplayName("\u00a7c\u0110\u00f3ng Giao Di\u1ec7n");
            barrier.setItemMeta(bMeta);
        }
        gui.setItem(49, barrier);
    }

    private ItemStack createBottleItem(String title, int reqLvl, int reqBooks, int currentBooks, Player player) {
        if (currentBooks < reqBooks) {
            ItemStack barrier = new ItemStack(Material.BARRIER);
            ItemMeta meta = barrier.getItemMeta();
            if (meta != null) {
                meta.setDisplayName("\u00a7c" + title + " \u00a77(B\u1ecb Kh\u00f3a)");
                meta.setLore(Arrays.asList("\u00a77Y\u00eau c\u1ea7u c\u1ea5u h\u00ecnh ph\u00f2ng t\u1ebf:", " \u00a78\u2022 \u00a77C\u1ea7n \u00edt nh\u1ea5t: \u00a7e" + reqBooks + " K\u1ec7 S\u00e1ch", " \u00a78\u2022 \u00a77Th\u1ef1c t\u1ebf hi\u1ec7n c\u00f3: \u00a7c" + currentBooks + "/" + reqBooks));
                barrier.setItemMeta(meta);
            }
            return barrier;
        }
        ItemStack bottle = new ItemStack(Material.EXPERIENCE_BOTTLE);
        ItemMeta meta = bottle.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(title + " - \u00a76" + reqLvl + " Levels");
            if (player.getLevel() < reqLvl) {
                meta.setLore(Arrays.asList("\u00a7cM\u00e0y \u0111\u00e9o \u0111\u1ee7 c\u1ea5p \u0111\u1ed9 kinh nghi\u1ec7m!"));
            } else {
                meta.setLore(Arrays.asList("\u00a7eClick chu\u1ed9t \u0111\u1ec3 ti\u1ebfn h\u00e0nh b\u00f9a ch\u00fa!"));
            }
            bottle.setItemMeta(meta);
        }
        return bottle;
    }

    private void setDisabledButtons(Inventory gui) {
        ItemStack glass = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta meta = glass.getItemMeta();
        if (meta != null) {
            meta.setDisplayName("\u00a77 ");
            glass.setItemMeta(meta);
        }
        gui.setItem(29, glass);
        gui.setItem(31, glass);
        gui.setItem(33, glass);
        ItemStack barrier = new ItemStack(Material.BARRIER);
        ItemMeta barrierMeta = barrier.getItemMeta();
        if (barrierMeta != null) {
            barrierMeta.setDisplayName("\u00a7cCh\u01b0a c\u00f3 v\u1eadt ph\u1ea9m!");
            barrierMeta.setLore(Arrays.asList("\u00a77H\u00e3y \u0111\u1eb7t V\u0169 kh\u00ed ho\u1eb7c Gi\u00e1p v\u00e0o \u00f4 ph\u00eda tr\u00ean"));
            barrier.setItemMeta(barrierMeta);
        }
        gui.setItem(49, barrier);
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player)event.getPlayer();
        String title = event.getView().getTitle();
        if (title.equals("\u00a74\u2699 \u00a7lAdmin Enchant Editor")) {
            this.adminGuiPlayers.remove(player.getUniqueId());
            return;
        }
        if (!title.equals("Enchant Item")) {
            return;
        }
        Inventory gui = event.getInventory();
        this.activeBookshelves.remove(player.getUniqueId());
        ItemStack itemInTable = gui.getItem(13);
        if (itemInTable != null && itemInTable.getType() != Material.AIR) {
            for (ItemStack drop : player.getInventory().addItem(new ItemStack[]{itemInTable}).values()) {
                player.getWorld().dropItemNaturally(player.getLocation(), drop);
            }
        }
    }
}

