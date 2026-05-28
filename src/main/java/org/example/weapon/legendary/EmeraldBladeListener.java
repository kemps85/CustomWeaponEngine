package org.example.weapon.legendary;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.persistence.PersistentDataType;
import org.example.core.CustomWeaponEngine;

public class EmeraldBladeListener implements Listener {
    private final NamespacedKey idKey;

    public EmeraldBladeListener(CustomWeaponEngine plugin) {
        this.idKey = new NamespacedKey(plugin, "cwe_id");
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player) || !(event.getEntity() instanceof LivingEntity)) return;
        Player p = (Player) event.getDamager();
        if (p.getInventory().getItemInMainHand().getItemMeta() == null) return;

        String id = p.getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer().get(idKey, PersistentDataType.STRING);
        if ("cwe_emerald_blade".equalsIgnoreCase(id)) {
            Economy econ = CustomWeaponEngine.getEconomy();
            if (econ != null) {
                double bal = econ.getBalance(p);
                // Emerald Blade scaling formula roughly: 2.5 * bal^(1/4)
                if (bal > 0) {
                    double bonus = 2.5 * Math.pow(bal, 0.25);
                    event.setDamage(event.getDamage() + bonus);
                }
            }
        }
    }
}
