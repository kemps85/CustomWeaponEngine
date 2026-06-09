package org.example.weapon;

import org.example.core.CustomWeaponEngine;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityTeleportEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Material;
import org.bukkit.event.entity.EntityShootBowEvent;

public class ShortbowListener implements Listener {
    private final CustomWeaponEngine plugin;
    private final NamespacedKey cweIdKey;
    private final Map<UUID, Long> cooldowns = new HashMap<>();

    public ShortbowListener(CustomWeaponEngine plugin) {
        this.plugin = plugin;
        this.cweIdKey = new NamespacedKey(plugin, "cwe_id");
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getHand() != org.bukkit.inventory.EquipmentSlot.HAND) return;
        Action action = event.getAction();
        // Cho phép cả Left Click và Right Click
        if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK || action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK) {
            Player player = event.getPlayer();
            ItemStack item = player.getInventory().getItemInMainHand();

            if (item == null || !item.hasItemMeta()) return;

            PersistentDataContainer container = item.getItemMeta().getPersistentDataContainer();
            if (container.has(cweIdKey, PersistentDataType.STRING)) {
                String id = container.get(cweIdKey, PersistentDataType.STRING);
                if ("cwe_juju_shortbow".equalsIgnoreCase(id) || "juju_shortbow".equalsIgnoreCase(id) || "shortbow".equalsIgnoreCase(id)) {
                    event.setCancelled(true); // Luôn luôn cancel để chặn gồng cung Vanilla

                    // Sử dụng hệ thống Cooldown của Bukkit để đồng bộ với Client
                    if (player.hasCooldown(item.getType())) {
                        return;
                    }

                    // Đặt cooldown 10 tick (0.5s). Client sẽ tự động gửi lại packet sau 10 tick nếu người chơi vẫn đè chuột phải.
                    player.setCooldown(item.getType(), 10);
                    
                    // Shoot arrow
                    Arrow arrow = player.launchProjectile(Arrow.class);
                    arrow.setVelocity(player.getLocation().getDirection().normalize().multiply(3.0));
                    arrow.setShooter(player);
                    arrow.setMetadata("juju_arrow", new FixedMetadataValue(plugin, true));
                    arrow.setPierceLevel(2); // Can pierce 2 times, hitting 3 targets total

                    double dmg = container.getOrDefault(new NamespacedKey(plugin, "stat_damage"), PersistentDataType.DOUBLE, 0.0);
                    if (dmg == 0) dmg = container.getOrDefault(new NamespacedKey(plugin, "cwe_damage"), PersistentDataType.DOUBLE, 0.0);
                    if (dmg > 0) arrow.setMetadata("cwe_base_damage", new FixedMetadataValue(plugin, dmg));

                    player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ARROW_SHOOT, 1.0f, 1.0f);
                }
            }
        }
    }

    @EventHandler
    public void onShootBow(EntityShootBowEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        ItemStack bow = event.getBow();
        if (bow == null || !bow.hasItemMeta()) return;

        PersistentDataContainer container = bow.getItemMeta().getPersistentDataContainer();
        if (container.has(cweIdKey, PersistentDataType.STRING)) {
            String id = container.get(cweIdKey, PersistentDataType.STRING);
            if ("cwe_juju_shortbow".equalsIgnoreCase(id) || "juju_shortbow".equalsIgnoreCase(id) || "shortbow".equalsIgnoreCase(id)) {
                event.setCancelled(true); // Ngăn không cho cung Vanilla bắn ra mũi tên thường
            }
        }
    }

    @EventHandler
    public void onJujuDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Arrow) {
            Arrow arrow = (Arrow) event.getDamager();
            if (arrow.hasMetadata("juju_arrow")) {
                if (event.getEntity() instanceof Enderman) {
                    // Prevent enderman teleport via juju arrow
                    arrow.setMetadata("hit_enderman", new FixedMetadataValue(plugin, true));
                }
                
                // [ĐOẠN CODE MỚI] Gây sát thương thêm lên quái vật hệ Bóng tối
                if (isDarkMob(event.getEntity().getType())) {
                    event.setDamage(event.getDamage() * 1.5); // Tăng 50% sát thương
                }
            }
        }
    }

    // [ĐOẠN CODE MỚI] Hàm kiểm tra quái vật hệ Bóng tối
    private boolean isDarkMob(org.bukkit.entity.EntityType type) {
        switch (type) {
            case ENDERMAN:
            case WITHER_SKELETON:
            case PHANTOM:
            case WITHER:
            case ENDER_DRAGON:
            case WARDEN:
            case ENDERMITE:
            case SHULKER:
            case ZOMBIE:
            case SKELETON:
                return true;
            default:
                return false;
        }
    }
}
