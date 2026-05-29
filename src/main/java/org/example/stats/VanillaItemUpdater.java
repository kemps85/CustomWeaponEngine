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
                || name.contains("HELMET") || name.contains("CHESTPLATE") || name.contains("LEGGINGS") || name.contains("BOOTS");

        if (!isEquipment) return;

        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;
        PersistentDataContainer pdc = meta.getPersistentDataContainer();

        boolean needsUpdate = false;

        // Auto-tag rarity for vanilla items without rarity
        if (!pdc.has(rarityKey, PersistentDataType.STRING)) {
            String rarity = "COMMON";
            if (name.startsWith("IRON_") || name.startsWith("CHAINMAIL_") || name.startsWith("GOLDEN_")) {
                rarity = "UNCOMMON";
            } else if (name.startsWith("DIAMOND_")) {
                rarity = "RARE";
            } else if (name.startsWith("NETHERITE_")) {
                rarity = "EPIC";
            }
            pdc.set(rarityKey, PersistentDataType.STRING, rarity);
            needsUpdate = true;
        }

        // Bug fix: Rebuild enchant lore to fix old items that have bugged stats
        if (!pdc.has(updatedKey, PersistentDataType.INTEGER)) {
            // Mark as updated so we don't spam rebuild
            pdc.set(updatedKey, PersistentDataType.INTEGER, 1);
            needsUpdate = true;
        }

        if (needsUpdate) {
            // Apply rarity to lore via ItemStatsGUI
            String rarityStr = pdc.get(rarityKey, PersistentDataType.STRING);
            org.example.stats.ItemStatsGUI.Rarity rEnum = org.example.stats.ItemStatsGUI.Rarity.COMMON;
            try { rEnum = org.example.stats.ItemStatsGUI.Rarity.valueOf(rarityStr); } catch (Exception ignored) {}
            
            String bTitle = pdc.has(new NamespacedKey(plugin, org.example.stats.ItemStatsGUI.KEY_SETBONUS_TITLE), PersistentDataType.STRING) ? pdc.get(new NamespacedKey(plugin, org.example.stats.ItemStatsGUI.KEY_SETBONUS_TITLE), PersistentDataType.STRING) : "";
            String bD1 = pdc.has(new NamespacedKey(plugin, org.example.stats.ItemStatsGUI.KEY_SETBONUS_DESC1), PersistentDataType.STRING) ? pdc.get(new NamespacedKey(plugin, org.example.stats.ItemStatsGUI.KEY_SETBONUS_DESC1), PersistentDataType.STRING) : "";
            String bD2 = pdc.has(new NamespacedKey(plugin, org.example.stats.ItemStatsGUI.KEY_SETBONUS_DESC2), PersistentDataType.STRING) ? pdc.get(new NamespacedKey(plugin, org.example.stats.ItemStatsGUI.KEY_SETBONUS_DESC2), PersistentDataType.STRING) : "";
            String click = pdc.has(new NamespacedKey(plugin, org.example.stats.ItemStatsGUI.KEY_ABILITY_CLICK), PersistentDataType.STRING) ? pdc.get(new NamespacedKey(plugin, org.example.stats.ItemStatsGUI.KEY_ABILITY_CLICK), PersistentDataType.STRING) : "RIGHT CLICK";
            
            double[] stats = new double[7];
            stats[0] = pdc.has(new NamespacedKey(plugin, org.example.stats.ItemStatsGUI.KEY_STRENGTH), PersistentDataType.DOUBLE) ? pdc.get(new NamespacedKey(plugin, org.example.stats.ItemStatsGUI.KEY_STRENGTH), PersistentDataType.DOUBLE) : 0.0;
            stats[1] = pdc.has(new NamespacedKey(plugin, org.example.stats.ItemStatsGUI.KEY_CRIT_CHANCE), PersistentDataType.DOUBLE) ? pdc.get(new NamespacedKey(plugin, org.example.stats.ItemStatsGUI.KEY_CRIT_CHANCE), PersistentDataType.DOUBLE) : 0.0;
            stats[2] = pdc.has(new NamespacedKey(plugin, org.example.stats.ItemStatsGUI.KEY_CRIT_DAMAGE), PersistentDataType.DOUBLE) ? pdc.get(new NamespacedKey(plugin, org.example.stats.ItemStatsGUI.KEY_CRIT_DAMAGE), PersistentDataType.DOUBLE) : 0.0;
            stats[3] = pdc.has(new NamespacedKey(plugin, org.example.stats.ItemStatsGUI.KEY_HEALTH), PersistentDataType.DOUBLE) ? pdc.get(new NamespacedKey(plugin, org.example.stats.ItemStatsGUI.KEY_HEALTH), PersistentDataType.DOUBLE) : 0.0;
            stats[4] = pdc.has(new NamespacedKey(plugin, org.example.stats.ItemStatsGUI.KEY_DEFENSE), PersistentDataType.DOUBLE) ? pdc.get(new NamespacedKey(plugin, org.example.stats.ItemStatsGUI.KEY_DEFENSE), PersistentDataType.DOUBLE) : 0.0;
            stats[5] = pdc.has(new NamespacedKey(plugin, org.example.stats.ItemStatsGUI.KEY_INTELLIGENCE), PersistentDataType.DOUBLE) ? pdc.get(new NamespacedKey(plugin, org.example.stats.ItemStatsGUI.KEY_INTELLIGENCE), PersistentDataType.DOUBLE) : 0.0;
            stats[6] = pdc.has(new NamespacedKey(plugin, org.example.stats.ItemStatsGUI.KEY_SPEED), PersistentDataType.DOUBLE) ? pdc.get(new NamespacedKey(plugin, org.example.stats.ItemStatsGUI.KEY_SPEED), PersistentDataType.DOUBLE) : 0.0;
            
            double damage = pdc.has(new NamespacedKey(plugin, "stat_damage"), PersistentDataType.DOUBLE) ? pdc.get(new NamespacedKey(plugin, "stat_damage"), PersistentDataType.DOUBLE) : 0.0;
            if (damage == 0.0) {
                damage = pdc.has(new NamespacedKey(plugin, "cwe_damage"), PersistentDataType.DOUBLE) ? pdc.get(new NamespacedKey(plugin, "cwe_damage"), PersistentDataType.DOUBLE) : 0.0;
            }

            boolean isWeapon = name.contains("SWORD") || name.contains("AXE") || name.contains("BOW") || name.contains("CROSSBOW") || item.getType() == Material.TRIDENT;
            org.example.stats.ItemStatsGUI.rebuildLore(item, meta, stats, damage, rEnum, bTitle, bD1, bD2, click, isWeapon);
            
            item.setItemMeta(meta);
            
            // Rewrite lore and stats using EnchantManager
            EnchantManager em = CustomWeaponEngine.getEnchantManager();
            if (em != null) {
                em.rebuildEnchantLore(item);
            }
        }
    }
}
