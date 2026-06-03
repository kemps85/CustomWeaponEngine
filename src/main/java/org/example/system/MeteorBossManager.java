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
    private String activeType = null;
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
        if (this.activeBossId != null && (old = Bukkit.getEntity((UUID)this.activeBossId)) != null && !old.isDead()) {
            old.remove();
        }
        Location spawnLoc = loc.clone().add(0.0, 2.0, 0.0);
        this.activeType = type;
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
        if ("FIRE".equals(type)) {
            WitherSkeleton boss = (WitherSkeleton)loc.getWorld().spawnEntity(spawnLoc, EntityType.WITHER_SKELETON);
            this.setupBossStats((LivingEntity)boss, "\u00a7c\u00a7lH\u1ecfa Di\u1ec7m Ma V\u01b0\u01a1ng", 2000.0, type);
            this.spawnGuards(loc, EntityType.BLAZE, 4, type, 600.0);
        } else if ("ICE".equals(type)) {
            Zombie boss = (Zombie)loc.getWorld().spawnEntity(spawnLoc, EntityType.ZOMBIE);
            this.setupBossStats((LivingEntity)boss, "\u00a7b\u00a7lB\u0103ng Gi\u00e1 C\u1ed5 Th\u1ea7n", 15000.0, type);
            this.spawnGuards(loc, EntityType.STRAY, 4, type, 500.0);
        } else if ("VOID".equals(type)) {
            Enderman boss = (Enderman)loc.getWorld().spawnEntity(spawnLoc, EntityType.ENDERMAN);
            this.setupBossStats((LivingEntity)boss, "\u00a75\u00a7lCh\u00faa T\u1ec3 H\u01b0 Kh\u00f4ng", 300000.0, type);
            this.spawnGuards(loc, EntityType.ENDERMITE, 3, type, 15000.0);
            this.spawnGuards(loc, EntityType.ENDERMAN, 2, type, 15000.0);
        }
        this.startBossLogic();
    }

    private void setupBossStats(LivingEntity boss, String name, double hp, String type) {
        boss.setCustomName(name);
        boss.setCustomNameVisible(true);
        boss.setMetadata("LM_IGNORE", (MetadataValue)new FixedMetadataValue((Plugin)this.plugin, true));
        boss.getPersistentDataContainer().set(new NamespacedKey((Plugin)this.plugin, "cwe_meteor_boss"), PersistentDataType.STRING, type);
        this.maxBossHealth = hp;
        Bukkit.getScheduler().runTaskLater((Plugin)this.plugin, () -> {
            if (boss.isValid()) {
                boss.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(hp);
                boss.setHealth(hp);
            }
        }, 5L);
        this.activeBossId = boss.getUniqueId();
    }

    private void spawnGuards(Location loc, EntityType entityType, int amount, String type, double hp) {
        for (int i = 0; i < amount; ++i) {
            Location guardLoc = loc.clone().add((double)(random.nextInt(10) - 5), 2.0, (double)(random.nextInt(10) - 5));
            LivingEntity guard = (LivingEntity)loc.getWorld().spawnEntity(guardLoc, entityType);
            guard.setCustomName("\u00a78[H\u1ed9 V\u1ec7] \u00a7c" + entityType.name());
            guard.setCustomNameVisible(true);
            guard.setMetadata("LM_IGNORE", (MetadataValue)new FixedMetadataValue((Plugin)this.plugin, true));
            guard.getPersistentDataContainer().set(new NamespacedKey((Plugin)this.plugin, "cwe_meteor_guard"), PersistentDataType.STRING, type);
            Bukkit.getScheduler().runTaskLater((Plugin)this.plugin, () -> {
                if (guard.isValid()) {
                    guard.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(hp);
                    guard.setHealth(hp);
                }
            }, 5L);
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
            if (activeBoss.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue() != this.maxBossHealth) {
                activeBoss.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(this.maxBossHealth);
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
                    activeBoss.teleport((Entity)p);
                    p.damage(40.0, (Entity)activeBoss);
                    p.sendMessage("\u00a7c\u00a7lBoss n\u1ed5i \u0111i\u00ean v\u00ec \u0111\u00e0n em ch\u1ebft! Lao \u0111\u1ebfn \u0111\u00e2m b\u1ea1n!");
                }
                Bukkit.getScheduler().runTaskLater((Plugin)this.plugin, () -> {
                    if (activeBoss != null && !activeBoss.isDead()) {
                        this.spawnGuards(activeBoss.getLocation(), EntityType.BLAZE, 4, "FIRE", 600.0);
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
            activeBoss.teleport(activeBoss.getLocation().add(0.0, 5.0, 0.0));
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
                }
            } else {
                this.iceFrozenPlayer = null;
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
                p.getLocation().getBlock().setType(Material.ICE);
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
                                activeBoss.teleport(furthest);
                                this.iceFrozenPlayer = furthest.getUniqueId();
                                this.iceFrozenTicks = 30;
                                this.iceFrozenLoc = furthest.getLocation().getBlock().getLocation().add(0.5, 0.0, 0.5);
                                furthest.teleport(this.iceFrozenLoc);
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
                activeBoss.teleport(this.voidRealClone.getLocation());
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
            for (Player p : this.getNearbyPlayers(this.voidBlackHoleLoc, 30.0)) {
                double distSq = p.getLocation().distanceSquared(this.voidBlackHoleLoc);
                if (distSq < 4.0) {
                    p.damage(5.0);
                    this.voidBlackHoleVictims.add(p.getUniqueId());
                    continue;
                }
                Vector v = this.voidBlackHoleLoc.toVector().subtract(p.getLocation().toVector()).normalize().multiply(0.4);
                p.setVelocity(p.getVelocity().add(v));
            }
            this.voidBlackHoleLoc.getWorld().spawnParticle(Particle.PORTAL, this.voidBlackHoleLoc, 50, 2.0, 2.0, 2.0, 0.1);
            if (this.voidBlackHoleTicks == 0) {
                Player target = null;
                for (Player p : this.getNearbyPlayers(activeBoss.getLocation(), 40.0)) {
                    if (this.voidBlackHoleVictims.contains(p.getUniqueId())) continue;
                    target = p;
                    break;
                }
                if (target != null) {
                    Bukkit.broadcastMessage((String)"\u00a75\u00a7lBoss \u0111\u00e3 ph\u00e1t hi\u1ec7n k\u1ebb lu\u1ed3n l\u00e1ch! \u0110ang truy s\u00e1t!");
                    activeBoss.teleport(target.getLocation().add(target.getLocation().getDirection().multiply(-1)));
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
            Enderman clone = (Enderman)center.getWorld().spawnEntity(loc, EntityType.ENDERMAN);
            clone.setCustomName("\u00a75\u00a7lCh\u00faa T\u1ec3 H\u01b0 Kh\u00f4ng");
            clone.setMetadata("LM_IGNORE", (MetadataValue)new FixedMetadataValue((Plugin)this.plugin, true));
            clone.getPersistentDataContainer().set(new NamespacedKey((Plugin)this.plugin, "cwe_void_clone"), PersistentDataType.INTEGER, i);
            Bukkit.getScheduler().runTaskLater((Plugin)this.plugin, () -> {
                if (clone.isValid()) {
                    clone.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(20.0);
                    clone.setHealth(20.0);
                }
            }, 5L);
            this.voidClones.add((LivingEntity)clone);
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
                    p.getLocation().getBlock().setType(Material.AIR);
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

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        Enderman e;
        if (this.activeBossId != null && event.getDamager().getUniqueId().equals(this.activeBossId)) {
            Entity activeBoss = Bukkit.getEntity((UUID)this.activeBossId);
            if (activeBoss == null) {
                return;
            }
            if ("FIRE".equals(this.activeType)) {
                event.setDamage(500.0);
                if (event.getEntity() instanceof Player) {
                    ((Player)event.getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 100, 1));
                }
            } else if ("VOID".equals(this.activeType) && event.getEntity() instanceof Player) {
                Vector bossDir;
                Player p = (Player)event.getEntity();
                Vector pDir = p.getLocation().getDirection().setY(0).normalize();
                double dot = pDir.dot(bossDir = activeBoss.getLocation().getDirection().setY(0).normalize());
                if (dot > 0.5) {
                    event.setDamage(2000.0);
                    p.sendMessage("\u00a75\u00a7lB\u1ea0N B\u1eca \u0110\u00c2M L\u00c9N!");
                } else {
                    event.setDamage(800.0);
                }
            }
        }
        if (event.getDamager() instanceof Snowball && event.getDamager().hasMetadata("ice_boss_projectile")) {
            event.setDamage(5.0);
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
                    boss.teleport(e.getLocation());
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
            if (to != null && (from.getX() != to.getX() || from.getZ() != to.getZ())) {
                event.getPlayer().teleport(this.iceFrozenLoc);
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
        Entity activeBoss;
        if (this.iceFrozenPlayer != null && event.getEntity().getUniqueId().equals(this.iceFrozenPlayer)) {
            this.iceFrozenPlayer = null;
            if (this.iceFrozenLoc != null) {
                this.iceFrozenLoc.getBlock().setType(Material.AIR);
                this.iceFrozenLoc = null;
            }
        }
        if (this.activeBossId != null && (activeBoss = Bukkit.getEntity((UUID)this.activeBossId)) != null && !activeBoss.isDead() && event.getEntity().getLocation().distanceSquared(activeBoss.getLocation()) < 10000.0) {
            this.eventParticipants.add(event.getEntity().getUniqueId());
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Entity activeBoss;
        if (this.activeBossId != null && this.eventParticipants.contains(event.getPlayer().getUniqueId()) && (activeBoss = Bukkit.getEntity((UUID)this.activeBossId)) != null && !activeBoss.isDead()) {
            Location safeLoc = activeBoss.getLocation().clone().add((double)(random.nextInt(30) - 15), 0.0, (double)(random.nextInt(30) - 15));
            safeLoc.setY((double)(safeLoc.getWorld().getHighestBlockYAt(safeLoc) + 1));
            event.setRespawnLocation(safeLoc);
            event.getPlayer().sendMessage("\u00a7eB\u1ea1n \u0111\u00e3 \u0111\u01b0\u1ee3c h\u1ed3i sinh ngay t\u1ea1i chi\u1ebfn tr\u01b0\u1eddng Thi\u00ean Th\u1ea1ch!");
        }
    }
}

