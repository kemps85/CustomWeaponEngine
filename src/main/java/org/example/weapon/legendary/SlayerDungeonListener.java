package org.example.weapon.legendary;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.example.core.CustomWeaponEngine;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SlayerDungeonListener implements Listener {
    private final CustomWeaponEngine plugin;
    private final NamespacedKey idKey;
    private final Map<UUID, Integer> spiderHits = new HashMap<>();

    public SlayerDungeonListener(CustomWeaponEngine plugin) {
        this.plugin = plugin;
        this.idKey = new NamespacedKey(plugin, "cwe_id");
        
        // Task to allow flight for double jump
        Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (p.getGameMode() == org.bukkit.GameMode.CREATIVE || p.getGameMode() == org.bukkit.GameMode.SPECTATOR) continue;
                if (isWearingFullSet(p, "tarantula")) {
                    p.setAllowFlight(true);
                } else {
                    p.setAllowFlight(false);
                }
            }
        }, 20L, 20L);
    }

    private String getArmorId(ItemStack item) {
        if (item == null || item.getItemMeta() == null) return null;
        return item.getItemMeta().getPersistentDataContainer().get(idKey, PersistentDataType.STRING);
    }

    private boolean isWearingFullSet(Player p, String setPrefix) {
        String h = getArmorId(p.getInventory().getHelmet());
        String c = getArmorId(p.getInventory().getChestplate());
        String l = getArmorId(p.getInventory().getLeggings());
        String b = getArmorId(p.getInventory().getBoots());
        
        return h != null && h.startsWith(setPrefix) &&
               c != null && c.startsWith(setPrefix) &&
               l != null && l.startsWith(setPrefix) &&
               b != null && b.startsWith(setPrefix);
    }

    @EventHandler
    public void onDoubleJump(PlayerToggleFlightEvent event) {
        Player p = event.getPlayer();
        if (p.getGameMode() == org.bukkit.GameMode.CREATIVE || p.getGameMode() == org.bukkit.GameMode.SPECTATOR) return;
        
        if (isWearingFullSet(p, "tarantula")) {
            event.setCancelled(true);
            p.setAllowFlight(false);
            p.setFlying(false);
            
            if (ManaHelper.consumeMana(p, 40.0, plugin, "Slayer Ability")) {
                org.bukkit.util.Vector v = p.getLocation().getDirection().multiply(1.2).setY(0.8);
                p.setVelocity(v);
                p.playSound(p.getLocation(), Sound.ENTITY_SPIDER_STEP, 1.0f, 1.0f);
            }
        }
    }

    @EventHandler
    public void onDamageByEntity(EntityDamageByEntityEvent event) {
        // Octodexterity (Tarantula)
        if (event.getDamager() instanceof Player && event.getEntity() instanceof LivingEntity) {
            Player p = (Player) event.getDamager();
            if (isWearingFullSet(p, "tarantula")) {
                int hits = spiderHits.getOrDefault(p.getUniqueId(), 0) + 1;
                if (hits >= 4) {
                    event.setDamage(event.getDamage() * 2.0);
                    p.playSound(p.getLocation(), Sound.ENTITY_ZOMBIE_BREAK_WOODEN_DOOR, 0.5f, 2.0f);
                    hits = 0;
                }
                spiderHits.put(p.getUniqueId(), hits);
            }
        }
        
        // Werewolf Armor
        if (event.getEntity() instanceof Player && event.getDamager() instanceof LivingEntity) {
            Player p = (Player) event.getEntity();
            if (isWearingFullSet(p, "werewolf")) {
                if (Math.random() <= 0.20) { // 20% chance
                    LivingEntity attacker = (LivingEntity) event.getDamager();
                    attacker.addPotionEffect(new PotionEffect(PotionEffectType.NAUSEA, 100, 1));
                    attacker.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 100, 1));
                    p.playSound(p.getLocation(), Sound.ENTITY_WOLF_GROWL, 1.0f, 1.0f);
                }
            }
        }
    }
    
    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity().getKiller() != null) {
            Player p = event.getEntity().getKiller();
            // Golem Iron Resolve
        if (isWearingFullSet(p, "golem")) {
                p.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 200, 1));
            }
        }
    }
}
