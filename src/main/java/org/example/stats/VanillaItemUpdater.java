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

        // Auto-tag RARE for vanilla items without rarity
        if (!pdc.has(rarityKey, PersistentDataType.STRING)) {
            pdc.set(rarityKey, PersistentDataType.STRING, "RARE");
            needsUpdate = true;
        }

        // Bug fix: Rebuild enchant lore to fix old items that have bugged stats
        if (!pdc.has(updatedKey, PersistentDataType.INTEGER)) {
            // Mark as updated so we don't spam rebuild
            pdc.set(updatedKey, PersistentDataType.INTEGER, 1);
            needsUpdate = true;
        }

        if (needsUpdate) {
            item.setItemMeta(meta);
            
            // Rewrite lore and stats using EnchantManager
            EnchantManager em = CustomWeaponEngine.getEnchantManager();
            if (em != null) {
                em.rebuildEnchantLore(item);
            }
        }
    }
}
