package org.example.weapon.legendary;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.example.core.CustomWeaponEngine;

public class EpicWeaponListeners implements Listener {
    private final CustomWeaponEngine plugin;
    private final NamespacedKey idKey;
    private final NamespacedKey killsKey;

    public EpicWeaponListeners(CustomWeaponEngine plugin) {
        this.plugin = plugin;
        this.idKey = new NamespacedKey(plugin, "cwe_id");
        this.killsKey = new NamespacedKey(plugin, "cwe_fel_kills");
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getHand() != org.bukkit.inventory.EquipmentSlot.HAND) return;
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (event.getItem() == null || event.getItem().getItemMeta() == null) return;

        ItemMeta meta = event.getItem().getItemMeta();
        String id = meta.getPersistentDataContainer().get(idKey, PersistentDataType.STRING);
        if (id == null) return;

        Player p = event.getPlayer();

        switch (id) {
            case "cwe_excalibur":
                handleExcalibur(p);
                break;
            case "cwe_golem_sword":
                handleGolemSword(p);
                break;
            case "cwe_zombie_sword":
                handleZombieSword(p);
                break;
            case "cwe_pigman_sword":
                handlePigmanSword(p);
                break;
        }
    }

    private void handleGolemSword(Player p) {
        if (!ManaHelper.consumeMana(p, 25.0, plugin, "Iron Punch")) return;

        p.playSound(p.getLocation(), Sound.ENTITY_IRON_GOLEM_HURT, 1.0f, 0.5f);
        p.getWorld().spawnParticle(Particle.BLOCK, p.getLocation(), 40, 2, 0.5, 2, 0.1, org.bukkit.Material.IRON_BLOCK.createBlockData());
        
        for (Entity e : p.getNearbyEntities(4, 3, 4)) {
            if (e instanceof LivingEntity && e != p && !(e instanceof org.bukkit.entity.Player)) {
                LivingEntity target = (LivingEntity) e;
                target.setNoDamageTicks(0);
                target.damage(150.0, p);
                // Hất tung nhẹ
                target.setVelocity(new Vector(0, 0.4, 0));
            }
        }
    }

    private void handleZombieSword(Player p) {
        // Cooldown nhẹ 1s để tránh spam
        if (p.hasMetadata("cwe_zombie_cd")) {
            if (System.currentTimeMillis() < p.getMetadata("cwe_zombie_cd").get(0).asLong()) return;
        }
        p.setMetadata("cwe_zombie_cd", new org.bukkit.metadata.FixedMetadataValue(plugin, System.currentTimeMillis() + 1000L));

        if (!ManaHelper.consumeMana(p, 50.0, plugin, "Instant Heal")) return;

        p.playSound(p.getLocation(), Sound.ENTITY_ZOMBIE_VILLAGER_CURE, 1.0f, 1.2f);
        p.getWorld().spawnParticle(Particle.HEART, p.getLocation().add(0, 2, 0), 5, 0.5, 0.5, 0.5, 0.0);

        // Hồi 10% máu bản thân
        double maxHp = p.getAttribute(org.bukkit.attribute.Attribute.GENERIC_MAX_HEALTH).getValue();
        p.setHealth(Math.min(maxHp, p.getHealth() + (maxHp * 0.10)));

        // Hồi 5% máu cho đồng minh
        for (Entity e : p.getNearbyEntities(8, 4, 8)) {
            if (e instanceof Player && e != p) {
                Player ally = (Player) e;
                double aMaxHp = ally.getAttribute(org.bukkit.attribute.Attribute.GENERIC_MAX_HEALTH).getValue();
                ally.setHealth(Math.min(aMaxHp, ally.getHealth() + (aMaxHp * 0.05)));
                ally.getWorld().spawnParticle(Particle.HEART, ally.getLocation().add(0, 2, 0), 3, 0.5, 0.5, 0.5, 0.0);
            }
        }
    }

    private void handlePigmanSword(Player p) {
        if (!ManaHelper.consumeMana(p, 150.0, plugin, "Burning Souls")) return;

        p.playSound(p.getLocation(), Sound.ENTITY_PIG_DEATH, 1.0f, 0.8f);
        
        // Thêm 300 Defense trong 5s thông qua metadata cwe_temp_defense
        long expireTime = System.currentTimeMillis() + 5000L;
        p.setMetadata("cwe_temp_defense_expire", new org.bukkit.metadata.FixedMetadataValue(plugin, expireTime));
        p.setMetadata("cwe_temp_defense", new org.bukkit.metadata.FixedMetadataValue(plugin, 300.0));
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            if (p.hasMetadata("cwe_temp_defense_expire")) {
                long currentExpire = p.getMetadata("cwe_temp_defense_expire").get(0).asLong();
                if (System.currentTimeMillis() >= currentExpire - 50L) {
                    p.removeMetadata("cwe_temp_defense", plugin);
                    p.removeMetadata("cwe_temp_defense_expire", plugin);
                }
            }
        }, 100L);

        // Lấy chỉ số Intelligence (Wisdom) của player để scale sát thương phép thuật
        double wisdom = 0.0;
        if (Bukkit.getPluginManager().isPluginEnabled("AuraSkills")) {
            try {
                dev.aurelium.auraskills.api.user.SkillsUser user = dev.aurelium.auraskills.api.AuraSkillsApi.get().getUser(p.getUniqueId());
                if (user != null) {
                    wisdom = user.getStatLevel(dev.aurelium.auraskills.api.stat.Stats.WISDOM);
                }
            } catch (Exception ignored) {}
        }
        double finalDamage = 300.0 * (1.0 + wisdom * 0.003);

        Location spawnLoc = p.getEyeLocation();
        Vector direction = spawnLoc.getDirection().normalize().multiply(1.2);

        new BukkitRunnable() {
            int ticks = 0;
            Location currentLoc = spawnLoc.clone();
            
            @Override
            public void run() {
                if (ticks++ > 30) {
                    this.cancel();
                    return;
                }

                currentLoc.add(direction);
                currentLoc.getWorld().spawnParticle(Particle.FLAME, currentLoc, 5, 0.1, 0.1, 0.1, 0.05);

                boolean collided = false;
                if (currentLoc.getBlock().getType().isSolid()) collided = true;
                else {
                    for (Entity e : currentLoc.getWorld().getNearbyEntities(currentLoc, 1.0, 1.0, 1.0)) {
                        if (e instanceof LivingEntity && e != p && !(e instanceof org.bukkit.entity.Player)) {
                            collided = true;
                            break;
                        }
                    }
                }

                if (collided) {
                    currentLoc.getWorld().createExplosion(currentLoc, 0F, false, false);
                    currentLoc.getWorld().playSound(currentLoc, Sound.ENTITY_GENERIC_EXPLODE, 1.0f, 1.5f);
                    for (Entity e : currentLoc.getWorld().getNearbyEntities(currentLoc, 3.0, 3.0, 3.0)) {
                        if (e instanceof LivingEntity && e != p && !(e instanceof org.bukkit.entity.Player)) {
                            ((LivingEntity) e).setNoDamageTicks(0);
                            ((LivingEntity) e).damage(finalDamage, p);
                        }
                    }
                    this.cancel();
                }
            }
        }.runTaskTimer(plugin, 0L, 1L);
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity().getKiller() == null) return;
        Player p = event.getEntity().getKiller();
        ItemStack item = p.getInventory().getItemInMainHand();
        
        if (item == null || !item.hasItemMeta()) return;
        ItemMeta meta = item.getItemMeta();
        String id = meta.getPersistentDataContainer().get(idKey, PersistentDataType.STRING);
        if (id == null) return;

        // Raider Axe: Tăng tiền (cần vault/eco API, tạm thời bỏ qua hoặc dùng lệnh eco give)
        if (id.equals("cwe_raider_axe")) {
            // Giả lập rớt tiền 20 xu
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "eco give " + p.getName() + " 20");
            p.sendMessage("§aThợ Săn Tiền Thưởng: +20$");
        }

        // Fel Sword: Theo dõi kills
        if (id.equals("cwe_fel_sword")) {
            int currentKills = meta.getPersistentDataContainer().getOrDefault(killsKey, PersistentDataType.INTEGER, 0);
            currentKills++;
            meta.getPersistentDataContainer().set(killsKey, PersistentDataType.INTEGER, currentKills);
            
            // Cứ 100 kills thì tăng 1 strength/damage
            if (currentKills % 100 == 0 && currentKills <= 10000) {
                double currentStrength = meta.getPersistentDataContainer().getOrDefault(new NamespacedKey(plugin, "stat_strength"), PersistentDataType.DOUBLE, 0.0);
                meta.getPersistentDataContainer().set(new NamespacedKey(plugin, "stat_strength"), PersistentDataType.DOUBLE, currentStrength + 1.0);
                p.sendMessage("§c§lHuyết Kiếm: §eThanh kiếm đã uống đủ máu (" + currentKills + ") và mạnh lên!");
            }
            item.setItemMeta(meta);
        }
    }

    private void handleExcalibur(Player p) {
        if (p.hasMetadata("cwe_excalibur_cd")) {
            long cd = p.getMetadata("cwe_excalibur_cd").get(0).asLong();
            if (System.currentTimeMillis() < cd) {
                long timeLeft = (cd - System.currentTimeMillis()) / 1000L;
                p.sendMessage("§c[Excalibur] Kỹ năng Holy Light đang hồi chiêu! Còn " + timeLeft + " giây.");
                return;
            }
        }

        if (!ManaHelper.consumeMana(p, 100.0, plugin, "Holy Light")) return;

        p.setMetadata("cwe_excalibur_cd", new org.bukkit.metadata.FixedMetadataValue(plugin, System.currentTimeMillis() + 30000L));

        p.getWorld().playSound(p.getLocation(), Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 1.0f, 1.2f);
        p.getWorld().spawnParticle(Particle.TOTEM_OF_UNDYING, p.getLocation().add(0, 1, 0), 60, 1.0, 1.0, 1.0, 0.25);
        
        for (Entity e : p.getNearbyEntities(6, 6, 6)) {
            if (e instanceof LivingEntity && e != p && !(e instanceof org.bukkit.entity.Player)) {
                LivingEntity target = (LivingEntity) e;
                target.setNoDamageTicks(0);
                target.damage(5000.0, p);
            }
        }
        p.sendMessage("§d§lExcalibur: §eÁnh Sáng Thánh đã thanh tẩy kẻ địch xung quanh!");
    }
}
