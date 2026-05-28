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
                relic = createRelic(Material.MAGMA_CREAM, "§c§lLõi Dung Nham", "§7Nguyên liệu rèn đúc.", "§7Thu thập bằng cách đánh bại quái vật", "§7tại vùng đất nóng bức (Desert, Nether...)");
            } else if (biomeName.contains("ICE") || biomeName.contains("SNOW") || biomeName.contains("FROZEN") || biomeName.contains("TAIGA")) {
                relic = createRelic(Material.PRISMARINE_CRYSTALS, "§b§lBăng Tinh Cổ Đại", "§7Nguyên liệu rèn đúc.", "§7Thu thập bằng cách đánh bại quái vật", "§7tại vùng đất băng giá.");
            } else if (biomeName.contains("DARK") || biomeName.contains("DEEP") || biomeName.contains("END")) {
                relic = createRelic(Material.ECHO_SHARD, "§8§lMảnh Vỡ Hắc Ám", "§7Nguyên liệu rèn đúc.", "§7Thu thập bằng cách đánh bại quái vật", "§7tại vùng đất tăm tối (Deep Dark, End...)");
            }

            if (relic != null) {
                event.getDrops().add(relic);
            }
        }

        if (mobLevel > 1) {
            double bonusXp = mobLevel * 20.0;
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
            double defenseXp = mobLevel * 4.0;
            AuraSkillsApi auraApi = AuraSkillsApi.get();
            SkillsUser user = auraApi.getUser(player.getUniqueId());

            if (user != null) {
                user.addSkillXp(Skills.DEFENSE, defenseXp);
                player.sendActionBar("§e[CWE] Đang chịu đòn từ quái Lvl " + mobLevel + ": +" + defenseXp + " XP Defense");
            }
        }
    }
    
    private ItemStack createRelic(Material mat, String name, String... lore) {
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            meta.setLore(Arrays.asList(lore));
            meta.getPersistentDataContainer().set(new org.bukkit.NamespacedKey("customweaponengine", "cwe_relic"), PersistentDataType.INTEGER, 1);
            item.setItemMeta(meta);
        }
        return item;
    }
}
