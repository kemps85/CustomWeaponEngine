package org.example.weapon;

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
import dev.aurelium.auraskills.api.stat.Stats;
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
import org.bukkit.Bukkit;
import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
import org.bukkit.Location;
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
import org.bukkit.entity.Entity;
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
import org.bukkit.entity.Monster;
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
import org.bukkit.entity.Sheep;
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
import org.bukkit.event.block.Action;
import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
import org.bukkit.event.player.PlayerInteractEvent;
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
import org.bukkit.metadata.FixedMetadataValue;
import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
import org.bukkit.persistence.PersistentDataContainer;
import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
import org.bukkit.persistence.PersistentDataType;
import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
import org.bukkit.scheduler.BukkitRunnable;
import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
import org.bukkit.util.Vector;

import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
import java.util.List;

import org.example.core.CustomWeaponEngine;
import org.example.weapon.*;
import org.example.armor.*;
import org.example.enchant.*;
import org.example.bazaar.*;
import org.example.stats.*;
import org.example.system.*;
public class AstralShepherdListener implements Listener {
    private final JavaPlugin plugin;
    private final NamespacedKey cweIdKey;

    public AstralShepherdListener(JavaPlugin plugin) {
        this.plugin = plugin;
        this.cweIdKey = new NamespacedKey(plugin, "cwe_id");
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        if (item == null || !item.hasItemMeta()) return;

        PersistentDataContainer container = item.getItemMeta().getPersistentDataContainer();
        if (container.has(cweIdKey, PersistentDataType.STRING)) {
            String id = container.get(cweIdKey, PersistentDataType.STRING);
            if ("cwe_astral_shepherd_wand".equalsIgnoreCase(id)) {
                event.setCancelled(true);
                handleAstralShepherdCast(player);
            }
        }
    }

    private void handleAstralShepherdCast(Player player) {
        // Cooldown 1s
        if (player.hasMetadata("cwe_astral_shepherd_cd")) {
            long cdEnd = player.getMetadata("cwe_astral_shepherd_cd").get(0).asLong();
            if (System.currentTimeMillis() < cdEnd) {
                return;
            }
        }

        long newCdEnd = System.currentTimeMillis() + 1000L;
        player.setMetadata("cwe_astral_shepherd_cd", new FixedMetadataValue(plugin, newCdEnd));

        // Lấy Strength từ AuraSkills và kiểm tra Mana
        double playerStrength = 0;
        SkillsUser user = AuraSkillsApi.get().getUser(player.getUniqueId());
        if (user != null) {
            if (user.getMana() < 100) {
                player.sendMessage("§cKhông đủ Mana để dùng kỹ năng (Yêu cầu: 100)!");
                return;
            }
            user.setMana(user.getMana() - 100);
            playerStrength = user.getStatLevel(Stats.STRENGTH);
        } else {
            return;
        }
        
        final double explosionDamage = playerStrength * 1.5;

        // Sinh ra cừu tại mắt người chơi
        Location spawnLoc = player.getEyeLocation();
        Vector direction = spawnLoc.getDirection().normalize().multiply(1.5); // Tốc độ đạn

        Sheep sheepProjectile = player.getWorld().spawn(spawnLoc, Sheep.class, sheep -> {
            sheep.setAI(false);
            sheep.setGravity(false);
            sheep.setInvulnerable(true);
            sheep.setSilent(true);
            sheep.setCollidable(false);
            sheep.setAgeLock(true); // Ngăn cừu lớn lên hoặc đổi màu
        });

        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_SHEEP_AMBIENT, 1.0f, 1.5f);
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDER_PEARL_THROW, 1.0f, 1.0f);

        // Chạy vòng lặp đạn đạo
        new BukkitRunnable() {
            int ticks = 0;

            @Override
            public void run() {
                if (ticks++ > 40 || sheepProjectile.isDead()) { // Max 40 ticks bay ~ 60 blocks distance
                    sheepProjectile.remove();
                    this.cancel();
                    return;
                }

                Location currentLoc = sheepProjectile.getLocation();
                Location nextLoc = currentLoc.clone().add(direction);

                // Cập nhật vị trí cừu
                sheepProjectile.teleport(nextLoc);

                // Sinh hạt END_ROD dọc đường bay để làm đuôi
                nextLoc.getWorld().spawnParticle(Particle.END_ROD, nextLoc, 5, 0.2, 0.2, 0.2, 0.05);

                boolean collided = false;

                // Va chạm block
                if (nextLoc.getBlock().getType().isSolid()) {
                    collided = true;
                } else {
                    // Va chạm thực thể
                    List<Entity> nearby = (List<Entity>) nextLoc.getWorld().getNearbyEntities(nextLoc, 1.0, 1.0, 1.0);
                    for (Entity e : nearby) {
                        if (e instanceof LivingEntity && e != player && e != sheepProjectile) {
                            collided = true;
                            break;
                        }
                    }
                }

                if (collided) {
                    triggerExplosion(nextLoc, player, explosionDamage);
                    sheepProjectile.remove();
                    this.cancel();
                }
            }
        }.runTaskTimer(plugin, 1L, 1L);
    }

    private void triggerExplosion(Location loc, Player shooter, double damage) {
        // Tạo vụ nổ ảo
        loc.getWorld().createExplosion(loc, 0F, false, false);
        loc.getWorld().playSound(loc, Sound.ENTITY_GENERIC_EXPLODE, 1.0f, 1.2f);
        loc.getWorld().spawnParticle(Particle.EXPLOSION, loc, 3);
        loc.getWorld().spawnParticle(Particle.CLOUD, loc, 20, 1, 1, 1, 0.1);

        // Gây sát thương AOE bán kính 2 ô
        for (Entity e : loc.getWorld().getNearbyEntities(loc, 2.0, 2.0, 2.0)) {
            if (e instanceof LivingEntity && !e.isDead() && e != shooter) {
                // Không sát thương ArmorStand
                if (e.getType() == org.bukkit.entity.EntityType.ARMOR_STAND) continue;
                
                LivingEntity target = (LivingEntity) e;
                target.damage(damage, shooter);
            }
        }
    }
}

