package org.example.weapon.legendary;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.persistence.PersistentDataType;
import org.example.core.CustomWeaponEngine;

public class LividDaggerListener implements Listener {
    private final NamespacedKey idKey;

    public LividDaggerListener(CustomWeaponEngine plugin) {
        this.idKey = new NamespacedKey(plugin, "cwe_id");
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player) || !(event.getEntity() instanceof LivingEntity)) return;
        Player p = (Player) event.getDamager();
        if (p.getInventory().getItemInMainHand().getItemMeta() == null) return;

        String id = p.getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer().get(idKey, PersistentDataType.STRING);
        if ("cwe_livid_dagger".equalsIgnoreCase(id)) {
            // Check backstab
            LivingEntity target = (LivingEntity) event.getEntity();
            double pYaw = p.getLocation().getYaw();
            double tYaw = target.getLocation().getYaw();
            
            // Normalize yaw
            pYaw = (pYaw % 360 + 360) % 360;
            tYaw = (tYaw % 360 + 360) % 360;
            
            double diff = Math.abs(pYaw - tYaw);
            if (diff <= 60 || diff >= 300) {
                // Hitting from behind!
                event.setDamage(event.getDamage() * 2.0);
                p.sendMessage("§c[CWE] Đâm Lén Tăng x2 Sát Thương!");
            }
        }
    }
}
