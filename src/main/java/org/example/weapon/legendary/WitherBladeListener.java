package org.example.weapon.legendary;

import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.persistence.PersistentDataType;
import org.example.core.CustomWeaponEngine;

import java.util.Set;

public class WitherBladeListener implements Listener {
    private final NamespacedKey idKey;

    public WitherBladeListener(CustomWeaponEngine plugin) {
        this.idKey = new NamespacedKey(plugin, "cwe_id");
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getHand() != org.bukkit.inventory.EquipmentSlot.HAND) return;
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (event.getItem() == null || event.getItem().getItemMeta() == null) return;
        
        String id = event.getItem().getItemMeta().getPersistentDataContainer().get(idKey, PersistentDataType.STRING);
        if (id == null) return;
        
        if (id.equals("cwe_hyperion") || id.equals("cwe_valkyrie") || id.equals("cwe_scylla") || id.equals("cwe_astraea")) {
            Player p = event.getPlayer();
            
            // Check mana cost (250 mana for Wither Impact)
            if (!ManaHelper.consumeMana(p, 250.0, (CustomWeaponEngine) org.bukkit.plugin.java.JavaPlugin.getPlugin(CustomWeaponEngine.class), "Wither Impact")) {
                return;
            }
            
            // 1. Wither Impact - Teleport 10 blocks
            Block target = p.getTargetBlock(Set.of(org.bukkit.Material.AIR, org.bukkit.Material.CAVE_AIR, org.bukkit.Material.VOID_AIR), 10);
            Location loc = target.getLocation().clone();
            loc.setPitch(p.getLocation().getPitch());
            loc.setYaw(p.getLocation().getYaw());
            loc.add(0.5, 1, 0.5);
            p.teleport(loc);
            
            // 2. Implosion
            p.playSound(p.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1.0f, 1.0f);
            p.getWorld().spawnParticle(Particle.EXPLOSION, p.getLocation(), 1);
            
            for (Entity e : p.getNearbyEntities(10, 5, 10)) {
                if (e instanceof LivingEntity && e != p && !(e instanceof org.bukkit.entity.Player)) {
                    ((LivingEntity) e).damage(500, p);
                }
            }
            
            // 3. Wither Shield - Heal 10% max health
            double maxHealth = p.getAttribute(org.bukkit.attribute.Attribute.GENERIC_MAX_HEALTH).getValue();
            double heal = maxHealth * 0.10;
            p.setHealth(Math.min(maxHealth, p.getHealth() + heal));
        }
    }
}
