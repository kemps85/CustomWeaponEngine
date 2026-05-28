package org.example.system;

import dev.aurelium.auraskills.api.AuraSkillsApi;
import dev.aurelium.auraskills.api.user.SkillsUser;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class ManaRegenTask {

    private final JavaPlugin plugin;
    // Tỷ lệ % Mana hồi mỗi giây
    // 1.5% → ~18.9 mana/s với 1262 max mana
    // Spam ability 30 mana sẽ tốn net → mana tụt kích hoạt Full Set Bonus
    // Hồi từ 0 về full ~53 giây — đủ áp lực mà không quá chậm
    private final double REGEN_PERCENTAGE = 0.015;

    public ManaRegenTask(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void start() {
        if (!Bukkit.getPluginManager().isPluginEnabled("AuraSkills")) {
            return;
        }

        plugin.getServer().getScheduler().runTaskTimer(plugin, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                SkillsUser user = AuraSkillsApi.get().getUser(player.getUniqueId());
                if (user != null) {
                    double maxMana = user.getMaxMana();
                    double currentMana = user.getMana();
                    
                    if (currentMana < maxMana) {
                        // Tính toán lượng Mana cần hồi (dựa trên % Mana tổng)
                        double regenAmount = maxMana * REGEN_PERCENTAGE;
                        
                        // Cộng thêm Mana
                        double newMana = currentMana + regenAmount;
                        if (newMana > maxMana) {
                            newMana = maxMana;
                        }
                        
                        user.setMana(newMana);
                    }
                }
            }
        }, 20L, 20L); // Chạy mỗi 20 ticks (1 giây)
    }
}
