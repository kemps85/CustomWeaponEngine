/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.milkbowl.vault.economy.Economy
 *  org.bukkit.Bukkit
 *  org.bukkit.Material
 *  org.bukkit.NamespacedKey
 *  org.bukkit.OfflinePlayer
 *  org.bukkit.Sound
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.configuration.file.FileConfiguration
 *  org.bukkit.configuration.file.YamlConfiguration
 *  org.bukkit.enchantments.Enchantment
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.inventory.InventoryClickEvent
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.inventory.ItemFlag
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.persistence.PersistentDataType
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.java.JavaPlugin
 */
package org.example.bazaar;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class BazaarGUI
implements Listener,
CommandExecutor {
    private final JavaPlugin plugin;
    private final Economy econ;
    private final String mainTitle = "\u00a78Bazaar > Categories";
    private final String prefixCat = "\u00a78Bazaar > Category: ";
    private final String prefixProd = "\u00a78Bazaar > Product: ";
    private final String prefixTrade = "\u00a78Bazaar > Action: ";
    private File dataFile;
    private FileConfiguration dataConfig;
    private final Map<String, Commodity> market = new HashMap<String, Commodity>();
    private final Map<String, Integer> currentStocks = new HashMap<String, Integer>();

    public BazaarGUI(JavaPlugin plugin, Economy econ) {
        this.plugin = plugin;
        this.econ = econ;
        this.setupMarketRegistry();
        this.loadStockDataFile();
    }

    private void setupMarketRegistry() {
        this.register(new Commodity("WHEAT", Material.WHEAT, "\u00a7aL\u00faa M\u00ec Th\u00f4", 10, 2, 50, 10000));
        this.register(new Commodity("ENCHANTED_WHEAT", Material.WHEAT, "\u00a7aEnchanted Wheat", 1600, 320, 8000, 1000));
        this.register(new Commodity("CARROT", Material.CARROT, "\u00a7aC\u00e0 R\u1ed1t Th\u00f4", 12, 3, 60, 10000));
        this.register(new Commodity("ENCHANTED_CARROT", Material.CARROT, "\u00a7aEnchanted Carrot", 1920, 480, 9600, 1000));
        this.register(new Commodity("POTATO", Material.POTATO, "\u00a7aKhoai T\u00e2y Th\u00f4", 12, 3, 60, 10000));
        this.register(new Commodity("ENCHANTED_POTATO", Material.POTATO, "\u00a7aEnchanted Potato", 1920, 480, 9600, 1000));
        this.register(new Commodity("COAL", Material.COAL, "\u00a79Than \u0110\u00e1 Th\u00f4", 20, 5, 100, 12000));
        this.register(new Commodity("ENCHANTED_COAL", Material.COAL, "\u00a79Enchanted Coal", 3200, 800, 16000, 1200));
        this.register(new Commodity("ENCHANTED_COAL_BLOCK", Material.COAL_BLOCK, "\u00a75Enchanted Block of Coal", 28800, 7200, 144000, 200));
        this.register(new Commodity("IRON_INGOT", Material.IRON_INGOT, "\u00a79Ph\u00f4i S\u1eaft Th\u00f4", 40, 10, 250, 8000));
        this.register(new Commodity("ENCHANTED_IRON", Material.IRON_INGOT, "\u00a79Enchanted Iron Ingot", 6400, 1600, 40000, 800));
        this.register(new Commodity("ENCHANTED_IRON_BLOCK", Material.IRON_BLOCK, "\u00a75Enchanted Block of Iron", 57600, 14400, 360000, 150));
        this.register(new Commodity("GOLD_INGOT", Material.GOLD_INGOT, "\u00a79Ph\u00f4i V\u00e0ng Th\u00f4", 60, 15, 350, 7000));
        this.register(new Commodity("ENCHANTED_GOLD", Material.GOLD_INGOT, "\u00a79Enchanted Gold Ingot", 9600, 2400, 56000, 700));
        this.register(new Commodity("ENCHANTED_GOLD_BLOCK", Material.GOLD_BLOCK, "\u00a75Enchanted Block of Gold", 86400, 21600, 504000, 100));
        this.register(new Commodity("DIAMOND", Material.DIAMOND, "\u00a79Kim C\u01b0\u01a1ng Th\u00f4", 100, 20, 600, 5000));
        this.register(new Commodity("ENCHANTED_DIAMOND", Material.DIAMOND, "\u00a79Enchanted Diamond", 16000, 3200, 96000, 500));
        this.register(new Commodity("ENCHANTED_DIAMOND_BLOCK", Material.DIAMOND_BLOCK, "\u00a75Enchanted Block of Diamond", 144000, 28800, 864000, 100));
        this.register(new Commodity("EMERALD", Material.EMERALD, "\u00a79Ng\u1ecdc L\u1ee5c B\u1ea3o Th\u00f4", 120, 25, 700, 4000));
        this.register(new Commodity("ENCHANTED_EMERALD", Material.EMERALD, "\u00a79Enchanted Emerald", 19200, 4000, 112000, 400));
        this.register(new Commodity("ENCHANTED_EMERALD_BLOCK", Material.EMERALD_BLOCK, "\u00a75Enchanted Block of Emerald", 172800, 36000, 1008000, 50));
        this.register(new Commodity("LAPIS_LAZULI", Material.LAPIS_LAZULI, "\u00a79Lapis Lazuli Th\u00f4", 15, 3, 80, 15000));
        this.register(new Commodity("ENCHANTED_LAPIS", Material.LAPIS_LAZULI, "\u00a79Enchanted Lapis Lazuli", 2400, 480, 12800, 1500));
        this.register(new Commodity("ENCHANTED_LAPIS_BLOCK", Material.LAPIS_BLOCK, "\u00a75Enchanted Block of Lapis", 21600, 4320, 115200, 200));
        this.register(new Commodity("REDSTONE", Material.REDSTONE, "\u00a79\u0110\u00e1 \u0110\u1ecf Th\u00f4", 10, 2, 50, 20000));
        this.register(new Commodity("ENCHANTED_REDSTONE", Material.REDSTONE, "\u00a79Enchanted Redstone", 1600, 320, 8000, 2000));
        this.register(new Commodity("ENCHANTED_REDSTONE_BLOCK", Material.REDSTONE_BLOCK, "\u00a75Enchanted Block of Redstone", 14400, 2880, 72000, 250));
        this.register(new Commodity("QUARTZ", Material.QUARTZ, "\u00a79Th\u1ea1ch Anh Th\u00f4", 15, 3, 80, 15000));
        this.register(new Commodity("ENCHANTED_QUARTZ", Material.QUARTZ, "\u00a79Enchanted Quartz", 2400, 480, 12800, 1500));
        this.register(new Commodity("ENCHANTED_QUARTZ_BLOCK", Material.QUARTZ_BLOCK, "\u00a75Enchanted Block of Quartz", 21600, 4320, 115200, 200));
        this.register(new Commodity("CWE_XP_BOTTLE_T1", Material.EXPERIENCE_BOTTLE, "\u00a7fExperience Bottle", 85, 20, 450, 5000));
        this.register(new Commodity("CWE_XP_BOTTLE_T2", Material.EXPERIENCE_BOTTLE, "\u00a7aGrand Experience Bottle", 13500, 2800, 72000, 1000));
        this.register(new Commodity("CWE_XP_BOTTLE_T3", Material.EXPERIENCE_BOTTLE, "\u00a79Titanic Experience Bottle", 125000, 25000, 650000, 200));
        this.register(new Commodity("ROTTEN_FLESH", Material.ROTTEN_FLESH, "\u00a7cTh\u1ecbt Th\u1ed1i Th\u00f4", 5, 1, 30, 15000));
        this.register(new Commodity("ENCHANTED_ROTTEN_FLESH", Material.ROTTEN_FLESH, "\u00a7aEnchanted Rotten Flesh", 800, 160, 4800, 1500));
        this.register(new Commodity("BEEF", Material.BEEF, "\u00a7cTh\u1ecbt B\u00f2 Th\u00f4", 15, 4, 80, 10000));
        this.register(new Commodity("ENCHANTED_BEEF", Material.BEEF, "\u00a7aEnchanted Raw Beef", 2400, 640, 12800, 1000));
        this.register(new Commodity("PORKCHOP", Material.PORKCHOP, "\u00a7cTh\u1ecbt Heo Th\u00f4", 15, 4, 80, 10000));
        this.register(new Commodity("ENCHANTED_PORKCHOP", Material.PORKCHOP, "\u00a7aEnchanted Raw Porkchop", 2400, 640, 12800, 1000));
        this.register(new Commodity("CHICKEN", Material.CHICKEN, "\u00a7cTh\u1ecbt G\u00e0 Th\u00f4", 10, 2, 50, 10000));
        this.register(new Commodity("ENCHANTED_CHICKEN", Material.CHICKEN, "\u00a7aEnchanted Raw Chicken", 1600, 320, 8000, 1000));
        this.register(new Commodity("MUTTON", Material.MUTTON, "\u00a7cTh\u1ecbt C\u1eebu Th\u00f4", 15, 4, 80, 10000));
        this.register(new Commodity("ENCHANTED_MUTTON", Material.MUTTON, "\u00a7aEnchanted Raw Mutton", 2400, 640, 12800, 1000));
        this.register(new Commodity("RABBIT", Material.RABBIT, "\u00a7cTh\u1ecbt Th\u1ecf Th\u00f4", 20, 5, 100, 10000));
        this.register(new Commodity("ENCHANTED_RABBIT", Material.RABBIT, "\u00a7aEnchanted Raw Rabbit", 3200, 800, 16000, 1000));
        this.register(new Commodity("BONE", Material.BONE, "\u00a7cKh\u00fac X\u01b0\u01a1ng Th\u00f4", 6, 1, 35, 12000));
        this.register(new Commodity("ENCHANTED_BONE", Material.BONE, "\u00a7aEnchanted Bone", 960, 200, 4500, 1200));
        this.register(new Commodity("GUNPOWDER", Material.GUNPOWDER, "\u00a7cThu\u1ed1c S\u00fang Th\u00f4", 15, 3, 70, 8000));
        this.register(new Commodity("ENCHANTED_GUNPOWDER", Material.GUNPOWDER, "\u00a7aEnchanted Gunpowder", 2400, 500, 11000, 800));
        this.register(new Commodity("ENDER_PEARL", Material.ENDER_PEARL, "\u00a7cNg\u1ecdc Ender Th\u00f4", 50, 10, 250, 4000));
        this.register(new Commodity("ENCHANTED_ENDER_PEARL", Material.ENDER_PEARL, "\u00a7aEnchanted Ender Pearl", 8000, 1600, 40000, 400));
        this.register(new Commodity("STRING", Material.STRING, "\u00a7cS\u1ee3i T\u01a1 Nh\u1ec7n Th\u00f4", 5, 1, 30, 15000));
        this.register(new Commodity("ENCHANTED_STRING", Material.STRING, "\u00a7aEnchanted String", 800, 160, 4500, 1500));
        this.register(new Commodity("SPIDER_EYE", Material.SPIDER_EYE, "\u00a7cM\u1eaft Nh\u1ec7n Th\u00f4", 20, 5, 100, 10000));
        this.register(new Commodity("ENCHANTED_SPIDER_EYE", Material.SPIDER_EYE, "\u00a7aEnchanted Spider Eye", 3200, 800, 16000, 1000));
        this.register(new Commodity("SLIME_BALL", Material.SLIME_BALL, "\u00a7cB\u00f3ng Nh\u1ea7y Th\u00f4", 30, 8, 150, 8000));
        this.register(new Commodity("ENCHANTED_SLIME_BALL", Material.SLIME_BALL, "\u00a7aEnchanted Slimeball", 4800, 1200, 24000, 800));
        this.register(new Commodity("OAK_LOG", Material.OAK_LOG, "\u00a7aG\u1ed7 S\u1ed3i Th\u00f4", 15, 4, 80, 10000));
        this.register(new Commodity("ENCHANTED_OAK_LOG", Material.OAK_LOG, "\u00a7aEnchanted Oak Wood", 2400, 640, 12800, 1000));
        this.register(new Commodity("SPRUCE_LOG", Material.SPRUCE_LOG, "\u00a7aG\u1ed7 Th\u00f4ng Th\u00f4", 15, 4, 80, 10000));
        this.register(new Commodity("ENCHANTED_SPRUCE_LOG", Material.SPRUCE_LOG, "\u00a7aEnchanted Spruce Wood", 2400, 640, 12800, 1000));
        this.register(new Commodity("BIRCH_LOG", Material.BIRCH_LOG, "\u00a7aG\u1ed7 B\u1ea1ch D\u01b0\u01a1ng Th\u00f4", 15, 4, 80, 10000));
        this.register(new Commodity("ENCHANTED_BIRCH_LOG", Material.BIRCH_LOG, "\u00a7aEnchanted Birch Wood", 2400, 640, 12800, 1000));
        this.register(new Commodity("DARK_OAK_LOG", Material.DARK_OAK_LOG, "\u00a7aG\u1ed7 S\u1ed3i S\u1eabm Th\u00f4", 15, 4, 80, 10000));
        this.register(new Commodity("ENCHANTED_DARK_OAK_LOG", Material.DARK_OAK_LOG, "\u00a7aEnchanted Dark Oak Wood", 2400, 640, 12800, 1000));
        this.register(new Commodity("COBBLESTONE", Material.COBBLESTONE, "\u00a77\u0110\u00e1 Cu\u1ed9i Th\u00f4", 3, 1, 15, 50000));
        this.register(new Commodity("ENCHANTED_COBBLESTONE", Material.COBBLESTONE, "\u00a79Enchanted Cobblestone", 480, 100, 2400, 5000));
        this.register(new Commodity("ENCHANTED_COBBLESTONE_BLOCK", Material.COBBLESTONE, "\u00a75Enchanted Cobblestone Block", 4320, 900, 21600, 500));
        this.register(new Commodity("COBBLED_DEEPSLATE", Material.COBBLED_DEEPSLATE, "\u00a77\u0110\u00e1 Kh\u1ea3m Th\u00f4", 5, 1, 25, 40000));
        this.register(new Commodity("ENCHANTED_COBBLED_DEEPSLATE", Material.COBBLED_DEEPSLATE, "\u00a79Enchanted Cobbled Deepslate", 800, 160, 4000, 4000));
        this.register(new Commodity("ENCHANTED_COBBLED_DEEPSLATE_BLOCK", Material.COBBLED_DEEPSLATE, "\u00a75Enchanted Cobbled Deepslate Block", 7200, 1440, 36000, 400));
    }

    private void register(Commodity com) {
        this.market.put(com.id, com);
    }

    private void loadStockDataFile() {
        this.dataFile = new File(this.plugin.getDataFolder(), "bazaar_data.yml");
        if (!this.dataFile.exists()) {
            try {
                this.dataFile.getParentFile().mkdirs();
                this.dataFile.createNewFile();
            }
            catch (IOException iOException) {
                // empty catch block
            }
        }
        this.dataConfig = YamlConfiguration.loadConfiguration((File)this.dataFile);
        for (String id : this.market.keySet()) {
            int target = this.market.get(id).targetStock;
            int savedStock = this.dataConfig.getInt("stocks." + id, target);
            this.currentStocks.put(id, savedStock);
        }
    }

    private void saveStockDataFile() {
        for (Map.Entry<String, Integer> entry : this.currentStocks.entrySet()) {
            this.dataConfig.set("stocks." + entry.getKey(), entry.getValue());
        }
        try {
            this.dataConfig.save(this.dataFile);
        }
        catch (IOException iOException) {
            // empty catch block
        }
    }

    private int getSingleItemPrice(String id, int simulatedStock, boolean isBuy) {
        Commodity com = this.market.get(id);
        if (com == null) {
            return 0;
        }
        int stock = Math.max(1, simulatedStock);
        double priceMultiplier = (double)com.targetStock / (double)stock;
        int calculatedPrice = (int)((double)com.basePrice * priceMultiplier);
        int finalPrice = Math.max(com.minPrice, Math.min(com.maxPrice, calculatedPrice));
        return isBuy ? (int)((double)finalPrice * 1.05) : (int)((double)finalPrice * 0.95);
    }

    private int getBulkBuyCost(String id, int amount) {
        int total = 0;
        int tempStock = this.currentStocks.getOrDefault(id, 1000);
        for (int i = 0; i < amount; ++i) {
            total += this.getSingleItemPrice(id, tempStock, true);
            tempStock = Math.max(1, tempStock - 1);
        }
        return total;
    }

    private int getBulkSellRevenue(String id, int amount) {
        int total = 0;
        int tempStock = this.currentStocks.getOrDefault(id, 1000);
        for (int i = 0; i < amount; ++i) {
            total += this.getSingleItemPrice(id, tempStock, false);
            ++tempStock;
        }
        return total;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("\u00a7c[Bazaar] L\u1ec7nh n\u00e0y ch\u1ec9 d\u00e0nh cho ng\u01b0\u1eddi ch\u01a1i trong game!");
            return true;
        }
        this.openMainMenu((Player)sender);
        return true;
    }

    public void openMainMenu(Player player) {
        Inventory gui = Bukkit.createInventory(null, (int)54, (String)"\u00a78Bazaar > Categories");
        this.fillGlassBackground(gui);
        gui.setItem(19, this.createMenuIcon(Material.WHEAT, "\u00a7aN\u00f4ng S\u1ea3n (Farming)", "\u00a77Bao g\u1ed3m: L\u00faa m\u00ec, C\u00e0 r\u1ed1t, Khoai t\u00e2y..."));
        gui.setItem(21, this.createMenuIcon(Material.DIAMOND_PICKAXE, "\u00a79Khai Kho\u00e1ng (Mining)", "\u00a77Bao g\u1ed3m: Than, S\u1eaft, V\u00e0ng, Kim c\u01b0\u01a1ng, Ng\u1ecdc..."));
        gui.setItem(23, this.createMenuIcon(Material.ROTTEN_FLESH, "\u00a7cChi\u1ebfn \u0110\u1ea5u / S\u0103n B\u1eafn (Combat)", "\u00a77Bao g\u1ed3m: V\u1eadt ph\u1ea9m thu th\u1eadp t\u1eeb qu\u00e1i v\u1eadt..."));
        gui.setItem(25, this.createMenuIcon(Material.OAK_LOG, "\u00a76L\u00e2m Nghi\u1ec7p (Wood/Foraging)", "\u00a77Bao g\u1ed3m: C\u00e1c lo\u1ea1i g\u1ed7 th\u00f4 v\u00e0 g\u1ed7 Enchanted..."));
        
        // Nút Bán Tất Cả ở ô 40
        gui.setItem(40, this.createActionButton(Material.HOPPER, "§c§lBán Tất Cả Vật Phẩm Hợp Lệ", Arrays.asList(
            "§7Tự động quét toàn bộ hành trang cá nhân,",
            "§7bán tất cả vật phẩm có thể giao dịch",
            "§7trên chợ Bazaar lập tức.",
            "",
            "§eClick để đại thanh lý vật phẩm!"
        )));
        
        player.openInventory(gui);
    }

    private void openCategoryMenu(Player player, String catName, String[] resourceBases, Material[] icons) {
        Inventory gui = Bukkit.createInventory(null, (int)54, (String)("\u00a78Bazaar > Category: " + catName));
        this.fillGlassBackground(gui);
        int[] layoutSlots = new int[]{10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34, 37, 38, 39, 40, 41, 42, 43};
        for (int i = 0; i < resourceBases.length && i < layoutSlots.length; ++i) {
            ItemStack item = new ItemStack(icons[i]);
            ItemMeta meta = item.getItemMeta();
            if (meta != null) {
                meta.setDisplayName("\u00a7eB\u1ed9 H\u00e0ng Ho\u00e1: " + resourceBases[i]);
                meta.setLore(Arrays.asList("\u00a77Nh\u1ea5n v\u00e0o \u0111\u1ec3 xem chi ti\u1ebft to\u00e0n b\u1ed9", "\u00a77c\u00e1c bi\u1ebfn th\u1ec3 Enchanted c\u1ee7a t\u00e0i nguy\u00ean n\u00e0y.", "", "\u00a7eClick \u0111\u1ec3 m\u1edf r\u1ed9ng!"));
                meta.getPersistentDataContainer().set(new NamespacedKey((Plugin)this.plugin, "bazaar_res_base"), PersistentDataType.STRING, resourceBases[i]);
                item.setItemMeta(meta);
            }
            gui.setItem(layoutSlots[i], item);
        }
        this.addBackButton(gui, 49);
        player.openInventory(gui);
    }

    private void openProductVariantsMenu(Player player, String resBase, String[] variants) {
        Inventory gui = Bukkit.createInventory(null, (int)54, (String)("\u00a78Bazaar > Product: " + resBase));
        this.fillGlassBackground(gui);
        int[] horizontalSlots = new int[]{21, 22, 23};
        int index = 0;
        for (String id : variants) {
            if (index >= horizontalSlots.length) break;
            ItemStack item = new ItemStack(this.market.get(id).mat);
            ItemMeta meta = item.getItemMeta();
            if (meta != null) {
                int nextBuy = this.getSingleItemPrice(id, this.currentStocks.get(id), true);
                int nextSell = this.getSingleItemPrice(id, this.currentStocks.get(id), false);
                meta.setDisplayName(this.market.get(id).name);
                meta.setLore(Arrays.asList("\u00a77M\u1ee9c gi\u00e1 th\u1ecb tr\u01b0\u1eddng \u0111\u1ed9ng AMM:", " \u00a78\u2022 \u00a77Gi\u00e1 mua m\u00f3n k\u1ebf ti\u1ebfp: \u00a7a$" + nextBuy, " \u00a78\u2022 \u00a77Gi\u00e1 b\u00e1n m\u00f3n k\u1ebf ti\u1ebfp: \u00a7c$" + nextSell, "", "\u00a77\ud83d\udcca Kho d\u1ef1 tr\u1eef to\u00e0n c\u1ea7u: \u00a7b" + String.valueOf(this.currentStocks.get(id)) + " m\u00f3n", "", "\u00a7eClick \u0111\u1ec3 m\u1edf b\u1ea3ng kh\u1edbp l\u1ec7nh Mua/B\u00e1n!"));
                if (id.startsWith("ENCHANTED_")) {
                    meta.addEnchant(Enchantment.MENDING, 1, true);
                    meta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ENCHANTS});
                }
                meta.getPersistentDataContainer().set(new NamespacedKey((Plugin)this.plugin, "bazaar_final_id"), PersistentDataType.STRING, id);
                item.setItemMeta(meta);
            }
            gui.setItem(horizontalSlots[index++], item);
        }
        this.addBackButton(gui, 49);
        player.openInventory(gui);
    }

    private void openTradeActionMenu(Player player, String itemId) {
        Inventory gui = Bukkit.createInventory(null, (int)54, (String)("\u00a78Bazaar > Action: " + itemId));
        this.fillGlassBackground(gui);
        int cost1 = this.getBulkBuyCost(itemId, 1);
        int cost64 = this.getBulkBuyCost(itemId, 64);
        int rev1 = this.getBulkSellRevenue(itemId, 1);
        gui.setItem(19, this.createActionButton(Material.GOLD_NUGGET, "\u00a7a\u00a7lInstant Buy (x1)", Arrays.asList("\u00a77Kh\u1edbp l\u1ec7nh mua l\u1eadp t\u1ee9c 1 s\u1ea3n ph\u1ea9m.", "", "\u00a77T\u1ed5ng chi ph\u00ed: \u00a7e$" + cost1, "", "\u00a7eClick \u0111\u1ec3 thanh to\u00e1n v\u00ed Vault!")));
        gui.setItem(28, this.createActionButton(Material.GOLD_INGOT, "\u00a7a\u00a7lInstant Buy (x64 Stack)", Arrays.asList("\u00a77Kh\u1edbp l\u1ec7nh mua l\u1eadp t\u1ee9c 64 s\u1ea3n ph\u1ea9m.", "\u00a77(\u0110\u00e3 t\u00ednh t\u1ef7 l\u1ec7 tr\u01b0\u1ee3t gi\u00e1 khan hi\u1ebfm qu\u1eb7ng)", "", "\u00a77T\u1ed5ng chi ph\u00ed: \u00a7e$" + cost64, "", "\u00a7eClick \u0111\u1ec3 thanh to\u00e1n v\u00ed Vault!")));
        ItemStack displayItem = new ItemStack(this.market.get(itemId).mat);
        ItemMeta dMeta = displayItem.getItemMeta();
        if (dMeta != null) {
            dMeta.setDisplayName(this.market.get(itemId).name);
            dMeta.setLore(Arrays.asList("\u00a77\ud83d\udcca Th\u1ed1ng k\u00ea kho: \u00a7b" + String.valueOf(this.currentStocks.get(itemId)) + " m\u00f3n"));
            if (itemId.startsWith("ENCHANTED_")) {
                dMeta.addEnchant(Enchantment.MENDING, 1, true);
                dMeta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ENCHANTS});
            }
            displayItem.setItemMeta(dMeta);
        }
        gui.setItem(22, displayItem);
        gui.setItem(25, this.createActionButton(Material.IRON_NUGGET, "\u00a7c\u00a7lInstant Sell (x1)", Arrays.asList("\u00a77Kh\u1edbp l\u1ec7nh b\u00e1n l\u1eadp t\u1ee9c 1 s\u1ea3n ph\u1ea9m.", "", "\u00a77T\u1ed5ng thu v\u1ec1: \u00a7e$" + rev1, "", "\u00a7eClick \u0111\u1ec3 x\u1ea3 h\u00e0ng l\u1ea5y ti\u1ec1n m\u1eb7t!")));
        gui.setItem(34, this.createActionButton(Material.IRON_INGOT, "\u00a7c\u00a7lInstant Sell All (X\u1ea3 S\u1ea1ch)", Arrays.asList("\u00a77Thanh l\u00fd s\u1ea1ch s\u00e0nh sanh kho \u0111\u1ed3 h\u00e0nh trang.", "\u00a77(Gi\u00e1 s\u1ebd t\u1ef1 s\u1eadp th\u1ea5p d\u1ea7n n\u1ebfu x\u1ea3 l\u01b0\u1ee3ng l\u1edbn c\u00f9ng l\u00fac)", "", "\u00a7eClick \u0111\u1ec3 \u0111\u1ea1i thanh l\u00fd kho \u0111\u1ed3!")));
        this.addBackButton(gui, 49);
        player.openInventory(gui);
    }

    @EventHandler
    public void onBazaarClick(InventoryClickEvent event) {
        String title = event.getView().getTitle();
        if (!title.startsWith("\u00a78Bazaar")) {
            return;
        }
        event.setCancelled(true);
        Player player = (Player)event.getWhoClicked();
        ItemStack clicked = event.getCurrentItem();
        if (clicked == null || clicked.getType() == Material.AIR) {
            return;
        }
        if (event.getSlot() == 49 && clicked.getType() == Material.ARROW) {
            if (title.equals("\u00a78Bazaar > Categories")) {
                return;
            }
            this.openMainMenu(player);
            return;
        }
        if (title.equals("\u00a78Bazaar > Categories")) {
            int slot = event.getSlot();
            if (slot == 19) {
                this.openCategoryMenu(player, "Farming", new String[]{"L\u00faa M\u00ec", "C\u00e0 R\u1ed1t", "Khoai T\u00e2y"}, new Material[]{Material.WHEAT, Material.CARROT, Material.POTATO});
            }
            if (slot == 21) {
                this.openCategoryMenu(player, "Mining", new String[]{"Đá Cuội", "Đá Khảm", "Than Đá", "Sắt Ma Thuật", "Vàng Ròng", "Kim Cương", "Ngọc Lục Bảo", "Lapis Lazuli", "Đá Đỏ", "Thạch Anh", "Bình Kinh Nghiệm"}, new Material[]{Material.COBBLESTONE, Material.COBBLED_DEEPSLATE, Material.COAL, Material.IRON_INGOT, Material.GOLD_INGOT, Material.DIAMOND, Material.EMERALD, Material.LAPIS_LAZULI, Material.REDSTONE, Material.QUARTZ, Material.EXPERIENCE_BOTTLE});
            }
            if (slot == 23) {
                this.openCategoryMenu(player, "Combat", new String[]{"Th\u1ecbt Th\u1ed1i", "Th\u1ecbt B\u00f2", "Th\u1ecbt Heo", "Th\u1ecbt G\u00e0", "Th\u1ecbt C\u1eebu", "Th\u1ecbt Th\u1ecf", "Kh\u00fac X\u01b0\u01a1ng", "Thu\u1ed1c S\u00fang", "Ng\u1ecdc Ender", "T\u01a1 Nh\u1ec7n", "M\u1eaft Nh\u1ec7n", "B\u00f3ng Nh\u1ea7y"}, new Material[]{Material.ROTTEN_FLESH, Material.BEEF, Material.PORKCHOP, Material.CHICKEN, Material.MUTTON, Material.RABBIT, Material.BONE, Material.GUNPOWDER, Material.ENDER_PEARL, Material.STRING, Material.SPIDER_EYE, Material.SLIME_BALL});
            }
            if (slot == 25) {
                this.openCategoryMenu(player, "Foraging", new String[]{"G\u1ed7 S\u1ed3i", "G\u1ed7 Th\u00f4ng", "G\u1ed7 B\u1ea1ch D\u01b0\u01a1ng", "G\u1ed7 S\u1ed3i S\u1eabm"}, new Material[]{Material.OAK_LOG, Material.SPRUCE_LOG, Material.BIRCH_LOG, Material.DARK_OAK_LOG});
            }
            if (slot == 40) {
                this.handleGlobalSellAll(player);
            }
        } else if (title.startsWith("\u00a78Bazaar > Category: ")) {
            ItemMeta meta = clicked.getItemMeta();
            if (meta == null) {
                return;
            }
            String base = (String)meta.getPersistentDataContainer().get(new NamespacedKey((Plugin)this.plugin, "bazaar_res_base"), PersistentDataType.STRING);
            if (base == null) {
                return;
            }
            if (base.equals("L\u00faa M\u00ec")) {
                this.openProductVariantsMenu(player, "L\u00faa M\u00ec", new String[]{"WHEAT", "ENCHANTED_WHEAT"});
            }
            if (base.equals("C\u00e0 R\u1ed1t")) {
                this.openProductVariantsMenu(player, "C\u00e0 R\u1ed1t", new String[]{"CARROT", "ENCHANTED_CARROT"});
            }
            if (base.equals("Khoai T\u00e2y")) {
                this.openProductVariantsMenu(player, "Khoai T\u00e2y", new String[]{"POTATO", "ENCHANTED_POTATO"});
            }
            if (base.equals("Đá Cuội")) {
                this.openProductVariantsMenu(player, "Đá Cuội", new String[]{"COBBLESTONE", "ENCHANTED_COBBLESTONE", "ENCHANTED_COBBLESTONE_BLOCK"});
            }
            if (base.equals("Đá Khảm")) {
                this.openProductVariantsMenu(player, "Đá Khảm", new String[]{"COBBLED_DEEPSLATE", "ENCHANTED_COBBLED_DEEPSLATE", "ENCHANTED_COBBLED_DEEPSLATE_BLOCK"});
            }
            if (base.equals("Than Đá")) {
                this.openProductVariantsMenu(player, "Than \u0110\u00e1", new String[]{"COAL", "ENCHANTED_COAL", "ENCHANTED_COAL_BLOCK"});
            }
            if (base.equals("S\u1eaft Ma Thu\u1eadt")) {
                this.openProductVariantsMenu(player, "S\u1eaft Ma Thu\u1eadt", new String[]{"IRON_INGOT", "ENCHANTED_IRON", "ENCHANTED_IRON_BLOCK"});
            }
            if (base.equals("V\u00e0ng R\u00f2ng")) {
                this.openProductVariantsMenu(player, "V\u00e0ng R\u00f2ng", new String[]{"GOLD_INGOT", "ENCHANTED_GOLD", "ENCHANTED_GOLD_BLOCK"});
            }
            if (base.equals("Kim C\u01b0\u01a1ng")) {
                this.openProductVariantsMenu(player, "Kim C\u01b0\u01a1ng", new String[]{"DIAMOND", "ENCHANTED_DIAMOND", "ENCHANTED_DIAMOND_BLOCK"});
            }
            if (base.equals("Ng\u1ecdc L\u1ee5c B\u1ea3o")) {
                this.openProductVariantsMenu(player, "Ng\u1ecdc L\u1ee5c B\u1ea3o", new String[]{"EMERALD", "ENCHANTED_EMERALD", "ENCHANTED_EMERALD_BLOCK"});
            }
            if (base.equals("Lapis Lazuli")) {
                this.openProductVariantsMenu(player, "Lapis Lazuli", new String[]{"LAPIS_LAZULI", "ENCHANTED_LAPIS", "ENCHANTED_LAPIS_BLOCK"});
            }
            if (base.equals("\u0110\u00e1 \u0110\u1ecf")) {
                this.openProductVariantsMenu(player, "\u0110\u00e1 \u0110\u1ecf", new String[]{"REDSTONE", "ENCHANTED_REDSTONE", "ENCHANTED_REDSTONE_BLOCK"});
            }
            if (base.equals("Th\u1ea1ch Anh")) {
                this.openProductVariantsMenu(player, "Th\u1ea1ch Anh", new String[]{"QUARTZ", "ENCHANTED_QUARTZ", "ENCHANTED_QUARTZ_BLOCK"});
            }
            if (base.equals("B\u00ecnh Kinh Nghi\u1ec7m")) {
                this.openProductVariantsMenu(player, "B\u00ecnh Kinh Nghi\u1ec7m", new String[]{"CWE_XP_BOTTLE_T1", "CWE_XP_BOTTLE_T2", "CWE_XP_BOTTLE_T3"});
            }
            if (base.equals("Th\u1ecbt Th\u1ed1i")) {
                this.openProductVariantsMenu(player, "Th\u1ecbt Th\u1ed1i", new String[]{"ROTTEN_FLESH", "ENCHANTED_ROTTEN_FLESH"});
            }
            if (base.equals("Th\u1ecbt B\u00f2")) {
                this.openProductVariantsMenu(player, "Th\u1ecbt B\u00f2", new String[]{"BEEF", "ENCHANTED_BEEF"});
            }
            if (base.equals("Th\u1ecbt Heo")) {
                this.openProductVariantsMenu(player, "Th\u1ecbt Heo", new String[]{"PORKCHOP", "ENCHANTED_PORKCHOP"});
            }
            if (base.equals("Th\u1ecbt G\u00e0")) {
                this.openProductVariantsMenu(player, "Th\u1ecbt G\u00e0", new String[]{"CHICKEN", "ENCHANTED_CHICKEN"});
            }
            if (base.equals("Th\u1ecbt C\u1eebu")) {
                this.openProductVariantsMenu(player, "Th\u1ecbt C\u1eebu", new String[]{"MUTTON", "ENCHANTED_MUTTON"});
            }
            if (base.equals("Th\u1ecbt Th\u1ecf")) {
                this.openProductVariantsMenu(player, "Th\u1ecbt Th\u1ecf", new String[]{"RABBIT", "ENCHANTED_RABBIT"});
            }
            if (base.equals("Kh\u00fac X\u01b0\u01a1ng")) {
                this.openProductVariantsMenu(player, "Kh\u00fac X\u01b0\u01a1ng", new String[]{"BONE", "ENCHANTED_BONE"});
            }
            if (base.equals("Thu\u1ed1c S\u00fang")) {
                this.openProductVariantsMenu(player, "Thu\u1ed1c S\u00fang", new String[]{"GUNPOWDER", "ENCHANTED_GUNPOWDER"});
            }
            if (base.equals("Ng\u1ecdc Ender")) {
                this.openProductVariantsMenu(player, "Ng\u1ecdc Ender", new String[]{"ENDER_PEARL", "ENCHANTED_ENDER_PEARL"});
            }
            if (base.equals("T\u01a1 Nh\u1ec7n")) {
                this.openProductVariantsMenu(player, "T\u01a1 Nh\u1ec7n", new String[]{"STRING", "ENCHANTED_STRING"});
            }
            if (base.equals("M\u1eaft Nh\u1ec7n")) {
                this.openProductVariantsMenu(player, "M\u1eaft Nh\u1ec7n", new String[]{"SPIDER_EYE", "ENCHANTED_SPIDER_EYE"});
            }
            if (base.equals("B\u00f3ng Nh\u1ea7y")) {
                this.openProductVariantsMenu(player, "B\u00f3ng Nh\u1ea7y", new String[]{"SLIME_BALL", "ENCHANTED_SLIME_BALL"});
            }
            if (base.equals("G\u1ed7 S\u1ed3i")) {
                this.openProductVariantsMenu(player, "G\u1ed7 S\u1ed3i", new String[]{"OAK_LOG", "ENCHANTED_OAK_LOG"});
            }
            if (base.equals("G\u1ed7 Th\u00f4ng")) {
                this.openProductVariantsMenu(player, "G\u1ed7 Th\u00f4ng", new String[]{"SPRUCE_LOG", "ENCHANTED_SPRUCE_LOG"});
            }
            if (base.equals("G\u1ed7 B\u1ea1ch D\u01b0\u01a1ng")) {
                this.openProductVariantsMenu(player, "G\u1ed7 B\u1ea1ch D\u01b0\u01a1ng", new String[]{"BIRCH_LOG", "ENCHANTED_BIRCH_LOG"});
            }
            if (base.equals("G\u1ed7 S\u1ed3i S\u1eabm")) {
                this.openProductVariantsMenu(player, "G\u1ed7 S\u1ed3i S\u1eabm", new String[]{"DARK_OAK_LOG", "ENCHANTED_DARK_OAK_LOG"});
            }
        } else if (title.startsWith("\u00a78Bazaar > Product: ")) {
            ItemMeta meta = clicked.getItemMeta();
            if (meta == null) {
                return;
            }
            String id = (String)meta.getPersistentDataContainer().get(new NamespacedKey((Plugin)this.plugin, "bazaar_final_id"), PersistentDataType.STRING);
            if (id != null) {
                this.openTradeActionMenu(player, id);
            }
        } else if (title.startsWith("\u00a78Bazaar > Action: ")) {
            String itemId = title.replace("\u00a78Bazaar > Action: ", "");
            int slot = event.getSlot();
            if (slot == 19) {
                this.handleBuyAction(player, itemId, 1);
            }
            if (slot == 28) {
                this.handleBuyAction(player, itemId, 64);
            }
            if (slot == 25) {
                this.handleSellAction(player, itemId, 1);
            }
            if (slot == 34) {
                this.handleSellAllAction(player, itemId);
            }
        }
    }

    private void handleBuyAction(Player player, String id, int amount) {
        ItemMeta meta;
        int totalCost = this.getBulkBuyCost(id, amount);
        if (this.econ.getBalance((OfflinePlayer)player) < (double)totalCost) {
            player.sendMessage("\u00a7cV\u00ed ti\u1ec1n Vault r\u1ed7ng tu\u1ebfch, \u0111\u00e9o \u0111\u1ee7 ti\u1ec1n kh\u1edbp l\u1ec7nh!");
            return;
        }
        this.econ.withdrawPlayer((OfflinePlayer)player, (double)totalCost);
        int stock = this.currentStocks.get(id);
        this.currentStocks.put(id, Math.max(1, stock - amount));
        this.saveStockDataFile();
        ItemStack item = new ItemStack(this.market.get(id).mat, amount);
        if ((id.startsWith("ENCHANTED_") || id.startsWith("CWE_XP_BOTTLE_")) && (meta = item.getItemMeta()) != null) {
            meta.setDisplayName(this.market.get(id).name);
            meta.getPersistentDataContainer().set(new NamespacedKey((Plugin)this.plugin, "bazaar_id"), PersistentDataType.STRING, id);
            if (id.equals("CWE_XP_BOTTLE_T1")) {
                meta.getPersistentDataContainer().set(new NamespacedKey((Plugin)this.plugin, "xp_payout"), PersistentDataType.INTEGER, 250);
                meta.setLore(Arrays.asList("\u00a77Grants \u00a73250 \u00a77Experience", "\u00a77when thrown.", "", "\u00a7eRight-click to throw!", "\u00a7f", "\u00a7f\u00a7lCOMMON"));
            } else if (id.equals("CWE_XP_BOTTLE_T2")) {
                meta.getPersistentDataContainer().set(new NamespacedKey((Plugin)this.plugin, "xp_payout"), PersistentDataType.INTEGER, 3000);
                meta.setLore(Arrays.asList("\u00a77Grants \u00a733,000 \u00a77Experience", "\u00a77when thrown.", "", "\u00a7eRight-click to throw!", "\u00a7f", "\u00a7a\u00a7lUNCOMMON"));
            } else if (id.equals("CWE_XP_BOTTLE_T3")) {
                meta.getPersistentDataContainer().set(new NamespacedKey((Plugin)this.plugin, "xp_payout"), PersistentDataType.INTEGER, 15000);
                meta.setLore(Arrays.asList("\u00a77Grants \u00a7315,000 \u00a77Experience", "\u00a77when thrown.", "", "\u00a7eRight-click to throw!", "\u00a7f", "\u00a79\u00a7lRARE"));
            } else {
                meta.addEnchant(Enchantment.MENDING, 1, true);
                meta.addItemFlags(new ItemFlag[]{ItemFlag.HIDE_ENCHANTS});
            }
            item.setItemMeta(meta);
        }
        for (ItemStack drop : player.getInventory().addItem(new ItemStack[]{item}).values()) {
            player.getWorld().dropItemNaturally(player.getLocation(), drop);
        }
        player.sendMessage("\u00a7a\ud83d\udfe9 Kh\u1edbp l\u1ec7nh mua x" + amount + " " + this.market.get(id).name + " tr\u1eeb \u00a7e$" + totalCost);
        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
        this.openTradeActionMenu(player, id);
    }

    private void handleSellAction(Player player, String id, int amount) {
        ItemStack match = this.findMatchingItem(player, id);
        if (match == null || match.getAmount() < amount) {
            player.sendMessage("\u00a7cTrong t\u00fai \u0111\u1ed3 h\u00e0nh trang \u0111\u00e9o c\u00f3 v\u1eadt ph\u1ea9m n\u00e0y \u0111\u1ec3 x\u1ea3!");
            return;
        }
        int revenue = this.getBulkSellRevenue(id, amount);
        match.setAmount(match.getAmount() - amount);
        this.currentStocks.put(id, this.currentStocks.get(id) + amount);
        this.saveStockDataFile();
        this.econ.depositPlayer((OfflinePlayer)player, (double)revenue);
        player.sendMessage("\u00a7a\ud83d\udfe5 Kh\u1edbp l\u1ec7nh b\u00e1n x" + amount + " " + this.market.get(id).name + " h\u00fap v\u1ec1 \u00a7e$" + revenue);
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 1.0f, 1.5f);
        this.openTradeActionMenu(player, id);
    }

    private void handleSellAllAction(Player player, String id) {
        int totalSold = 0;
        NamespacedKey key = new NamespacedKey((Plugin)this.plugin, "bazaar_id");
        for (ItemStack item : player.getInventory().getContents()) {
            if (item == null || item.getType() == Material.AIR) continue;
            boolean isMatch = false;
            if (id.startsWith("ENCHANTED_") || id.startsWith("CWE_XP_BOTTLE_")) {
                if (item.hasItemMeta() && item.getItemMeta().getPersistentDataContainer().has(key, PersistentDataType.STRING) && ((String)item.getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.STRING)).equals(id)) {
                    isMatch = true;
                }
            } else if (!(item.getType() != this.market.get(id).mat || item.hasItemMeta() && item.getItemMeta().getPersistentDataContainer().has(key, PersistentDataType.STRING))) {
                isMatch = true;
            }
            if (!isMatch) continue;
            totalSold += item.getAmount();
            item.setAmount(0);
        }
        if (totalSold == 0) {
            player.sendMessage("\u00a7cKh\u00f4ng qu\u00e9t th\u1ea5y m\u00f3n n\u00e0o h\u1ee3p l\u1ec7 trong h\u00e0nh trang c\u00e1 nh\u00e2n \u0111\u1ec3 x\u1ea3 s\u1ec9 c\u1ea3!");
            return;
        }
        int totalRevenue = this.getBulkSellRevenue(id, totalSold);
        this.currentStocks.put(id, this.currentStocks.get(id) + totalSold);
        this.saveStockDataFile();
        this.econ.depositPlayer((OfflinePlayer)player, (double)totalRevenue);
        player.sendMessage("\u00a7a\ud83d\udfe5 \u0110\u1ea0I X\u1ea2 KHO! \u0110\u00e3 thanh l\u00fd x" + totalSold + " " + this.market.get(id).name + " nh\u1eadn v\u1ec1 \u00a7e$" + totalRevenue);
        player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_CELEBRATE, 1.0f, 1.0f);
        player.updateInventory();
        this.openTradeActionMenu(player, id);
    }

    private ItemStack findMatchingItem(Player player, String id) {
        NamespacedKey key = new NamespacedKey((Plugin)this.plugin, "bazaar_id");
        for (ItemStack item : player.getInventory().getContents()) {
            if (item == null || item.getType() == Material.AIR || !(id.startsWith("ENCHANTED_") || id.startsWith("CWE_XP_BOTTLE_") ? item.hasItemMeta() && item.getItemMeta().getPersistentDataContainer().has(key, PersistentDataType.STRING) && ((String)item.getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.STRING)).equals(id) : item.getType() == this.market.get(id).mat && (!item.hasItemMeta() || !item.getItemMeta().getPersistentDataContainer().has(key, PersistentDataType.STRING)))) continue;
            return item;
        }
        return null;
    }

    private void fillGlassBackground(Inventory gui) {
        ItemStack gray = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta m = gray.getItemMeta();
        if (m != null) {
            m.setDisplayName("\u00a77 ");
            gray.setItemMeta(m);
        }
        for (int i = 0; i < gui.getSize(); ++i) {
            gui.setItem(i, gray);
        }
    }

    private void addBackButton(Inventory gui, int slot) {
        ItemStack back = new ItemStack(Material.ARROW);
        ItemMeta bM = back.getItemMeta();
        if (bM != null) {
            bM.setDisplayName("\u00a7c\u25c0 Quay L\u1ea1i Menu");
            back.setItemMeta(bM);
        }
        gui.setItem(slot, back);
    }

    private ItemStack createMenuIcon(Material mat, String name, String loreDesc) {
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName("\u00a7e" + name);
            meta.setLore(Arrays.asList(loreDesc, "", "\u00a7eClick chu\u1ed9t \u0111\u1ec3 xem b\u00e1ch h\u00f3a m\u1eb7t h\u00e0ng!"));
            item.setItemMeta(meta);
        }
        return item;
    }

    private ItemStack createActionButton(Material mat, String name, List<String> lore) {
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            meta.setLore(lore);
            item.setItemMeta(meta);
        }
        return item;
    }

    private Commodity findCommodityMatch(ItemStack item) {
        if (item == null || item.getType() == Material.AIR) return null;
        NamespacedKey key = new NamespacedKey((Plugin)this.plugin, "bazaar_id");
        ItemMeta meta = item.getItemMeta();
        
        if (meta != null) {
            PersistentDataContainer pdc = meta.getPersistentDataContainer();
            if (pdc.has(key, PersistentDataType.STRING)) {
                String bazaarId = pdc.get(key, PersistentDataType.STRING);
                return this.market.get(bazaarId);
            }
        }
        
        for (Commodity com : this.market.values()) {
            if (com.mat == item.getType() && !com.id.startsWith("ENCHANTED_") && !com.id.startsWith("CWE_XP_BOTTLE_")) {
                if (meta != null) {
                    PersistentDataContainer pdc = meta.getPersistentDataContainer();
                    if (pdc.has(new NamespacedKey((Plugin)this.plugin, "cwe_id"), PersistentDataType.STRING) 
                        || pdc.has(new NamespacedKey((Plugin)this.plugin, "cwe_autocraft_bag"), PersistentDataType.INTEGER)) {
                        continue;
                    }
                }
                return com;
            }
        }
        return null;
    }

    private void handleGlobalSellAll(Player player) {
        java.util.Map<String, Integer> itemsToSell = new java.util.HashMap<>();
        
        for (ItemStack item : player.getInventory().getContents()) {
            Commodity com = this.findCommodityMatch(item);
            if (com != null) {
                itemsToSell.put(com.id, itemsToSell.getOrDefault(com.id, 0) + item.getAmount());
            }
        }
        
        if (itemsToSell.isEmpty()) {
            player.sendMessage("§cKhông tìm thấy vật phẩm nào hợp lệ trên Bazaar trong hành trang của bạn để bán!");
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
            return;
        }
        
        int totalRevenue = 0;
        java.util.List<String> details = new java.util.ArrayList<>();
        
        for (java.util.Map.Entry<String, Integer> entry : itemsToSell.entrySet()) {
            String id = entry.getKey();
            int amount = entry.getValue();
            
            int revenue = this.getBulkSellRevenue(id, amount);
            totalRevenue += revenue;
            
            int currentStock = this.currentStocks.getOrDefault(id, 1000);
            this.currentStocks.put(id, currentStock + amount);
            
            Commodity com = this.market.get(id);
            details.add("§a- x" + amount + " " + com.name + " §8» §e+$" + revenue);
        }
        
        for (int i = 0; i < player.getInventory().getSize(); i++) {
            ItemStack item = player.getInventory().getItem(i);
            Commodity com = this.findCommodityMatch(item);
            if (com != null) {
                player.getInventory().setItem(i, null);
            }
        }
        
        this.saveStockDataFile();
        this.econ.depositPlayer((OfflinePlayer)player, (double)totalRevenue);
        
        player.sendMessage("§a§l[Bazaar] ĐẠI THANH LÝ HÀNH TRANG THÀNH CÔNG!");
        for (String line : details) {
            player.sendMessage(line);
        }
        player.sendMessage("§d§lTổng thu nhập: §a+$" + totalRevenue);
        player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_CELEBRATE, 1.0f, 1.0f);
        player.updateInventory();
        
        this.openMainMenu(player); // Re-open main menu to refresh
    }

    private static class Commodity {
        String id;
        Material mat;
        String name;
        int basePrice;
        int minPrice;
        int maxPrice;
        int targetStock;

        Commodity(String id, Material mat, String name, int base, int min, int max, int target) {
            this.id = id;
            this.mat = mat;
            this.name = name;
            this.basePrice = base;
            this.minPrice = min;
            this.maxPrice = max;
            this.targetStock = target;
        }
    }
}

