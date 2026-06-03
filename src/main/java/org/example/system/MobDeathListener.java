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
        if (mob.getPersistentDataContainer().has(lmLevelKey, PersistentDataType.INTEGER)) {
            Integer level = mob.getPersistentDataContainer().get(lmLevelKey, PersistentDataType.INTEGER);
            if (level != null) mobLevel = level;
        }

        
        if (mobLevel >= 2 && random.nextDouble() < 0.15) { // 15% chance
            ItemStack relic = null;
            String biomeName = mob.getLocation().getBlock().getBiome().name();
            if (biomeName.contains("DESERT") || biomeName.contains("BADLANDS") || biomeName.contains("NETHER")) {
                relic = createCraftMaterial(Material.MAGMA_CREAM, "§9Lõi Dung Nham", "RARE");
            } else if (biomeName.contains("ICE") || biomeName.contains("SNOW") || biomeName.contains("FROZEN") || biomeName.contains("TAIGA")) {
                relic = createCraftMaterial(Material.DRAGON_BREATH, "§5Băng Tinh Cổ Đại", "EPIC");
            } else if (biomeName.contains("DARK") || biomeName.contains("DEEP") || biomeName.contains("END")) {
                relic = createCraftMaterial(Material.BEACON, "§6§lLõi Năng Lượng Cao Cấp", "LEGENDARY");
            }

            if (relic != null) {
                event.getDrops().add(relic);
            }
        }

        if (mobLevel > 1) {
            // Đã giảm AuraSkills XP Requirement đi một nửa, nên tăng nhẹ XP này lên 6.0 để tạo cảm giác cày cuốc thỏa mãn
            double bonusXp = mobLevel * 6.0;
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
        if (mob.getPersistentDataContainer().has(lmLevelKey, PersistentDataType.INTEGER)) {
            Integer level = mob.getPersistentDataContainer().get(lmLevelKey, PersistentDataType.INTEGER);
            if (level != null) mobLevel = level;
        }

        if (mobLevel > 1) {
            // Tăng nhẹ lên 1.0 cho cân bằng
            double defenseXp = mobLevel * 1.0;
            AuraSkillsApi auraApi = AuraSkillsApi.get();
            SkillsUser user = auraApi.getUser(player.getUniqueId());

            if (user != null) {
                user.addSkillXp(Skills.DEFENSE, defenseXp);
                player.sendActionBar("§e[CWE] Đang chịu đòn từ quái Lvl " + mobLevel + ": +" + defenseXp + " XP Defense");
            }
        }
    }
    
    private ItemStack createCraftMaterial(Material mat, String name, String rarity) {
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            meta.setLore(Arrays.asList("§7Nguyên liệu chế tạo thần khí."));
            org.bukkit.plugin.java.JavaPlugin plugin = org.example.core.CustomWeaponEngine.getPlugin(org.example.core.CustomWeaponEngine.class);
            meta.getPersistentDataContainer().set(new org.bukkit.NamespacedKey(plugin, "cwe_tier"), PersistentDataType.STRING, rarity);
            item.setItemMeta(meta);
        }
        return item;
    }
}
