package org.example.system;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.example.core.CustomWeaponEngine;

import java.util.ArrayList;
import java.util.List;

public class LibraryGUI implements Listener {

    private final CustomWeaponEngine plugin;
    private final NamespacedKey idKey;

    public LibraryGUI(CustomWeaponEngine plugin) {
        this.plugin = plugin;
        this.idKey = new NamespacedKey(plugin, "cwe_id");
    }

    public void openMainMenu(Player player) {
        Inventory inv = Bukkit.createInventory(null, 27, "§8Thư Viện CWE - Danh Mục");
        
        ItemStack weapon = createGuiItem(Material.DIAMOND_SWORD, "§c§lVũ Khí (Weapons)", "§7Nhấn để xem kho vũ khí.");
        ItemStack armor = createGuiItem(Material.DIAMOND_CHESTPLATE, "§b§lGiáp (Armors)", "§7Nhấn để xem kho giáp.", "§7(Chỉ hiển thị Mũ, lấy được cả set)");
        ItemStack skillbook = createGuiItem(Material.ENCHANTED_BOOK, "§a§lSách Kỹ Năng", "§7Nhấn để xem các loại Sách Kỹ Năng.");
        
        inv.setItem(10, weapon);
        inv.setItem(13, armor);
        inv.setItem(16, skillbook);
        
        player.openInventory(inv);
        player.playSound(player.getLocation(), Sound.BLOCK_CHEST_OPEN, 1, 1);
    }

    public void openCategoryGUI(Player player, String category, int page) {
        Inventory inv = Bukkit.createInventory(null, 54, "§8Thư Viện: " + category + " - Trang " + page);
        
        FileConfiguration config = plugin.getLibraryConfig();
        if (config.getConfigurationSection("items") == null) {
            player.openInventory(inv);
            return;
        }

        List<ItemStack> itemsToShow = new ArrayList<>();
        
        for (String key : config.getConfigurationSection("items").getKeys(false)) {
            ItemStack item = config.getItemStack("items." + key);
            if (item == null) continue;
            
            boolean isArmor = key.contains("helmet") || key.contains("chestplate") || key.contains("leggings") || key.contains("boots") || key.contains("armor");
            boolean isSkillBook = key.startsWith("skillbook_");
            
            if (category.equals("Armor")) {
                if (isArmor && (key.contains("helmet") || key.equals("cwe_hardened_diamond_helmet") || key.equals("cwe_golem_armor_helmet"))) {
                    itemsToShow.add(item.clone());
                } else if (isArmor && !key.contains("chestplate") && !key.contains("leggings") && !key.contains("boots")) {
                    // Fallback for non-standard armor names
                    if (!key.contains("_")) itemsToShow.add(item.clone()); 
                }
            } else if (category.equals("SkillBook")) {
                if (isSkillBook) {
                    itemsToShow.add(item.clone());
                }
            } else { // Weapon
                if (!isArmor && !isSkillBook) {
                    itemsToShow.add(item.clone());
                }
            }
        }

        int maxItemsPerPage = 45;
        int startIndex = (page - 1) * maxItemsPerPage;
        int endIndex = Math.min(startIndex + maxItemsPerPage, itemsToShow.size());

        for (int i = startIndex; i < endIndex; i++) {
            inv.setItem(i - startIndex, itemsToShow.get(i));
        }

        if (page > 1) {
            inv.setItem(45, createGuiItem(Material.ARROW, "§aTrang Trước"));
        }
        if (endIndex < itemsToShow.size()) {
            inv.setItem(53, createGuiItem(Material.ARROW, "§aTrang Sau"));
        }
        
        inv.setItem(49, createGuiItem(Material.BARRIER, "§cQuay Lại"));

        player.openInventory(inv);
    }

    private ItemStack createGuiItem(Material material, String name, String... loreLines) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            if (loreLines.length > 0) {
                List<String> lore = new ArrayList<>();
                for (String l : loreLines) lore.add(l);
                meta.setLore(lore);
            }
            item.setItemMeta(meta);
        }
        return item;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        String title = event.getView().getTitle();
        if (!title.startsWith("§8Thư Viện")) return;
        event.setCancelled(true);

        if (!(event.getWhoClicked() instanceof Player)) return;
        Player player = (Player) event.getWhoClicked();

        if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) return;
        ItemStack clickedItem = event.getCurrentItem();
        
        if (title.equals("§8Thư Viện CWE - Danh Mục")) {
            if (clickedItem.getType() == Material.DIAMOND_SWORD) {
                openCategoryGUI(player, "Weapon", 1);
            } else if (clickedItem.getType() == Material.DIAMOND_CHESTPLATE) {
                openCategoryGUI(player, "Armor", 1);
            } else if (clickedItem.getType() == Material.ENCHANTED_BOOK) {
                openCategoryGUI(player, "SkillBook", 1);
            }
            return;
        }

        String[] parts = title.split(" ");
        String category = parts[2];
        int currentPage = Integer.parseInt(parts[parts.length - 1]);

        if (clickedItem.getType() == Material.ARROW) {
            if (clickedItem.getItemMeta().getDisplayName().equals("§aTrang Trước")) {
                openCategoryGUI(player, category, currentPage - 1);
            } else {
                openCategoryGUI(player, category, currentPage + 1);
            }
            return;
        } else if (clickedItem.getType() == Material.BARRIER) {
            openMainMenu(player);
            return;
        }

        if (clickedItem.getItemMeta() == null) return;
        String id = clickedItem.getItemMeta().getPersistentDataContainer().get(idKey, PersistentDataType.STRING);
        if (id == null) return;

        if (event.isShiftClick() && event.isRightClick()) {
            FileConfiguration config = plugin.getLibraryConfig();
            config.set("items." + id, null);
            plugin.saveLibraryConfig();
            player.sendMessage("§a[CWE] Đã xóa vật phẩm " + id + " khỏi thư viện.");
            openCategoryGUI(player, category, currentPage);
            return;
        }

        if (category.equals("Armor") && id.endsWith("_helmet")) {
            String baseId = id.replace("_helmet", "");
            FileConfiguration config = plugin.getLibraryConfig();
            
            ItemStack helmet = config.getItemStack("items." + baseId + "_helmet");
            ItemStack chestplate = config.getItemStack("items." + baseId + "_chestplate");
            ItemStack leggings = config.getItemStack("items." + baseId + "_leggings");
            ItemStack boots = config.getItemStack("items." + baseId + "_boots");
            
            if (helmet != null) player.getInventory().addItem(helmet.clone());
            if (chestplate != null) player.getInventory().addItem(chestplate.clone());
            if (leggings != null) player.getInventory().addItem(leggings.clone());
            if (boots != null) player.getInventory().addItem(boots.clone());
            
            player.sendMessage("§a[CWE] Đã nhận Full Set Giáp " + baseId + "!");
        } else {
            // BUG FIX: Đọc lại item từ config bằng ID thay vì clone clickedItem trong GUI
            // clickedItem trong GUI chỉ là item để hiển thị, đọc từ config mới giữ nguyên toàn bộ data (lore, stat, PDC)
            FileConfiguration config = plugin.getLibraryConfig();
            ItemStack fromConfig = config.getItemStack("items." + id);
            if (fromConfig != null) {
                player.getInventory().addItem(fromConfig.clone());
                player.sendMessage("§a[CWE] Đã lấy " + id + " thành công!");
            } else {
                player.sendMessage("§c[CWE] Không tìm thấy vật phẩm " + id + " trong thư viện!");
            }
        }
    }
}
