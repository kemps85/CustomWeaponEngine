package org.example.weapon.legendary;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.example.core.CustomWeaponEngine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DragonTask extends BukkitRunnable {
    private final CustomWeaponEngine plugin;
    private final NamespacedKey idKey;
    private int ticks = 0;

    public DragonTask(CustomWeaponEngine plugin) {
        this.plugin = plugin;
        this.idKey = new NamespacedKey(plugin, "cwe_id");
    }

    private String getArmorId(ItemStack item) {
        if (item == null || item.getItemMeta() == null) return null;
        return item.getItemMeta().getPersistentDataContainer().get(idKey, PersistentDataType.STRING);
    }

    private String getDragonSetPrefix(Player p) {
        String h = getArmorId(p.getInventory().getHelmet());
        String c = getArmorId(p.getInventory().getChestplate());
        String l = getArmorId(p.getInventory().getLeggings());
        String b = getArmorId(p.getInventory().getBoots());
        
        if (h == null || c == null || l == null || b == null) return null;
        if (h.startsWith("holy_") && c.startsWith("holy_") && l.startsWith("holy_") && b.startsWith("holy_")) return "holy";
        if (h.startsWith("unstable_") && c.startsWith("unstable_") && l.startsWith("unstable_") && b.startsWith("unstable_")) return "unstable";
        
        return null;
    }

    @Override
    public void run() {
        ticks++;
        for (Player p : Bukkit.getOnlinePlayers()) {
            String setPrefix = getDragonSetPrefix(p);
            if (setPrefix == null) continue;
            
            if (setPrefix.equals("holy") && ticks % 2 == 0) { // Every 2 seconds
                // Heal self and allies
                double maxHp = p.getAttribute(org.bukkit.attribute.Attribute.GENERIC_MAX_HEALTH).getValue();
                double healAmount = maxHp * 0.03; // 3% max HP
                
                p.setHealth(Math.min(maxHp, p.getHealth() + healAmount));
                for (Entity e : p.getNearbyEntities(6, 4, 6)) {
                    if (e instanceof Player) {
                        Player ally = (Player) e;
                        double aMaxHp = ally.getAttribute(org.bukkit.attribute.Attribute.GENERIC_MAX_HEALTH).getValue();
                        ally.setHealth(Math.min(aMaxHp, ally.getHealth() + healAmount));
                    }
                }
            }
            
            if (setPrefix.equals("unstable") && ticks % 10 == 0) { // Every 10 seconds
                List<LivingEntity> targets = new ArrayList<>();
                for (Entity e : p.getNearbyEntities(8, 4, 8)) {
                    if (e instanceof LivingEntity && e != p && !(e instanceof org.bukkit.entity.ArmorStand) && !(e instanceof Player)) {
                        if (((LivingEntity)e).isValid() && !e.isDead()) {
                            targets.add((LivingEntity) e);
                        }
                    }
                }
                
                if (!targets.isEmpty()) {
                    Collections.shuffle(targets);
                    int strikeCount = Math.min(3, targets.size());
                    for (int i = 0; i < strikeCount; i++) {
                        LivingEntity target = targets.get(i);
                        target.getWorld().strikeLightningEffect(target.getLocation());
                        // Calculate damage based on crit damage
                        double cdmg = 500;
                        try {
                            if (Bukkit.getPluginManager().isPluginEnabled("AuraSkills")) {
                                dev.aurelium.auraskills.api.user.SkillsUser u = dev.aurelium.auraskills.api.AuraSkillsApi.get().getUser(p.getUniqueId());
                                if (u != null) cdmg = u.getStatLevel(dev.aurelium.auraskills.api.stat.Stats.CRIT_DAMAGE);
                            }
                        } catch (Exception ex) {}
                        
                        target.damage(150 + (cdmg * 0.5), p);
                    }
                }
            }
        }
    }
}
