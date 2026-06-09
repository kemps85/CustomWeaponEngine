package org.example.stats;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.example.core.CustomWeaponEngine;
import org.example.enchant.EnchantManager;

public class VanillaItemUpdater implements Listener {

    private final CustomWeaponEngine plugin;
    private final NamespacedKey updatedKey;
    private final NamespacedKey rarityKey;

    public VanillaItemUpdater(CustomWeaponEngine plugin) {
        this.plugin = plugin;
        this.updatedKey = new NamespacedKey(plugin, "cwe_item_updated");
        this.rarityKey = new NamespacedKey(plugin, ItemStatsGUI.KEY_RARITY);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onCraft(CraftItemEvent event) {
        ItemStack item = event.getCurrentItem();
        if (item != null) {
            updateItem(item, (Player) event.getWhoClicked());
            event.setCurrentItem(item);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPickup(EntityPickupItemEvent event) {
        if (event.getEntity() instanceof Player) {
            updateItem(event.getItem().getItemStack(), (Player) event.getEntity());
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onInventoryClick(InventoryClickEvent event) {
        ItemStack item = event.getCurrentItem();
        if (item != null) {
            updateItem(item, (Player) event.getWhoClicked());
            event.setCurrentItem(item);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onCreative(org.bukkit.event.inventory.InventoryCreativeEvent event) {
        ItemStack item = event.getCursor();
        if (item != null && !item.getType().isAir()) {
            updateItem(item, (Player) event.getWhoClicked());
            event.setCursor(item);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onInteract(PlayerInteractEvent event) {
        ItemStack item = event.getItem();
        if (item != null) {
            updateItem(item, event.getPlayer());
        }
    }
    
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null) {
                updateItem(item, player);
            }
        }
    }

    private void updateItem(ItemStack item, Player player) {
        if (item == null || item.getType().isAir()) return;

        String name = item.getType().name();
        boolean isEquipment = name.contains("SWORD") || name.contains("AXE") || name.contains("BOW") || name.contains("CROSSBOW")
                || name.contains("HELMET") || name.contains("CHESTPLATE") || name.contains("LEGGINGS") || name.contains("BOOTS") || name.contains("SPEAR")
                || item.getType() == Material.TRIDENT || item.getType() == Material.ELYTRA || item.getType() == Material.MACE || item.getType() == Material.SHIELD;

        if (!isEquipment) return;

        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;
        PersistentDataContainer pdc = meta.getPersistentDataContainer();

        boolean needsUpdate = false;

        // Auto-tag rarity for vanilla items without rarity
        if (!pdc.has(rarityKey, PersistentDataType.STRING)) {
            String rarity = "COMMON";
            if (name.startsWith("IRON_") || name.startsWith("CHAINMAIL_") || name.startsWith("GOLDEN_") || item.getType() == Material.SHIELD) {
                rarity = "UNCOMMON";
            } else if (name.startsWith("DIAMOND_") || item.getType() == Material.TRIDENT) {
                rarity = "RARE";
            } else if (name.startsWith("NETHERITE_") || item.getType() == Material.ELYTRA || item.getType() == Material.MACE) {
                rarity = "EPIC";
            }
            pdc.set(rarityKey, PersistentDataType.STRING, rarity);
            needsUpdate = true;
        }

        // Auto-assign base stats for special vanilla items
        if (!pdc.has(new NamespacedKey(plugin, "cwe_base_stats_assigned"), PersistentDataType.INTEGER)) {
            NamespacedKey dmgKey = new NamespacedKey(plugin, "stat_damage");
            NamespacedKey strKey = new NamespacedKey(plugin, org.example.stats.ItemStatsGUI.KEY_STRENGTH);
            NamespacedKey defKey = new NamespacedKey(plugin, org.example.stats.ItemStatsGUI.KEY_DEFENSE);
            NamespacedKey hpKey = new NamespacedKey(plugin, org.example.stats.ItemStatsGUI.KEY_HEALTH);

            if (item.getType() == Material.TRIDENT && !pdc.has(dmgKey, PersistentDataType.DOUBLE)) {
                pdc.set(dmgKey, PersistentDataType.DOUBLE, 60.0);
                pdc.set(strKey, PersistentDataType.DOUBLE, 35.0);
                needsUpdate = true;
            } else if (item.getType() == Material.MACE && !pdc.has(dmgKey, PersistentDataType.DOUBLE)) {
                pdc.set(dmgKey, PersistentDataType.DOUBLE, 75.0);
                pdc.set(strKey, PersistentDataType.DOUBLE, 50.0);
                needsUpdate = true;
            } else if (item.getType() == Material.ELYTRA && !pdc.has(defKey, PersistentDataType.DOUBLE)) {
                pdc.set(defKey, PersistentDataType.DOUBLE, 40.0);
                pdc.set(hpKey, PersistentDataType.DOUBLE, 80.0);
                needsUpdate = true;
            } else if (item.getType() == Material.SHIELD && !pdc.has(defKey, PersistentDataType.DOUBLE)) {
                pdc.set(defKey, PersistentDataType.DOUBLE, 30.0);
                pdc.set(hpKey, PersistentDataType.DOUBLE, 20.0);
                needsUpdate = true;
            }
            pdc.set(new NamespacedKey(plugin, "cwe_base_stats_assigned"), PersistentDataType.INTEGER, 1);
            pdc.set(new NamespacedKey(plugin, org.example.stats.ItemStatsGUI.KEY_HAS_STATS), PersistentDataType.INTEGER, 1);
        }

        // Bug fix: Rebuild enchant lore to fix old items that have bugged stats
        if (!pdc.has(updatedKey, PersistentDataType.INTEGER)) {
            // Mark as updated so we don't spam rebuild
            pdc.set(updatedKey, PersistentDataType.INTEGER, 1);
            needsUpdate = true;
        }

        if (needsUpdate) {
            item.setItemMeta(meta);
            ItemBuilder.updateItem(item);
        }
    }
}
