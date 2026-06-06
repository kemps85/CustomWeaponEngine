package org.example.stats;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
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
import org.example.core.CustomWeaponEngine;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class AppraiserGUI implements Listener, CommandExecutor {

    private final CustomWeaponEngine plugin;
    private final String guiTitle = "§8Máy Giám Định (Appraiser)";
    private final int ITEM_SLOT = 22;
    private final int BUTTON_SLOT = 31;
    
    private final Random random = new Random();

    public AppraiserGUI(CustomWeaponEngine plugin) {
        this.plugin = plugin;
    }

    public void openGUI(Player player) {
        Inventory inv = Bukkit.createInventory(null, 54, guiTitle);

        ItemStack bg = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta bgMeta = bg.getItemMeta();
        bgMeta.setDisplayName(" ");
        bg.setItemMeta(bgMeta);

        for (int i = 0; i < 54; i++) {
            if (i != ITEM_SLOT) {
                inv.setItem(i, bg);
            }
        }

        updateButton(inv, null);
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            player.closeInventory();
            player.openInventory(inv);
        }, 3L);
    }

    private void updateButton(Inventory inv, ItemStack itemToAppraise) {
        ItemStack btn;
        if (itemToAppraise == null || itemToAppraise.getType().isAir()) {
            btn = new ItemStack(Material.RED_STAINED_GLASS_PANE);
            ItemMeta meta = btn.getItemMeta();
            meta.setDisplayName("§cChưa có vật phẩm");
            List<String> lore = new ArrayList<>();
            lore.add("§7Hãy đặt một vật phẩm Vanilla");
            lore.add("§7(Trident, Mace, Elytra, v.v...)");
            lore.add("§7vào ô trống phía trên.");
            meta.setLore(lore);
            btn.setItemMeta(meta);
        } else {
            if (isValidForAppraisal(itemToAppraise)) {
                btn = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
                ItemMeta meta = btn.getItemMeta();
                meta.setDisplayName("§aGiám Định (Appraise)");
                List<String> lore = new ArrayList<>();
                int cost = plugin.getConfig().getInt("appraiser.cost", 1000);
                lore.add("§7Chi phí: §6" + cost + " Vault");
                lore.add("");
                lore.add("§eNhấn để giám định nhân phẩm!");
                meta.setLore(lore);
                btn.setItemMeta(meta);
            } else {
            btn = new ItemStack(Material.BARRIER);
            ItemMeta meta = btn.getItemMeta();
            meta.setDisplayName("§cKhông thể giám định!");
            List<String> lore = new ArrayList<>();
            
            ItemMeta targetMeta = itemToAppraise.getItemMeta();
            if (targetMeta != null && targetMeta.getPersistentDataContainer().has(new NamespacedKey(plugin, "cwe_appraised"), PersistentDataType.INTEGER)) {
                lore.add("§7Vật phẩm này đã được giám định rồi!");
            } else {
                lore.add("§7Vật phẩm này không phải là trang bị");
                lore.add("§7hợp lệ (Kiếm, Cung, Giáp, v.v...).");
            }
            
            meta.setLore(lore);
            btn.setItemMeta(meta);
        }
        }
        inv.setItem(BUTTON_SLOT, btn);
    }

    private boolean isValidForAppraisal(ItemStack item) {
        if (item == null || item.getType().isAir()) return false;
        
        String name = item.getType().name();
        boolean isEquipment = name.contains("SWORD") || name.contains("AXE") || name.contains("BOW") || name.contains("CROSSBOW")
                || name.contains("HELMET") || name.contains("CHESTPLATE") || name.contains("LEGGINGS") || name.contains("BOOTS")
                || item.getType() == Material.TRIDENT || item.getType() == Material.ELYTRA || item.getType() == Material.MACE || item.getType() == Material.SHIELD;

        if (!isEquipment) return false;

        ItemMeta meta = item.getItemMeta();
        if (meta == null) return false;

        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        
        // Cannot appraise custom weapons
        if (pdc.has(new NamespacedKey(plugin, "cwe_id"), PersistentDataType.STRING)) {
            return false;
        }

        // Cannot appraise twice
        if (pdc.has(new NamespacedKey(plugin, "cwe_appraised"), PersistentDataType.INTEGER)) {
            return false;
        }

        return true;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!event.getView().getTitle().equals(guiTitle)) return;

        Player player = (Player) event.getWhoClicked();
        int slot = event.getRawSlot();

        // Chặn Shift-Click để tránh lọt đồ vào các slot UI kính đen
        if (event.getAction().name().contains("MOVE_TO_OTHER_INVENTORY")) {
            event.setCancelled(true);
            return;
        }

        // Cho phép click đồ trong túi đồ của người chơi (slot 54+)
        if (slot >= 54) {
            return;
        }

        if (slot == ITEM_SLOT) {
            // Let them put/take item
            Bukkit.getScheduler().runTask(plugin, () -> {
                updateButton(event.getInventory(), event.getInventory().getItem(ITEM_SLOT));
            });
            return;
        }

        // Chặn tất cả các tương tác khác trên top GUI
        event.setCancelled(true);

        if (slot == BUTTON_SLOT) {
            ItemStack item = event.getInventory().getItem(ITEM_SLOT);
            if (isValidForAppraisal(item)) {
                int cost = plugin.getConfig().getInt("appraiser.cost", 1000);
                
                // Thu tien
                if (CustomWeaponEngine.getEconomy() != null) {
                    if (CustomWeaponEngine.getEconomy().getBalance(player) < cost) {
                        player.sendMessage("§cBạn không có đủ §6" + cost + "$ §cđể giám định!");
                        player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
                        return;
                    }
                    CustomWeaponEngine.getEconomy().withdrawPlayer(player, cost);
                    player.sendMessage("§aĐã thanh toán §6" + cost + "$ §acho phí giám định.");
                }

                try {
                    // Gacha Rarity
                    ItemMeta meta = item.getItemMeta();
                    if (meta == null) {
                        player.sendMessage("§cCó lỗi xảy ra: Vật phẩm không hợp lệ!");
                        return;
                    }
                    PersistentDataContainer pdc = meta.getPersistentDataContainer();
                    
                    String itemType = item.getType().name();
                    String rarity = rollRarity(itemType);

                    // Tăng chỉ số
                    String oldRarityStr = pdc.getOrDefault(new NamespacedKey(plugin, ItemStatsGUI.KEY_RARITY), PersistentDataType.STRING, "COMMON");
                    double oldMultiplier = 1.0;
                    if (oldRarityStr.equals("UNCOMMON")) oldMultiplier = 1.05;
                    if (oldRarityStr.equals("RARE")) oldMultiplier = 1.1;
                    if (oldRarityStr.equals("EPIC")) oldMultiplier = 1.25;
                    if (oldRarityStr.equals("LEGENDARY")) oldMultiplier = 1.5;
                    if (oldRarityStr.equals("MYTHIC")) oldMultiplier = 1.75;

                    double newMultiplier = 1.0;
                    if (rarity.equals("UNCOMMON")) newMultiplier = 1.05;
                    if (rarity.equals("RARE")) newMultiplier = 1.1;
                    if (rarity.equals("EPIC")) newMultiplier = 1.25;
                    if (rarity.equals("LEGENDARY")) newMultiplier = 1.5;
                    if (rarity.equals("MYTHIC")) newMultiplier = 1.75;

                    double scale = newMultiplier / oldMultiplier;

                    // Scale base stats if they exist
                    NamespacedKey dmgKey = new NamespacedKey(plugin, "stat_damage");
                    NamespacedKey strKey = new NamespacedKey(plugin, ItemStatsGUI.KEY_STRENGTH);
                    NamespacedKey defKey = new NamespacedKey(plugin, ItemStatsGUI.KEY_DEFENSE);
                    NamespacedKey hpKey = new NamespacedKey(plugin, ItemStatsGUI.KEY_HEALTH);

                    // Add base stats for raw vanilla items if they don't exist yet
                    if (!pdc.has(dmgKey, PersistentDataType.DOUBLE) && !pdc.has(defKey, PersistentDataType.DOUBLE)) {
                        double base = 20.0;
                        if (itemType.contains("DIAMOND_")) base = 50.0;
                        if (itemType.contains("NETHERITE_")) base = 80.0;
                        if (itemType.contains("IRON_")) base = 30.0;
                        
                        if (itemType.contains("SWORD") || itemType.contains("AXE") || itemType.contains("BOW") || itemType.contains("CROSSBOW")) {
                            pdc.set(dmgKey, PersistentDataType.DOUBLE, base);
                            pdc.set(strKey, PersistentDataType.DOUBLE, base / 2.0);
                        } else if (itemType.contains("HELMET") || itemType.contains("CHESTPLATE") || itemType.contains("LEGGINGS") || itemType.contains("BOOTS")) {
                            pdc.set(defKey, PersistentDataType.DOUBLE, base / 1.5);
                            pdc.set(hpKey, PersistentDataType.DOUBLE, base);
                        }
                    }

                    if (pdc.has(dmgKey, PersistentDataType.DOUBLE)) {
                        double val = pdc.getOrDefault(dmgKey, PersistentDataType.DOUBLE, 0.0);
                        pdc.set(dmgKey, PersistentDataType.DOUBLE, val * scale);
                    }
                    if (pdc.has(strKey, PersistentDataType.DOUBLE)) {
                        double val = pdc.getOrDefault(strKey, PersistentDataType.DOUBLE, 0.0);
                        pdc.set(strKey, PersistentDataType.DOUBLE, val * scale);
                    }
                    if (pdc.has(defKey, PersistentDataType.DOUBLE)) {
                        double val = pdc.getOrDefault(defKey, PersistentDataType.DOUBLE, 0.0);
                        pdc.set(defKey, PersistentDataType.DOUBLE, val * scale);
                    }
                    if (pdc.has(hpKey, PersistentDataType.DOUBLE)) {
                        double val = pdc.getOrDefault(hpKey, PersistentDataType.DOUBLE, 0.0);
                        pdc.set(hpKey, PersistentDataType.DOUBLE, val * scale);
                    }

                    pdc.set(new NamespacedKey(plugin, ItemStatsGUI.KEY_RARITY), PersistentDataType.STRING, rarity);
                    pdc.set(new NamespacedKey(plugin, "cwe_appraised"), PersistentDataType.INTEGER, 1);
                    
                    // Force updater to rebuild lore
                    pdc.remove(new NamespacedKey(plugin, "cwe_item_updated"));
                    
                    meta.setUnbreakable(true);
                    item.setItemMeta(meta);

                    player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f);
                    
                    String itemName = item.getType().name();
                    if (item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
                        itemName = item.getItemMeta().getDisplayName();
                    }

                    if (rarity.equals("LEGENDARY") || rarity.equals("MYTHIC")) {
                        String color = rarity.equals("MYTHIC") ? "§d§l" : "§6§l";
                        Bukkit.broadcastMessage("§6§lCWE §8» §eTuyệt đỉnh! §b" + player.getName() + " §7vừa nâng cấp " + itemName + " §7lên phẩm chất " + color + rarity + "§7!");
                    } else if (rarity.equals("RARE") && (itemType.equals("TRIDENT") || itemType.equals("ELYTRA") || itemType.equals("MACE") || itemType.contains("NETHERITE_"))) {
                        Bukkit.broadcastMessage("§6§lCWE §8» §cHaha! §b" + player.getName() + " §7vừa tạch giám định " + itemName + " §7rớt thảm hại xuống §9§lRARE§7! Gà vl =)))");
                    } else {
                        player.sendMessage("§aGiám định hoàn tất! Bậc: " + rarity);
                    }

                    event.getInventory().setItem(ITEM_SLOT, null);
                    safeGiveItem(player, item);
                    updateButton(event.getInventory(), null);
                } catch (Exception e) {
                    player.sendMessage("§cLỗi hệ thống khi giám định! Báo cáo Admin ngay! Lỗi: " + e.getMessage());
                    e.printStackTrace();
                    
                    // Trả lại tiền
                    if (CustomWeaponEngine.getEconomy() != null) {
                        CustomWeaponEngine.getEconomy().depositPlayer(player, plugin.getConfig().getInt("appraiser.cost", 1000));
                        player.sendMessage("§aĐã hoàn tiền giám định do lỗi hệ thống.");
                    }
                }
            }
        }
    }

    private void safeGiveItem(Player player, ItemStack item) {
        Map<Integer, ItemStack> leftover = player.getInventory().addItem(item);
        for (ItemStack left : leftover.values()) {
            player.getWorld().dropItemNaturally(player.getLocation(), left);
        }
    }

    private String rollRarity(String type) {
        Map<String, Integer> pools = new HashMap<>();
        
        if (plugin.getConfig().contains("appraiser.pools." + type) && plugin.getConfig().getConfigurationSection("appraiser.pools." + type) != null) {
            for (String r : plugin.getConfig().getConfigurationSection("appraiser.pools." + type).getKeys(false)) {
                pools.put(r, plugin.getConfig().getInt("appraiser.pools." + type + "." + r));
            }
        } else if (plugin.getConfig().contains("appraiser.pools.default") && plugin.getConfig().getConfigurationSection("appraiser.pools.default") != null) {
            for (String r : plugin.getConfig().getConfigurationSection("appraiser.pools.default").getKeys(false)) {
                pools.put(r, plugin.getConfig().getInt("appraiser.pools.default." + r));
            }
        } 
        
        // Fallback in case pools were left completely empty or invalid in config
        if (pools.isEmpty()) {
            if (type.equals("TRIDENT") || type.equals("ELYTRA") || type.equals("MACE") || type.contains("NETHERITE_")) {
                pools.put("RARE", 60);
                pools.put("EPIC", 30);
                pools.put("LEGENDARY", 10);
            } else {
                pools.put("COMMON", 50);
                pools.put("UNCOMMON", 25);
                pools.put("RARE", 15);
                pools.put("EPIC", 7);
                pools.put("LEGENDARY", 2);
                pools.put("MYTHIC", 1);
            }
        }

        int totalWeight = 0;
        for (int w : pools.values()) totalWeight += w;

        // Failsafe: if weight is somehow 0 or negative
        if (totalWeight <= 0) {
            pools.clear();
            if (type.equals("TRIDENT") || type.equals("ELYTRA") || type.equals("MACE") || type.contains("NETHERITE_")) {
                pools.put("RARE", 60);
                pools.put("EPIC", 30);
                pools.put("LEGENDARY", 10);
            } else {
                pools.put("COMMON", 50);
                pools.put("UNCOMMON", 25);
                pools.put("RARE", 15);
                pools.put("EPIC", 7);
                pools.put("LEGENDARY", 2);
                pools.put("MYTHIC", 1);
            }
            
            totalWeight = 0;
            for (int w : pools.values()) totalWeight += w;
        }

        int randomVal = random.nextInt(totalWeight);
        int currentWeight = 0;

        for (Map.Entry<String, Integer> entry : pools.entrySet()) {
            currentWeight += entry.getValue();
            if (randomVal < currentWeight) {
                return entry.getKey();
            }
        }
        return "COMMON";
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        if (event.getView().getTitle().equals(guiTitle)) {
            ItemStack item = event.getInventory().getItem(ITEM_SLOT);
            if (item != null && !item.getType().isAir()) {
                safeGiveItem((Player) event.getPlayer(), item);
            }
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cChỉ người chơi mới có thể sử dụng lệnh này!");
            return true;
        }
        openGUI((Player) sender);
        return true;
    }
}
