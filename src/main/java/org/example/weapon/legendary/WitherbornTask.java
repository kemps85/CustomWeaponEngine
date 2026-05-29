package org.example.weapon.legendary;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.WitherSkull;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.example.core.CustomWeaponEngine;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class WitherbornTask extends BukkitRunnable {
    private final CustomWeaponEngine plugin;
    private final NamespacedKey idKey;

    private final Map<UUID, Double> angles = new HashMap<>();
    private final Map<UUID, Long> lastFired = new HashMap<>();

    public WitherbornTask(CustomWeaponEngine plugin) {
        this.plugin = plugin;
        this.idKey = new NamespacedKey(plugin, "cwe_id");
    }

    private String getArmorId(ItemStack item) {
        if (item == null || item.getItemMeta() == null) return null;
        return item.getItemMeta().getPersistentDataContainer().get(idKey, PersistentDataType.STRING);
    }

    private String getWitherSetPrefix(Player p) {
        String h = getArmorId(p.getInventory().getHelmet());
        String c = getArmorId(p.getInventory().getChestplate());
        String l = getArmorId(p.getInventory().getLeggings());
        String b = getArmorId(p.getInventory().getBoots());
        
        if (h == null || c == null || l == null || b == null) return null;
        
        if (h.startsWith("necron_") && c.startsWith("necron_") && l.startsWith("necron_") && b.startsWith("necron_")) return "necron";
        if (h.startsWith("storm_") && c.startsWith("storm_") && l.startsWith("storm_") && b.startsWith("storm_")) return "storm";
        if (h.startsWith("maxor_") && c.startsWith("maxor_") && l.startsWith("maxor_") && b.startsWith("maxor_")) return "maxor";
        if (h.startsWith("goldor_") && c.startsWith("goldor_") && l.startsWith("goldor_") && b.startsWith("goldor_")) return "goldor";
        
        return null;
    }

    @Override
    public void run() {
        long now = System.currentTimeMillis();
        for (Player p : Bukkit.getOnlinePlayers()) {
            String setPrefix = getWitherSetPrefix(p);
            if (setPrefix != null) {
                // Witherborn is active
                double angle = angles.getOrDefault(p.getUniqueId(), 0.0);
                angle += 0.3; // Rotate around player
                angles.put(p.getUniqueId(), angle);
                
                // Draw particle (Wither skull shape approximately, or just smoke)
                double x = p.getLocation().getX() + 1.2 * Math.cos(angle);
                double y = p.getLocation().getY() + 1.5;
                double z = p.getLocation().getZ() + 1.2 * Math.sin(angle);
                Location particleLoc = new Location(p.getWorld(), x, y, z);
                
                p.getWorld().spawnParticle(Particle.SMOKE, particleLoc, 2, 0, 0, 0, 0);
                p.getWorld().spawnParticle(Particle.WITCH, particleLoc, 1, 0, 0, 0, 0);
                
                // Fire skull every 3 seconds
                long lastFire = lastFired.getOrDefault(p.getUniqueId(), 0L);
                if (now - lastFire >= 3000) {
                    LivingEntity target = findTarget(p);
                    if (target != null) {
                        lastFired.put(p.getUniqueId(), now);
                        fireWitherSkull(p, particleLoc, target, setPrefix);
                    }
                }
            } else {
                angles.remove(p.getUniqueId());
            }
        }
    }

    private LivingEntity findTarget(Player p) {
        LivingEntity best = null;
        double minDist = 100.0;
        for (Entity e : p.getNearbyEntities(10, 5, 10)) {
            if (e instanceof LivingEntity && e != p) {
                if (e instanceof org.bukkit.entity.ArmorStand) continue;
                if (e.isDead() || !((LivingEntity) e).isValid()) continue;
                double dist = e.getLocation().distanceSquared(p.getLocation());
                if (dist < minDist) {
                    minDist = dist;
                    best = (LivingEntity) e;
                }
            }
        }
        return best;
    }

    private void fireWitherSkull(Player p, Location origin, LivingEntity target, String setPrefix) {
        p.playSound(origin, Sound.ENTITY_WITHER_SHOOT, 0.5f, 1.2f);
        
        org.bukkit.util.Vector dir = target.getLocation().add(0, target.getHeight()/2, 0).toVector().subtract(origin.toVector()).normalize();
        
        WitherSkull skull = origin.getWorld().spawn(origin, WitherSkull.class);
        skull.setShooter(p);
        skull.setDirection(dir.multiply(1.5));
        skull.setYield(0); // No block breaking
        
        // Tính sát thương
        double statValue = 0;
        try {
            if (Bukkit.getPluginManager().isPluginEnabled("AuraSkills")) {
                dev.aurelium.auraskills.api.user.SkillsUser u = dev.aurelium.auraskills.api.AuraSkillsApi.get().getUser(p.getUniqueId());
                if (u != null) {
                    if (setPrefix.equals("necron")) statValue = u.getStatLevel(dev.aurelium.auraskills.api.stat.Stats.STRENGTH);
                    else if (setPrefix.equals("storm")) statValue = u.getStatLevel(dev.aurelium.auraskills.api.stat.Stats.WISDOM);
                    else if (setPrefix.equals("goldor")) statValue = u.getStatLevel(dev.aurelium.auraskills.api.stat.Stats.HEALTH);
                    else if (setPrefix.equals("maxor")) statValue = u.getStatLevel(dev.aurelium.auraskills.api.stat.Stats.CRIT_DAMAGE);
                }
            }
        } catch (Exception e) {}

        // NERF: Base 50 + 0.3*stat (thay vì 200 + 2*stat)
        // Ở stat ~900: 50 + 270 = 320 dmg (thay vì 2000)
        double damage = 50 + (statValue * 0.3);

        skull.getPersistentDataContainer().set(new NamespacedKey(plugin, "witherborn_damage"), PersistentDataType.DOUBLE, damage);
        // Đánh dấu target có phải Player không để ArmorSetListener áp dụng PvP reduction
        boolean isTargetPlayer = (target instanceof Player);
        skull.getPersistentDataContainer().set(new NamespacedKey(plugin, "witherborn_pvp"), PersistentDataType.INTEGER, isTargetPlayer ? 1 : 0);
    }
}
