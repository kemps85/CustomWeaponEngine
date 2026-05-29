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

public class GiantsSwordListener implements Listener {
    private final NamespacedKey idKey;

    public GiantsSwordListener(CustomWeaponEngine plugin) {
        this.idKey = new NamespacedKey(plugin, "cwe_id");
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (event.getItem() == null || event.getItem().getItemMeta() == null) return;
        
        String id = event.getItem().getItemMeta().getPersistentDataContainer().get(idKey, PersistentDataType.STRING);
        if ("cwe_giants_sword".equalsIgnoreCase(id)) {
            Player p = event.getPlayer();

            // Thêm tiêu hao Mana (100) cho Giant's Slam
            if (!ManaHelper.consumeMana(p, 100.0, (CustomWeaponEngine) org.bukkit.plugin.java.JavaPlugin.getPlugin(CustomWeaponEngine.class), "Giant's Slam")) {
                return;
            }

            p.playSound(p.getLocation(), Sound.ENTITY_IRON_GOLEM_DEATH, 1.0f, 0.5f);
            p.getWorld().spawnParticle(Particle.EXPLOSION_EMITTER, p.getLocation(), 2);
            p.getWorld().spawnParticle(Particle.BLOCK, p.getLocation(), 100, 4, 0.1, 4, 1, org.bukkit.Material.DIRT.createBlockData());
            
            for (Entity e : p.getNearbyEntities(8, 4, 8)) {
                if (e instanceof LivingEntity && e != p) {
                    ((LivingEntity) e).setNoDamageTicks(0);
                    ((LivingEntity) e).damage(250, p);
                }
            }
        }
    }
}
