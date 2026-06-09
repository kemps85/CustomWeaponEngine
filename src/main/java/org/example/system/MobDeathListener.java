package org.example.system;

import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
import dev.aurelium.auraskills.api.AuraSkillsApi;
import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
import dev.aurelium.auraskills.api.skill.Skills;
import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
import dev.aurelium.auraskills.api.user.SkillsUser;
import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
import org.bukkit.NamespacedKey;
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
import org.bukkit.entity.Projectile;
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
import org.bukkit.event.entity.EntityDeathEvent;
import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.block.Biome;
import org.bukkit.Material;
import java.util.Arrays;
import java.util.Random;

import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
public class MobDeathListener implements Listener {

    private final NamespacedKey lmLevelKey = new NamespacedKey("levelledmobs", "level");
    private final Random random = new Random();

    @EventHandler
    public void onMobDeath(EntityDeathEvent event) {
        LivingEntity mob = event.getEntity();
        Player player = mob.getKiller();
        if (player == null) return;

        int mobLevel = 1;
        io.lumine.mythic.core.mobs.ActiveMob activeMob = io.lumine.mythic.bukkit.MythicBukkit.inst().getMobManager().getMythicMobInstance(mob);
        if (activeMob != null) {
            mobLevel = (int) activeMob.getLevel();
        }

        if (mobLevel > 1) {
            // NERF: Đã giảm tỉ lệ XP xuống x1.5 (Thay vì x6.0) để chống lạm phát Level 150
            double bonusXp = mobLevel * 1.5;
            AuraSkillsApi auraApi = AuraSkillsApi.get();
            SkillsUser user = auraApi.getUser(player.getUniqueId());

            if (user != null) {
                boolean isRangedKill = false;
                if (mob.getLastDamageCause() instanceof EntityDamageByEntityEvent) {
                    EntityDamageByEntityEvent damageEvent = (EntityDamageByEntityEvent) mob.getLastDamageCause();
                    if (damageEvent.getDamager() instanceof Projectile) {
                        isRangedKill = true;
                    }
                }

                if (isRangedKill) {
                    user.addSkillXp(Skills.ARCHERY, bonusXp);
                    player.sendMessage("§a[Bridge] Kết liễu tầm xa! +" + bonusXp + " XP Archery (Quái Lvl " + mobLevel + ")");
                } else {
                    user.addSkillXp(Skills.FIGHTING, bonusXp);
                    player.sendMessage("§a[Bridge] Kết liễu cận chiến! +" + bonusXp + " XP Fighting (Quái Lvl " + mobLevel + ")");
                }
            }
        }
    }

    @EventHandler
    public void onPlayerTakeDamage(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();
        LivingEntity mob = null;

        if (event.getDamager() instanceof LivingEntity) {
            mob = (LivingEntity) event.getDamager();
        } else if (event.getDamager() instanceof Projectile) {
            Projectile projectile = (Projectile) event.getDamager();
            if (projectile.getShooter() instanceof LivingEntity) {
                mob = (LivingEntity) projectile.getShooter();
            }
        }

        if (mob == null || mob instanceof Player) return;

        int mobLevel = 1;
        io.lumine.mythic.core.mobs.ActiveMob activeMob = io.lumine.mythic.bukkit.MythicBukkit.inst().getMobManager().getMythicMobInstance(mob);
        if (activeMob != null) {
            mobLevel = (int) activeMob.getLevel();
        }

        if (mobLevel > 1) {
            // NERF: Giảm lượng Defense XP xuống x0.2 (Thay vì x1.0)
            double defenseXp = mobLevel * 0.2;
            AuraSkillsApi auraApi = AuraSkillsApi.get();
            SkillsUser user = auraApi.getUser(player.getUniqueId());

            if (user != null) {
                user.addSkillXp(Skills.DEFENSE, defenseXp);
                player.sendActionBar("§e[CWE] Đang chịu đòn từ quái Lvl " + mobLevel + ": +" + defenseXp + " XP Defense");
            }
        }
    }

}
