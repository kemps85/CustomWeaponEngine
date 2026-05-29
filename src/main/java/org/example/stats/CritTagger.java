package org.example.stats;

import dev.aurelium.auraskills.api.AuraSkillsApi;
import dev.aurelium.auraskills.api.stat.Stats;
import dev.aurelium.auraskills.api.user.SkillsUser;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Random;

/**
 * Tính xác suất chí mạng (Crit Chance) theo AuraSkills stats
 * và gắn metadata "cwe_is_crit" lên entity bị đánh.
 * DamageIndicator đọc metadata này để hiển thị màu đúng.
 */
public class CritTagger implements Listener {

    private final JavaPlugin plugin;
    private final Random random = new Random();

    public CritTagger(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onDamage(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof LivingEntity)) return;

        Player attacker = null;
        if (event.getDamager() instanceof Player) {
            attacker = (Player) event.getDamager();
        } else if (event.getDamager() instanceof org.bukkit.entity.Projectile) {
            org.bukkit.entity.Projectile proj = (org.bukkit.entity.Projectile) event.getDamager();
            if (proj.getShooter() instanceof Player) {
                attacker = (Player) proj.getShooter();
            }
        }

        if (attacker == null) return;

        LivingEntity victim = (LivingEntity) event.getEntity();

        // Tính crit chance: base 20% + AuraSkills CRIT_CHANCE stat
        double critChance = 20.0;
        boolean auraEnabled = Bukkit.getPluginManager().isPluginEnabled("AuraSkills");
        if (auraEnabled) {
            SkillsUser user = AuraSkillsApi.get().getUser(attacker.getUniqueId());
            if (user != null) {
                critChance = user.getStatLevel(Stats.CRIT_CHANCE);
            }
        }

        // Cap crit chance tại 100%
        critChance = Math.min(critChance, 100.0);

        boolean isCrit = random.nextDouble() * 100.0 < critChance;

        // Gắn metadata để DamageIndicator đọc
        if (isCrit) {
            victim.setMetadata("cwe_is_crit", new FixedMetadataValue(plugin, true));
            // Tự xóa metadata sau 1 tick để không bị stale
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                victim.removeMetadata("cwe_is_crit", plugin);
            }, 2L);
        } else {
            victim.removeMetadata("cwe_is_crit", plugin);
        }
    }
}
