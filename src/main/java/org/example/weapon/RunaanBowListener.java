package org.example.weapon;

import org.example.core.CustomWeaponEngine;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class RunaanBowListener implements Listener {
    private final CustomWeaponEngine plugin;
    private final NamespacedKey cweIdKey;
    private final Map<UUID, Long> cooldowns = new HashMap<>();

    public RunaanBowListener(CustomWeaponEngine plugin) {
        this.plugin = plugin;
        this.cweIdKey = new NamespacedKey(plugin, "cwe_id");
    }



    @EventHandler
    public void onShootBow(EntityShootBowEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();
        ItemStack bow = event.getBow();

        if (bow == null || !bow.hasItemMeta()) return;

        PersistentDataContainer container = bow.getItemMeta().getPersistentDataContainer();
        if (container.has(cweIdKey, PersistentDataType.STRING)) {
            String id = container.get(cweIdKey, PersistentDataType.STRING);
            if ("cwe_runaan_bow".equalsIgnoreCase(id) || "runaan_bow".equalsIgnoreCase(id)) {
                
                Entity proj = event.getProjectile();
                if (!(proj instanceof Arrow)) return;
                
                Arrow mainArrow = (Arrow) proj;
                mainArrow.setMetadata("runaan_main", new FixedMetadataValue(plugin, true));

                Vector mainVel = mainArrow.getVelocity();
                float speed = (float) mainVel.length();

                // 12.5 degrees in radians
                double angleRadians = Math.toRadians(12.5);

                // Calculate left and right velocity vectors
                Vector leftVel = rotateVectorAroundY(mainVel, angleRadians).normalize().multiply(speed);
                Vector rightVel = rotateVectorAroundY(mainVel, -angleRadians).normalize().multiply(speed);

                // Spawn side arrows
                Arrow leftArrow = player.launchProjectile(Arrow.class);
                leftArrow.setVelocity(leftVel);
                leftArrow.setShooter(player);
                leftArrow.setMetadata("runaan_side", new FixedMetadataValue(plugin, true));

                Arrow rightArrow = player.launchProjectile(Arrow.class);
                rightArrow.setVelocity(rightVel);
                rightArrow.setShooter(player);
                rightArrow.setMetadata("runaan_side", new FixedMetadataValue(plugin, true));

                double dmg = container.getOrDefault(new NamespacedKey(plugin, "stat_damage"), PersistentDataType.DOUBLE, 0.0);
                if (dmg == 0) dmg = container.getOrDefault(new NamespacedKey(plugin, "cwe_damage"), PersistentDataType.DOUBLE, 0.0);
                if (dmg > 0) {
                    leftArrow.setMetadata("cwe_base_damage", new FixedMetadataValue(plugin, dmg));
                    rightArrow.setMetadata("cwe_base_damage", new FixedMetadataValue(plugin, dmg));
                }

                // Chỉ điều hướng (homing) khi cung được kéo đủ căng (force >= 0.8)
                if (event.getForce() >= 0.8f) {
                    startHomingTask(leftArrow, player);
                    startHomingTask(rightArrow, player);
                }
            }
        }
    }

    private Vector rotateVectorAroundY(Vector vector, double angle) {
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        double x = vector.getX() * cos - vector.getZ() * sin;
        double z = vector.getX() * sin + vector.getZ() * cos;
        return new Vector(x, vector.getY(), z);
    }

    private void startHomingTask(Arrow arrow, Player shooter) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (arrow.isDead() || arrow.isOnGround() || !arrow.isValid()) {
                    this.cancel();
                    return;
                }

                // Tăng tầm tìm kiếm từ 10.0 lên 25.0 block
                LivingEntity target = getNearestTarget(arrow, shooter, 25.0);
                if (target != null) {
                    Vector direction = target.getLocation().add(0, target.getHeight() / 2, 0).toVector().subtract(arrow.getLocation().toVector()).normalize();
                    arrow.setVelocity(direction.multiply(arrow.getVelocity().length()));
                }
            }
        }.runTaskTimer(plugin, 1L, 1L);
    }

    private LivingEntity getNearestTarget(Arrow arrow, Player shooter, double radius) {
        LivingEntity nearest = null;
        double minDistanceSq = radius * radius;

        List<Entity> nearby = arrow.getNearbyEntities(radius, radius, radius);
        for (Entity e : nearby) {
            if (e instanceof LivingEntity && e != shooter && !e.isDead() && !(e instanceof org.bukkit.entity.Player)) {
                if (e.getType() == org.bukkit.entity.EntityType.ARMOR_STAND) continue;
                
                double distSq = e.getLocation().distanceSquared(arrow.getLocation());
                if (distSq < minDistanceSq) {
                    minDistanceSq = distSq;
                    nearest = (LivingEntity) e;
                }
            }
        }
        return nearest;
    }

    @EventHandler
    public void onSideArrowDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Arrow) {
            Arrow arrow = (Arrow) event.getDamager();
            if (arrow.hasMetadata("runaan_side")) {
                // Runaan's side arrows deal 40% damage
                event.setDamage(event.getDamage() * 0.4);
            }
        }
    }
}
