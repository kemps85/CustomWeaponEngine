package org.example.weapon.legendary;

import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;
import org.example.core.CustomWeaponEngine;

public class AOTDListener implements Listener {
    private final NamespacedKey idKey;

    // Hằng số cho cone attack
    private static final double CONE_RANGE   = 8.0;  // độ dài phễu (block)
    private static final double CONE_RADIUS  = 4.0;  // bán kính đầu phễu (block)
    // Ngưỡng dot product: cos(atan2(radius, range)) = range / sqrt(range²+radius²)
    private static final double CONE_DOT_THRESHOLD = CONE_RANGE / Math.sqrt(CONE_RANGE * CONE_RANGE + CONE_RADIUS * CONE_RADIUS);

    // Hệ số giảm sát thương khi PvP (giảm 80% → còn 20%)
    private static final double PVP_DMG_MULTIPLIER = 0.20;
    private static final double BASE_DAMAGE        = 400.0;

    public AOTDListener(CustomWeaponEngine plugin) {
        this.idKey = new NamespacedKey(plugin, "cwe_id");
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getHand() != org.bukkit.inventory.EquipmentSlot.HAND) return;
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (event.getItem() == null || event.getItem().getItemMeta() == null) return;

        String id = event.getItem().getItemMeta().getPersistentDataContainer().get(idKey, PersistentDataType.STRING);
        if (!"cwe_aotd".equalsIgnoreCase(id) && !"aotd".equalsIgnoreCase(id)) return;

        Player p = event.getPlayer();

        // Kiểm tra mana (100 mana cho Dragon Rage)
        try {
            if (!ManaHelper.consumeMana(p, 100.0, (CustomWeaponEngine) org.bukkit.plugin.java.JavaPlugin.getPlugin(CustomWeaponEngine.class), "Dragon Rage")) {
                return;
            }
        } catch (Throwable t) {
            // Ignored if AuraSkills API is broken
        }

        p.playSound(p.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1.0f, 1.0f);

        // Hướng nhìn của player (đã bỏ component Y để tính nón ngang)
        Vector forward = p.getLocation().getDirection().setY(0);
        if (forward.lengthSquared() < 0.0001) {
            forward = new Vector(1, 0, 0); // fallback nếu nhìn thẳng lên/xuống
        } else {
            forward.normalize();
        }
        
        Vector playerPos = p.getLocation().toVector();

        // Spawn particles hình phễu để visualize
        try {
            spawnConeParticles(p, forward);
        } catch (Exception ex) {
            // Bỏ qua lỗi particle
        }

        // Quét entities trong bán kính rộng rồi lọc theo cone
        for (Entity e : p.getNearbyEntities(CONE_RANGE + 1, CONE_RADIUS + 2, CONE_RANGE + 1)) {
            if (!(e instanceof LivingEntity) || e == p || e instanceof org.bukkit.entity.Player) continue;
            if (e instanceof org.bukkit.entity.ArmorStand) continue;

            LivingEntity target = (LivingEntity) e;

            // Vector từ player đến entity
            Vector toTarget = target.getLocation().toVector().subtract(playerPos);
            double horizontalDist = toTarget.clone().setY(0).length();

            if (horizontalDist > CONE_RANGE || horizontalDist < 0.5) continue; // Quá xa

            // Tính dot product (trên mặt phẳng ngang) để xem có nằm phía trước không
            Vector toTargetNorm = toTarget.clone().setY(0).normalize();
            double dot = forward.dot(toTargetNorm);

            if (dot < CONE_DOT_THRESHOLD) continue; // Không nằm trong phễu

            // Tính damage — giảm 80% nếu target là Player
            double dmg = BASE_DAMAGE;
            if (target instanceof Player) {
                dmg *= PVP_DMG_MULTIPLIER;
            }

            // Bỏ immunity frame để skill chắc chắn trúng
            target.setNoDamageTicks(0);

            // Knockback ra phía trước theo hướng nhìn
            Vector knockback = toTargetNorm.multiply(2.5).setY(1.2);
            target.setVelocity(knockback);
            target.damage(dmg, p);
        }
    }

    /**
     * Spawn particles hình phễu để visualize vùng tấn công.
     */
    private void spawnConeParticles(Player p, Vector forward) {
        // Vector vuông góc với forward trên mặt phẳng ngang
        Vector right = new Vector(-forward.getZ(), 0, forward.getX());
        if (right.lengthSquared() > 0.0001) right.normalize();
        Vector origin = p.getLocation().add(0, 1.2, 0).toVector(); // Căn ngang ngực

        // Vẽ các vòng tròn dọc theo trục phễu
        int rings = 6;
        for (int r = 1; r <= rings; r++) {
            double t = (double) r / rings;           // 0→1
            double dist = t * CONE_RANGE;
            double radius = t * CONE_RADIUS;
            int points = Math.max(12, (int)(radius * 12));

            for (int i = 0; i < points; i++) {
                double angle = 2 * Math.PI * i / points;
                Vector offset = forward.clone().multiply(dist)
                        .add(right.clone().multiply(Math.cos(angle) * radius))
                        .add(new Vector(0, Math.sin(angle) * radius * 0.6, 0)); // flatten vertically slightly
                
                org.bukkit.Location loc = origin.clone().add(offset).toLocation(p.getWorld());
                
                // Mix particles for extreme visibility
                if (i % 3 == 0) {
                    p.getWorld().spawnParticle(Particle.LAVA, loc, 1, 0, 0, 0, 0);
                } else if (i % 3 == 1) {
                    p.getWorld().spawnParticle(Particle.FLAME, loc, 3, 0.1, 0.1, 0.1, 0.05);
                } else {
                    p.getWorld().spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, loc, 1, 0.1, 0.1, 0.1, 0.01);
                }
            }
        }
    }
}
