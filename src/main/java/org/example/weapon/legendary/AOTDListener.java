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
import org.example.core.CustomWeaponEngine;

public class AOTDListener implements Listener {
    private final NamespacedKey idKey;

    public AOTDListener(CustomWeaponEngine plugin) {
        this.idKey = new NamespacedKey(plugin, "cwe_id");
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (event.getItem() == null || event.getItem().getItemMeta() == null) return;
        
        String id = event.getItem().getItemMeta().getPersistentDataContainer().get(idKey, PersistentDataType.STRING);
        if ("cwe_aotd".equalsIgnoreCase(id)) {
            Player p = event.getPlayer();
            p.playSound(p.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1.0f, 1.0f);
            p.getWorld().spawnParticle(Particle.FLAME, p.getLocation().add(0, 1, 0), 100, 2, 1, 2, 0.1);
            
            for (Entity e : p.getNearbyEntities(6, 4, 6)) {
                if (e instanceof LivingEntity && e != p) {
                    // Knockback and damage
                    LivingEntity target = (LivingEntity) e;
                    org.bukkit.util.Vector dir = target.getLocation().toVector().subtract(p.getLocation().toVector()).normalize();
                    target.setVelocity(dir.multiply(2.5).setY(1.2));
                    target.damage(12000, p);
                }
            }
        }
    }
}
