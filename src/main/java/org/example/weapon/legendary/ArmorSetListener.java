package org.example.weapon.legendary;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.entity.LivingEntity;
import org.example.core.CustomWeaponEngine;
import org.bukkit.World.Environment;

public class ArmorSetListener implements Listener {
    private final NamespacedKey idKey;

    public ArmorSetListener(CustomWeaponEngine plugin) {
        this.idKey = new NamespacedKey(plugin, "cwe_id");
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
        
        return h != null && h.equals(setPrefix + "_helmet") &&
               c != null && c.equals(setPrefix + "_chestplate") &&
               l != null && l.equals(setPrefix + "_leggings") &&
               b != null && b.equals(setPrefix + "_boots");
    }

    @EventHandler
    public void onDamageTake(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        Player p = (Player) event.getEntity();
        
        if (isWearingFullSet(p, "cwe_ender")) {
            if (event.getDamager() instanceof org.bukkit.entity.Enderman) {
                event.setDamage(event.getDamage() * 0.5); // Giảm 50% sát thương từ Enderman
                p.sendMessage("§d[CWE] Giáp Ender đã chặn bớt sát thương từ Enderman!");
            }
        }
    }
    
    @EventHandler
    public void onWitherbornHit(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof LivingEntity)) return;
        if (event.getDamager() instanceof org.bukkit.entity.WitherSkull) {
            org.bukkit.entity.WitherSkull skull = (org.bukkit.entity.WitherSkull) event.getDamager();
            NamespacedKey wKey = new NamespacedKey(org.bukkit.plugin.java.JavaPlugin.getPlugin(CustomWeaponEngine.class), "witherborn_damage");
            if (skull.getPersistentDataContainer().has(wKey, PersistentDataType.DOUBLE)) {
                double customDmg = skull.getPersistentDataContainer().get(wKey, PersistentDataType.DOUBLE);
                event.setDamage(customDmg);
                
                // Show damage indicator if needed, but the custom damage indicator plugin will pick it up since the shooter is a Player
            }
        }
    }
}
