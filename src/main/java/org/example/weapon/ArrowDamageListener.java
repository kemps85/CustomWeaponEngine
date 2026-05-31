package org.example.weapon;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Arrow;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.example.core.CustomWeaponEngine;

public class ArrowDamageListener implements Listener {

    private final CustomWeaponEngine plugin;

    public ArrowDamageListener(CustomWeaponEngine plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onShootVanillaBow(EntityShootBowEvent event) {
        if (!(event.getProjectile() instanceof Arrow)) return;
        ItemStack bow = event.getBow();
        if (bow == null || !bow.hasItemMeta()) return;

        PersistentDataContainer pdc = bow.getItemMeta().getPersistentDataContainer();
        double dmg = pdc.getOrDefault(new NamespacedKey(plugin, "stat_damage"), PersistentDataType.DOUBLE, 0.0);
        if (dmg == 0) {
            dmg = pdc.getOrDefault(new NamespacedKey(plugin, "cwe_damage"), PersistentDataType.DOUBLE, 0.0);
        }

        if (dmg > 0) {
            event.getProjectile().setMetadata("cwe_base_damage", new FixedMetadataValue(plugin, dmg));
        }
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onArrowHit(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Arrow) {
            Arrow arrow = (Arrow) event.getDamager();
            if (arrow.hasMetadata("cwe_base_damage")) {
                double customDmg = arrow.getMetadata("cwe_base_damage").get(0).asDouble();
                // Add the custom bow damage to the vanilla arrow damage
                event.setDamage(event.getDamage() + customDmg);
            }
        }
    }
}
