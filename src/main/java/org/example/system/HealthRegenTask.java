package org.example.system;

import dev.aurelium.auraskills.api.AuraSkillsApi;
import dev.aurelium.auraskills.api.user.SkillsUser;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class HealthRegenTask {

    private final JavaPlugin plugin;
    // Tỷ lệ % Máu hồi mỗi giây cơ bản (1.5% max HP / giây)
    private final double BASE_REGEN_PERCENTAGE = 0.015;

    public HealthRegenTask(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void start() {
        plugin.getServer().getScheduler().runTaskTimer(plugin, () -> {
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (p.isDead()) continue;
                
                double maxHealth = p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
                double currentHealth = p.getHealth();
                
                if (currentHealth < maxHealth) {
                    // Base amount
                    double regenAmount = maxHealth * BASE_REGEN_PERCENTAGE;
                    
                    // Nếu có AuraSkills, lấy chỉ số Regeneration (bao gồm từ Rejuvenate)
                    if (Bukkit.getPluginManager().isPluginEnabled("AuraSkills")) {
                        try {
                            SkillsUser user = AuraSkillsApi.get().getUser(p.getUniqueId());
                            if (user != null) {
                                double regenStat = user.getStatLevel(dev.aurelium.auraskills.api.stat.Stats.REGENERATION);
                                // Cứ 10 Regen = +0.5% Max HP/s (Nerf theo yêu cầu)
                                double extraRegenPercent = regenStat / 2000.0;
                                regenAmount += maxHealth * extraRegenPercent;
                            }
                        } catch (Exception ignored) {}
                    }
                    
                    double newHealth = currentHealth + regenAmount;
                    if (newHealth > maxHealth) {
                        newHealth = maxHealth;
                    }
                    
                    try {
                        p.setHealth(newHealth);
                    } catch (IllegalArgumentException e) {
                        p.setHealth(maxHealth);
                    }
                }
            }
        }, 20L, 20L); // Chạy mỗi 20 ticks (1 giây)
    }
}
