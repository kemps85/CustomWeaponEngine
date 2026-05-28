package org.example.enchant;

import org.example.core.CustomWeaponEngine;
import org.example.enchant.UltimateEnchant;
import org.example.enchant.EnchantManager;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

public class UltimateSystemListener implements Listener {
    private final CustomWeaponEngine plugin;
    private final EnchantManager enchantManager;

    public UltimateSystemListener(CustomWeaponEngine plugin, EnchantManager enchantManager) {
        this.plugin = plugin;
        this.enchantManager = enchantManager;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onWeaponDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) return;
        Player player = (Player) event.getDamager();
        ItemStack weapon = player.getInventory().getItemInMainHand();

        if (weapon == null || !weapon.hasItemMeta()) return;

        double damage = event.getDamage();

        // ONE FOR ALL: +210% base damage
        if (enchantManager.hasUltimateEnchant(weapon, UltimateEnchant.ONE_FOR_ALL)) {
            damage *= 3.1;
        }

        // SWARM: +2% damage per nearby entity per level
        int swarmLvl = enchantManager.getUltimateEnchantLevel(weapon, UltimateEnchant.SWARM);
        if (swarmLvl > 0) {
            int entities = player.getNearbyEntities(10, 10, 10).size();
            damage *= (1 + (entities * 0.02 * swarmLvl));
        }

        // COMBO: +10% damage per level temporarily on consecutive hits (simplified)
        int comboLvl = enchantManager.getUltimateEnchantLevel(weapon, UltimateEnchant.COMBO);
        if (comboLvl > 0) {
            damage *= (1 + (0.10 * comboLvl));
        }

        // INFERNO: Chance to ignite
        int infernoLvl = enchantManager.getUltimateEnchantLevel(weapon, UltimateEnchant.INFERNO);
        if (infernoLvl > 0 && Math.random() < (0.1 * infernoLvl)) {
            event.getEntity().setFireTicks(100);
            damage += 50 * infernoLvl;
        }

        event.setDamage(damage);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onArmorHit(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();
        
        int lastStandLvl = getTotalUltimateLevel(player, UltimateEnchant.LAST_STAND);
        if (lastStandLvl > 0) {
            if (player.getHealth() <= player.getMaxHealth() / 2) {
                // Reduces damage taken
                double reduction = Math.min(0.9, lastStandLvl * 0.05);
                event.setDamage(event.getDamage() * (1 - reduction));
            }
        }

        int habaneroLvl = getTotalUltimateLevel(player, UltimateEnchant.HABANERO_TACTICS);
        if (habaneroLvl > 0) {
            if (event.getDamager() instanceof LivingEntity) {
                ((LivingEntity) event.getDamager()).damage(event.getDamage() * (0.1 * habaneroLvl), player);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        
        // Mất 50% tiền nếu không có Bank enchant
        try {
            net.milkbowl.vault.economy.Economy econ = CustomWeaponEngine.getEconomy();
            if (econ != null) {
                double balance = econ.getBalance(player);
                double lost = balance * 0.5;
                
                int bankLvl = getTotalUltimateLevel(player, UltimateEnchant.BANK);
                if (bankLvl > 0) {
                    // Bank saves 10% per level (up to 50% saved if 5 pieces? Max level is 5, total max could be 20)
                    // If bankLvl == 5, saves 50% of the lost amount.
                    double saveRatio = Math.min(1.0, bankLvl * 0.1);
                    lost = lost * (1 - saveRatio);
                }
                
                if (lost > 0) {
                    econ.withdrawPlayer(player, lost);
                    player.sendMessage("§cBạn đã chết và mất §e" + String.format("%.1f", lost) + " coins§c!");
                }
            }
        } catch (Exception ignored) {
        }
    }

    private int getTotalUltimateLevel(Player player, UltimateEnchant enchant) {
        int total = 0;
        for (ItemStack armor : player.getInventory().getArmorContents()) {
            if (armor != null && enchantManager.hasUltimateEnchant(armor, enchant)) {
                total += enchantManager.getUltimateEnchantLevel(armor, enchant);
            }
        }
        return total;
    }
}
