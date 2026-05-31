package org.example.weapon.legendary;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
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

import java.util.ArrayList;
import java.util.List;

public class ShadowFuryListener implements Listener {
    private final NamespacedKey idKey;
    private final CustomWeaponEngine plugin;

    public ShadowFuryListener(CustomWeaponEngine plugin) {
        this.plugin = plugin;
        this.idKey = new NamespacedKey(plugin, "cwe_id");
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getHand() != org.bukkit.inventory.EquipmentSlot.HAND) return;
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (event.getItem() == null || event.getItem().getItemMeta() == null) return;
        
        String id = event.getItem().getItemMeta().getPersistentDataContainer().get(idKey, PersistentDataType.STRING);
        if ("cwe_shadow_fury".equalsIgnoreCase(id)) {
            Player p = event.getPlayer();
            
            List<LivingEntity> targets = new ArrayList<>();
            for (Entity e : p.getNearbyEntities(12, 6, 12)) {
                if (e instanceof LivingEntity && e != p && !(e instanceof org.bukkit.entity.Player)) {
                    targets.add((LivingEntity) e);
                    if (targets.size() >= 5) break;
                }
            }
            
            if (targets.isEmpty()) {
                p.sendMessage("§cKhông có mục tiêu xung quanh!");
                return;
            }
            
            executeShadowFury(p, targets, 0);
        }
    }
    
    private void executeShadowFury(Player p, List<LivingEntity> targets, int index) {
        if (index >= targets.size()) return;
        LivingEntity target = targets.get(index);
        if (target.isDead()) {
            executeShadowFury(p, targets, index + 1);
            return;
        }
        
        Location back = target.getLocation().clone().subtract(target.getLocation().getDirection().multiply(1.5));
        back.setYaw(target.getLocation().getYaw()); // Face the back
        p.teleport(back);
        p.playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
        target.damage(600, p);
        
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            executeShadowFury(p, targets, index + 1);
        }, 4L); // 4 ticks between hits
    }
}
