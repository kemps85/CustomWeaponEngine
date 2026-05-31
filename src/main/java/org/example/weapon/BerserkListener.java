package org.example.weapon;

import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
import org.bukkit.Bukkit;
import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
import org.bukkit.ChatColor;
import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
import org.bukkit.Particle;
import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
import org.bukkit.Sound;
import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
import org.bukkit.attribute.Attribute;
import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
import org.bukkit.attribute.AttributeInstance;
import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
import org.bukkit.entity.LivingEntity;
import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
import org.bukkit.entity.Player;
import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
import org.bukkit.event.EventHandler;
import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
import org.bukkit.event.Listener;
import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
import org.bukkit.event.entity.EntityDamageEvent; // 🟩 ĐỔI SANG THƯ VIỆN SÁT THƯƠNG CHUNG
import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
import org.bukkit.inventory.ItemStack;

import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
import java.util.HashMap;
import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
import java.util.UUID;

import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
public class BerserkListener implements Listener {

    private final HashMap<UUID, Long> rageCooldowns = new HashMap<>();

    public void clearCache() {
        this.rageCooldowns.clear();
    }

    private int countBerserkPieces(Player player) {
        int count = 0;
        FileConfiguration config = CustomWeaponEngine.getInstance().getConfig();
        String configName = config.getString("sets.berserk.display-name", "Berserk");
        String cleanConfigName = ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', configName)).toLowerCase();
        
        ItemStack[] armor = player.getInventory().getArmorContents();
        for (ItemStack item : armor) {
            if (item != null && item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
                String cleanPlayerItemName = ChatColor.stripColor(item.getItemMeta().getDisplayName()).toLowerCase();
                if (cleanPlayerItemName.contains(cleanConfigName)) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * 🛡️ NỘI TẠI 1: HÓA ĐIÊN KHI XUỐNG MÁU THẤP (CHẤP MỌI NGUỒN SÁT THƯƠNG / LỆNH)
     */
    @EventHandler
    public void onBerserkTakeDamage(EntityDamageEvent event) { // 🟩 ĐỔI THÀNH EntityDamageEvent
        if (!(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();

        int pieces = countBerserkPieces(player);
        AttributeInstance maxHealthAttr = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        double maxHealth = maxHealthAttr != null ? maxHealthAttr.getValue() : 20.0;
        double predictedHealth = player.getHealth() - event.getFinalDamage();
        double healthPercent = (predictedHealth / maxHealth);

        if (pieces < 4) return; 

        // Ngưỡng kích hoạt: Dưới 35% máu tối đa
        if (predictedHealth > 0 && healthPercent <= 0.35) {
            long currentTime = System.currentTimeMillis();
            UUID uuid = player.getUniqueId();
            
            if (rageCooldowns.containsKey(uuid) && (currentTime - rageCooldowns.get(uuid) < 40000L)) {
                return;
            }

            // Ép hệ lệnh gốc vanilla
            String playerName = player.getName();
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "minecraft:effect give " + playerName + " minecraft:strength 15 1");
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "minecraft:effect give " + playerName + " minecraft:speed 15 1");
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "minecraft:effect give " + playerName + " minecraft:resistance 15 0");

            // Hiệu ứng gầm rú
            player.getWorld().playSound(player.getLocation(), Sound.ENTITY_WARDEN_ROAR, 1.2f, 0.8f);
            player.getWorld().spawnParticle(Particle.ANGRY_VILLAGER, player.getLocation().add(0, 1.5, 0), 15, 0.3, 0.3, 0.3, 0.0);
            player.getWorld().spawnParticle(Particle.FLAME, player.getLocation(), 20, 0.4, 0.2, 0.4, 0.1);
            
            player.sendMessage("§c§l[Berserk] §4§lRAGE MODE ACTIVATED! §cEffects applied.");
            
            rageCooldowns.put(uuid, currentTime);
        }
    }

    /**
     * ⚔️ NỘI TẠI 2: CÀNG THẤP MÁU ĐÁNH CÀNG ĐAU (GIỮ NGUYÊN VÌ PHẢI ĐI ĐÁNH ĐẤU)
     */
    @EventHandler
    public void onBerserkDealDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player) || !(event.getEntity() instanceof LivingEntity)) return;
        Player player = (Player) event.getDamager();
        LivingEntity target = (LivingEntity) event.getEntity();

        if (countBerserkPieces(player) < 4) return;

        AttributeInstance maxHealthAttr = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        if (maxHealthAttr == null) return;

        double maxHealth = maxHealthAttr.getValue();
        double currentHealth = player.getHealth();
        double missingHealthPercent = (maxHealth - currentHealth) / maxHealth; 

        if (missingHealthPercent > 0) {
            double bonusMultiplier = 1.0 + (missingHealthPercent * 0.8);
            event.setDamage(event.getDamage() * bonusMultiplier);

            target.getWorld().spawnParticle(Particle.DAMAGE_INDICATOR, target.getLocation().add(0, 1, 0), 4, 0.1, 0.1, 0.1, 0.1);
            
            int bonusPercent = (int) (missingHealthPercent * 0.8 * 100);
            if (bonusPercent > 5) {
                player.sendActionBar("§4§l⚔️ Berserk Damage Buff: +" + bonusPercent + "%");
            }
        }
    }
}
