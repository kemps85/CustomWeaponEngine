package org.example.bazaar;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
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
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BazaarGUI implements Listener, CommandExecutor {
    private final JavaPlugin plugin;
    private final Economy econ;
    
    private final String mainTitle = "§8Bazaar > Categories";
    private final String prefixCat = "§8Bazaar > Category: ";
    private final String prefixProd = "§8Bazaar > Product: ";
    private final String prefixTrade = "§8Bazaar > Action: ";

    private File dataFile;
    private FileConfiguration dataConfig;

    private static class Commodity {
        String id; Material mat; String name;
        int basePrice; int minPrice; int maxPrice; int targetStock;

        Commodity(String id, Material mat, String name, int base, int min, int max, int target) {
            this.id = id; this.mat = mat; this.name = name;
            this.basePrice = base; this.minPrice = min; this.maxPrice = max; this.targetStock = target;
        }
    }

    private final Map<String, Commodity> market = new HashMap<>();
    private final Map<String, Integer> currentStocks = new HashMap<>();

    public BazaarGUI(JavaPlugin plugin, Economy econ) {
        this.plugin = plugin;
        this.econ = econ;
        
        setupMarketRegistry();
        loadStockDataFile();
    }

    private void setupMarketRegistry() {
        // 🌾 Nhóm Nông Sản
        register(new Commodity("WHEAT", Material.WHEAT, "§aLúa Mì Thô", 10, 2, 50, 10000));
        register(new Commodity("ENCHANTED_WHEAT", Material.WHEAT, "§aEnchanted Wheat", 1600, 320, 8000, 1000));
        register(new Commodity("CARROT", Material.CARROT, "§aCà Rốt Thô", 12, 3, 60, 10000));
        register(new Commodity("ENCHANTED_CARROT", Material.CARROT, "§aEnchanted Carrot", 1920, 480, 9600, 1000));
        register(new Commodity("POTATO", Material.POTATO, "§aKhoai Tây Thô", 12, 3, 60, 10000));
        register(new Commodity("ENCHANTED_POTATO", Material.POTATO, "§aEnchanted Potato", 1920, 480, 9600, 1000));
        
        // ⛏️ Nhóm Khai Khoáng
        register(new Commodity("COAL", Material.COAL, "§9Than Đá Thô", 20, 5, 100, 12000));
        register(new Commodity("ENCHANTED_COAL", Material.COAL, "§9Enchanted Coal", 3200, 800, 16000, 1200));
        register(new Commodity("ENCHANTED_COAL_BLOCK", Material.COAL_BLOCK, "§5Enchanted Block of Coal", 28800, 7200, 144000, 200));
        register(new Commodity("IRON_INGOT", Material.IRON_INGOT, "§9Phôi Sắt Thô", 40, 10, 250, 8000));
        register(new Commodity("ENCHANTED_IRON", Material.IRON_INGOT, "§9Enchanted Iron Ingot", 6400, 1600, 40000, 800));
        register(new Commodity("ENCHANTED_IRON_BLOCK", Material.IRON_BLOCK, "§5Enchanted Block of Iron", 57600, 14400, 360000, 150));
        register(new Commodity("GOLD_INGOT", Material.GOLD_INGOT, "§9Phôi Vàng Thô", 60, 15, 350, 7000));
        register(new Commodity("ENCHANTED_GOLD", Material.GOLD_INGOT, "§9Enchanted Gold Ingot", 9600, 2400, 56000, 700));
        register(new Commodity("ENCHANTED_GOLD_BLOCK", Material.GOLD_BLOCK, "§5Enchanted Block of Gold", 86400, 21600, 504000, 100));
        register(new Commodity("DIAMOND", Material.DIAMOND, "§9Kim Cương Thô", 100, 20, 600, 5000));
        register(new Commodity("ENCHANTED_DIAMOND", Material.DIAMOND, "§9Enchanted Diamond", 16000, 3200, 96000, 500));
        register(new Commodity("ENCHANTED_DIAMOND_BLOCK", Material.DIAMOND_BLOCK, "§5Enchanted Block of Diamond", 144000, 28800, 864000, 100));
        register(new Commodity("EMERALD", Material.EMERALD, "§9Ngọc Lục Bảo Thô", 120, 25, 700, 4000));
        register(new Commodity("ENCHANTED_EMERALD", Material.EMERALD, "§9Enchanted Emerald", 19200, 4000, 112000, 400));
        register(new Commodity("ENCHANTED_EMERALD_BLOCK", Material.EMERALD_BLOCK, "§5Enchanted Block of Emerald", 172800, 36000, 1008000, 50));
        register(new Commodity("LAPIS_LAZULI", Material.LAPIS_LAZULI, "§9Lapis Lazuli Thô", 15, 3, 80, 15000));
        register(new Commodity("ENCHANTED_LAPIS", Material.LAPIS_LAZULI, "§9Enchanted Lapis Lazuli", 2400, 480, 12800, 1500));
        register(new Commodity("ENCHANTED_LAPIS_BLOCK", Material.LAPIS_BLOCK, "§5Enchanted Block of Lapis", 21600, 4320, 115200, 200));
        register(new Commodity("REDSTONE", Material.REDSTONE, "§9Đá Đỏ Thô", 10, 2, 50, 20000));
        register(new Commodity("ENCHANTED_REDSTONE", Material.REDSTONE, "§9Enchanted Redstone", 1600, 320, 8000, 2000));
        register(new Commodity("ENCHANTED_REDSTONE_BLOCK", Material.REDSTONE_BLOCK, "§5Enchanted Block of Redstone", 14400, 2880, 72000, 250));

        // 🥩 Nhóm Săn Bắn / Quái Vật
        register(new Commodity("ROTTEN_FLESH", Material.ROTTEN_FLESH, "§cThịt Thối Thô", 5, 1, 30, 15000));
        register(new Commodity("ENCHANTED_ROTTEN_FLESH", Material.ROTTEN_FLESH, "§aEnchanted Rotten Flesh", 800, 160, 4800, 1500));
        register(new Commodity("BEEF", Material.BEEF, "§cThịt Bò Thô", 15, 4, 80, 10000));
        register(new Commodity("ENCHANTED_BEEF", Material.BEEF, "§aEnchanted Raw Beef", 2400, 640, 12800, 1000));
        register(new Commodity("PORKCHOP", Material.PORKCHOP, "§cThịt Heo Thô", 15, 4, 80, 10000));
        register(new Commodity("ENCHANTED_PORKCHOP", Material.PORKCHOP, "§aEnchanted Raw Porkchop", 2400, 640, 12800, 1000));
        register(new Commodity("CHICKEN", Material.CHICKEN, "§cThịt Gà Thô", 10, 2, 50, 10000));
        register(new Commodity("ENCHANTED_CHICKEN", Material.CHICKEN, "§aEnchanted Raw Chicken", 1600, 320, 8000, 1000));
        register(new Commodity("MUTTON", Material.MUTTON, "§cThịt Cừu Thô", 15, 4, 80, 10000));
        register(new Commodity("ENCHANTED_MUTTON", Material.MUTTON, "§aEnchanted Raw Mutton", 2400, 640, 12800, 1000));
        register(new Commodity("RABBIT", Material.RABBIT, "§cThịt Thỏ Thô", 20, 5, 100, 10000));
        register(new Commodity("ENCHANTED_RABBIT", Material.RABBIT, "§aEnchanted Raw Rabbit", 3200, 800, 16000, 1000));
        register(new Commodity("BONE", Material.BONE, "§cKhúc Xương Thô", 6, 1, 35, 12000));
        register(new Commodity("ENCHANTED_BONE", Material.BONE, "§aEnchanted Bone", 960, 200, 4500, 1200));
        register(new Commodity("GUNPOWDER", Material.GUNPOWDER, "§cThuốc Súng Thô", 15, 3, 70, 8000));
        register(new Commodity("ENCHANTED_GUNPOWDER", Material.GUNPOWDER, "§aEnchanted Gunpowder", 2400, 500, 11000, 800));
        register(new Commodity("ENDER_PEARL", Material.ENDER_PEARL, "§cNgọc Ender Thô", 50, 10, 250, 4000));
        register(new Commodity("ENCHANTED_ENDER_PEARL", Material.ENDER_PEARL, "§aEnchanted Ender Pearl", 8000, 1600, 40000, 400));
        register(new Commodity("STRING", Material.STRING, "§cSợi Tơ Nhện Thô", 5, 1, 30, 15000));
        register(new Commodity("ENCHANTED_STRING", Material.STRING, "§aEnchanted String", 800, 160, 4500, 1500));
        register(new Commodity("SPIDER_EYE", Material.SPIDER_EYE, "§cMắt Nhện Thô", 20, 5, 100, 10000));
        register(new Commodity("ENCHANTED_SPIDER_EYE", Material.SPIDER_EYE, "§aEnchanted Spider Eye", 3200, 800, 16000, 1000));
        register(new Commodity("SLIME_BALL", Material.SLIME_BALL, "§cBóng Nhầy Thô", 30, 8, 150, 8000));
        register(new Commodity("ENCHANTED_SLIME_BALL", Material.SLIME_BALL, "§aEnchanted Slimeball", 4800, 1200, 24000, 800));
    }

    private void register(Commodity com) { market.put(com.id, com); }

    private void loadStockDataFile() {
        dataFile = new File(plugin.getDataFolder(), "bazaar_data.yml");
        if (!dataFile.exists()) { try { dataFile.getParentFile().mkdirs(); dataFile.createNewFile(); } catch (IOException ignored) {} }
        dataConfig = YamlConfiguration.loadConfiguration(dataFile);
        for (String id : market.keySet()) {
            int target = market.get(id).targetStock;
            int savedStock = dataConfig.getInt("stocks." + id, target);
            currentStocks.put(id, savedStock);
        }
    }

    private void saveStockDataFile() {
        for (Map.Entry<String, Integer> entry : currentStocks.entrySet()) { dataConfig.set("stocks." + entry.getKey(), entry.getValue()); }
        try { dataConfig.save(dataFile); } catch (IOException ignored) {}
    }

    private int getSingleItemPrice(String id, int simulatedStock, boolean isBuy) {
        Commodity com = market.get(id); if (com == null) return 0;
        int stock = Math.max(1, simulatedStock);
        double priceMultiplier = (double) com.targetStock / (double) stock;
        int calculatedPrice = (int) (com.basePrice * priceMultiplier);
        int finalPrice = Math.max(com.minPrice, Math.min(com.maxPrice, calculatedPrice));
        return isBuy ? (int) (finalPrice * 1.05) : (int) (finalPrice * 0.95);
    }

    private int getBulkBuyCost(String id, int amount) {
        int total = 0; int tempStock = currentStocks.getOrDefault(id, 1000);
        for (int i = 0; i < amount; i++) { total += getSingleItemPrice(id, tempStock, true); tempStock = Math.max(1, tempStock - 1); }
        return total;
    }

    private int getBulkSellRevenue(String id, int amount) {
        int total = 0; int tempStock = currentStocks.getOrDefault(id, 1000);
        for (int i = 0; i < amount; i++) { total += getSingleItemPrice(id, tempStock, false); tempStock++; }
        return total;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§c[Bazaar] Lệnh này chỉ dành cho người chơi trong game!");
            return true;
        }
        openMainMenu((Player) sender);
        return true;
    }

    public void openMainMenu(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, mainTitle);
        fillGlassBackground(gui);
        gui.setItem(20, createMenuIcon(Material.WHEAT, "§aNông Sản (Farming)", "§7Bao gồm: Lúa mì, Cà rốt, Khoai tây..."));
        gui.setItem(22, createMenuIcon(Material.DIAMOND_PICKAXE, "§9Khai Khoáng (Mining)", "§7Bao gồm: Than, Sắt, Vàng, Kim cương, Ngọc..."));
        gui.setItem(24, createMenuIcon(Material.ROTTEN_FLESH, "§cChiến Đấu / Săn Bắn (Combat)", "§7Bao gồm: Vật phẩm thu thập từ quái vật..."));
        player.openInventory(gui);
    }

    private void openCategoryMenu(Player player, String catName, String[] resourceBases, Material[] icons) {
        Inventory gui = Bukkit.createInventory(null, 54, prefixCat + catName);
        fillGlassBackground(gui);
        int[] layoutSlots = {10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34, 37, 38, 39, 40, 41, 42, 43};
        for (int i = 0; i < resourceBases.length; i++) {
            if (i >= layoutSlots.length) break;
            ItemStack item = new ItemStack(icons[i]);
            ItemMeta meta = item.getItemMeta();
            if (meta != null) {
                meta.setDisplayName("§eBộ Hàng Hoá: " + resourceBases[i]);
                meta.setLore(Arrays.asList("§7Nhấn vào để xem chi tiết toàn bộ", "§7các biến thể Enchanted của tài nguyên này.", "", "§eClick để mở rộng!"));
                meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "bazaar_res_base"), PersistentDataType.STRING, resourceBases[i]);
                item.setItemMeta(meta);
            }
            gui.setItem(layoutSlots[i], item);
        }
        addBackButton(gui, 49);
        player.openInventory(gui);
    }

    private void openProductVariantsMenu(Player player, String resBase, String[] variants) {
        Inventory gui = Bukkit.createInventory(null, 54, prefixProd + resBase);
        fillGlassBackground(gui);
        int[] horizontalSlots = {21, 22, 23};
        int index = 0;
        for (String id : variants) {
            if (index >= horizontalSlots.length) break;
            ItemStack item = new ItemStack(market.get(id).mat);
            ItemMeta meta = item.getItemMeta();
            if (meta != null) {
                int nextBuy = getSingleItemPrice(id, currentStocks.get(id), true);
                int nextSell = getSingleItemPrice(id, currentStocks.get(id), false);
                meta.setDisplayName(market.get(id).name);
                meta.setLore(Arrays.asList("§7Mức giá thị trường động AMM:", " §8• §7Giá mua món kế tiếp: §a$" + nextBuy, " §8• §7Giá bán món kế tiếp: §c$" + nextSell, "", "§7📊 Kho dự trữ toàn cầu: §b" + currentStocks.get(id) + " món", "", "§eClick để mở bảng khớp lệnh Mua/Bán!"));
                if (id.startsWith("ENCHANTED_")) { meta.addEnchant(org.bukkit.enchantments.Enchantment.MENDING, 1, true); meta.addItemFlags(ItemFlag.HIDE_ENCHANTS); }
                meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "bazaar_final_id"), PersistentDataType.STRING, id);
                item.setItemMeta(meta);
            }
            gui.setItem(horizontalSlots[index++], item);
        }
        addBackButton(gui, 49);
        player.openInventory(gui);
    }

    private void openTradeActionMenu(Player player, String itemId) {
        Inventory gui = Bukkit.createInventory(null, 54, prefixTrade + itemId);
        fillGlassBackground(gui);
        int cost1 = getBulkBuyCost(itemId, 1); int cost64 = getBulkBuyCost(itemId, 64); int rev1 = getBulkSellRevenue(itemId, 1);
        gui.setItem(19, createActionButton(Material.GOLD_NUGGET, "§a§lInstant Buy (x1)", Arrays.asList("§7Khớp lệnh mua lập tức 1 sản phẩm.", "", "§7Tổng chi phí: §e$" + cost1, "", "§eClick để thanh toán ví Vault!")));
        gui.setItem(28, createActionButton(Material.GOLD_INGOT, "§a§lInstant Buy (x64 Stack)", Arrays.asList("§7Khớp lệnh mua lập tức 64 sản phẩm.", "§7(Đã tính tỷ lệ trượt giá khan hiếm quặng)", "", "§7Tổng chi phí: §e$" + cost64, "", "§eClick để thanh toán ví Vault!")));
        ItemStack displayItem = new ItemStack(market.get(itemId).mat); ItemMeta dMeta = displayItem.getItemMeta();
        if (dMeta != null) { dMeta.setDisplayName(market.get(itemId).name); dMeta.setLore(Arrays.asList("§7📊 Thống kê kho: §b" + currentStocks.get(itemId) + " món")); if (itemId.startsWith("ENCHANTED_")) { dMeta.addEnchant(org.bukkit.enchantments.Enchantment.MENDING, 1, true); dMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS); } displayItem.setItemMeta(dMeta); }
        gui.setItem(22, displayItem);
        gui.setItem(25, createActionButton(Material.IRON_NUGGET, "§c§lInstant Sell (x1)", Arrays.asList("§7Khớp lệnh bán lập tức 1 sản phẩm.", "", "§7Tổng thu về: §e$" + rev1, "", "§eClick để xả hàng lấy tiền mặt!")));
        gui.setItem(34, createActionButton(Material.IRON_INGOT, "§c§lInstant Sell All (Xả Sạch)", Arrays.asList("§7Thanh lý sạch sành sanh kho đồ hành trang.", "§7(Giá sẽ tự sập thấp dần nếu xả lượng lớn cùng lúc)", "", "§eClick để đại thanh lý kho đồ!")));
        addBackButton(gui, 49);
        player.openInventory(gui);
    }

    @EventHandler
    public void onBazaarClick(InventoryClickEvent event) {
        String title = event.getView().getTitle(); if (!title.startsWith("§8Bazaar")) return;
        event.setCancelled(true); Player player = (Player) event.getWhoClicked(); ItemStack clicked = event.getCurrentItem();
        if (clicked == null || clicked.getType() == Material.AIR) return;
        if (event.getSlot() == 49 && clicked.getType() == Material.ARROW) { if (title.equals(mainTitle)) return; openMainMenu(player); return; }

        if (title.equals(mainTitle)) {
            int slot = event.getSlot();
            if (slot == 20) openCategoryMenu(player, "Farming", new String[]{"Lúa Mì", "Cà Rốt", "Khoai Tây"}, new Material[]{Material.WHEAT, Material.CARROT, Material.POTATO});
            if (slot == 22) openCategoryMenu(player, "Mining", new String[]{"Than Đá", "Sắt Ma Thuật", "Vàng Ròng", "Kim Cương", "Ngọc Lục Bảo", "Lapis Lazuli", "Đá Đỏ"}, new Material[]{Material.COAL, Material.IRON_INGOT, Material.GOLD_INGOT, Material.DIAMOND, Material.EMERALD, Material.LAPIS_LAZULI, Material.REDSTONE});
            if (slot == 24) openCategoryMenu(player, "Combat", new String[]{"Thịt Thối", "Thịt Bò", "Thịt Heo", "Thịt Gà", "Thịt Cừu", "Thịt Thỏ", "Khúc Xương", "Thuốc Súng", "Ngọc Ender", "Tơ Nhện", "Mắt Nhện", "Bóng Nhầy"}, new Material[]{Material.ROTTEN_FLESH, Material.BEEF, Material.PORKCHOP, Material.CHICKEN, Material.MUTTON, Material.RABBIT, Material.BONE, Material.GUNPOWDER, Material.ENDER_PEARL, Material.STRING, Material.SPIDER_EYE, Material.SLIME_BALL});
        } 
        else if (title.startsWith(prefixCat)) {
            ItemMeta meta = clicked.getItemMeta(); if (meta == null) return;
            String base = meta.getPersistentDataContainer().get(new NamespacedKey(plugin, "bazaar_res_base"), PersistentDataType.STRING); if (base == null) return;
            if (base.equals("Lúa Mì")) openProductVariantsMenu(player, "Lúa Mì", new String[]{"WHEAT", "ENCHANTED_WHEAT"});
            if (base.equals("Cà Rốt")) openProductVariantsMenu(player, "Cà Rốt", new String[]{"CARROT", "ENCHANTED_CARROT"});
            if (base.equals("Khoai Tây")) openProductVariantsMenu(player, "Khoai Tây", new String[]{"POTATO", "ENCHANTED_POTATO"});
            
            if (base.equals("Than Đá")) openProductVariantsMenu(player, "Than Đá", new String[]{"COAL", "ENCHANTED_COAL", "ENCHANTED_COAL_BLOCK"});
            if (base.equals("Sắt Ma Thuật")) openProductVariantsMenu(player, "Sắt Ma Thuật", new String[]{"IRON_INGOT", "ENCHANTED_IRON", "ENCHANTED_IRON_BLOCK"});
            if (base.equals("Vàng Ròng")) openProductVariantsMenu(player, "Vàng Ròng", new String[]{"GOLD_INGOT", "ENCHANTED_GOLD", "ENCHANTED_GOLD_BLOCK"});
            if (base.equals("Kim Cương")) openProductVariantsMenu(player, "Kim Cương", new String[]{"DIAMOND", "ENCHANTED_DIAMOND", "ENCHANTED_DIAMOND_BLOCK"});
            if (base.equals("Ngọc Lục Bảo")) openProductVariantsMenu(player, "Ngọc Lục Bảo", new String[]{"EMERALD", "ENCHANTED_EMERALD", "ENCHANTED_EMERALD_BLOCK"});
            if (base.equals("Lapis Lazuli")) openProductVariantsMenu(player, "Lapis Lazuli", new String[]{"LAPIS_LAZULI", "ENCHANTED_LAPIS", "ENCHANTED_LAPIS_BLOCK"});
            if (base.equals("Đá Đỏ")) openProductVariantsMenu(player, "Đá Đỏ", new String[]{"REDSTONE", "ENCHANTED_REDSTONE", "ENCHANTED_REDSTONE_BLOCK"});
            
            if (base.equals("Thịt Thối")) openProductVariantsMenu(player, "Thịt Thối", new String[]{"ROTTEN_FLESH", "ENCHANTED_ROTTEN_FLESH"});
            if (base.equals("Thịt Bò")) openProductVariantsMenu(player, "Thịt Bò", new String[]{"BEEF", "ENCHANTED_BEEF"});
            if (base.equals("Thịt Heo")) openProductVariantsMenu(player, "Thịt Heo", new String[]{"PORKCHOP", "ENCHANTED_PORKCHOP"});
            if (base.equals("Thịt Gà")) openProductVariantsMenu(player, "Thịt Gà", new String[]{"CHICKEN", "ENCHANTED_CHICKEN"});
            if (base.equals("Thịt Cừu")) openProductVariantsMenu(player, "Thịt Cừu", new String[]{"MUTTON", "ENCHANTED_MUTTON"});
            if (base.equals("Thịt Thỏ")) openProductVariantsMenu(player, "Thịt Thỏ", new String[]{"RABBIT", "ENCHANTED_RABBIT"});
            if (base.equals("Khúc Xương")) openProductVariantsMenu(player, "Khúc Xương", new String[]{"BONE", "ENCHANTED_BONE"});
            if (base.equals("Thuốc Súng")) openProductVariantsMenu(player, "Thuốc Súng", new String[]{"GUNPOWDER", "ENCHANTED_GUNPOWDER"});
            if (base.equals("Ngọc Ender")) openProductVariantsMenu(player, "Ngọc Ender", new String[]{"ENDER_PEARL", "ENCHANTED_ENDER_PEARL"});
            if (base.equals("Tơ Nhện")) openProductVariantsMenu(player, "Tơ Nhện", new String[]{"STRING", "ENCHANTED_STRING"});
            if (base.equals("Mắt Nhện")) openProductVariantsMenu(player, "Mắt Nhện", new String[]{"SPIDER_EYE", "ENCHANTED_SPIDER_EYE"});
            if (base.equals("Bóng Nhầy")) openProductVariantsMenu(player, "Bóng Nhầy", new String[]{"SLIME_BALL", "ENCHANTED_SLIME_BALL"});
        } 
        else if (title.startsWith(prefixProd)) {
            ItemMeta meta = clicked.getItemMeta(); if (meta == null) return;
            String id = meta.getPersistentDataContainer().get(new NamespacedKey(plugin, "bazaar_final_id"), PersistentDataType.STRING); if (id != null) openTradeActionMenu(player, id);
        } 
        else if (title.startsWith(prefixTrade)) {
            String itemId = title.replace(prefixTrade, ""); int slot = event.getSlot();
            if (slot == 19) handleBuyAction(player, itemId, 1);
            if (slot == 28) handleBuyAction(player, itemId, 64);
            if (slot == 25) handleSellAction(player, itemId, 1);
            if (slot == 34) handleSellAllAction(player, itemId);
        }
    }

    private void handleBuyAction(Player player, String id, int amount) {
        int totalCost = getBulkBuyCost(id, amount); if (econ.getBalance(player) < totalCost) { player.sendMessage("§cVí tiền Vault rỗng tuếch, đéo đủ tiền khớp lệnh!"); return; }
        econ.withdrawPlayer(player, totalCost); int stock = currentStocks.get(id); currentStocks.put(id, Math.max(1, stock - amount)); saveStockDataFile();
        ItemStack item = new ItemStack(market.get(id).mat, amount);
        if (id.startsWith("ENCHANTED_")) {
            ItemMeta meta = item.getItemMeta();
            if (meta != null) {
                meta.setDisplayName(market.get(id).name); meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "bazaar_id"), PersistentDataType.STRING, id);
                meta.addEnchant(org.bukkit.enchantments.Enchantment.MENDING, 1, true); meta.addItemFlags(ItemFlag.HIDE_ENCHANTS); item.setItemMeta(meta);
            }
        }
        for (ItemStack drop : player.getInventory().addItem(item).values()) { player.getWorld().dropItemNaturally(player.getLocation(), drop); }
        player.sendMessage("§a🟩 Khớp lệnh mua x" + amount + " " + market.get(id).name + " trừ §e$" + totalCost);
        player.playSound(player.getLocation(), org.bukkit.Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
        openTradeActionMenu(player, id);
    }

    private void handleSellAction(Player player, String id, int amount) {
        ItemStack match = findMatchingItem(player, id); if (match == null || match.getAmount() < amount) { player.sendMessage("§cTrong túi đồ hành trang đéo có vật phẩm này để xả!"); return; }
        int revenue = getBulkSellRevenue(id, amount); match.setAmount(match.getAmount() - amount);
        currentStocks.put(id, currentStocks.get(id) + amount); saveStockDataFile(); econ.depositPlayer(player, revenue);
        player.sendMessage("§a🟥 Khớp lệnh bán x" + amount + " " + market.get(id).name + " húp về §e$" + revenue);
        player.playSound(player.getLocation(), org.bukkit.Sound.BLOCK_NOTE_BLOCK_CHIME, 1.0f, 1.5f);
        openTradeActionMenu(player, id);
    }

    private void handleSellAllAction(Player player, String id) {
        int totalSold = 0; NamespacedKey key = new NamespacedKey(plugin, "bazaar_id");
        for (ItemStack item : player.getInventory().getContents()) {
            if (item == null || item.getType() == Material.AIR) continue; boolean isMatch = false;
            if (id.startsWith("ENCHANTED_")) {
                if (item.hasItemMeta() && item.getItemMeta().getPersistentDataContainer().has(key, PersistentDataType.STRING)) {
                    if (item.getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.STRING).equals(id)) isMatch = true;
                }
            } else {
                if (item.getType() == market.get(id).mat) {
                    if (!item.hasItemMeta() || !item.getItemMeta().getPersistentDataContainer().has(key, PersistentDataType.STRING)) isMatch = true;
                }
            }
            if (isMatch) { totalSold += item.getAmount(); item.setAmount(0); }
        }
        if (totalSold == 0) { player.sendMessage("§cKhông quét thấy món nào hợp lệ trong hành trang cá nhân để xả sỉ cả!"); return; }
        int totalRevenue = getBulkSellRevenue(id, totalSold); currentStocks.put(id, currentStocks.get(id) + totalSold); saveStockDataFile(); econ.depositPlayer(player, totalRevenue);
        player.sendMessage("§a🟥 ĐẠI XẢ KHO! Đã thanh lý x" + totalSold + " " + market.get(id).name + " nhận về §e$" + totalRevenue);
        player.playSound(player.getLocation(), org.bukkit.Sound.ENTITY_VILLAGER_CELEBRATE, 1.0f, 1.0f);
        player.updateInventory();
        openTradeActionMenu(player, id);
    }

    private ItemStack findMatchingItem(Player player, String id) {
        NamespacedKey key = new NamespacedKey(plugin, "bazaar_id");
        for (ItemStack item : player.getInventory().getContents()) {
            if (item == null || item.getType() == Material.AIR) continue;
            if (id.startsWith("ENCHANTED_")) {
                if (item.hasItemMeta() && item.getItemMeta().getPersistentDataContainer().has(key, PersistentDataType.STRING)) {
                    if (item.getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.STRING).equals(id)) return item;
                }
            } else {
                if (item.getType() == market.get(id).mat) {
                    if (!item.hasItemMeta() || !item.getItemMeta().getPersistentDataContainer().has(key, PersistentDataType.STRING)) return item;
                }
            }
        }
        return null;
    }

    private void fillGlassBackground(Inventory gui) { ItemStack gray = new ItemStack(Material.GRAY_STAINED_GLASS_PANE); ItemMeta m = gray.getItemMeta(); if(m!=null){m.setDisplayName("§7 "); gray.setItemMeta(m);} for (int i = 0; i < gui.getSize(); i++) gui.setItem(i, gray); }
    private void addBackButton(Inventory gui, int slot) { ItemStack back = new ItemStack(Material.ARROW); ItemMeta bM = back.getItemMeta(); if(bM!=null){bM.setDisplayName("§c◀ Quay Lại Menu"); back.setItemMeta(bM);} gui.setItem(slot, back); }
    private ItemStack createMenuIcon(Material mat, String name, String loreDesc) { ItemStack item = new ItemStack(mat); ItemMeta meta = item.getItemMeta(); if(meta != null) { meta.setDisplayName("§e" + name); meta.setLore(Arrays.asList(loreDesc, "", "§eClick chuột để xem bách hóa mặt hàng!")); item.setItemMeta(meta); } return item; }
    private ItemStack createActionButton(Material mat, String name, List<String> lore) { ItemStack item = new ItemStack(mat); ItemMeta meta = item.getItemMeta(); if(meta != null) { meta.setDisplayName(name); meta.setLore(lore); item.setItemMeta(meta); } return item; }
}
