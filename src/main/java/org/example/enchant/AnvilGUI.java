package org.example.enchant;

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
import org.bukkit.block.Block;
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
import org.bukkit.event.block.Action;
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
import org.bukkit.event.player.PlayerInteractEvent;
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
import java.util.Arrays;
import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
import java.util.HashMap;
import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
import java.util.Map;
import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
import java.util.UUID;

import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
public class AnvilGUI implements Listener {
    private final JavaPlugin plugin;
    private final EnchantManager enchantManager;
    private final String guiName = "Anvil";

    private final Map<UUID, Boolean> resultAvailable = new HashMap<>();

    public AnvilGUI(JavaPlugin plugin, EnchantManager enchantManager) {
        this.plugin = plugin;
        this.enchantManager = enchantManager;
    }

    @EventHandler
    public void onOpenAnvil(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        Block block = event.getClickedBlock();
        if (block == null) return;
        
        String matName = block.getType().name();
        if (!matName.contains("ANVIL")) return;

        event.setCancelled(true);
        openCustomAnvilMenu(event.getPlayer());
    }

    public void openCustomAnvilMenu(Player player) {
        Inventory gui = Bukkit.createInventory(null, 54, guiName);

        ItemStack gray = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta gMeta = gray.getItemMeta();
        if (gMeta != null) { gMeta.setDisplayName("§7 "); gray.setItemMeta(gMeta); }
        for (int i = 0; i < 54; i++) { gui.setItem(i, gray); }

        ItemStack lime = new ItemStack(Material.LIME_STAINED_GLASS_PANE);
        ItemMeta lMeta = lime.getItemMeta();
        if (lMeta != null) { lMeta.setDisplayName("§aHướng dẫn kết hợp bùa chú"); lime.setItemMeta(lMeta); }
        int[] limeSlots = {11, 12, 14, 15, 20, 24};
        for (int slot : limeSlots) { gui.setItem(slot, lime); }

        gui.setItem(29, new ItemStack(Material.AIR)); 
        gui.setItem(33, new ItemStack(Material.AIR)); 
        gui.setItem(13, new ItemStack(Material.AIR)); 

        setDisabledButton(gui);
        player.openInventory(gui);
    }

    @EventHandler
    public void onAnvilClick(InventoryClickEvent event) {
        if (!event.getView().getTitle().equals(guiName)) return;

        Player player = (Player) event.getWhoClicked();
        Inventory gui = event.getInventory();
        int slot = event.getSlot();

        if (event.getClick().isShiftClick()) {
            event.setCancelled(true);
            return;
        }

        if (event.getClickedInventory() != event.getView().getTopInventory()) {
            Bukkit.getScheduler().runTask(plugin, () -> updateAnvilResult(gui, player));
            return;
        }

        if (slot != 29 && slot != 33 && slot != 31 && slot != 13) {
            event.setCancelled(true);
            return;
        }

        if (slot == 13) {
            if (resultAvailable.getOrDefault(player.getUniqueId(), false)) {
                if (event.getCursor() != null && event.getCursor().getType() != Material.AIR) {
                    event.setCancelled(true);
                    return;
                }
                Bukkit.getScheduler().runTask(plugin, () -> {
                    ItemStack item = gui.getItem(13);
                    if (item == null || item.getType().isAir()) {
                        resultAvailable.put(player.getUniqueId(), false);
                        setDisabledButton(gui);
                    }
                });
                return;
            } else {
                event.setCancelled(true);
                return;
            }
        }

        if (slot == 29 || slot == 33) {
            Bukkit.getScheduler().runTask(plugin, () -> updateAnvilResult(gui, player));
            return;
        }

        if (slot == 31) {
            event.setCancelled(true);

            ItemStack leftItem = gui.getItem(29);
            ItemStack rightItem = gui.getItem(33);

            if (leftItem == null || leftItem.getType().isAir() || rightItem == null || rightItem.getType().isAir()) return;

            // Xử lý sạc pin khi click
            CustomEnchant chargeableEnchant = getActiveChargeableEnchant(leftItem, rightItem);
            if (chargeableEnchant != null) {
                int currentCharges = enchantManager.getCharges(leftItem, chargeableEnchant);
                NamespacedKey key = new NamespacedKey(plugin, "enchant_" + chargeableEnchant.getId());
                int level = leftItem.getItemMeta().getPersistentDataContainer().getOrDefault(key, PersistentDataType.INTEGER, 1);
                int maxCharges = enchantManager.getMaxCharges(chargeableEnchant, level);

                int fuelAmount = rightItem.getAmount();
                int chargesToAdd = fuelAmount * 25; // Mỗi cục sạc 25 pin
                int finalCharges = Math.min(currentCharges + chargesToAdd, maxCharges);
                int usedFuel = (int) Math.ceil((double) (finalCharges - currentCharges) / 25.0);
                int cost = 2 * usedFuel; // Chi phí 2 levels cho mỗi cục nguyên liệu sử dụng

                if (player.getLevel() < cost) {
                    player.sendMessage("§cMày đéo đủ Level XP để sạc pin món đồ này người anh em ơi!");
                    player.playSound(player.getLocation(), org.bukkit.Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 0.5f);
                    return;
                }

                ItemStack resultItem = leftItem.clone();
                enchantManager.setCharges(resultItem, chargeableEnchant, finalCharges);
                enchantManager.rebuildEnchantLore(resultItem);

                player.setLevel(player.getLevel() - cost);
                player.sendMessage("§a⚡ Sạc pin báu vật thành công! Đã nạp §e+" + (finalCharges - currentCharges) + " pin §avà tiêu hao §e" + usedFuel + "x nguyên liệu§a.");
                player.playSound(player.getLocation(), org.bukkit.Sound.BLOCK_ANVIL_USE, 1.0f, 1.0f);

                gui.setItem(29, new ItemStack(Material.AIR));
                ItemStack remainingFuel = rightItem.clone();
                remainingFuel.setAmount(rightItem.getAmount() - usedFuel);
                gui.setItem(33, remainingFuel.getAmount() <= 0 ? new ItemStack(Material.AIR) : remainingFuel);

                gui.setItem(13, resultItem);
                resultAvailable.put(player.getUniqueId(), true);

                ItemStack barrier = new ItemStack(Material.BARRIER);
                ItemMeta bMeta = barrier.getItemMeta();
                if (bMeta != null) {
                    bMeta.setDisplayName("§aThành phẩm đã sẵn sàng!");
                    bMeta.setLore(Arrays.asList("§7Hãy nhấc báu vật của mày ở ô số 13 phía trên nhé cu m."));
                    barrier.setItemMeta(bMeta);
                }
                gui.setItem(31, barrier);
                return;
            }

            int reqLevel = calculateAnvilCost(leftItem, rightItem);
            if (reqLevel == -1) return;

            if (player.getLevel() < reqLevel) {
                player.sendMessage("§cMày đéo đủ Level XP để kết hợp món đồ này người anh em ơi!");
                player.playSound(player.getLocation(), org.bukkit.Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 0.5f);
                return;
            }

            ItemStack resultItem = leftItem.clone();
            applyCombinationLogic(resultItem, rightItem);

            player.setLevel(player.getLevel() - reqLevel);
            player.sendMessage("§a🟩 Kết hợp vật phẩm thành công! Đã khấu trừ §e" + reqLevel + " Exp Levels§a.");
            player.playSound(player.getLocation(), org.bukkit.Sound.BLOCK_ANVIL_USE, 1.0f, 1.0f);

            gui.setItem(29, new ItemStack(Material.AIR));
            gui.setItem(33, new ItemStack(Material.AIR));

            gui.setItem(13, resultItem);
            resultAvailable.put(player.getUniqueId(), true);

            ItemStack barrier = new ItemStack(Material.BARRIER);
            ItemMeta bMeta = barrier.getItemMeta();
            if (bMeta != null) {
                bMeta.setDisplayName("§aThành phẩm đã sẵn sàng!");
                bMeta.setLore(Arrays.asList("§7Hãy nhấc báu vật của mày ở ô số 13 phía trên nhé cu m."));
                barrier.setItemMeta(bMeta);
            }
            gui.setItem(31, barrier);
        }
    }

    private void updateAnvilResult(Inventory gui, Player player) {
        if (resultAvailable.getOrDefault(player.getUniqueId(), false)) {
            return;
        }

        ItemStack leftItem = gui.getItem(29);
        ItemStack rightItem = gui.getItem(33);

        if (leftItem == null || leftItem.getType().isAir() || rightItem == null || rightItem.getType().isAir()) {
            gui.setItem(13, new ItemStack(Material.AIR));
            setDisabledButton(gui);
            return;
        }

        // Kiểm tra xem có đang thực hiện sạc pin không
        CustomEnchant chargeableEnchant = getActiveChargeableEnchant(leftItem, rightItem);
        if (chargeableEnchant != null) {
            int currentCharges = enchantManager.getCharges(leftItem, chargeableEnchant);
            NamespacedKey key = new NamespacedKey(plugin, "enchant_" + chargeableEnchant.getId());
            int level = leftItem.getItemMeta().getPersistentDataContainer().getOrDefault(key, PersistentDataType.INTEGER, 1);
            int maxCharges = enchantManager.getMaxCharges(chargeableEnchant, level);

            if (currentCharges >= maxCharges) {
                gui.setItem(13, new ItemStack(Material.AIR));
                ItemStack barrier = new ItemStack(Material.BARRIER);
                ItemMeta bMeta = barrier.getItemMeta();
                if (bMeta != null) {
                    bMeta.setDisplayName("§cPin đã đầy!");
                    bMeta.setLore(Arrays.asList("§7Bùa chú §e" + chargeableEnchant.getDisplayName() + " §7trên vật phẩm", "§7đã được sạc đầy pin (" + currentCharges + "/" + maxCharges + "⚡)"));
                    barrier.setItemMeta(bMeta);
                }
                gui.setItem(31, barrier);
                return;
            }

            int fuelAmount = rightItem.getAmount();
            int chargesToAdd = fuelAmount * 25;
            int finalCharges = Math.min(currentCharges + chargesToAdd, maxCharges);
            int usedFuel = (int) Math.ceil((double) (finalCharges - currentCharges) / 25.0);
            int cost = 2 * usedFuel;

            ItemStack previewItem = leftItem.clone();
            enchantManager.setCharges(previewItem, chargeableEnchant, finalCharges);
            enchantManager.rebuildEnchantLore(previewItem);
            gui.setItem(13, previewItem);

            ItemStack anvilBtn = new ItemStack(Material.ANVIL);
            ItemMeta aMeta = anvilBtn.getItemMeta();
            if (aMeta != null) {
                aMeta.setDisplayName("§a⚡ SẠC PIN BỬA CHÚ ⚡");
                if (player.getLevel() < cost) {
                    aMeta.setLore(Arrays.asList(
                        "§7Nạp pin cho bùa §e" + chargeableEnchant.getDisplayName(),
                        "§7Số lượng nạp: §b+" + (finalCharges - currentCharges) + "⚡ (" + finalCharges + "/" + maxCharges + "⚡)",
                        "§7Tiêu hao: §6" + usedFuel + "x §7nguyên liệu",
                        "",
                        "§7Chi phí nạp: §c" + cost + " Exp Levels",
                        "§cMày không đủ cấp độ kinh nghiệm!"
                    ));
                } else {
                    aMeta.setLore(Arrays.asList(
                        "§7Nạp pin cho bùa §e" + chargeableEnchant.getDisplayName(),
                        "§7Số lượng nạp: §b+" + (finalCharges - currentCharges) + "⚡ (" + finalCharges + "/" + maxCharges + "⚡)",
                        "§7Tiêu hao: §6" + usedFuel + "x §7nguyên liệu",
                        "",
                        "§7Chi phí nạp: §e" + cost + " Exp Levels",
                        "§eClick để sạc pin báu vật!"
                    ));
                }
                anvilBtn.setItemMeta(aMeta);
            }
            gui.setItem(31, anvilBtn);
            return;
        }

        int cost = calculateAnvilCost(leftItem, rightItem);
        if (cost == -1) {
            gui.setItem(13, new ItemStack(Material.AIR));
            ItemStack barrier = new ItemStack(Material.BARRIER);
            ItemMeta bMeta = barrier.getItemMeta();
            if (bMeta != null) {
                bMeta.setDisplayName("§cGhép đồ đéo hợp lệ!");
                bMeta.setLore(Arrays.asList("§7Bùa chú của nguyên liệu phụ xung đột môn phái", "§7với vũ khí chính đang sở hữu ở ô bên trái."));
                barrier.setItemMeta(bMeta);
            }
            gui.setItem(31, barrier);
            return;
        }

        ItemStack previewItem = leftItem.clone();
        applyCombinationLogic(previewItem, rightItem);
        gui.setItem(13, previewItem);

        ItemStack anvilBtn = new ItemStack(Material.ANVIL);
        ItemMeta aMeta = anvilBtn.getItemMeta();
        if (aMeta != null) {
            aMeta.setDisplayName("§aCombine Items");
            if (player.getLevel() < cost) {
                aMeta.setLore(Arrays.asList("§7Hợp nhất toàn bộ bùa chú từ ô bên phải vào ô bên trái.", "", "§7Chi phí đúc: §c" + cost + " Exp Levels", "§cMày không đủ cấp độ kinh nghiệm!"));
            } else {
                aMeta.setLore(Arrays.asList("§7Hợp nhất toàn bộ bùa chú từ ô bên phải vào ô bên trái.", "", "§7Chi phí đúc: §e" + cost + " Exp Levels", "§eClick chuột để tiến hành rèn đúc báu vật!"));
            }
            anvilBtn.setItemMeta(aMeta);
        }
        gui.setItem(31, anvilBtn);
    }

    private CustomEnchant getActiveChargeableEnchant(ItemStack left, ItemStack right) {
        if (left == null || !left.hasItemMeta() || right == null) return null;
        PersistentDataContainer container = left.getItemMeta().getPersistentDataContainer();
        for (CustomEnchant enchant : CustomEnchant.values()) {
            if (enchantManager.isChargeable(enchant)) {
                NamespacedKey key = new NamespacedKey(plugin, "enchant_" + enchant.getId());
                if (container.has(key, PersistentDataType.INTEGER)) {
                    if (right.getType() == enchantManager.getFuelMaterial(enchant)) {
                        return enchant;
                    }
                }
            }
        }
        return null;
    }

    private int calculateAnvilCost(ItemStack left, ItemStack right) {
        if (!left.hasItemMeta() || !right.hasItemMeta()) return 5;
        PersistentDataContainer rightContainer = right.getItemMeta().getPersistentDataContainer();
        boolean hasValidEnchant = false;
        int totalLevelsFound = 0;

        for (CustomEnchant enchant : CustomEnchant.values()) {
            NamespacedKey key = new NamespacedKey(plugin, "enchant_" + enchant.getId());
            if (rightContainer.has(key, PersistentDataType.INTEGER)) {
                if (left.getType() == Material.BOOK || left.getType() == Material.ENCHANTED_BOOK || enchant.getItemGroup().canApply(left.getType())) {
                    String matName = left.getType().name();
                    boolean isActualAxe = matName.endsWith("_AXE") || matName.equals("AXE");
                    if (isActualAxe) {
                        if (enchant.getItemGroup() == CustomEnchant.ItemGroup.SWORD && enchantManager.hasEnchantFromGroup(left, CustomEnchant.ItemGroup.TOOL)) {
                            continue;
                        }
                        if (enchant.getItemGroup() == CustomEnchant.ItemGroup.TOOL && enchantManager.hasEnchantFromGroup(left, CustomEnchant.ItemGroup.SWORD)) {
                            continue;
                        }
                    }

                    hasValidEnchant = true;
                    totalLevelsFound += rightContainer.get(key, PersistentDataType.INTEGER);
                }
            }
        }
        
        for (UltimateEnchant enchant : UltimateEnchant.values()) {
            NamespacedKey key = new NamespacedKey(plugin, "ult_enchant_" + enchant.getId());
            if (rightContainer.has(key, PersistentDataType.INTEGER)) {
                if (left.getType() == Material.BOOK || left.getType() == Material.ENCHANTED_BOOK || enchant.getGroup().canApply(left.getType())) {
                    hasValidEnchant = true;
                    totalLevelsFound += rightContainer.get(key, PersistentDataType.INTEGER) * 2; // Ultimate enchants cost double
                }
            }
        }

        if (!hasValidEnchant) return -1;
        return 10 + (totalLevelsFound * 6);
    }

    private void applyCombinationLogic(ItemStack target, ItemStack ingredient) {
        ItemMeta targetMeta = target.getItemMeta();
        ItemMeta ingMeta = ingredient.getItemMeta();
        if (targetMeta == null || ingMeta == null) return;

        PersistentDataContainer targetContainer = targetMeta.getPersistentDataContainer();
        PersistentDataContainer ingContainer = ingMeta.getPersistentDataContainer();

        for (CustomEnchant enchant : CustomEnchant.values()) {
            NamespacedKey key = new NamespacedKey(plugin, "enchant_" + enchant.getId());
            if (ingContainer.has(key, PersistentDataType.INTEGER)) {
                int ingLvl = ingContainer.get(key, PersistentDataType.INTEGER);
                if (target.getType() != Material.BOOK && target.getType() != Material.ENCHANTED_BOOK && !enchant.getItemGroup().canApply(target.getType())) {
                    continue;
                }

                String matName = target.getType().name();
                boolean isActualAxe = matName.endsWith("_AXE") || matName.equals("AXE");
                if (isActualAxe) {
                    if (enchant.getItemGroup() == CustomEnchant.ItemGroup.SWORD && enchantManager.hasEnchantFromGroup(target, CustomEnchant.ItemGroup.TOOL)) {
                        continue;
                    }
                    if (enchant.getItemGroup() == CustomEnchant.ItemGroup.TOOL && enchantManager.hasEnchantFromGroup(target, CustomEnchant.ItemGroup.SWORD)) {
                        continue;
                    }
                }

                if (targetContainer.has(key, PersistentDataType.INTEGER)) {
                    int targetLvl = targetContainer.get(key, PersistentDataType.INTEGER);
                    int finalLvl = (targetLvl == ingLvl) ? Math.min(targetLvl + 1, enchant.getMaxLevel()) : Math.max(targetLvl, ingLvl);
                    targetContainer.set(key, PersistentDataType.INTEGER, finalLvl);
                } else {
                    targetContainer.set(key, PersistentDataType.INTEGER, ingLvl);
                }
                
                // Giữ nguyên số pin báu vật cũ nếu gộp
                if (enchantManager.isChargeable(enchant) && !targetContainer.has(enchantManager.getChargesKey(enchant), PersistentDataType.INTEGER)) {
                    targetContainer.set(enchantManager.getChargesKey(enchant), PersistentDataType.INTEGER, enchantManager.getCharges(target, enchant));
                }
            }
        }
        
        for (UltimateEnchant enchant : UltimateEnchant.values()) {
            NamespacedKey key = new NamespacedKey(plugin, "ult_enchant_" + enchant.getId());
            if (ingContainer.has(key, PersistentDataType.INTEGER)) {
                int ingLvl = ingContainer.get(key, PersistentDataType.INTEGER);
                if (target.getType() != Material.BOOK && target.getType() != Material.ENCHANTED_BOOK && !enchant.getGroup().canApply(target.getType())) {
                    continue;
                }
                
                int finalLvl = ingLvl;
                if (targetContainer.has(key, PersistentDataType.INTEGER)) {
                    int targetLvl = targetContainer.get(key, PersistentDataType.INTEGER);
                    finalLvl = (targetLvl == ingLvl) ? Math.min(targetLvl + 1, enchant.getMaxLevel()) : Math.max(targetLvl, ingLvl);
                }
                
                enchantManager.applyUltimateEnchantToItem(target, enchant, finalLvl);
            }
        }

        target.setItemMeta(targetMeta);
        enchantManager.rebuildEnchantLore(target);
    }

    private void setDisabledButton(Inventory gui) {
        ItemStack barrier = new ItemStack(Material.BARRIER);
        ItemMeta bMeta = barrier.getItemMeta();
        if (bMeta != null) {
            bMeta.setDisplayName("§cChưa đủ nguyên liệu!");
            bMeta.setLore(Arrays.asList("§7Hãy đặt Vật phẩm chính vào ô bên TRÁI (Slot 29)", "§7và Nguyên liệu phụ/Sách vào ô bên PHẢI (Slot 33)."));
            barrier.setItemMeta(bMeta);
        }
        gui.setItem(31, barrier);
    }

    @EventHandler
    public void onAnvilClose(InventoryCloseEvent event) {
        if (!event.getView().getTitle().equals(guiName)) return;
        Inventory gui = event.getInventory();
        Player player = (Player) event.getPlayer();

        int[] slotsToReturn = {29, 33};
        for (int slot : slotsToReturn) {
            ItemStack item = gui.getItem(slot);
            if (item != null && item.getType() != Material.AIR) {
                for (ItemStack drop : player.getInventory().addItem(item).values()) {
                    player.getWorld().dropItemNaturally(player.getLocation(), drop);
                }
            }
        }

        if (resultAvailable.getOrDefault(player.getUniqueId(), false)) {
            ItemStack resultItem = gui.getItem(13);
            if (resultItem != null && resultItem.getType() != Material.AIR) {
                for (ItemStack drop : player.getInventory().addItem(resultItem).values()) {
                    player.getWorld().dropItemNaturally(player.getLocation(), drop);
                }
            }
            resultAvailable.remove(player.getUniqueId());
        }
    }

    @EventHandler
    public void onPrepareVanillaAnvil(org.bukkit.event.inventory.PrepareAnvilEvent event) {
        ItemStack left = event.getInventory().getItem(0);
        ItemStack right = event.getInventory().getItem(1);
        if (left == null || right == null) return;
        
        ItemStack result = event.getResult();
        if (result == null) return;

        // Bypass "Too Expensive" limit
        event.getInventory().setMaximumRepairCost(999);

        // Reset repair cost tag on result
        ItemMeta resultMeta = result.getItemMeta();
        if (resultMeta instanceof org.bukkit.inventory.meta.Repairable) {
            ((org.bukkit.inventory.meta.Repairable) resultMeta).setRepairCost(0);
            event.getInventory().setRepairCost(1);
        }

        ItemMeta leftMeta = left.getItemMeta();
        ItemMeta rightMeta = right.getItemMeta();
        
        if (leftMeta != null && rightMeta != null && resultMeta != null) {
            PersistentDataContainer leftPDC = leftMeta.getPersistentDataContainer();
            PersistentDataContainer rightPDC = rightMeta.getPersistentDataContainer();
            PersistentDataContainer resultPDC = resultMeta.getPersistentDataContainer();
            
            boolean changed = false;
            for (CustomEnchant enchant : CustomEnchant.values()) {
                NamespacedKey key = new NamespacedKey(plugin, "enchant_" + enchant.getId());
                if (rightPDC.has(key, PersistentDataType.INTEGER)) {
                    int rightLvl = rightPDC.get(key, PersistentDataType.INTEGER);
                    if (left.getType() == Material.BOOK || left.getType() == Material.ENCHANTED_BOOK || enchant.getItemGroup().canApply(left.getType())) {
                        int leftLvl = leftPDC.getOrDefault(key, PersistentDataType.INTEGER, 0);
                        int finalLvl = (leftLvl == rightLvl) ? Math.min(leftLvl + 1, enchant.getMaxLevel()) : Math.max(leftLvl, rightLvl);
                        resultPDC.set(key, PersistentDataType.INTEGER, finalLvl);
                        changed = true;
                    }
                }
            }
            
            for (UltimateEnchant enchant : UltimateEnchant.values()) {
                NamespacedKey key = new NamespacedKey(plugin, "ult_enchant_" + enchant.getId());
                if (rightPDC.has(key, PersistentDataType.INTEGER)) {
                    int rightLvl = rightPDC.get(key, PersistentDataType.INTEGER);
                    if (left.getType() == Material.BOOK || left.getType() == Material.ENCHANTED_BOOK || enchant.getGroup().canApply(left.getType())) {
                        int leftLvl = leftPDC.getOrDefault(key, PersistentDataType.INTEGER, 0);
                        int finalLvl = (leftLvl == rightLvl) ? Math.min(leftLvl + 1, enchant.getMaxLevel()) : Math.max(leftLvl, rightLvl);
                        
                        enchantManager.applyUltimateEnchantToItem(result, enchant, finalLvl);
                        changed = true;
                    }
                }
            }

            if (changed) {
                result.setItemMeta(resultMeta);
                enchantManager.rebuildEnchantLore(result);
                event.setResult(result);
            } else {
                // If it's just repairing or vanilla combining, still apply the repair cost reset
                result.setItemMeta(resultMeta);
                event.setResult(result);
            }
        }
    }
}
