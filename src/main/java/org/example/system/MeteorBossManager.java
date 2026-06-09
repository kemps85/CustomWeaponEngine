/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.NamespacedKey
 *  org.bukkit.Particle
 *  org.bukkit.Sound
 *  org.bukkit.attribute.Attribute
 *  org.bukkit.entity.Enderman
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.EntityType
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.entity.Snowball
 *  org.bukkit.entity.WitherSkeleton
 *  org.bukkit.entity.Zombie
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.block.BlockBreakEvent
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.event.entity.EntityDamageEvent
 *  org.bukkit.event.entity.EntityDamageEvent$DamageCause
 *  org.bukkit.event.entity.EntityDeathEvent
 *  org.bukkit.event.entity.PlayerDeathEvent
 *  org.bukkit.event.player.PlayerBucketEmptyEvent
 *  org.bukkit.event.player.PlayerMoveEvent
 *  org.bukkit.event.player.PlayerQuitEvent
 *  org.bukkit.event.player.PlayerRespawnEvent
 *  org.bukkit.event.player.PlayerTeleportEvent
 *  org.bukkit.event.player.PlayerTeleportEvent$TeleportCause
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.persistence.PersistentDataType
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 *  org.bukkit.util.Vector
 */
package org.example.system;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.entity.WitherSkeleton;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import org.example.core.CustomWeaponEngine;
import org.example.system.RegionBossManager;

public class MeteorBossManager
implements Listener {
    private final CustomWeaponEngine plugin;
    private final RegionBossManager regionBossManager;
    private static final Random random = new Random();
    public UUID activeBossId = null;
    private Location activeBossSpawnLoc = null;
    private String activeType = null;
    private boolean activeBossIsMythic = false;
    private boolean isPluginTeleporting = false;
    private int mainTask = -1;
    private double maxBossHealth = 0.0;
    private int fireETimer = 0;
    private int fireECount = 0;
    private boolean fireGuardsDeadTriggered = false;
    private int iceETimer = 30;
    private int iceFreezeCount = 0;
    private int iceFlightTimer = 0;
    private boolean iceIsFlying = false;
    public UUID iceFrozenPlayer = null;
    private int iceFrozenTicks = 0;
    private Location iceFrozenLoc = null;
    private int iceBasicAttackTimer = 0;
    private int voidETimer = 35;
    private int voidECount = 0;
    private int voidBlackHoleTicks = 0;
    private Location voidBlackHoleLoc = null;
    private Set<UUID> voidBlackHoleVictims = new HashSet<UUID>();
    private boolean voidIsSplit = false;
    private int voidSplitTimer = 0;
    private List<LivingEntity> voidClones = new ArrayList<LivingEntity>();
    private LivingEntity voidRealClone = null;
    private boolean voidIsUltimate = false;
    private Set<UUID> eventParticipants = new HashSet<UUID>();

    public MeteorBossManager(CustomWeaponEngine plugin, RegionBossManager regionBossManager) {
        this.plugin = plugin;
        this.regionBossManager = regionBossManager;
    }

    public static void spawnBoss(Location loc, String type) {
        CustomWeaponEngine cfr_ignored_0 = (CustomWeaponEngine)CustomWeaponEngine.getPlugin(CustomWeaponEngine.class);
        MeteorBossManager instance = CustomWeaponEngine.getMeteorBossManager();
        if (instance != null) {
            instance.doSpawnBoss(loc, type);
        }
    }

    private void doSpawnBoss(Location loc, String type) {
        Entity old;
        if (this.activeBossId != null) {
            old = Bukkit.getEntity(this.activeBossId);
            if (old != null && !old.isDead()) {
                old.remove();
            }
            if (this.activeType != null) {
                this.regionBossManager.resetBossState(this.activeType);
            }
        }
        Location spawnLoc = loc.clone().add(0.0, 2.0, 0.0);
        this.activeBossSpawnLoc = loc.clone();
        this.activeType = type;
        this.activeBossIsMythic = false;
        this.fireETimer = 0;
        this.fireECount = 0;
        this.fireGuardsDeadTriggered = false;
        this.iceETimer = 30;
        this.iceFreezeCount = 0;
        this.iceFlightTimer = 0;
        this.iceIsFlying = false;
        this.iceFrozenPlayer = null;
        this.iceFrozenTicks = 0;
        this.voidETimer = 35;
        this.voidECount = 0;
        this.voidBlackHoleTicks = 0;
        this.voidIsSplit = false;
        this.voidClones.clear();
        this.voidIsUltimate = false;
        this.voidBlackHoleVictims.clear();
        this.eventParticipants.clear();
        this.registerArenaParticipants(loc);

        LivingEntity boss = null;
        String mmMobId = "";
        if ("FIRE".equals(type)) mmMobId = "FireBoss";
        else if ("ICE".equals(type)) mmMobId = "IceBoss";
        else if ("VOID".equals(type)) mmMobId = "VoidBoss";

        try {
            org.example.bazaar.BazaarMobDropListener.BYPASS_SPAWN.set(true);
            if (Bukkit.getPluginManager().isPluginEnabled("MythicMobs") && !mmMobId.isEmpty()) {
                try {
                    io.lumine.mythic.core.mobs.ActiveMob am = io.lumine.mythic.bukkit.MythicBukkit.inst().getMobManager().spawnMob(mmMobId, spawnLoc, 1);
                    if (am != null && am.getEntity() != null) {
                        Entity e = am.getEntity().getBukkitEntity();
                        if (e instanceof LivingEntity) {
                            boss = (LivingEntity) e;
                            this.activeBossIsMythic = true;
                        }
                    }
                } catch (Throwable t) {
                    plugin.getLogger().warning("Không thể spawn MythicMob Boss " + mmMobId + " qua API, chuyển sang vanilla fallback: " + t.getMessage());
                }
            }

            if (boss == null) {
                // Vanilla Fallback
                if ("FIRE".equals(type)) {
                    WitherSkeleton fallbackBoss = (WitherSkeleton)loc.getWorld().spawnEntity(spawnLoc, EntityType.WITHER_SKELETON);
                    this.setupBossStats((LivingEntity)fallbackBoss, "\u00a7c\u00a7lH\u1ecfa Di\u1ec7m Ma V\u01b0\u01a1ng", 5000.0, type);
                    this.spawnGuards(loc, null, EntityType.BLAZE, 4, type, 600.0);
                } else if ("ICE".equals(type)) {
                    Zombie fallbackBoss = (Zombie)loc.getWorld().spawnEntity(spawnLoc, EntityType.ZOMBIE);
                    this.setupBossStats((LivingEntity)fallbackBoss, "\u00a7b\u00a7lB\u0103ng Gi\u00e1 C\u1ed5 Th\u1ea7n", 15000.0, type);
                    this.spawnGuards(loc, null, EntityType.STRAY, 4, type, 500.0);
                } else if ("VOID".equals(type)) {
                    Enderman fallbackBoss = (Enderman)loc.getWorld().spawnEntity(spawnLoc, EntityType.ENDERMAN);
                    this.setupBossStats((LivingEntity)fallbackBoss, "\u00a75\u00a7lCh\u00faa T\u1ec3 H\u01b0 Kh\u00f4ng", 150000.0, type);
                    this.spawnGuards(loc, null, EntityType.SHULKER, 3, type, 15000.0);
                    this.spawnGuards(loc, null, EntityType.ENDERMAN, 2, type, 15000.0);
                }
            } else {
                // MythicMob spawned successfully
                boss.setMetadata("LM_IGNORE", (MetadataValue)new FixedMetadataValue((Plugin)this.plugin, true));
                boss.getPersistentDataContainer().set(new NamespacedKey((Plugin)this.plugin, "cwe_meteor_boss"), PersistentDataType.STRING, type);
                boss.setRemoveWhenFarAway(false);
                this.activeBossId = boss.getUniqueId();
                this.maxBossHealth = boss.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();

                // Spawn guards via MythicMobs as well
                if ("FIRE".equals(type)) {
                    this.spawnGuards(loc, "FireGuard", EntityType.BLAZE, 4, type, 600.0);
                } else if ("ICE".equals(type)) {
                    this.spawnGuards(loc, "IceGuard", EntityType.STRAY, 4, type, 500.0);
                } else if ("VOID".equals(type)) {
                    this.spawnGuards(loc, "VoidGuard", EntityType.SHULKER, 3, type, 15000.0);
                    this.spawnGuards(loc, "VoidGuardEnderman", EntityType.ENDERMAN, 2, type, 15000.0);
                }
            }
        } finally {
            org.example.bazaar.BazaarMobDropListener.BYPASS_SPAWN.set(false);
        }
        this.startBossLogic();

        // Kiểm tra sau 2 ticks xem boss có thực sự hợp lệ không. Nếu không, reset trạng thái đấu trường để tránh bị kẹt.
        Bukkit.getScheduler().runTaskLater((Plugin)this.plugin, () -> {
            if (this.activeBossId != null) {
                Entity ent = Bukkit.getEntity(this.activeBossId);
                if (ent == null || !ent.isValid()) {
                    this.activeBossId = null;
                    this.activeBossSpawnLoc = null;
                    this.activeBossIsMythic = false;
                    this.regionBossManager.resetBossState(type);
                    Bukkit.broadcastMessage("§c[CWE] Lỗi: Boss " + type + " không thể spawn (có thể bị chặn bởi WorldGuard hoặc giới hạn thực thể)! Đã hoàn tác trạng thái chiến đấu.");
                }
            }
        }, 2L);
    }

    private void setupBossStats(LivingEntity boss, String name, double hp, String type) {
        boss.setCustomName(name);
        boss.setCustomNameVisible(true);
        boss.setMetadata("LM_IGNORE", (MetadataValue)new FixedMetadataValue((Plugin)this.plugin, true));
        boss.getPersistentDataContainer().set(new NamespacedKey((Plugin)this.plugin, "cwe_meteor_boss"), PersistentDataType.STRING, type);
        boss.setRemoveWhenFarAway(false);
        this.maxBossHealth = hp;
        Bukkit.getScheduler().runTaskLater((Plugin)this.plugin, () -> {
            if (boss.isValid()) {
                boss.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(hp);
                boss.setHealth(hp);
            }
        }, 5L);
        this.activeBossId = boss.getUniqueId();
    }

    private void spawnGuards(Location loc, String mmMobId, EntityType fallbackType, int amount, String type, double hp) {
        try {
            org.example.bazaar.BazaarMobDropListener.BYPASS_SPAWN.set(true);
            for (int i = 0; i < amount; ++i) {
                Location guardLoc = loc.clone().add((double)(random.nextInt(10) - 5), 2.0, (double)(random.nextInt(10) - 5));
                LivingEntity guard = null;
                boolean isMythicMob = false;

                if (Bukkit.getPluginManager().isPluginEnabled("MythicMobs") && mmMobId != null && !mmMobId.isEmpty()) {
                    try {
                        io.lumine.mythic.core.mobs.ActiveMob am = io.lumine.mythic.bukkit.MythicBukkit.inst().getMobManager().spawnMob(mmMobId, guardLoc, 1);
                        if (am != null && am.getEntity() != null) {
                            Entity e = am.getEntity().getBukkitEntity();
                            if (e instanceof LivingEntity) {
                                guard = (LivingEntity) e;
                                isMythicMob = true;
                            }
                        }
                    } catch (Throwable t) {
                        plugin.getLogger().warning("Không thể spawn MythicMob Guard " + mmMobId + " qua API, chuyển sang vanilla fallback: " + t.getMessage());
                    }
                }

                if (guard == null) {
                    guard = (LivingEntity) loc.getWorld().spawnEntity(guardLoc, fallbackType);
                    guard.setCustomName("§8[Hộ Vệ] §c" + fallbackType.name());
                    guard.setCustomNameVisible(true);
                    final LivingEntity finalGuard = guard;
                    Bukkit.getScheduler().runTaskLater((Plugin)this.plugin, () -> {
                        if (finalGuard.isValid()) {
                            finalGuard.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(hp);
                            finalGuard.setHealth(hp);
                        }
                    }, 5L);
                }

                guard.setMetadata("LM_IGNORE", (MetadataValue)new FixedMetadataValue((Plugin)this.plugin, true));
                guard.getPersistentDataContainer().set(new NamespacedKey((Plugin)this.plugin, "cwe_meteor_guard"), PersistentDataType.STRING, type);
                guard.setRemoveWhenFarAway(false);
            }
        } finally {
            org.example.bazaar.BazaarMobDropListener.BYPASS_SPAWN.set(false);
        }
    }

    private void startBossLogic() {
        if (this.mainTask != -1) {
            Bukkit.getScheduler().cancelTask(this.mainTask);
        }
        this.mainTask = Bukkit.getScheduler().runTaskTimer((Plugin)this.plugin, () -> {
            if (this.activeBossId == null) {
                Bukkit.getScheduler().cancelTask(this.mainTask);
                return;
            }
            Entity ent = Bukkit.getEntity((UUID)this.activeBossId);
            if (ent == null || !ent.isValid()) {
                return;
            }
            if (!(!ent.isDead() || "VOID".equals(this.activeType) && this.voidIsSplit)) {
                Bukkit.getScheduler().cancelTask(this.mainTask);
                return;
            }
            LivingEntity activeBoss = (LivingEntity)ent;
            
            // Tự động kéo boss/hộ vệ/phân thân về lại đấu trường nếu bị rơi xuống hoặc đi quá xa
            if (this.activeBossSpawnLoc != null) {
                Location spawnCenter = this.activeBossSpawnLoc;
                
                // 1. Kiểm tra Boss chính
                if (!this.voidIsSplit) {
                    Location bossLoc = activeBoss.getLocation();
                    double dx = bossLoc.getX() - spawnCenter.getX();
                    double dz = bossLoc.getZ() - spawnCenter.getZ();
                    double horizDistSq = dx * dx + dz * dz;
                    if (bossLoc.getY() < spawnCenter.getY() - 5.0 || horizDistSq > 900.0) { // 30 blocks
                        this.teleportBoss(activeBoss, spawnCenter.clone().add(0.0, 1.0, 0.0));
                        activeBoss.getWorld().spawnParticle(Particle.PORTAL, activeBoss.getLocation(), 100, 0.5, 1.0, 0.5, 0.1);
                        activeBoss.getWorld().playSound(activeBoss.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
                    }
                }
                
                // 2. Kiểm tra các phân thân VoidClone
                if (this.voidIsSplit) {
                    for (LivingEntity clone : this.voidClones) {
                        if (clone.isValid()) {
                            Location cloneLoc = clone.getLocation();
                            double dx = cloneLoc.getX() - spawnCenter.getX();
                            double dz = cloneLoc.getZ() - spawnCenter.getZ();
                            double horizDistSq = dx * dx + dz * dz;
                            if (cloneLoc.getY() < spawnCenter.getY() - 5.0 || horizDistSq > 900.0) {
                                Location safeLoc = spawnCenter.clone().add(
                                    (double)(random.nextInt(10) - 5),
                                    1.0,
                                    (double)(random.nextInt(10) - 5)
                                );
                                this.teleportBoss(clone, safeLoc);
                                clone.getWorld().spawnParticle(Particle.PORTAL, clone.getLocation(), 50, 0.5, 1.0, 0.5, 0.1);
                            }
                        }
                    }
                }
                
                // 3. Kiểm tra các Hộ vệ (Guards)
                for (Entity e : spawnCenter.getWorld().getNearbyEntities(spawnCenter, 50.0, 50.0, 50.0)) {
                    if (e instanceof LivingEntity && e.getPersistentDataContainer().has(new NamespacedKey((Plugin)this.plugin, "cwe_meteor_guard"), PersistentDataType.STRING)) {
                        LivingEntity guard = (LivingEntity) e;
                        Location guardLoc = guard.getLocation();
                        double dx = guardLoc.getX() - spawnCenter.getX();
                        double dz = guardLoc.getZ() - spawnCenter.getZ();
                        double horizDistSq = dx * dx + dz * dz;
                        if (guardLoc.getY() < spawnCenter.getY() - 5.0 || horizDistSq > 900.0) {
                            Location safeLoc = spawnCenter.clone().add(
                                (double)(random.nextInt(10) - 5),
                                1.0,
                                (double)(random.nextInt(10) - 5)
                            );
                            this.teleportBoss(guard, safeLoc);
                            guard.getWorld().spawnParticle(Particle.PORTAL, guard.getLocation(), 30, 0.3, 0.5, 0.3, 0.1);
                        }
                    }
                }
            }

            if (!this.activeBossIsMythic) {
                if (activeBoss.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue() != this.maxBossHealth) {
                    activeBoss.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(this.maxBossHealth);
                }
            }
            
            if (this.voidIsSplit) {
                for (LivingEntity clone : this.voidClones) {
                    if (clone.isValid()) {
                        clone.getWorld().spawnParticle(Particle.PORTAL, clone.getLocation().add(0.0, 1.0, 0.0), 5, 0.2, 0.5, 0.2, 0.01);
                    }
                }
            }
            
            if (this.plugin.getConfig().getBoolean("bosses.use-mythic-mobs-skills", false) && this.activeBossIsMythic) {
                return;
            }

            if ("FIRE".equals(this.activeType)) {
                this.tickFireBoss(activeBoss);
            } else if ("ICE".equals(this.activeType)) {
                this.tickIceBoss(activeBoss);
            } else if ("VOID".equals(this.activeType)) {
                this.tickVoidBoss(activeBoss);
            }
        }, 0L, 20L).getTaskId();
    }

    public void cleanupOnDisable() {
        if (this.activeBossId != null) {
            Entity ent = Bukkit.getEntity((UUID)this.activeBossId);
            if (ent != null) {
                ent.remove();
            }
            this.activeBossId = null;
            this.activeBossSpawnLoc = null;
        }
        for (LivingEntity c : this.voidClones) {
            if (!c.isValid()) continue;
            c.remove();
        }
        this.voidClones.clear();
        if (this.iceFrozenLoc != null) {
            this.iceFrozenLoc.getBlock().setType(Material.AIR);
            this.iceFrozenLoc = null;
        }
        if (this.mainTask != -1) {
            Bukkit.getScheduler().cancelTask(this.mainTask);
            this.mainTask = -1;
        }
    }

    private void tickFireBoss(LivingEntity activeBoss) {
        boolean playerNear = false;
        Player nearest = this.getNearestPlayer(activeBoss.getLocation(), 15.0);
        if (nearest != null) {
            playerNear = true;
        }
        if (this.fireETimer <= 0 && playerNear) {
            this.fireETimer = 30;
            ++this.fireECount;
            for (Entity e : activeBoss.getNearbyEntities(15.0, 5.0, 15.0)) {
                if (!(e instanceof Player)) continue;
                Player p = (Player)e;
                p.setFireTicks(100);
                p.getWorld().spawnParticle(Particle.FLAME, p.getLocation(), 50, 0.5, 1.0, 0.5, 0.1);
                p.sendMessage("\u00a7cB\u1ea1n \u0111\u00e3 b\u01b0\u1edbc v\u00e0o L\u00e3nh \u0110\u1ecba L\u1eeda!");
            }
            activeBoss.getWorld().playSound(activeBoss.getLocation(), Sound.ENTITY_BLAZE_SHOOT, 2.0f, 1.0f);
            if (this.fireECount >= 3) {
                this.fireECount = 0;
                Player ultTarget = this.getNearestPlayer(activeBoss.getLocation(), 20.0);
                if (ultTarget != null) {
                    ultTarget.setVelocity(new Vector(0.0, 1.5, 0.0));
                    double targetHp = ultTarget.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() * 0.2;
                    double newHp = targetHp - 10.0;
                    if (newHp <= 0.0) {
                        ultTarget.setHealth(0.0);
                    } else {
                        ultTarget.setHealth(newHp);
                    }
                    ultTarget.setFireTicks(100);
                    ultTarget.sendMessage("\u00a74\u00a7lBoss \u0111\u00e3 s\u1eed d\u1ee5ng N\u1ed9: H\u1ea4T TUNG TR\u00daT GI\u1eacN!");
                }
            }
        }
        if (this.fireETimer > 0) {
            --this.fireETimer;
        }
        if (!this.fireGuardsDeadTriggered) {
            boolean hasGuards = false;
            for (Entity e : activeBoss.getWorld().getLivingEntities()) {
                if (!e.getPersistentDataContainer().has(new NamespacedKey((Plugin)this.plugin, "cwe_meteor_guard"), PersistentDataType.STRING)) continue;
                hasGuards = true;
                break;
            }
            if (!hasGuards) {
                this.fireGuardsDeadTriggered = true;
                Player p = this.getNearestPlayer(activeBoss.getLocation(), 20.0);
                if (p != null) {
                    this.teleportBoss(activeBoss, p.getLocation());
                    p.damage(40.0, (Entity)activeBoss);
                    p.sendMessage("\u00a7c\u00a7lBoss n\u1ed5i \u0111i\u00ean v\u00ec \u0111\u00e0n em ch\u1ebft! Lao \u0111\u1ebfn \u0111\u00e2m b\u1ea1n!");
                }
                Bukkit.getScheduler().runTaskLater((Plugin)this.plugin, () -> {
                    if (activeBoss != null && !activeBoss.isDead()) {
                        this.spawnGuards(activeBoss.getLocation(), "FireGuard", EntityType.BLAZE, 4, "FIRE", 600.0);
                        this.fireGuardsDeadTriggered = false;
                    }
                }, 200L);
            }
        }
    }

    private void tickIceBoss(LivingEntity activeBoss) {
        Player p;
        ++this.iceFlightTimer;
        if (this.iceIsFlying && this.iceFlightTimer >= 20) {
            this.iceIsFlying = false;
            this.iceFlightTimer = 0;
            activeBoss.setGravity(true);
        } else if (!this.iceIsFlying && this.iceFlightTimer >= 15) {
            this.iceIsFlying = true;
            this.iceFlightTimer = 0;
            activeBoss.setGravity(false);
            this.teleportBoss(activeBoss, activeBoss.getLocation().add(0.0, 5.0, 0.0));
        }
        if (this.iceIsFlying) {
            activeBoss.setVelocity(new Vector(0.0, 0.05, 0.0));
        }
        ++this.iceBasicAttackTimer;
        if (this.iceBasicAttackTimer >= 3) {
            this.iceBasicAttackTimer = 0;
            List<Player> nearby = this.getNearbyPlayers(activeBoss.getLocation(), 20.0);
            if (!nearby.isEmpty()) {
                Player target = nearby.get(random.nextInt(nearby.size()));
                Location from = activeBoss.getEyeLocation();
                Vector dir = target.getEyeLocation().subtract(from).toVector().normalize().multiply(1.5);
                Snowball sb = (Snowball)activeBoss.getWorld().spawnEntity(from, EntityType.SNOWBALL);
                sb.setVelocity(dir);
                sb.setMetadata("ice_boss_projectile", (MetadataValue)new FixedMetadataValue((Plugin)this.plugin, true));
            }
        }
        if (this.iceFrozenPlayer != null) {
            p = Bukkit.getPlayer((UUID)this.iceFrozenPlayer);
            if (p != null && p.isOnline()) {
                p.damage(4.0);
                --this.iceFrozenTicks;
                if (this.iceFrozenTicks <= 0) {
                    this.iceFrozenPlayer = null;
                    p.sendMessage("\u00a7aB\u1ea1n \u0111\u00e3 tho\u00e1t kh\u1ecfi l\u1edbp b\u0103ng!");
                    if (this.iceFrozenLoc != null) {
                        this.iceFrozenLoc.getBlock().setType(Material.AIR);
                        this.iceFrozenLoc = null;
                    }
                }
            } else {
                this.iceFrozenPlayer = null;
                if (this.iceFrozenLoc != null) {
                    this.iceFrozenLoc.getBlock().setType(Material.AIR);
                    this.iceFrozenLoc = null;
                }
            }
        }
        --this.iceETimer;
        if (this.iceETimer <= 0) {
            this.iceETimer = 30;
            p = this.getNearestPlayer(activeBoss.getLocation(), 15.0);
            if (p != null) {
                this.iceFrozenPlayer = p.getUniqueId();
                this.iceFrozenTicks = 10;
                this.iceFrozenLoc = p.getLocation().getBlock().getLocation().add(0.5, 0.0, 0.5);
                p.teleport(this.iceFrozenLoc);
                this.iceFrozenLoc.getBlock().setType(Material.ICE);
                p.sendBlockChange(this.iceFrozenLoc, Material.AIR.createBlockData());
                p.sendMessage("\u00a7b\u00a7lB\u1ea1n \u0111\u00e3 b\u1ecb \u0110\u00d3NG B\u0102NG T\u1ee6Y X\u01af\u01a0NG! \u0110\u1ed3ng \u0111\u1ed9i ph\u1ea3i g\u00e2y 20 s\u00e1t th\u01b0\u01a1ng l\u00ean b\u1ea1n \u0111\u1ec3 gi\u1ea3i c\u1ee9u!");
                ++this.iceFreezeCount;
                if (this.iceFreezeCount >= 5) {
                    this.iceFreezeCount = 0;
                    if (this.iceFrozenPlayer != null) {
                        Player ultP = Bukkit.getPlayer((UUID)this.iceFrozenPlayer);
                        if (ultP != null) {
                            ultP.setHealth(0.0);
                            ultP.sendMessage("\u00a74\u00a7lBoss tung N\u1ed9: B\u0102NG GI\u00c1 V\u0128NH C\u1eecU! B\u1ea0N \u0110\u00c3 V\u1ee0 V\u1ee4N!");
                        }
                    } else {
                        List<Player> all = this.getNearbyPlayers(activeBoss.getLocation(), 30.0);
                        if (!all.isEmpty()) {
                            Player furthest = null;
                            double maxDist = -1.0;
                            for (Player pp : all) {
                                double d = pp.getLocation().distanceSquared(activeBoss.getLocation());
                                if (!(d > maxDist)) continue;
                                maxDist = d;
                                furthest = pp;
                            }
                            if (furthest != null) {
                                this.teleportBoss(activeBoss, furthest.getLocation());
                                this.iceFrozenPlayer = furthest.getUniqueId();
                                this.iceFrozenTicks = 30;
                                this.iceFrozenLoc = furthest.getLocation().getBlock().getLocation().add(0.5, 0.0, 0.5);
                                furthest.teleport(this.iceFrozenLoc);
                                this.iceFrozenLoc.getBlock().setType(Material.ICE);
                                furthest.sendBlockChange(this.iceFrozenLoc, Material.AIR.createBlockData());
                                furthest.sendMessage("\u00a74\u00a7lBoss tung N\u1ed9 l\u00ean b\u1ea1n! B\u1ecb \u0111\u00f3ng b\u0103ng 30 GI\u00c2Y!");
                            }
                        }
                    }
                }
            }
        }
    }

    private void tickVoidBoss(LivingEntity activeBoss) {
        if (this.voidIsSplit) {
            --this.voidSplitTimer;
            if (this.voidSplitTimer <= 0) {
                for (LivingEntity clone : this.voidClones) {
                    if (!clone.isValid()) continue;
                    clone.remove();
                }
                this.voidClones.clear();
                this.voidIsSplit = false;
                this.teleportBoss(activeBoss, this.voidRealClone.getLocation());
                activeBoss.setInvisible(false);
                if (this.voidIsUltimate) {
                    for (Player p : this.getNearbyPlayers(activeBoss.getLocation(), 30.0)) {
                        p.setHealth(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() * 0.1);
                        p.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 200, 4));
                        p.addPotionEffect(new PotionEffect(PotionEffectType.MINING_FATIGUE, 200, 4));
                        p.sendMessage("\u00a75\u00a7l[L\u00c3NH \u0110\u1ecaA B\u00d3NG T\u1ed0I] Th\u1ea5t b\u1ea1i! B\u1ea1n b\u1ecb b\u00f3ng t\u1ed1i c\u1eafn nu\u1ed1t!");
                        p.playSound(p.getLocation(), Sound.ENTITY_WITHER_DEATH, 1.0f, 0.5f);
                    }
                } else {
                    double maxHp = activeBoss.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
                    activeBoss.setHealth(Math.min(maxHp, activeBoss.getHealth() + maxHp * 0.1));
                    Bukkit.broadcastMessage((String)"\u00a75Boss \u0111\u00e3 thu h\u1ed3i ph\u00e2n th\u00e2n v\u00e0 h\u1ed3i 10% m\u00e1u!");
                }
            }
            return;
        }
        if (this.voidBlackHoleTicks > 0 && this.voidBlackHoleLoc != null) {
            --this.voidBlackHoleTicks;
            // Hiệu ứng hạt hoành tráng và âm thanh kéo người chơi
            this.voidBlackHoleLoc.getWorld().playSound(this.voidBlackHoleLoc, Sound.BLOCK_PORTAL_AMBIENT, 1.5f, 0.5f);
            this.voidBlackHoleLoc.getWorld().spawnParticle(Particle.PORTAL, this.voidBlackHoleLoc, 60, 2.5, 2.5, 2.5, 0.05);
            this.voidBlackHoleLoc.getWorld().spawnParticle(Particle.DRAGON_BREATH, this.voidBlackHoleLoc, 20, 1.5, 1.5, 1.5, 0.02);

            for (Player p : this.getNearbyPlayers(this.voidBlackHoleLoc, 30.0)) {
                if (p.getGameMode() == org.bukkit.GameMode.CREATIVE || p.getGameMode() == org.bukkit.GameMode.SPECTATOR) continue;
                double dist = p.getLocation().distance(this.voidBlackHoleLoc);
                if (dist < 2.5) {
                    // Gây 15.0 Sát thương chuẩn (True Damage) trực tiếp vào máu
                    double newHp = p.getHealth() - 15.0;
                    if (newHp <= 0.0) {
                        boolean hasTotem = false;
                        ItemStack main = p.getInventory().getItemInMainHand();
                        ItemStack off = p.getInventory().getItemInOffHand();
                        if (main != null && main.getType() == Material.TOTEM_OF_UNDYING) {
                            hasTotem = true;
                            main.setAmount(main.getAmount() - 1);
                        } else if (off != null && off.getType() == Material.TOTEM_OF_UNDYING) {
                            hasTotem = true;
                            off.setAmount(off.getAmount() - 1);
                        }
                        
                        if (hasTotem) {
                            p.playEffect(org.bukkit.EntityEffect.TOTEM_RESURRECT);
                            p.setHealth(1.0);
                            p.clearActivePotionEffects();
                            p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 900, 1));
                            p.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 800, 0));
                            p.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 100, 1));
                        } else {
                            p.setHealth(0.0);
                        }
                    } else {
                        p.setHealth(newHp);
                        p.damage(0.01, activeBoss); // Tạo hoạt ảnh chớp đỏ sát thương
                    }
                    this.voidBlackHoleVictims.add(p.getUniqueId());
                    continue;
                }
                
                // Lực hút tăng dần khi càng gần hố đen, đồng thời hơi nâng nhẹ người chơi để triệt tiêu ma sát mặt đất
                double force = 1.0 - (dist / 30.0);
                force = Math.max(0.15, force * 0.7);
                Vector v = this.voidBlackHoleLoc.toVector().subtract(p.getLocation().toVector()).normalize().multiply(force);
                v.setY(0.2);
                p.setVelocity(v);
            }
            if (this.voidBlackHoleTicks == 0) {
                Player target = null;
                for (Player p : this.getNearbyPlayers(activeBoss.getLocation(), 40.0)) {
                    if (this.voidBlackHoleVictims.contains(p.getUniqueId())) continue;
                    target = p;
                    break;
                }
                if (target != null) {
                    Bukkit.broadcastMessage((String)"\u00a75\u00a7lBoss \u0111\u00e3 ph\u00e1t hi\u1ec7n k\u1ebb lu\u1ed3n l\u00e1ch! \u0110ang truy s\u00e1t!");
                    this.teleportBoss(activeBoss, target.getLocation().add(target.getLocation().getDirection().multiply(-1)));
                    target.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 200, 1));
                }
            }
        }
        --this.voidETimer;
        if (this.voidETimer <= 0 && this.voidBlackHoleTicks <= 0) {
            this.voidETimer = 35;
            this.voidBlackHoleTicks = 10;
            this.voidBlackHoleLoc = activeBoss.getLocation().clone();
            this.voidBlackHoleVictims.clear();
            Bukkit.broadcastMessage((String)"\u00a75Boss tung H\u1ed1 \u0110en! K\u00e9o d\u00e0i 10 gi\u00e2y!");
            ++this.voidECount;
            if (this.voidECount >= 3) {
                this.voidECount = 0;
                Bukkit.broadcastMessage((String)"\u00a75\u00a7lBoss tung N\u1ed9: L\u00c3NH \u0110\u1ecaA B\u00d3NG T\u1ed0I! T\u00ecm b\u1ea3n th\u1ec3 th\u1eadt trong 10 gi\u00e2y!");
                this.triggerVoidSplit(true, activeBoss);
            }
        }
    }

    private void triggerVoidSplit(boolean isUltimate, LivingEntity activeBoss) {
        this.voidIsSplit = true;
        this.voidIsUltimate = isUltimate;
        this.voidSplitTimer = isUltimate ? 10 : 20;
        activeBoss.setInvisible(true);
        Location center = activeBoss.getLocation();
        this.voidClones.clear();
        for (int i = 0; i < 12; ++i) {
            Location loc = center.clone().add((double)(random.nextInt(20) - 10), 0.0, (double)(random.nextInt(20) - 10));
            LivingEntity clone = null;
            boolean isMythicMob = false;

            if (Bukkit.getPluginManager().isPluginEnabled("MythicMobs")) {
                try {
                    io.lumine.mythic.core.mobs.ActiveMob am = io.lumine.mythic.bukkit.MythicBukkit.inst().getMobManager().spawnMob("VoidClone", loc, 1);
                    if (am != null && am.getEntity() != null) {
                        Entity e = am.getEntity().getBukkitEntity();
                        if (e instanceof LivingEntity) {
                            clone = (LivingEntity) e;
                            isMythicMob = true;
                        }
                    }
                } catch (Throwable t) {
                    // Fallback silently
                }
            }

            if (clone == null) {
                clone = (LivingEntity) center.getWorld().spawnEntity(loc, EntityType.ENDERMAN);
                clone.setCustomName("§5§lChúa Tể Hư Không");
                final LivingEntity finalClone = clone;
                Bukkit.getScheduler().runTaskLater((Plugin)this.plugin, () -> {
                    if (finalClone.isValid()) {
                        finalClone.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(20.0);
                        finalClone.setHealth(20.0);
                    }
                }, 5L);
            }

            clone.setMetadata("LM_IGNORE", (MetadataValue)new FixedMetadataValue((Plugin)this.plugin, true));
            clone.getPersistentDataContainer().set(new NamespacedKey((Plugin)this.plugin, "cwe_void_clone"), PersistentDataType.INTEGER, i);
            clone.setRemoveWhenFarAway(false);
            this.voidClones.add(clone);
        }
        this.voidRealClone = this.voidClones.get(random.nextInt(12));
        if (isUltimate) {
            for (Player p : this.getNearbyPlayers(center, 30.0)) {
                p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 200, 1));
                p.addPotionEffect(new PotionEffect(PotionEffectType.DARKNESS, 200, 1));
            }
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (this.activeBossId == null) {
            return;
        }
        Entity activeBoss = Bukkit.getEntity((UUID)this.activeBossId);
        if (activeBoss == null) {
            return;
        }
        if (event.getEntity() instanceof Player && "ICE".equals(this.activeType)) {
            EntityDamageByEntityEvent e;
            Player p = (Player)event.getEntity();
            if (this.iceFrozenPlayer != null && p.getUniqueId().equals(this.iceFrozenPlayer) && event instanceof EntityDamageByEntityEvent && (e = (EntityDamageByEntityEvent)event).getDamager() instanceof Player) {
                if (e.getDamage() >= 20.0) {
                    this.iceFrozenPlayer = null;
                    p.sendMessage("\u00a7aB\u1ea1n \u0111\u00e3 \u0111\u01b0\u1ee3c gi\u1ea3i c\u1ee9u b\u1edfi \u0111\u1ed3ng \u0111\u1ed9i!");
                    e.getDamager().sendMessage("\u00a7a\u0110\u00e3 ph\u00e1 b\u0103ng gi\u1ea3i c\u1ee9u th\u00e0nh c\u00f4ng!");
                    if (this.iceFrozenLoc != null) {
                        this.iceFrozenLoc.getBlock().setType(Material.AIR);
                        this.iceFrozenLoc = null;
                    } else {
                        p.getLocation().getBlock().setType(Material.AIR);
                    }
                    event.setCancelled(true);
                } else {
                    e.getDamager().sendMessage("\u00a7cS\u00e1t th\u01b0\u01a1ng c\u1ee7a b\u1ea1n k \u0111\u1ee7 ph\u00e1 b\u0103ng! C\u1ea7n >= 20 s\u00e1t th\u01b0\u01a1ng 1 hit!");
                }
            }
        }
        if (event.getEntity().getUniqueId().equals(activeBoss.getUniqueId()) && "VOID".equals(this.activeType)) {
            if (event.getCause() == EntityDamageEvent.DamageCause.PROJECTILE || event.getCause() == EntityDamageEvent.DamageCause.DROWNING || event.getCause() == EntityDamageEvent.DamageCause.CONTACT) {
                event.setCancelled(true);
                return;
            }
            if (this.voidIsSplit) {
                event.setCancelled(true);
                return;
            }
            if (event.getDamage() > ((LivingEntity)activeBoss).getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() * 0.15) {
                Bukkit.broadcastMessage((String)"\u00a75Boss ch\u1ecbu s\u00e1t th\u01b0\u01a1ng qu\u00e1 l\u1edbn! T\u00e1ch th\u00e0nh 12 ph\u00e2n th\u00e2n!");
                this.triggerVoidSplit(false, (LivingEntity)activeBoss);
            }
        }
    }

    private double getBossBaseDamage(LivingEntity activeBoss) {
        if (Bukkit.getPluginManager().isPluginEnabled("MythicMobs") && this.activeBossIsMythic) {
            try {
                java.util.Optional<io.lumine.mythic.core.mobs.ActiveMob> amOpt = io.lumine.mythic.bukkit.MythicBukkit.inst().getMobManager().getActiveMob(activeBoss.getUniqueId());
                if (amOpt.isPresent()) {
                    io.lumine.mythic.core.mobs.ActiveMob am = amOpt.get();
                    if (am.getType() != null) {
                        return am.getType().getDamage(am);
                    }
                }
            } catch (Throwable ignored) {
            }
        }
        if (activeBoss.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE) != null) {
            return activeBoss.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).getValue();
        }
        return 8.0;
    }

    private void applyHybridDamage(EntityDamageByEntityEvent event, Player player, LivingEntity boss, double totalDamage, double trueDamagePercent) {
        if (player.getGameMode() == org.bukkit.GameMode.CREATIVE || player.getGameMode() == org.bukkit.GameMode.SPECTATOR) {
            return;
        }
        double trueDamage = totalDamage * trueDamagePercent;
        double normalDamage = totalDamage * (1.0 - trueDamagePercent);
        
        double newHealth = player.getHealth() - trueDamage;
        if (newHealth <= 0.0) {
            boolean hasTotem = false;
            ItemStack main = player.getInventory().getItemInMainHand();
            ItemStack off = player.getInventory().getItemInOffHand();
            if (main != null && main.getType() == Material.TOTEM_OF_UNDYING) {
                hasTotem = true;
                main.setAmount(main.getAmount() - 1);
            } else if (off != null && off.getType() == Material.TOTEM_OF_UNDYING) {
                hasTotem = true;
                off.setAmount(off.getAmount() - 1);
            }
            
            if (hasTotem) {
                player.playEffect(org.bukkit.EntityEffect.TOTEM_RESURRECT);
                player.setHealth(1.0);
                player.clearActivePotionEffects();
                player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 900, 1));
                player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 800, 0));
                player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 100, 1));
                event.setDamage(0.0);
            } else {
                player.setHealth(0.01);
                event.setDamage(100.0); // Đảm bảo lượng sát thương thô đủ để hạ gục 0.01 HP cuối cùng của người chơi qua giáp
            }
        } else {
            player.setHealth(newHealth);
            event.setDamage(normalDamage);
        }
    }

    public void registerArenaParticipants(Location loc) {
        for (Player p : loc.getWorld().getPlayers()) {
            if (p.getLocation().distanceSquared(loc) <= 900.0) { // 30 block radius
                this.eventParticipants.add(p.getUniqueId());
                p.sendMessage("§a[Đấu Trường] Bạn đã được hệ thống ghi nhận là người tham gia khiêu chiến Boss!");
            }
        }
    }

    public boolean isParticipant(UUID uuid) {
        return this.eventParticipants.contains(uuid);
    }

    @EventHandler(priority = org.bukkit.event.EventPriority.HIGH)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.isCancelled()) {
            return;
        }
        
        // Prevent non-participants from dealing damage to boss or guards
        if (this.activeBossId != null && event.getDamager() instanceof Player) {
            Player damager = (Player) event.getDamager();
            boolean targetingBossOrGuard = false;
            if (event.getEntity().getUniqueId().equals(this.activeBossId)) {
                targetingBossOrGuard = true;
            } else if (event.getEntity().getPersistentDataContainer().has(new NamespacedKey((Plugin)this.plugin, "cwe_meteor_guard"), PersistentDataType.STRING)) {
                targetingBossOrGuard = true;
            } else if (event.getEntity().getPersistentDataContainer().has(new NamespacedKey((Plugin)this.plugin, "cwe_void_clone"), PersistentDataType.INTEGER)) {
                targetingBossOrGuard = true;
            }
            
            if (targetingBossOrGuard && !this.isParticipant(damager.getUniqueId())) {
                event.setCancelled(true);
                damager.sendMessage("§cBạn không có quyền can thiệp vào đấu trường này! Hãy đợi Boss bị tiêu diệt hoặc tham gia từ đầu.");
                return;
            }
        }

        if (this.activeBossId != null && event.getEntity() instanceof Player) {
            Player p = (Player) event.getEntity();
            boolean isBossDamage = false;
            if (event.getDamager().getUniqueId().equals(this.activeBossId)) {
                isBossDamage = true;
            } else if (event.getDamager().getPersistentDataContainer().has(new NamespacedKey((Plugin)this.plugin, "cwe_meteor_guard"), PersistentDataType.STRING)) {
                isBossDamage = true;
            } else if (event.getDamager().getPersistentDataContainer().has(new NamespacedKey((Plugin)this.plugin, "cwe_void_clone"), PersistentDataType.INTEGER)) {
                isBossDamage = true;
            }
            if (isBossDamage) {
                this.eventParticipants.add(p.getUniqueId());
            }
        }
        Enderman e;
        if (this.activeBossId != null && event.getDamager().getUniqueId().equals(this.activeBossId)) {
            Entity activeBoss = Bukkit.getEntity((UUID)this.activeBossId);
            if (activeBoss == null) {
                return;
            }
            
            // Nếu sử dụng kỹ năng của MythicMobs, không ghi đè sát thương mặc định bằng code Java
            if (this.plugin.getConfig().getBoolean("bosses.use-mythic-mobs-skills", false) && this.activeBossIsMythic) {
                return;
            }

            double baseDamage = 8.0; // Dự phòng mặc định
            if (activeBoss instanceof LivingEntity) {
                baseDamage = this.getBossBaseDamage((LivingEntity) activeBoss);
            }

            if ("FIRE".equals(this.activeType) && event.getEntity() instanceof Player) {
                // Tỷ lệ hóa: Mặc định trong code cũ là 500.0 với config YAML mặc định ban đầu là 30 (hoặc sau này là 100).
                // Nhân với 5.0 để khi set 100 trong YAML sẽ đấm 500 dmg thô -> ~100 máu thực (sau giảm 80% giáp).
                // 20% True Damage (Sát thương chuẩn), 80% Normal Damage.
                double totalDmg = baseDamage * 5.0;
                this.applyHybridDamage(event, (Player) event.getEntity(), (LivingEntity) activeBoss, totalDmg, 0.20);
                ((Player)event.getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 100, 1));
            } else if ("ICE".equals(this.activeType) && event.getEntity() instanceof Player) {
                // Sát thương cận chiến của Boss Băng: Nhân 3.0 (Ví dụ: YAML set 50 -> 150 dmg thô)
                // 15% True Damage (Sát thương chuẩn), 85% Normal Damage.
                double totalDmg = baseDamage * 3.0;
                this.applyHybridDamage(event, (Player) event.getEntity(), (LivingEntity) activeBoss, totalDmg, 0.15);
                ((Player)event.getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 100, 1));
            } else if ("VOID".equals(this.activeType) && event.getEntity() instanceof Player) {
                // Tỷ lệ hóa: Mặc định trong code cũ là 800.0 (thường) và 2000.0 (đâm lén).
                // 25% True Damage (Sát thương chuẩn), 75% Normal Damage.
                Vector bossDir;
                Player p = (Player)event.getEntity();
                Vector pDir = p.getLocation().getDirection().setY(0).normalize();
                double dot = pDir.dot(bossDir = activeBoss.getLocation().getDirection().setY(0).normalize());
                double totalDmg;
                if (dot > 0.5) {
                    totalDmg = baseDamage * 25.0;
                    p.sendMessage("\u00a75\u00a7lB\u1ea0N B\u1eca \u0110\u00c2M L\u00c9N!");
                } else {
                    totalDmg = baseDamage * 10.0;
                }
                this.applyHybridDamage(event, p, (LivingEntity) activeBoss, totalDmg, 0.25);
            }
        }
        if (event.getDamager() instanceof Snowball && event.getDamager().hasMetadata("ice_boss_projectile")) {
            double dmg = 5.0;
            org.bukkit.projectiles.ProjectileSource shooter = ((Snowball) event.getDamager()).getShooter();
            if (shooter instanceof LivingEntity) {
                LivingEntity boss = (LivingEntity) shooter;
                dmg = this.getBossBaseDamage(boss) * 2.5;
                if (event.getEntity() instanceof Player) {
                    // 15% True Damage (Sát thương chuẩn), 85% Normal Damage cho đạn tuyết của Boss Băng
                    this.applyHybridDamage(event, (Player) event.getEntity(), boss, dmg, 0.15);
                } else {
                    event.setDamage(dmg);
                }
            } else {
                event.setDamage(dmg);
            }
            if (event.getEntity() instanceof Player) {
                ((Player)event.getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 100, 0));
            }
        }
        if (this.voidIsSplit && event.getEntity() instanceof Enderman && (e = (Enderman)event.getEntity()).getPersistentDataContainer().has(new NamespacedKey((Plugin)this.plugin, "cwe_void_clone"), PersistentDataType.INTEGER) && e.getHealth() - event.getFinalDamage() <= 0.0) {
            event.setCancelled(true);
            e.remove();
            if (e.equals(this.voidRealClone)) {
                Entity boss;
                Bukkit.broadcastMessage((String)"\u00a7a\u0110\u00e3 ph\u00e1 h\u1ee7y ph\u00e2n th\u00e2n th\u1eadt! Boss b\u1ecb cho\u00e1ng!");
                for (LivingEntity clone : this.voidClones) {
                    if (!clone.isValid()) continue;
                    clone.remove();
                }
                this.voidClones.clear();
                this.voidIsSplit = false;
                if (this.activeBossId != null && (boss = Bukkit.getEntity((UUID)this.activeBossId)) != null) {
                    this.teleportBoss(boss, e.getLocation());
                    if (boss instanceof LivingEntity) {
                        ((LivingEntity)boss).setInvisible(false);
                        ((LivingEntity)boss).addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 200, 10));
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (this.iceFrozenPlayer != null && event.getPlayer().getUniqueId().equals(this.iceFrozenPlayer)) {
            Location from = event.getFrom();
            Location to = event.getTo();
            if (to != null && (from.getX() != to.getX() || from.getY() != to.getY() || from.getZ() != to.getZ())) {
                Location newLoc = from.clone();
                newLoc.setYaw(to.getYaw());
                newLoc.setPitch(to.getPitch());
                event.setTo(newLoc);
            }
        }
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        if (this.iceFrozenPlayer != null && event.getPlayer().getUniqueId().equals(this.iceFrozenPlayer) && (event.getCause() == PlayerTeleportEvent.TeleportCause.ENDER_PEARL || event.getCause() == PlayerTeleportEvent.TeleportCause.CHORUS_FRUIT)) {
            event.setCancelled(true);
            event.getPlayer().sendMessage("\u00a7cB\u1ea1n \u0111ang b\u1ecb \u0111\u00f3ng b\u0103ng! Kh\u00f4ng th\u1ec3 d\u1ecbch chuy\u1ec3n!");
        }
    }

    @EventHandler
    public void onBossDeath(EntityDeathEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity.getPersistentDataContainer().has(new NamespacedKey((Plugin)this.plugin, "cwe_meteor_guard"), PersistentDataType.STRING)) {
            event.getDrops().clear();
            event.setDroppedExp(0);
        } else if (entity.getPersistentDataContainer().has(new NamespacedKey((Plugin)this.plugin, "cwe_meteor_boss"), PersistentDataType.STRING)) {
            event.getDrops().clear();
            event.setDroppedExp(0);
            if (this.mainTask != -1) {
                Bukkit.getScheduler().cancelTask(this.mainTask);
                this.mainTask = -1;
            }
            for (LivingEntity c : this.voidClones) {
                if (!c.isValid()) continue;
                c.remove();
            }
            if (this.iceFrozenPlayer != null) {
                Player p = Bukkit.getPlayer((UUID)this.iceFrozenPlayer);
                if (p != null) {
                    p.getLocation().getBlock().setType(Material.AIR);
                }
                this.iceFrozenPlayer = null;
            }
            if (this.iceFrozenLoc != null) {
                this.iceFrozenLoc.getBlock().setType(Material.AIR);
                this.iceFrozenLoc = null;
            }
            this.activeBossId = null;
            this.activeBossSpawnLoc = null;
            this.regionBossManager.onBossDeath(this.activeType);
        }
    }

    private Player getNearestPlayer(Location loc, double radius) {
        Player nearest = null;
        double minSq = radius * radius;
        for (Player p : loc.getWorld().getPlayers()) {
            double sq = p.getLocation().distanceSquared(loc);
            if (!(sq <= minSq)) continue;
            minSq = sq;
            nearest = p;
        }
        return nearest;
    }

    private List<Player> getNearbyPlayers(Location loc, double radius) {
        ArrayList<Player> list = new ArrayList<Player>();
        double rsq = radius * radius;
        for (Player p : loc.getWorld().getPlayers()) {
            if (!(p.getLocation().distanceSquared(loc) <= rsq)) continue;
            list.add(p);
        }
        return list;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (this.iceFrozenPlayer != null && event.getPlayer().getUniqueId().equals(this.iceFrozenPlayer)) {
            this.iceFrozenPlayer = null;
            if (this.iceFrozenLoc != null) {
                this.iceFrozenLoc.getBlock().setType(Material.AIR);
                for (Player p : this.getNearbyPlayers(this.iceFrozenLoc, 20.0)) {
                    p.damage(20.0);
                    p.sendMessage("\u00a7c\u0110\u1ed3ng minh c\u1ee7a b\u1ea1n h\u00e8n nh\u00e1t b\u1ecf tr\u1ed1n! Kh\u1ed1i b\u0103ng v\u1ee1 v\u1ee5n g\u00e2y 20 s\u00e1t th\u01b0\u01a1ng!");
                }
                this.iceFrozenLoc = null;
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (this.iceFrozenLoc != null && event.getBlock().getLocation().equals(this.iceFrozenLoc.getBlock().getLocation())) {
            event.setCancelled(true);
            event.getPlayer().sendMessage("\u00a7cKh\u00f4ng th\u1ec3 t\u1ef1 \u0111\u00e0o kh\u1ed1i b\u0103ng \u0111\u1ec3 tho\u00e1t!");
        }
    }

    @EventHandler
    public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event) {
        Entity activeBoss;
        if (this.activeBossId != null && "FIRE".equals(this.activeType) && event.getBucket() == Material.WATER_BUCKET && (activeBoss = Bukkit.getEntity((UUID)this.activeBossId)) != null && event.getPlayer().getLocation().distanceSquared(activeBoss.getLocation()) < 2500.0) {
            event.setCancelled(true);
            event.getPlayer().getInventory().setItemInMainHand(new ItemStack(Material.BUCKET));
            event.getBlockClicked().getRelative(event.getBlockFace()).setType(Material.AIR);
            event.getPlayer().getWorld().playSound(event.getBlockClicked().getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, 1.0f, 1.0f);
            event.getPlayer().getWorld().spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, event.getBlockClicked().getLocation(), 10, 0.5, 0.5, 0.5, 0.1);
            event.getPlayer().sendMessage("\u00a7c\u00a7lN\u01b0\u1edbc b\u1ed1c h\u01a1i ngay l\u1eadp t\u1ee9c v\u00ec s\u1ee9c n\u00f3ng c\u1ee7a Ma V\u01b0\u01a1ng!");
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (this.iceFrozenPlayer != null && event.getEntity().getUniqueId().equals(this.iceFrozenPlayer)) {
            this.iceFrozenPlayer = null;
            if (this.iceFrozenLoc != null) {
                this.iceFrozenLoc.getBlock().setType(Material.AIR);
                this.iceFrozenLoc = null;
            }
        }
        if (this.activeBossSpawnLoc != null && event.getEntity().getWorld().equals(this.activeBossSpawnLoc.getWorld())) {
            if (event.getEntity().getLocation().distanceSquared(this.activeBossSpawnLoc) < 10000.0) {
                this.eventParticipants.add(event.getEntity().getUniqueId());
            }
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        if (this.activeBossSpawnLoc != null && this.eventParticipants.contains(event.getPlayer().getUniqueId())) {
            Location safeLoc = this.activeBossSpawnLoc.clone().add((double)(random.nextInt(10) - 5), 2.0, (double)(random.nextInt(10) - 5));
            event.setRespawnLocation(safeLoc);
            event.getPlayer().sendMessage("§eBạn đã được hồi sinh ngay tại chiến trường Thiên Thạch!");
        }
    }

    private void teleportBoss(Entity ent, Location loc) {
        this.isPluginTeleporting = true;
        ent.teleport(loc);
        this.isPluginTeleporting = false;
    }

    @EventHandler
    public void onEntityTeleport(org.bukkit.event.entity.EntityTeleportEvent event) {
        Entity entity = event.getEntity();
        if (this.isPluginTeleporting) {
            return;
        }
        
        // 1. Kiểm tra Boss chính
        if (this.activeBossId != null && entity.getUniqueId().equals(this.activeBossId)) {
            event.setCancelled(true);
            return;
        }
        
        // 2. Kiểm tra phân thân VoidClone
        if (entity.getPersistentDataContainer().has(new NamespacedKey((Plugin)this.plugin, "cwe_void_clone"), PersistentDataType.INTEGER) 
                || (this.voidIsSplit && this.voidClones.contains(entity))) {
            event.setCancelled(true);
            return;
        }
        
        // 3. Kiểm tra Hộ vệ (Guards)
        if (entity.getPersistentDataContainer().has(new NamespacedKey((Plugin)this.plugin, "cwe_meteor_guard"), PersistentDataType.STRING)) {
            event.setCancelled(true);
            return;
        }
    }

    @EventHandler
    public void onEndermanEscape(com.destroystokyo.paper.event.entity.EndermanEscapeEvent event) {
        Entity entity = event.getEntity();
        if (this.isPluginTeleporting) {
            return;
        }
        
        // 1. Kiểm tra Boss chính
        if (this.activeBossId != null && entity.getUniqueId().equals(this.activeBossId)) {
            event.setCancelled(true);
            return;
        }
        
        // 2. Kiểm tra phân thân VoidClone
        if (entity.getPersistentDataContainer().has(new NamespacedKey((Plugin)this.plugin, "cwe_void_clone"), PersistentDataType.INTEGER) 
                || (this.voidIsSplit && this.voidClones.contains(entity))) {
            event.setCancelled(true);
            return;
        }
        
        // 3. Kiểm tra Hộ vệ (Guards)
        if (entity.getPersistentDataContainer().has(new NamespacedKey((Plugin)this.plugin, "cwe_meteor_guard"), PersistentDataType.STRING)) {
            event.setCancelled(true);
            return;
        }
    }
}

