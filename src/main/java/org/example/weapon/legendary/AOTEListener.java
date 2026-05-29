package org.example.weapon.legendary;

import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.persistence.PersistentDataType;
import org.example.core.CustomWeaponEngine;

import java.util.Set;

public class AOTEListener implements Listener {
    private final NamespacedKey idKey;

    public AOTEListener(CustomWeaponEngine plugin) {
        this.idKey = new NamespacedKey(plugin, "cwe_id");
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (event.getItem() == null || event.getItem().getItemMeta() == null) return;
        
        String id = event.getItem().getItemMeta().getPersistentDataContainer().get(idKey, PersistentDataType.STRING);
        if ("cwe_aote".equalsIgnoreCase(id)) {
            Player p = event.getPlayer();
            
            // Check mana cost (50 mana for AOTE teleport)
            if (!ManaHelper.consumeMana(p, 50.0, (CustomWeaponEngine) org.bukkit.plugin.java.JavaPlugin.getPlugin(CustomWeaponEngine.class), "Instant Transmission")) {
                return;
            }
            
            boolean isStrong = true;
            org.bukkit.inventory.ItemStack[] armors = p.getInventory().getArmorContents();
            for (int i = 0; i < 4; i++) {
                org.bukkit.inventory.ItemStack armor = armors[i];
                if (armor == null || !armor.hasItemMeta()) { isStrong = false; break; }
                String aid = armor.getItemMeta().getPersistentDataContainer().get(idKey, PersistentDataType.STRING);
                if (aid == null || !aid.startsWith("cwe_strong_")) { isStrong = false; break; }
            }
            int tpDistance = isStrong ? 10 : 8;
            
            Block target = p.getTargetBlock(Set.of(org.bukkit.Material.AIR, org.bukkit.Material.CAVE_AIR, org.bukkit.Material.VOID_AIR), tpDistance);
            Location loc = target.getLocation().clone();
            loc.setPitch(p.getLocation().getPitch());
            loc.setYaw(p.getLocation().getYaw());
            loc.add(0.5, 1, 0.5); // Center and adjust for player height
            
            p.teleport(loc);
            p.playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
        }
    }
}
