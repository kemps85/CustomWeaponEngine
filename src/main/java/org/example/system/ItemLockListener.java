package org.example.system;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.example.core.CustomWeaponEngine;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ItemLockListener implements Listener {

    private final CustomWeaponEngine plugin;
    private final NamespacedKey ownerKey;

    public ItemLockListener(CustomWeaponEngine plugin) {
        this.plugin = plugin;
        this.ownerKey = new NamespacedKey(plugin, "owner_uuid");
    }

    // 1. Khi người chơi vứt đồ ra
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        Item item = event.getItemDrop();
        item.getPersistentDataContainer().set(ownerKey, PersistentDataType.STRING, event.getPlayer().getUniqueId().toString());
    }

    // 2. Khi quái chết và rớt đồ
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDeath(EntityDeathEvent event) {
        Player killer = event.getEntity().getKiller();
        if (killer != null && !event.getDrops().isEmpty()) {
            List<ItemStack> drops = new ArrayList<>(event.getDrops());
            event.getDrops().clear();
            
            for (ItemStack stack : drops) {
                if (stack == null || stack.getType().isAir()) continue;
                Item itemEntity = event.getEntity().getWorld().dropItemNaturally(event.getEntity().getLocation(), stack);
                itemEntity.getPersistentDataContainer().set(ownerKey, PersistentDataType.STRING, killer.getUniqueId().toString());
            }
        }
    }

    // 3. Khi người chơi đập block rớt đồ
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockDropItem(BlockDropItemEvent event) {
        Player player = event.getPlayer();
        for (Item item : event.getItems()) {
            item.getPersistentDataContainer().set(ownerKey, PersistentDataType.STRING, player.getUniqueId().toString());
        }
    }

    // 4. Ngăn chặn nhặt đồ không phải của mình
    @EventHandler(priority = EventPriority.LOWEST)
    public void onEntityPickup(EntityPickupItemEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            // Ngăn cản quái vật nhặt đồ luôn nếu item đó có owner
            if (event.getItem().getPersistentDataContainer().has(ownerKey, PersistentDataType.STRING)) {
                event.setCancelled(true);
            }
            return;
        }

        Player player = (Player) event.getEntity();
        Item item = event.getItem();

        if (item.getPersistentDataContainer().has(ownerKey, PersistentDataType.STRING)) {
            String ownerUuidStr = item.getPersistentDataContainer().get(ownerKey, PersistentDataType.STRING);
            if (ownerUuidStr != null && !ownerUuidStr.equals(player.getUniqueId().toString())) {
                event.setCancelled(true);
            }
        }
    }
}
